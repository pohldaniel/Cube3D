package de.security.components;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import de.security.service.CubeUserDetailsService;

public class PasswordAuthenticationProvider implements AuthenticationProvider {

	private String pepper = "sjddjw768wlsmj882z2rnknlahffajsdgw2mAW!sjhjsc9870asfj3f";
	
	@Autowired 
	private CubeUserDetailsService userDetailsService; 
	
	@SuppressWarnings("serial")
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
		try {
			String username = authentication.getName();
			String password = authentication.getCredentials().toString();
        
			UserDetails  userDetails = userDetailsService.loadUserByUsername(username);
			MessageDigest digest = MessageDigest.getInstance("SHA-512");		
			byte[] hash = digest.digest((password + pepper).getBytes());
			String hexHash = String.format("%x", new BigInteger(1, hash));
			if (!userDetails.getPassword().substring(6).equals(hexHash)) {
				throw new AuthenticationException("Invalid credentials") {};
			}

			Authentication authenticated = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
			return authenticated;
			
		} catch (UsernameNotFoundException ue) {
			throw new UsernameNotFoundException("User not found");
		}catch (NoSuchAlgorithmException ne) {
			throw new UsernameNotFoundException("User not found");
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		 return true; 
	}

}
