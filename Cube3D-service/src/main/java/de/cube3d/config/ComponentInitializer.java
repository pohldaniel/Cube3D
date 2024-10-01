package de.cube3d.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.cube3d.components.CubePasswordEncoder;

@Configuration
public class ComponentInitializer {
	
	@Bean
	CubePasswordEncoder cubePasswordEncoder() {
		return new CubePasswordEncoder();
	}
}
