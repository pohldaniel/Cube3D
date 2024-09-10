package de.security.database;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DevDBInitializer implements InitializingBean {

    private DBConfigurer dbConfigurer;

    @Value("${empty-and-refill-on-startup}")
    private String emptyAndRefillDatabase;

    public DevDBInitializer() {
        this.dbConfigurer = new DBConfigurer();
    }

    @Override
    public void afterPropertiesSet() {
        if (emptyAndRefillDatabase.equals("true")) {
            dbConfigurer.emptyRepositories();
            dbConfigurer.fillRepositories();
        }
    }
}


