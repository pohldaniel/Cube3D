package de.cube3d.services;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CipherService {

	private final static Logger LOG = LoggerFactory.getLogger(CipherService.class);
	
	private MessageDigest md;
	
	public synchronized String encryptWithMD5(final String toEncrypt) {
		try {
			md = MessageDigest.getInstance("MD5");
			final byte[] passBytes = toEncrypt.getBytes();
			md.reset();
			final byte[] digested = md.digest(passBytes);
			final StringBuffer sb = new StringBuffer();
			for (final byte element : digested) {
				sb.append(Integer.toHexString(0xff & element));
			}
			return sb.toString();
		} catch (final NoSuchAlgorithmException ex) {
			LOG.error("Encryption failed.", ex);
		}
		return null;
	}
	
	public boolean matches(CharSequence rawPassword, String encodedPassword) {		
		return encryptWithMD5(rawPassword.toString()).equals(encodedPassword);		
	}
}
