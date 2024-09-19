package de.security.service;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Enumeration;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

public class CertificateService {

	public static RSAPrivateKey loadRSAPrivateKey(String privateKey) throws IOException, GeneralSecurityException {
	    
	    byte[] fullFileAsBytes = IOUtils.toByteArray(org.apache.commons.io.IOUtils.toInputStream(privateKey, "UTF-8"));
	    String fullFileAsString = new String(fullFileAsBytes, "UTF-8");

	    Pattern parse = Pattern.compile("(?m)(?s)^---*BEGIN.*---*$(.*)^---*END.*---*$.*");
	    String encoded = parse.matcher(fullFileAsString).replaceFirst("$1");

	    byte[] keyDecoded = Base64.getMimeDecoder().decode(encoded);
	    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyDecoded);
	    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	    return (RSAPrivateKey)keyFactory.generatePrivate(keySpec);
	}
	
	public static RSAPublicKey loadRSAPublicKey(String publicKey) throws Exception {
		byte[] fullFileAsBytes = IOUtils.toByteArray(org.apache.commons.io.IOUtils.toInputStream(publicKey, "UTF-8"));
	    String fullFileAsString = new String(fullFileAsBytes, "UTF-8");

	    Pattern parse = Pattern.compile("(?m)(?s)^---*BEGIN.*---*$(.*)^---*END.*---*$.*");
	    String encoded = parse.matcher(fullFileAsString).replaceFirst("$1");
	    byte[] decoded = Base64.getMimeDecoder().decode(encoded);
	    
	    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	    X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
	    return (RSAPublicKey) keyFactory.generatePublic(keySpec);
	}
	
	public static X509Certificate loadX509Certificate(String certificate) throws Exception {
		byte[] fullFileAsBytes = IOUtils.toByteArray(org.apache.commons.io.IOUtils.toInputStream(certificate, "UTF-8"));
	    String fullFileAsString = new String(fullFileAsBytes, "UTF-8");

	    Pattern parse = Pattern.compile("(?m)(?s)^---*BEGIN.*---*$(.*)^---*END.*---*$.*");
	    String encoded = parse.matcher(fullFileAsString).replaceFirst("$1");
	    byte[] decoded = Base64.getMimeDecoder().decode(encoded);
	    
	    return (X509Certificate)CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(decoded));
	}
	
	public static RSAPrivateKey loadRSAPrivateKeyFromStore(String resourcePath, String password, String alias) throws IOException, GeneralSecurityException {    
		KeyStore keystore = KeyStore.getInstance("PKCS12");
		keystore.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath), password.toCharArray());
		return (RSAPrivateKey)keystore.getKey(alias, password.toCharArray());
	}
	
	public static RSAPublicKey loadRSAPublicKeyFromStore(String resourcePath, String password, String alias) throws IOException, GeneralSecurityException {    
		KeyStore keystore = KeyStore.getInstance("PKCS12");
		keystore.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath), password.toCharArray());		
		X509Certificate cert = (X509Certificate)keystore.getCertificate(alias);
		return (RSAPublicKey)cert.getPublicKey();  		
	}
	
	public static X509Certificate loadX509CertificateFromStore(String resourcePath, String password, String alias) throws Exception {
		KeyStore keystore = KeyStore.getInstance("PKCS12");
		keystore.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath), password.toCharArray());
		return (X509Certificate)keystore.getCertificate(alias);
	}
	
	@SuppressWarnings("unused")
	public static void listX509CertificatesFromStore(String resourcePath, String password) throws IOException, GeneralSecurityException {
		KeyStore keystore = KeyStore.getInstance("PKCS12");
		keystore.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath), password.toCharArray());
		
		Enumeration<String> enumeration = keystore.aliases();
        while(enumeration.hasMoreElements()) {
            String _alias = enumeration.nextElement();
            System.out.println("alias name: " + _alias);
            Certificate certificate = keystore.getCertificate(_alias);
            //System.out.println(certificate.toString());
        }
	}
	
	public static void writeX509CertificateToFile(X509Certificate certificate, String outPath) throws IOException, CertificateEncodingException {
		StringBuilder sb = new StringBuilder("-----BEGIN CERTIFICATE-----");
    	sb.append(System.getProperty("line.separator"));
    	InputStream is = new ByteArrayInputStream(Base64.getEncoder().withoutPadding().encode(certificate.getEncoded()));
    	String result = IOUtils.toString(is, StandardCharsets.UTF_8).replaceAll("(.{64})", "$1\n"); 	
    	is.close();
    	sb.append(result);
    	sb.append(System.getProperty("line.separator"));
    	sb.append("-----END CERTIFICATE-----");
    	
    	FileOutputStream certificateOut = new FileOutputStream(outPath);
    	certificateOut.write(sb.toString().getBytes());
    	certificateOut.close();
	}
	
	public static void writeX509CertificateToConsole(X509Certificate certificate) throws IOException, CertificateEncodingException {
		StringBuilder sb = new StringBuilder("-----BEGIN CERTIFICATE-----");
    	sb.append(System.getProperty("line.separator"));
    	InputStream is = new ByteArrayInputStream(Base64.getEncoder().withoutPadding().encode(certificate.getEncoded()));
    	String result = IOUtils.toString(is, StandardCharsets.UTF_8).replaceAll("(.{64})", "$1\n"); 	
    	is.close();
    	sb.append(result);
    	sb.append(System.getProperty("line.separator"));
    	sb.append("-----END CERTIFICATE-----");
    	System.out.println(sb.toString());
	}
	
	public static void writeRSAPublicKeyToFile(RSAPublicKey publicKey, String outPath) throws IOException, CertificateEncodingException {
		StringBuilder sb = new StringBuilder("-----BEGIN PUBLIC KEY-----");
    	sb.append(System.getProperty("line.separator"));
    	InputStream is = new ByteArrayInputStream(Base64.getEncoder().withoutPadding().encode(publicKey.getEncoded()));
    	String result = IOUtils.toString(is, StandardCharsets.UTF_8).replaceAll("(.{64})", "$1\n"); 	
    	is.close();
    	sb.append(result).append("==");
    	sb.append(System.getProperty("line.separator"));
    	sb.append("-----END PUBLIC KEY-----");
    	
    	FileOutputStream certificateOut = new FileOutputStream(outPath);
    	certificateOut.write(sb.toString().getBytes());
    	certificateOut.close();
	}
	
	public static void writeRSAPublicKeyToConsole(RSAPublicKey publicKey) throws IOException, CertificateEncodingException {
		StringBuilder sb = new StringBuilder("-----BEGIN PUBLIC KEY-----");
    	sb.append(System.getProperty("line.separator"));
    	InputStream is = new ByteArrayInputStream(Base64.getEncoder().withoutPadding().encode(publicKey.getEncoded()));
    	String result = IOUtils.toString(is, StandardCharsets.UTF_8).replaceAll("(.{64})", "$1\n"); 	
    	is.close();
    	sb.append(result).append("==");
    	sb.append(System.getProperty("line.separator"));
    	sb.append("-----END PUBLIC KEY-----");
    	System.out.println(sb.toString());
	}
	
	public static void writeRSAPrivateKeyToFile(RSAPrivateKey privateKey, String outPath) throws IOException, CertificateEncodingException {
		StringBuilder sb = new StringBuilder("-----BEGIN PRIVATE KEY-----");
    	sb.append(System.getProperty("line.separator"));
    	InputStream is = new ByteArrayInputStream(Base64.getEncoder().withoutPadding().encode(privateKey.getEncoded()));
    	String result = IOUtils.toString(is, StandardCharsets.UTF_8).replaceAll("(.{64})", "$1\n"); 	
    	is.close();
    	sb.append(result).append("==");
    	sb.append(System.getProperty("line.separator"));
    	sb.append("-----END PRIVATE KEY-----");
    	
    	 FileOutputStream certificateOut = new FileOutputStream(outPath);
    	 certificateOut.write(sb.toString().getBytes());
         certificateOut.close();
	}
	
	public static void writeRSAPrivateKeyToConsole(RSAPrivateKey privateKey) throws IOException, CertificateEncodingException {
		StringBuilder sb = new StringBuilder("-----BEGIN PRIVATE KEY-----");
    	sb.append(System.getProperty("line.separator"));
    	InputStream is = new ByteArrayInputStream(Base64.getEncoder().withoutPadding().encode(privateKey.getEncoded()));
    	String result = IOUtils.toString(is, StandardCharsets.UTF_8).replaceAll("(.{64})", "$1\n"); 	
    	is.close();
    	sb.append(result).append("==");
    	sb.append(System.getProperty("line.separator"));
    	sb.append("-----END PRIVATE KEY-----");
    	System.out.println(sb.toString());
	}
}
