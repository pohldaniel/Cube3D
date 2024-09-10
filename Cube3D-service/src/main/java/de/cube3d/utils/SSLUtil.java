package de.cube3d.utils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.cert.CertificateFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.regex.Pattern;

import javax.net.ssl.SSLException;

import org.apache.commons.io.IOUtils;
import org.apache.http.conn.ssl.TrustStrategy;

public class SSLUtil {

	//"TLS", "TLSv1", "TLSv1.1","TLSv1.2","TLSv1.3", "SSLv3"
	static String TLS = "TLSv1.2";

	private SSLUtil() {		
	}

	public static SSLContext getDefaultSSLContext() {
		try {
			return SSLContext.getDefault();
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}
	
	public static SSLContext getSSLContext() {
		
		try {
			final SSLContext sslcontext = SSLContext.getInstance(TLS);
			sslcontext.init(null, new TrustManager[] { new X509TrustManager() {

				@Override
				public void checkClientTrusted(final X509Certificate[] arg0, final String arg1)
						throws CertificateException {
				}

				@Override
				public void checkServerTrusted(final X509Certificate[] arg0, final String arg1)
						throws CertificateException {
				}

				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return new X509Certificate[0];
				}
			} }, new SecureRandom());
			return sslcontext;
		} catch (NoSuchAlgorithmException | KeyManagementException e) {
			//throw new IOException(e.getMessage(), e);
			return null;
		}
	}
	
	public static KeyStore createEmptyKeyStore() throws IOException, GeneralSecurityException {
	    KeyStore keyStore = KeyStore.getInstance("JKS");
	    keyStore.load(null,null);
	    return keyStore;
	}

	public static X509Certificate loadCertificate(InputStream publicCertIn) throws IOException, GeneralSecurityException {
	    CertificateFactory factory = CertificateFactory.getInstance("X.509");
	    X509Certificate cert = (X509Certificate)factory.generateCertificate(publicCertIn);
	    return cert;
	}
	
	public static PrivateKey loadPrivateKey(InputStream privateKeyIn) throws IOException, GeneralSecurityException {
	    
	    byte[] fullFileAsBytes = IOUtils.toByteArray( privateKeyIn );
	    String fullFileAsString = new String(fullFileAsBytes, "UTF-8");

	    Pattern parse = Pattern.compile("(?m)(?s)^---*BEGIN.*---*$(.*)^---*END.*---*$.*");
	    String encoded = parse.matcher(fullFileAsString).replaceFirst("$1");

	    byte[] keyDecoded = Base64.getMimeDecoder().decode(encoded);
	    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyDecoded);
	    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	    PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

	    return privateKey;
	}
	
	public static KeyStore createKeyStore(InputStream publicCertIn, InputStream privateKeyIn, String password) throws IOException, GeneralSecurityException {
	    
	    KeyStore keyStore = createEmptyKeyStore();	    
	    X509Certificate publicCert = loadCertificate(publicCertIn);
	    Key privateKey = loadPrivateKey(privateKeyIn);  
	    keyStore.setCertificateEntry("Client Cert", publicCert);
	    keyStore.setKeyEntry("Client Key", privateKey, password.toCharArray(), new X509Certificate[]{publicCert});

	    publicCertIn.close();
	    privateKeyIn.close();
	    return keyStore;
	}
	
	public static SSLContext getSSLContext(String password) throws CertificateException, SSLException {
		
		TrustStrategy acceptingTrustStrategy = new TrustStrategy() {
			@Override
			public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {					
				return true;
			}
		};
			
		try {		
			//InputStream cert = Thread.currentThread().getContextClassLoader().getResourceAsStream("certs/spring-client.crt");
			//InputStream key = Thread.currentThread().getContextClassLoader().getResourceAsStream("certs/spring-client.key");
			//KeyStore keyStore = createKeyStore(cert, key,password);
			
			KeyStore keyStore = KeyStore.getInstance("PKCS12");
			keyStore.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("certs/spring-client.p12"), password.toCharArray());
			
			
			SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
					.loadTrustMaterial(null, acceptingTrustStrategy)
					.loadKeyMaterial(keyStore, password.toCharArray())
					.build();
			return sslContext;
		} catch (Exception e) {	
			throw new SSLException("Unable to initialize SSL-context", e);
		} 
	}
	
	public static void init(String password) {
		
		TrustStrategy acceptingTrustStrategy = new TrustStrategy() {
			@Override
			public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {					
				return true;
			}
		};
			
		try {					
			KeyStore keyStore = KeyStore.getInstance("PKCS12");
			keyStore.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("certs/spring-client.p12"), password.toCharArray());
						
			SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
					.loadTrustMaterial(null, acceptingTrustStrategy)
					.loadKeyMaterial(keyStore, password.toCharArray())
					.build();

			SSLContext.setDefault(sslContext);
		} catch (IOException | KeyStoreException | KeyManagementException | UnrecoverableKeyException | NoSuchAlgorithmException | CertificateException e) {	
			e.printStackTrace();
		}
	}
}

