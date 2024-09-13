package de.security.rest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.security.utils.SSLUtil;


@RestController
@RequestMapping("/")
public class LoginRestController {

	/*@Autowired
	FormLoginConfigurer formLoginConfigurer;
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(Principal principal)  throws Exception {
		
		UserDetails currentUser = (UserDetails) ((Authentication) principal).getPrincipal();
		List<BasicHeader> header = new ArrayList<>(Arrays.asList(new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")));
		final SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(SSLUtil.getSSLContext("password"));

		CloseableHttpClient client = HttpClients.custom()	
				.setDefaultHeaders(header)
				.setSSLSocketFactory(csf)
				.build();
		
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("username", currentUser.getUsername());
		parameters.put("password", "default");

		String form = parameters.keySet().stream()
		.map(key -> {
			try {
				return key + "=" + URLEncoder.encode(parameters.get(key), StandardCharsets.UTF_8.toString());
			} catch (UnsupportedEncodingException e) {
				return "";
			}
		}).collect(Collectors.joining("&"));
		
		HttpPost httpPost = new HttpPost("https://localhost:8443/perform_login");        	
		httpPost.setEntity(new StringEntity(form));
		
		
		CloseableHttpResponse response = client.execute(httpPost);
		String output = EntityUtils.toString(response.getEntity());	
		System.out.println("Out Put: " + output);
		
		
		
		return currentUser.getUsername();
	}*/
	
	@RequestMapping(value = "/message", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
	public String confirm(Model model) {		
		return model.getAttribute("message").toString();
	}
	
}
