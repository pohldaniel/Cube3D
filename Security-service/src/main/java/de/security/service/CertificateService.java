package de.security.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

public class CertificateService {

	public static RSAPrivateKey loadPrivateKey(InputStream privateKeyIn) throws IOException, GeneralSecurityException {
	    
	    byte[] fullFileAsBytes = IOUtils.toByteArray( privateKeyIn );
	    String fullFileAsString = new String(fullFileAsBytes, "UTF-8");

	    Pattern parse = Pattern.compile("(?m)(?s)^---*BEGIN.*---*$(.*)^---*END.*---*$.*");
	    String encoded = parse.matcher(fullFileAsString).replaceFirst("$1");

	    byte[] keyDecoded = Base64.getMimeDecoder().decode(encoded);
	    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyDecoded);
	    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	    return (RSAPrivateKey)keyFactory.generatePrivate(keySpec);
	}
	
	public static RSAPublicKey readX509PublicKey(InputStream publicKeyIn) throws Exception {
		byte[] fullFileAsBytes = IOUtils.toByteArray( publicKeyIn );
	    String fullFileAsString = new String(fullFileAsBytes, "UTF-8");

	    Pattern parse = Pattern.compile("(?m)(?s)^---*BEGIN.*---*$(.*)^---*END.*---*$.*");
	    String encoded = parse.matcher(fullFileAsString).replaceFirst("$1");
	    byte[] decoded = Base64.getMimeDecoder().decode(encoded);
	    
	    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	    X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
	    return (RSAPublicKey) keyFactory.generatePublic(keySpec);
	}
	
	public static X509Certificate loadX509Certificate(InputStream certificate) throws Exception {
		byte[] fullFileAsBytes = IOUtils.toByteArray( certificate );
	    String fullFileAsString = new String(fullFileAsBytes, "UTF-8");

	    Pattern parse = Pattern.compile("(?m)(?s)^---*BEGIN.*---*$(.*)^---*END.*---*$.*");
	    String encoded = parse.matcher(fullFileAsString).replaceFirst("$1");
	    byte[] decoded = Base64.getMimeDecoder().decode(encoded);
	    
	    return (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(decoded));
	}
}
