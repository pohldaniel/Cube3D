package de.security.config;

import java.util.Collections;

import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.servlet.SessionTrackingMode;

@Configuration
public class WebConfig implements WebMvcConfigurer{

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/js/**", "/css/**")
		.addResourceLocations("classpath:/js/", "classpath:/css/");
	}
	
	@Bean
	public ServletContextInitializer servletContextInitializer() {
	    return servletContext -> servletContext.setSessionTrackingModes(
	    	Collections.singleton(SessionTrackingMode.COOKIE));
	}
}