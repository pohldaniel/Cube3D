package de.cube3d.database;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * This class just has a function that runs on startup of the Application.
 * If the "fill-database-on-startup=" in application.properties is set to "true" the function will run and write a few objects to the in memory database.
 * This database is saved in /etc/data/localdb.mv.db
 */
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


