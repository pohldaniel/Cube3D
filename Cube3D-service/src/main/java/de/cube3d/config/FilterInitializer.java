package de.cube3d.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.cube3d.filter.AuthFilter;
import de.cube3d.filter.OIDCFilter;
import de.cube3d.filter.RestAPIFilter;

@Configuration
public class FilterInitializer {
	
	@Bean(name="restAPI")
	public FilterRegistrationBean<RestAPIFilter> restAPIFilterRegistration() {
		FilterRegistrationBean<RestAPIFilter> registration = new FilterRegistrationBean<RestAPIFilter>();
		registration.setFilter(new RestAPIFilter());
		registration.addUrlPatterns("/cube/restAPI/*");
		return registration;
	}
	
	@Bean(name="auth")
	public FilterRegistrationBean<AuthFilter> authFilterRegistration() {
		FilterRegistrationBean<AuthFilter> registration = new FilterRegistrationBean<AuthFilter>();
		registration.setFilter(new AuthFilter());
		registration.addUrlPatterns("/auth/*");
		return registration;
	}
	
	@Bean(name="oidc")
	public FilterRegistrationBean<OIDCFilter> oidcFilterRegistration() {
		FilterRegistrationBean<OIDCFilter> registration = new FilterRegistrationBean<OIDCFilter>();
		registration.setFilter(new OIDCFilter());
		registration.addUrlPatterns("/oidc/*", "/login");
		return registration;
	}
}
