package de.security.components;

import org.springframework.security.crypto.password.PasswordEncoder;

import de.security.services.CipherService;

public class CubePasswordEncoder implements PasswordEncoder {

	private CipherService cipherService;	
	
	private static CubePasswordEncoder instance;
	
	private CubePasswordEncoder() {}
	 
	public static CubePasswordEncoder getInstance () {
	    if (CubePasswordEncoder.instance == null) {
	    	CubePasswordEncoder.instance = new CubePasswordEncoder();
	    }
	    return CubePasswordEncoder.instance;
	}
	
	public CubePasswordEncoder(CipherService cipherService) {
		this.cipherService = cipherService;
	}

	public void setCipherService(CipherService cipherService) {
		this.cipherService = cipherService;
	}
	
	@Override
	public String encode(CharSequence rawPassword) {
		return  cipherService.encryptWithMD5(rawPassword.toString());
	}
	
	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {	
		return cipherService.matches(rawPassword, encodedPassword);
	}	
}
