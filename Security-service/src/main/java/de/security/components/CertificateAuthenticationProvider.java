package de.security.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import de.security.service.PasswordUserDetailsService;

public class CertificateAuthenticationProvider implements AuthenticationProvider{
	
	@Autowired 
	private PasswordUserDetailsService userDetailsService; 
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		UserDetails  userDetails = userDetailsService.loadUserByUsername(authentication.getName());
		Authentication authenticated = new UsernamePasswordAuthenticationToken(userDetails, "not used", userDetails.getAuthorities());
		return authenticated;
	}
	
	@Override
	public boolean supports(Class<?> authentication) {
		 return true; 
	}

}
