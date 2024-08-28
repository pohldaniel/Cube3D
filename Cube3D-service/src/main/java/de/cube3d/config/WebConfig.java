package de.cube3d.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring6.ISpringTemplateEngine;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer{

	@Autowired
	private ApplicationContext applicationContext;
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/js/**", "/css/**")
		.addResourceLocations("classpath:/js/", "classpath:/css/");
	}
	
	@Bean
    public ViewResolver htmlViewResolver() {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine(htmlTemplateResolver()));
        resolver.setContentType("text/html");
        resolver.setCharacterEncoding("UTF-8");
        resolver.setViewNames(new String[]{"*.html"});
        return resolver;
    }
			
	@Bean
	public ISpringTemplateEngine templateEngine(ITemplateResolver templateResolver) {
		SpringTemplateEngine engine = new SpringTemplateEngine();
		engine.setEnableSpringELCompiler(true);
		engine.setTemplateResolver(templateResolver);
		return engine;
	}

	private ITemplateResolver htmlTemplateResolver() {
		SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
		resolver.setApplicationContext(applicationContext);
		resolver.setPrefix("classpath:/views/");
		resolver.setSuffix(".html");
		resolver.setCacheable(false);
		resolver.setTemplateMode(TemplateMode.HTML);
		return resolver;
	}
}
