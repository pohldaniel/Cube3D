package de.cube3d.components;

import org.springframework.stereotype.Component;

import de.cube3d.services.CipherService;

@Component
public class CubePasswordEncoder {
	
	private CipherService cipherService;	
	
	public CubePasswordEncoder() {

	}

	public CubePasswordEncoder(CipherService cipherService) {
		this.cipherService = cipherService;
	}

	public void setCipherService(CipherService cipherService) {
		this.cipherService = cipherService;
	}
	
	public String encode(String rawPassword) {
		return  cipherService.encryptWithMD5(rawPassword);
	}
	
	public boolean matches(CharSequence rawPassword, String encodedPassword) {	
		return cipherService.matches(rawPassword, encodedPassword);
	}	
}
