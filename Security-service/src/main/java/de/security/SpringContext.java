package de.security;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContext implements ApplicationContextAware{

	private static ApplicationContext context;
    
    public static <T extends Object> T getBean(Class<T> beanClass) {
        return context.getBean(beanClass);
    }
     
    public static String getPropertyStr(String key) {
    	return context.getEnvironment().getProperty(key); 
    }
    
    public static boolean getPropertyBool(String key) {
    	return Boolean.parseBoolean(context.getEnvironment().getProperty(key)); 
    }
    
    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {         
        SpringContext.context = context;
    }
}