package de.security.database;

import de.security.dao.PersonDao;
import de.security.entities.Person;
import de.security.entities.builder.PersonBuilder;
import de.security.entities.enums.Role;

public class DBConfigurer {
	private PersonDao personDao = PersonDao.getInstance();

    public void emptyRepositories() {      
    	personDao.deleteAll();       
    }
  
    public void fillRepositories() {       
        personDao.save(p1);
        personDao.save(p2);
        personDao.save(p3);
        personDao.save(p4);
        personDao.save(p5);
        personDao.save(p6);
        personDao.save(p7);
    }

    private Person p1 = new PersonBuilder()
            .id("admin")
            .prename("Anke")
            .surname("Admin")
            .mail("test@test.de")
            .externalCompany("N.N.")
            .passwordHash("73b3b928c7bf55e29d7d8e2840eadc")
            .roles(Role.ADMIN, Role.USER)
            .build();
    private Person p2 = new PersonBuilder()
            .id("actionmanager")
            .prename("Alex")
            .surname("Actionmanager")
            .mail("test@test.de")
            .externalCompany("N.N.")
            .passwordHash("73b3b928c7bf55e29d7d8e2840eadc")
            .roles(Role.ACTION_MANAGER)
            .build();
    private Person p3 = new PersonBuilder()
            .id("topicmanager")
            .prename("Rolf")
            .surname("Topicmanager")
            .mail("test@test.de")
            .externalCompany("N.N.")
            .passwordHash("73b3b928c7bf55e29d7d8e2840eadc")
            .roles(Role.TOPICAREA_MANAGER)
            .build();
    private Person p4 = new PersonBuilder()
            .id("user")
            .prename("Bibi")
            .surname("User")
            .mail("test@test.de")
            .externalCompany("N.N.")
            .passwordHash("73b3b928c7bf55e29d7d8e2840eadc")
            .roles(Role.USER)
            .build();    
    private Person p5 = new PersonBuilder()
            .id("personmanager")
            .prename("Frank")
            .surname("Schulze")
            .mail("test@test.de")
            .externalCompany("N.N.")
            .passwordHash("73b3b928c7bf55e29d7d8e2840eadc")
            .roles(Role.PERSON_MANAGER)
            .build(); 
    private Person p6 = new PersonBuilder()
            .id("jnicolai")
            .prename("Jürgen")
            .surname("Nicolai")
            .mail("j.nicolai@main-gruppe.de")
            .externalCompany("N.N.")
            .passwordHash("7f13cb535270f67e99dd42b168cdb9")
            .roles(Role.USER)
            .build();
    private Person p7 = new PersonBuilder()
            .id("dpohl")
            .prename("Daniel")
            .surname("Pohl")
            .mail("d.pohl@main-gruppe.de")
            .externalCompany("N.N.")
            .passwordHash("73b3b928c7bf55e29d7d8e2840eadc")
            .roles(Role.ADMIN)
            .build();
}
