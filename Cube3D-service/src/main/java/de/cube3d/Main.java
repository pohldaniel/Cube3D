package de.cube3d;

import java.util.Locale;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import de.cube3d.components.SpringOIDCClient;
import de.cube3d.components.VaultOIDCClient;
import de.cube3d.filter.RestAPIFilter;
import de.cube3d.service.JwtService;
import de.cube3d.utils.HibernateUtil;
import de.cube3d.utils.SSLUtil;
import jakarta.annotation.PostConstruct;

/** starts the application in case everything is running check "localhost:8080/h2-console" */
@SpringBootApplication
public class Main extends SpringBootServletInitializer{

	@Value("${jwt.secret.key}")
	private String jwtSecretKey;
	
	@Value("${token.force.valiadtion}")
	private boolean forceValidation;
	
	@Autowired
	@Qualifier("restAPI")
	private FilterRegistrationBean<RestAPIFilter> restAPIFilterRegistration;
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private SpringOIDCClient springOIDCClient;
	
	@Autowired
	private VaultOIDCClient vaultOIDCClient;

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Berlin"));
		Locale.setDefault(Locale.GERMANY);
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
		jwtService.setSecretKey(jwtSecretKey);
		restAPIFilterRegistration.getFilter().setJwtService(jwtService);
		restAPIFilterRegistration.getFilter().setSpringOIDCClient(springOIDCClient);
		restAPIFilterRegistration.getFilter().setVaultOIDCClient(vaultOIDCClient);
		restAPIFilterRegistration.getFilter().setForceValidation(forceValidation);
	}
}
