package de.security.utils;

import java.io.File;
import java.io.FileInputStream;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class SSLUtil {
	
	static String TLS = "TLSv1.2";
	static KeyStore ks = null;
	
	public static KeyStore createEmptyKeyStore() throws IOException, GeneralSecurityException {
	    KeyStore keyStore = KeyStore.getInstance("PKCS12");
	    keyStore.load(null,null);
	    return keyStore;
	}
	
	public static X509Certificate loadCertificate(String path) throws IOException, GeneralSecurityException {
	    CertificateFactory factory = CertificateFactory.getInstance("X.509");
	    X509Certificate cert = (X509Certificate)factory.generateCertificate(new FileInputStream(new File(path)));
	    return cert;
	}
	
	public static KeyStore createKeyStore(String certPath) throws IOException, GeneralSecurityException {		
		KeyStore keyStore = createEmptyKeyStore();
		X509Certificate publicCert = loadCertificate(certPath);		
		keyStore.setCertificateEntry("Vault Issuing", publicCert);

		TrustManagerFactory tmf= TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		tmf.init(keyStore);
		
		return keyStore;	
	}
	
	public static void saveKeyStore(String certPath, String keyStorePath, String keyStorePasswd) throws IOException, GeneralSecurityException {
		KeyStore keyStore = createKeyStore(certPath);
		keyStore.store(new FileOutputStream(keyStorePath), keyStorePasswd.toCharArray());
	}
	
	public static void setSSLContext(String certPath) throws IOException, GeneralSecurityException {
		TrustManagerFactory tmf = TrustManagerFactory
			    .getInstance(TrustManagerFactory.getDefaultAlgorithm());
		// Using null here initialises the TMF with the default trust store.
		tmf.init((KeyStore) null);

		// Get hold of the default trust manager
		X509TrustManager defaultTm = null;
		for (TrustManager tm : tmf.getTrustManagers()) {
			 if (tm instanceof X509TrustManager) {
			     defaultTm = (X509TrustManager) tm;
			     break;
			  }
		}
		
		
		KeyStore keyStore = createEmptyKeyStore();
		X509Certificate publicCert = loadCertificate(certPath);		
		keyStore.setCertificateEntry("Vault Issuing", publicCert);

		tmf= TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		tmf.init(keyStore);
		
		X509TrustManager myTm = null;
		for (TrustManager tm : tmf.getTrustManagers()) {
		    if (tm instanceof X509TrustManager) {
		        myTm = (X509TrustManager) tm;
		        break;
		    }
		}
		
		final X509TrustManager finalDefaultTm = defaultTm;
		final X509TrustManager finalMyTm = myTm;
		X509TrustManager customTm = new X509TrustManager() {
		    @Override
		    public X509Certificate[] getAcceptedIssuers() {
		        // If you're planning to use client-cert auth,
		        // merge results from "defaultTm" and "myTm".
		        return finalDefaultTm.getAcceptedIssuers();
		    }

		    @Override
		    public void checkServerTrusted(X509Certificate[] chain,
		            String authType) throws CertificateException {
		        try {
		            finalMyTm.checkServerTrusted(chain, authType);
		        } catch (CertificateException e) {
		            // This will throw another CertificateException if this fails too.
		            finalDefaultTm.checkServerTrusted(chain, authType);
		        }
		    }

		    @Override
		    public void checkClientTrusted(X509Certificate[] chain,
		            String authType) throws CertificateException {
		        // If you're planning to use client-cert auth,
		        // do the same as checking the server.
		        finalDefaultTm.checkClientTrusted(chain, authType);
		    }
		};
		
		SSLContext sslContext = SSLContext.getInstance("TLS");
		sslContext.init(null, new TrustManager[] { customTm }, null);
		SSLContext.setDefault(sslContext);
	}
}
