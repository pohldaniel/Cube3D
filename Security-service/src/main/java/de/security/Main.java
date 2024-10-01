package de.security;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.cert.X509Certificate;
import java.util.Locale;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.PropertySource;

import de.security.services.BouncyCastleCertificateGenerator;
import de.security.services.CertificateService;
import de.security.services.CipherService;
import de.security.components.CubePasswordEncoder;
import de.security.utils.Certificate;
import de.security.utils.HibernateUtil;
import de.security.utils.SSLUtil;
import jakarta.annotation.PostConstruct;

@SuppressWarnings("unused")
@SpringBootApplication
@PropertySource({"classpath:application.properties", /*"classpath:application.yml"*/})
public class Main extends SpringBootServletInitializer{
   
	@Autowired
	CipherService cipherService;
	
    public static void main(String[] args) throws Exception  {
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Berlin"));
		Locale.setDefault(Locale.GERMANY);
		
		SSLUtil.init("raupenschmaus");
		SpringApplication.run(Main.class, args);    	  	
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {	    	
		return application.sources(new Class[] { Main.class});
	}   
    
    @PostConstruct
    public void init() throws Exception { 
    	HibernateUtil.createSessionFactoryH2("jdbc:h2:./database/cube;DB_CLOSE_ON_EXIT=FALSE;AUTO_RECONNECT=TRUE", "sa", "main100");
    	CubePasswordEncoder.getInstance().setCipherService(cipherService);
    }
}
