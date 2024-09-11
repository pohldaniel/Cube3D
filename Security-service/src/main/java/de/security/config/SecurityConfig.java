package de.security.config;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.web.SecurityFilterChain;

import de.security.SpringContext;
import de.security.components.CertificateAuthenticationProvider;
import de.security.components.PasswordAuthenticationProvider;
import de.security.service.PasswordUserDetailsService;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
	
	/*@Bean
    @Order(2)
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizeRequests -> authorizeRequests.anyRequest()
                .authenticated())
            .userDetailsService(passwordUserDetailsService())
            .authenticationProvider(passwordAuthenticationProvider())
            .formLogin(withDefaults());
        return http.build();
    }*/
	
	@Bean
	public PasswordUserDetailsService passwordUserDetailsService() {		
		return new PasswordUserDetailsService();
	}
	
	@Bean
	public PasswordAuthenticationProvider passwordAuthenticationProvider() {		
		return new PasswordAuthenticationProvider();
	}
	
	@Bean
	public CertificateAuthenticationProvider certificateAuthenticationProvider() {		
		return new CertificateAuthenticationProvider();
	}
	
	@Bean
	@Order(2)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		 http
		 .authorizeHttpRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated())
		 .x509(cert ->cert.subjectPrincipalRegex("CN=(.*?)(?:,|$)"))
		 .userDetailsService(passwordUserDetailsService())
		 .authenticationProvider(certificateAuthenticationProvider()).formLogin(withDefaults());
		 //.oauth2Login(withDefaults());
		 //.formLogin(login -> login.loginPage("/login"));
		 //.oauth2Login(oauth2Login ->
		 	//oauth2Login.loginPage("/oauth2/authorize/cube"))
		 //.oauth2Login(withDefaults());
		 //.oauth2Client(withDefaults());   
	     return http.build();
	}
	
	/*@Bean
	public CertificateUserDetailsService certificateUserDetailsService() {		
		return new CertificateUserDetailsService();
	}
	
	@Bean
	public CertificateAuthenticationProvider certificateAuthenticationProvider() {		
		return new CertificateAuthenticationProvider();
	}
	
	@Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(this.cubeClientRegistration());
    }
	
	private ClientRegistration cubeClientRegistration() {
		return ClientRegistration.withRegistrationId(UUID.randomUUID().toString())
	                .clientId("cube")
	                .clientSecret("{noop}secret")
	                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
	                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
	                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
	                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
	                .redirectUri("https://localhost:8080/spring/oidc/callback")
	                .scope("openid")
	                .authorizationUri("https://auth-server:8200/oauth2/authorize")
	                .tokenUri("https://auth-server:8200/oauth2/token")	               
	                .clientName("Cube Client").p
	                .build();
	}*/	
}