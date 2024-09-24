package de.security;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import de.security.service.CertificateService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CertificateFilter extends OncePerRequestFilter{
	
	private static final Logger LOG = LoggerFactory.getLogger(CertificateFilter.class);
	private static final String JAVAX_SERVLET_REQUEST_X509_CERTIFICATE = "jakarta.servlet.request.X509Certificate";
	
	@Override
	protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
	        X509Certificate x509Certificate = extractClientCertificate(httpServletRequest);
	        if (x509Certificate == null) {
	        	String x509CertHeader = httpServletRequest.getHeader("ssl-client-cert");	           
	        	try{	                
		        	x509CertHeader = URLDecoder.decode(x509CertHeader, StandardCharsets.UTF_8.toString());
	        		X509Certificate certificate = CertificateService.loadX509Certificate(x509CertHeader);                
	                httpServletRequest.setAttribute(JAVAX_SERVLET_REQUEST_X509_CERTIFICATE, new X509Certificate[]{certificate});
	            } catch (Exception e) {
	                LOG.error("X.509 certificate could not be created out of the header field {}. Exception: {}", e.getMessage());
	            }
	        }

	        filterChain.doFilter(httpServletRequest, httpServletResponse);
	    }

	private X509Certificate extractClientCertificate(HttpServletRequest request) {
		X509Certificate[] certs = (X509Certificate[]) request.getAttribute(JAVAX_SERVLET_REQUEST_X509_CERTIFICATE);

		if (certs != null && certs.length > 0) {
			LOG.debug("X.509 client authentication certificate:" + certs[0]);
			return certs[0];
		}

		LOG.debug("No client certificate found in request.");
		return null;
	}
}
