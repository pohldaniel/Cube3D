package de.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import de.security.components.CertificateAuthenticationProvider;
import de.security.components.PasswordAuthenticationProvider;
import de.security.service.CubeUserDetailsService;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
	
	@Bean
	public CubeUserDetailsService cubeUserDetailsService() {		
		return new CubeUserDetailsService();
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
		 .authorizeHttpRequests(authorizeRequests -> authorizeRequests.requestMatchers("/images/**","/css/**", "/login", "/message").permitAll().requestMatchers(HttpMethod.POST, "/perform_login").permitAll().anyRequest().authenticated())
		 .x509(cert ->cert.subjectPrincipalRegex("CN=(.*?)(?:,|$)"))
		 .userDetailsService(cubeUserDetailsService())
		 .authenticationProvider(certificateAuthenticationProvider()).formLogin(form->form
                 .loginPage("/login")
                 .loginProcessingUrl("/perform_login")
                 //.successForwardUrl("/login_success_handler")
                 .permitAll()
                 );
	     return http.build();
	}
	
	/*@Bean
    @Order(2)
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizeRequests -> authorizeRequests.anyRequest()
                .authenticated())
            .userDetailsService(cubeUserDetailsService())
            .authenticationProvider(passwordAuthenticationProvider())
            .formLogin(withDefaults());
        return http.build();
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