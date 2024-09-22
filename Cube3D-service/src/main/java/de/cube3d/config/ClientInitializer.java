package de.cube3d.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.cube3d.components.SpringOIDCClient;
import de.cube3d.components.VaultOIDCClient;

@Configuration
public class ClientInitializer {
	
	@Value("${oidc.spring.client.endpoint}") 
	private String endpointSpring;
	
	@Value("${oidc.spring.client.id}") 
	private String clientIdSpring;

	@Value("${oidc.spring.client.secret}")
	private String clientSecretSpring;
	
	@Value("${oidc.vault.client.endpoint}") 
	private String endpointVault;
	
	@Value("${oidc.vault.client.id}") 
	private String clientIdVault;

	@Value("${oidc.vault.client.secret}")
	private String clientSecretVault;
	
	@Value("${oidc.vault.client.provider}")
	private String clientProviderVault;
	
	@Bean
	public SpringOIDCClient springOIDCClient() {
		return new SpringOIDCClient.Builder()
				.endpoint(endpointSpring + "/idp")
				.clientId(clientIdSpring)
				.clientSecret(clientSecretSpring)	
				.build(); 
	}
	
	@Bean
	public VaultOIDCClient vaultOIDCClient() {
		return new VaultOIDCClient.Builder()
				.endpoint(endpointVault)
				.clientId(clientIdVault)
				.clientSecret(clientSecretVault)	
				.provider(clientProviderVault)
				.build(); 
	}
}
