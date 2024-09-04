package de.cube3d.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import de.cube3d.service.JwtService;

public class RestAPIFilter implements Filter{

	 private List<String> corsUrls = new ArrayList<>(Arrays.asList(
			 "http://localhost:4200", 
			 "http://127.0.0.1:4200", 
			 "http://0:0:0:0:0:0:0:1:4200",
			 "http://localhost:8200", 
			 "http://127.0.0.1:8200", 
			 "http://0:0:0:0:0:0:0:1:8200",
			 "http://localhost:8080",
			 "http://127.0.0.1:8080", 
			 "http://0:0:0:0:0:0:0:1:8080"));
	 
	 private JwtService jwtService;
	 
	 public RestAPIFilter() {	 
	 }
	 
	 public void setJwtService(JwtService jwtService) {
		 this.jwtService = jwtService;
	 }
	 
	 @Override
	 public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		 
		 HttpServletRequest httpServletRequest = (HttpServletRequest) request;
	     HttpServletResponse httpServletResponse = (HttpServletResponse) response;
	     
	     String origin = httpServletRequest.getHeader("Origin");
	     for (String url : corsUrls) {
	    	 if (origin != null && origin.startsWith(url)) {
	    		 httpServletResponse.setHeader("Access-Control-Allow-Origin", origin);
	    	 }
	     }
	              
	     httpServletResponse.addHeader("Access-Control-Allow-Credentials", "true");
	     httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
	     httpServletResponse.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept, Authorization");

	     if (httpServletRequest.getMethod().equals(HttpMethod.OPTIONS.name())) {
	    	 return;
	     }
	     
	     String authHeader = httpServletRequest.getHeader("Authorization");	      
	        
	     if(authHeader != null) { 
	    	 StringTokenizer st = new StringTokenizer(authHeader);        	
	    	 if(st.hasMoreTokens()) {

	    		 String basic = st.nextToken();
	    		 if(basic.equalsIgnoreCase("Bearer")) {
	    			 String jwt = st.nextToken();
	    			 
	    			 if(jwtService.validateToken(jwt)) {
	    				 httpServletRequest.setAttribute("roles", jwtService.getRoles(jwt));
	    				 chain.doFilter(httpServletRequest, httpServletResponse);
	    				 return;
	    			 }else {
	    				 httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
	    				 return;
	    			 }
	    		 }
	    	 }
	     }	     
	 }
}
