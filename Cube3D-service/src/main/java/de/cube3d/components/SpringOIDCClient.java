package de.cube3d.components;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.NoConnectionReuseStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.cube3d.utils.SSLUtil;

public class SpringOIDCClient {
	
	public static class Builder{
			
		private String endpoint;
		private String clientId;
		private String clientSecret;
	
		public Builder() {
		}
		
		public Builder endpoint(String endpoint) {
	            this.endpoint = endpoint;
	            return this;
	    }
		
		public Builder clientId(String clientId) {
            this.clientId = clientId;
            return this;
		}
	        
	    public Builder clientSecret(String clientSecret) {
	            this.clientSecret = clientSecret;
	            return this;
	    }
	    
	    public SpringOIDCClient build() {
            return new SpringOIDCClient(this);
        }
	}
	
	private String endpoint;
	private String clientId;
	private String clientSecret;
	
	CloseableHttpClient client;
	
	private SpringOIDCClient(Builder builder){	
		this.endpoint = builder.endpoint;
		this.clientId = builder.clientId; 		
		this.clientSecret = builder.clientSecret; 
		
        String credentials64 = new String(org.apache.commons.codec.binary.Base64.encodeBase64((clientId + ":" + clientSecret).getBytes()));			
		
		List<BasicHeader> header = new ArrayList<>(Arrays.asList(new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded"),
                                                                 new BasicHeader(HttpHeaders.ACCEPT, "application/json"),
																 new BasicHeader("Authorization", "Basic " + credentials64)));
		
		final SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(SSLUtil.getDefaultSSLContext());
		
		client = HttpClients.custom()	
				.setDefaultHeaders(header)
	            .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)			    
			    .setConnectionReuseStrategy(NoConnectionReuseStrategy.INSTANCE)
			    .setSSLSocketFactory(csf)
			    .build();
		
	}
	
	public boolean introspect(String token) {
			
		try {
			HttpPost httpPost = new HttpPost(this.endpoint + "/oauth2/introspect");        	
			httpPost.setEntity(new StringEntity("token=" + URLEncoder.encode(token, StandardCharsets.UTF_8.toString())));
			
			CloseableHttpResponse response = client.execute(httpPost);
			
			ObjectMapper mapper = new ObjectMapper();
			String output = EntityUtils.toString(response.getEntity());	
			JsonNode actualObj = mapper.readTree(output);
			return actualObj.get("active").asBoolean();
		} catch (IOException e) {
			return false;
		}	
	}
}
