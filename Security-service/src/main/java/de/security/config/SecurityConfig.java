package de.security.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import de.security.components.CertificateAuthenticationProvider;
import de.security.components.PasswordAuthenticationProvider;
import de.security.service.CubeUserDetailsService;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
	
	@Bean(name = "cubeUserDetailsService")
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
    public AuthenticationManager authenticationManagerBean(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(cubeUserDetailsService());
        return authenticationManagerBuilder.build();
    }
	
	@Bean
	@Order(1)
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
		.securityMatcher("/h2-console/**")
		.authorizeHttpRequests(auth -> auth.requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll())
	                .headers(headers -> headers.frameOptions(withDefaults()).disable())
	                .csrf(csrf -> csrf.ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")));
		return http.build();
	}
	
	@Bean
	@Order(2)
	public SecurityFilterChain downloadFilterChain(HttpSecurity http) throws Exception {
		http
		.securityMatcher("/download/**")
		.authorizeHttpRequests(auth -> auth.requestMatchers(AntPathRequestMatcher.antMatcher("/download/**")).permitAll())
	                .headers(headers -> headers.frameOptions(withDefaults()).disable())
	                .csrf(csrf -> csrf.ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/download/**")));
		return http.build();
	}
	
	@Bean
    @Order(3)
    SecurityFilterChain formFilterChain(HttpSecurity http) throws Exception {
        http
        .securityMatcher("/cert/**") 
        .authorizeHttpRequests(authorizeRequests -> authorizeRequests.requestMatchers("/images/**").permitAll().anyRequest().authenticated())
            .userDetailsService(cubeUserDetailsService())
            .authenticationProvider(passwordAuthenticationProvider())
            .formLogin(form->form.loginPage("/cert").loginProcessingUrl("/cert/login").defaultSuccessUrl("/cert/dashboard", true).permitAll());
        return http.build();
    }
	
	@Bean
	@Order(4)
    public SecurityFilterChain certFilterChain(HttpSecurity http) throws Exception {
		 http
		 //.securityMatcher("/login") 
		 .authorizeHttpRequests(authorizeRequests -> authorizeRequests.requestMatchers("/images/**","/css/**", "/message").permitAll().anyRequest().authenticated())
		 .x509(cert ->cert.subjectPrincipalRegex("CN=(.*?)(?:,|$)"))
		 .userDetailsService(cubeUserDetailsService())
		 .authenticationProvider(certificateAuthenticationProvider()).formLogin(form->form
                 .loginPage("/login")
                 .loginProcessingUrl("/perform_login")
                 //.successForwardUrl("/login_success_handler")
                 .permitAll());
	     return http.build();
	}
	
	
	/*private ClientRegistration cubeClientRegistration() {
		return ClientRegistration.withRegistrationId(UUID.randomUUID().toString())
	                .clientId("cube")
	                .clientSecret("{noop}secret")
	                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
	                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
	                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
	                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
	                .redirectUri("https://localhost:8080/spring/oidc/callback")
	                .scope("openid")
	                .authorizationUri("https://auth-server:8443/oauth2/authorize")
	                .tokenUri("https://auth-server:8443/oauth2/token")	               
	                .clientName("Cube Client").p
	                .build();
	}*/	
}