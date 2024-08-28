package de.cube3d.utils;

import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import de.cube3d.entities.Action;
import de.cube3d.entities.Person;
import de.cube3d.entities.TopicArea;

public class HibernateUtil {
	
  private static SessionFactory sessionFactory;	
	
  public static void createSessionFactoryH2(String url, String username, String password) {
    if (sessionFactory == null) {
      Configuration configuration = new Configuration();
         
      Properties settings = new Properties();
        
      settings.put(Environment.JAKARTA_JDBC_URL, url);
      settings.put(Environment.JAKARTA_JDBC_USER, username);
      settings.put(Environment.JAKARTA_JDBC_PASSWORD, password);
      settings.put(Environment.JAKARTA_JDBC_DRIVER, "org.h2.Driver");
      settings.put(Environment.DIALECT, "org.hibernate.dialect.H2Dialect");
      settings.put(Environment.POOL_SIZE, 20);
      settings.put("hibernate.connection.min_pool_size", 5);
      settings.put("hibernate.connection.autocommit", false);
      settings.put(Environment.SHOW_SQL, false);
      settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
      //settings.put(Environment.HBM2DDL_AUTO, "create-drop");
      settings.put(Environment.HBM2DDL_AUTO, "update");
      settings.put("hibernate.c3p0.min_size", 5);
      settings.put("hibernate.c3p0.max_size", 20);
      settings.put("hibernate.c3p0.acquire_increment", 5);
      settings.put("hibernate.c3p0.timeout", 1800);
      
      settings.put("hibernate.c3p0.testConnectionOnCheckout", true);
      settings.put("hibernate.c3p0.validate", true);
     

      configuration.setProperties(settings);	
      configuration.addAnnotatedClass(Person.class);
      configuration.addAnnotatedClass(Action.class);              
      configuration.addAnnotatedClass(TopicArea.class);
      
      ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
              .applySettings(configuration.getProperties()).build();

          sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    }
  }
  
  public static SessionFactory getSessionFactory() {
  	return sessionFactory;
  } 
}
