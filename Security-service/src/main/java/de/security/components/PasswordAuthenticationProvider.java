package de.security.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import de.security.services.CubeUserDetailsService;

public class PasswordAuthenticationProvider implements AuthenticationProvider {

	@Autowired 
	private CubeUserDetailsService userDetailsService; 
		
	@SuppressWarnings("serial")
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
		try {
			String username = authentication.getName();
			String password = authentication.getCredentials().toString();
        
			UserDetails  userDetails = userDetailsService.loadUserByUsername(username);
			if(!CubePasswordEncoder.getInstance().matches(password, userDetails.getPassword().substring(6))){
				throw new AuthenticationException("Invalid credentials") {};
			}
			
			Authentication authenticated = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
			return authenticated;
			
		} catch (UsernameNotFoundException ue) {
			throw new UsernameNotFoundException("User not found");
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		 return true; 
	}

}
