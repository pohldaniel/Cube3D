package de.security.config;

import static org.springframework.security.config.Customizer.withDefaults;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
	
	@Bean
    @Order(2)
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizeRequests -> authorizeRequests.anyRequest()
                .authenticated())
            .formLogin(withDefaults());
        return http.build();
    }
	
	@Bean
	UserDetailsService users() {
		PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		UserDetails user = User.builder()
							   .username("admin")
	                           .password("password")
	                           .passwordEncoder(encoder::encode)
	                           .roles("USER")
	                           .build();
		return new InMemoryUserDetailsManager(user);
	}

	
	/*@Bean
	@Order(2)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		 http
		 .authorizeHttpRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated())
		 .x509(cert ->cert.subjectPrincipalRegex("CN=(.*?)(?:,|$)"))
		 .userDetailsService(userDetailsService())
		 //.formLogin(login -> login.loginPage("/login"));
		 //.oauth2Login(oauth2Login ->
		 	//oauth2Login.loginPage("/oauth2/authorize/cube"))
		 //.oauth2Login(withDefaults());
		 .oauth2Client(withDefaults());   
	     return http.build();
	}

	@Bean
	public UserDetailsService userDetailsService() {
		return new UserDetailsService() {
			@Override
			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
				System.out.println("Name: " + username);
				if (username.equals("Daniel Pohl")) {
					return new User(username, "{noop}password", AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER"));
				}
				throw new UsernameNotFoundException("User not found!");
			}
		};
	}*/
	
}
