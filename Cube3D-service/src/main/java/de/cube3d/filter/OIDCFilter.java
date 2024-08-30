package de.cube3d.filter;

import java.io.IOException;

import org.springframework.http.HttpMethod;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class OIDCFilter implements Filter{
	 
	 @Override
	 public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		 
		 HttpServletRequest httpServletRequest = (HttpServletRequest) request;
	     HttpServletResponse httpServletResponse = (HttpServletResponse) response;
	     
	     httpServletResponse.setHeader("Access-Control-Allow-Origin", httpServletRequest.getHeader("Origin"));
	              
	     httpServletResponse.addHeader("Access-Control-Allow-Credentials", "true");
	     httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
	     httpServletResponse.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept, Authorization");
	     
	     if (httpServletRequest.getMethod().equals(HttpMethod.OPTIONS.name())) {
	    	 return;
	     }
	     String code = httpServletRequest.getParameter("code");
	     if(code != null) {
	    	 httpServletRequest.setAttribute("code", code);  
	     }else { 
	    	 httpServletRequest.setAttribute("code", "undefined");  
	     }
			
	     String returnUrl = httpServletRequest.getParameter("returnUrl");
	     if(returnUrl != null) {
	    	 httpServletRequest.setAttribute("returnUrl", returnUrl);  
	     }else { 
	    	 httpServletRequest.setAttribute("returnUrl", "none");  
	     }
			
	     String state = httpServletRequest.getParameter("state");
	     if(state != null) {
	    	 httpServletRequest.setAttribute("state", state);  
	     }else { 
	    	 httpServletRequest.setAttribute("state", "none");  
	     }
	     chain.doFilter(httpServletRequest, httpServletResponse);
	 }
}
