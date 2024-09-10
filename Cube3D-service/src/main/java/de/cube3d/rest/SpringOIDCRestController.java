package de.cube3d.rest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import de.cube3d.utils.SSLUtil;

@RestController
@RequestMapping("/")
public class SpringOIDCRestController {
	private static final Logger LOG = LoggerFactory.getLogger(SpringOIDCRestController.class);
	private String redirect = "https%3A%2F%2Flocalhost%3A8080%2Fspring%2Foidc%2Fcallback";
	private String clinetId = "cube";
	private String clientSecret = "secret";
	//private String provider = "user-provider";
	
	@RequestMapping(value = "login", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> login(@RequestAttribute("code") String code, @RequestAttribute("returnUrl") String returnUrl, @RequestAttribute("state") String state) throws URISyntaxException, ParseException, IOException {
		LOG.info("/oidc/login was called");	
		
		if(code.equalsIgnoreCase("undefined")) {	
			URI uri = returnUrl.equalsIgnoreCase("none") ?
				new URI("https://auth-server:8200/oauth2/authorize?client_id=" + this.clinetId + "&redirect_uri=" + this.redirect + "&response_type=code&scope=openid&nonce=www") :			
				new URI("https://auth-server:8200/oauth2/authorize?client_id=" + this.clinetId + "&redirect_uri=" + this.redirect + "&response_type=code&scope=openid&nonce=www&state=" + returnUrl);
	
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setLocation(uri);
			return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
		}else {

			URI uri = state.equalsIgnoreCase("none") ?
				new URI("https://localhost:4200/gateway?code=" + code) :			
				new URI("https://localhost:4200/gateway?code=" + code + "&returnUrl=" + state);

			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setLocation(uri);
			return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
		}	
	}
	
	@RequestMapping(value = "spring/oidc/callback", method = RequestMethod.GET)
	public ModelAndView callback(ModelMap model, @RequestAttribute("code") String code, @RequestAttribute("state") String state) {
		return state.equalsIgnoreCase("none") ? new ModelAndView("redirect:/login?code=" + code, model) : new ModelAndView("redirect:/login?code=" + code + "&state=" + state, model) ;
	}
	
	@RequestMapping(value = "spring/oidc/token", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> token(@RequestParam(value = "code", defaultValue = "undefined") String code) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		
		if(code.equalsIgnoreCase("undefined")) {
			ObjectNode statusMap = mapper.createObjectNode();
			statusMap.put("remoteUser", "Anonymus");
			return ResponseEntity.status(HttpStatus.OK).body(statusMap);
		}
		
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("grant_type", "authorization_code");
		parameters.put("code", code);
		parameters.put("client_id", this.clinetId);
		parameters.put("client_secret", this.clientSecret);
		parameters.put("redirect_uri", "https://localhost:8080/spring/oidc/callback");
		parameters.put("scope", "openid");
		
		String form = parameters.keySet().stream()
		.map(key -> {
			try {
				return key + "=" + URLEncoder.encode(parameters.get(key), StandardCharsets.UTF_8.toString());
			} catch (UnsupportedEncodingException e) {
				return "";
			}
		})
		.collect(Collectors.joining("&"));
		
		
		List<BasicHeader> header = new ArrayList<>(Arrays.asList(new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")));
		final SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(SSLUtil.getSSLContext("password"));

		CloseableHttpClient client = HttpClients.custom()	
				.setDefaultHeaders(header)
				.setSSLSocketFactory(csf)
				.build();

		HttpPost httpPost = new HttpPost("https://localhost:8200/oauth2/token");        	
		httpPost.setEntity(new StringEntity(form));
		CloseableHttpResponse response = client.execute(httpPost);
		
		String output = EntityUtils.toString(response.getEntity());	
		JsonNode actualObj = mapper.readTree(output);

		String jwt = actualObj.get("id_token").asText();	
		String[] jwtSplit = jwt.split("\\.");
		
		String body = new String(Base64.decodeBase64(jwtSplit[1]), "UTF-8");
		JsonNode bodyJson = mapper.readTree(body);
		
		ObjectNode statusMap = mapper.createObjectNode();
		statusMap.put("status", "200");
		statusMap.put("remoteUser", "Ann");
		statusMap.put("token", jwt);
		statusMap.put("accessToken", actualObj.get("access_token").asText());

		return ResponseEntity.status(HttpStatus.OK).body(statusMap);
	}
	
	@RequestMapping(value = "spring/oidc/refresh", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getRefreshToken(@RequestBody JsonNode tokenMap) throws Exception  {
		LOG.info("spring/oidc/refreshToken was called");	
		ObjectMapper mapper = new ObjectMapper();
				
		String idToken = tokenMap.get("id_token").asText();
		String accessToken = tokenMap.get("access_token").asText();
		String sub = tokenMap.get("remoteUser").asText();
		/*Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("grant_type", "refresh_token");
		parameters.put("refresh_token", accessToken);
		parameters.put("client_id", this.clinetId);
		parameters.put("client_secret", this.clientSecret);
		parameters.put("redirect_uri", "https://localhost:8080/oidc/callback");
		parameters.put("scope", "openid,user,groups");
		
		String form = parameters.keySet().stream()
						.map(key -> {
							try {
								return key + "=" + URLEncoder.encode(parameters.get(key), StandardCharsets.UTF_8.toString());
							} catch (UnsupportedEncodingException e) {
								return "";
							}
						})
						.collect(Collectors.joining("&"));
		
		List<BasicHeader> header = new ArrayList<>(Arrays.asList(new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")));
		final SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(SSLUtil.getSSLContext());

		CloseableHttpClient client = HttpClients.custom()	
				.setDefaultHeaders(header)
				.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)			    
				.setConnectionReuseStrategy(NoConnectionReuseStrategy.INSTANCE)
				.setSSLSocketFactory(csf)
				//.setProxy(new HttpHost(proxyHost, proxyPort, "http"))
				.build();
		
		HttpPost httpPost = new HttpPost("https://localhost:8200/oauth2/token");        	
		httpPost.setEntity(new StringEntity(form));
		CloseableHttpResponse response = client.execute(httpPost);
		
		String output = EntityUtils.toString(response.getEntity());	
		JsonNode actualObj = mapper.readTree(output);
			
		String jwt = actualObj.get("id_token").asText();	
		String[] jwtSplit = jwt.split("\\.");
		
		String body = new String(Base64.decodeBase64(jwtSplit[1]), "UTF-8");
		JsonNode bodyJson = mapper.readTree(body);*/
		
		ObjectNode statusMap = mapper.createObjectNode();
		statusMap.put("status", "200");
		statusMap.put("remoteUser", sub);
		statusMap.put("token", idToken);
		statusMap.put("accessToken", accessToken);
		return ResponseEntity.status(HttpStatus.OK).body(statusMap);
	}
}
