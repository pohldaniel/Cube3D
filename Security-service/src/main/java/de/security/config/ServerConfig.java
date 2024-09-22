package de.security.config;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.UUID;
import java.util.function.Function;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationContext;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ServerConfig {

	@Bean
    @Order(0)
    SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        

		Function<OidcUserInfoAuthenticationContext, OidcUserInfo> userInfoMapper = (context) -> { 
			OidcUserInfoAuthenticationToken authentication = context.getAuthentication();
			JwtAuthenticationToken principal = (JwtAuthenticationToken) authentication.getPrincipal();

			return new OidcUserInfo(principal.getToken().getClaims());
		};
		
        
        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
            .oidc(
            		oidc -> oidc.userInfoEndpoint(userInfo -> userInfo.userInfoMapper(userInfoMapper))
            	  );
        return http.formLogin(withDefaults()).build();
    }
	
	@Bean
    AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
                //.issuer("https://auth-server:8200")
                .authorizationEndpoint("/oauth2/authorize")
                .tokenEndpoint("/oauth2/token")
                .tokenIntrospectionEndpoint("/oauth2/introspect")
                .tokenRevocationEndpoint("/oauth2/revoke")
                .jwkSetEndpoint("/oauth2/jwks")
                .oidcUserInfoEndpoint("/userinfo")
                .oidcClientRegistrationEndpoint("/connect/register")
                .build();
    }
	
	@Bean
	public RegisteredClientRepository registeredClientRepository() {
		return new InMemoryRegisteredClientRepository(this.registeredClient());
	}
	
	private RegisteredClient registeredClient() {
		return RegisteredClient.withId(UUID.randomUUID().toString())
					.clientName("Cube Client")
	                .clientId("cube")
	                .clientSecret("{noop}secret")
	                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
	                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
	                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
	                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
	                .redirectUri("https://localhost:8080/cube/spring/oidc/callback")
	                .scope("openid")	               
	                .build();
	}
}
