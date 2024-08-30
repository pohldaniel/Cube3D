package de.cube3d.utils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class SSLUtil {

	//"TLS", "TLSv1", "TLSv1.1","TLSv1.2","TLSv1.3", "SSLv3"
	static String TLS = "TLSv1.2";

	private SSLUtil() {		
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
}

