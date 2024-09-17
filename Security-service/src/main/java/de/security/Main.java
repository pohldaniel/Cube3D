package de.security;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.cert.X509Certificate;
import java.util.Locale;
import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.PropertySource;

import de.security.service.BouncyCastleCertificateGenerator;
import de.security.service.CertificateService;
import de.security.utils.Certificate;
import de.security.utils.HibernateUtil;
import de.security.utils.SSLUtil;
import jakarta.annotation.PostConstruct;

@SuppressWarnings("unused")
@SpringBootApplication
@PropertySource({"classpath:application.properties", /*"classpath:application.yml"*/})
public class Main extends SpringBootServletInitializer{
   
    public static void main(String[] args) throws Exception  {
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Berlin"));
		Locale.setDefault(Locale.GERMANY);
		//SSLUtil.saveKeyStore("C:\\workspace-cube3d\\security-service\\src\\main\\resources\\certs\\vault-iss.crt", "C:\\workspace-cube3d\\security-service\\src\\main\\resources\\certs\\spring-trust.p12", "password");
		//System.setProperty("javax.net.ssl.trustStore", "classpath:certs/spring-trust.p12"); 
		//System.setProperty("javax.net.ssl.trustStorePassword", "password");
		//System.setProperty("javax.net.ssl.trustStoreType", "PKCS12");
		SSLUtil.init("password");
		SpringApplication.run(Main.class, args);    	  	
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {	    	
		return application.sources(new Class[] { Main.class});
	}   
    
    @PostConstruct
    public void init() throws Exception { 
    	HibernateUtil.createSessionFactoryH2("jdbc:h2:./database/cube;DB_CLOSE_ON_EXIT=FALSE;AUTO_RECONNECT=TRUE", "sa", "main100");
    	
    	RSAPublicKey isspub = CertificateService.readX509PublicKey(org.apache.commons.io.IOUtils.toInputStream(Certificate.ISS_PUBLIC, "UTF-8"));
    	RSAPrivateKey isskey = CertificateService.loadPrivateKey(org.apache.commons.io.IOUtils.toInputStream(Certificate.ISS_PRIVATE, "UTF-8"));
    	X509Certificate isscert = CertificateService.loadX509Certificate(org.apache.commons.io.IOUtils.toInputStream(Certificate.ISS_CERTIFICATE, "UTF-8"));
    	X509Certificate intcert = CertificateService.loadX509Certificate(org.apache.commons.io.IOUtils.toInputStream(Certificate.INT_CERTIFICATE, "UTF-8"));
    	
    	
    	//BouncyCastleCertificateGenerator.createUserStore(intcert, isscert, isspub, isskey);
    }
}
