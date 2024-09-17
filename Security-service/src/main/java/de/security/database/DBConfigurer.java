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
    }

    private Person p1 = new PersonBuilder()
            .id("admin")
            .prename("Anke")
            .surname("Admin")
            .mail("test@test.de")
            .externalCompany("N.N.")
            .passwordHash("3ecf9427c31104fed1364eb3f640671ad22d02b73bdcc5aa78edaf33bbcd659d11c616c3097308547a10131c513d55cf939645fbe1d56e1931d030c18741c616")
            .roles(Role.ADMIN, Role.USER)
            .build();
    private Person p2 = new PersonBuilder()
            .id("actionmanager")
            .prename("Alex")
            .surname("Actionmanager")
            .mail("test@test.de")
            .externalCompany("N.N.")
            .passwordHash("3ecf9427c31104fed1364eb3f640671ad22d02b73bdcc5aa78edaf33bbcd659d11c616c3097308547a10131c513d55cf939645fbe1d56e1931d030c18741c616")
            .roles(Role.ACTION_MANAGER)
            .build();
    private Person p3 = new PersonBuilder()
            .id("topicmanager")
            .prename("Rolf")
            .surname("Topicmanager")
            .mail("test@test.de")
            .externalCompany("N.N.")
            .passwordHash("3ecf9427c31104fed1364eb3f640671ad22d02b73bdcc5aa78edaf33bbcd659d11c616c3097308547a10131c513d55cf939645fbe1d56e1931d030c18741c616")
            .roles(Role.TOPICAREA_MANAGER)
            .build();
    private Person p4 = new PersonBuilder()
            .id("user")
            .prename("Bibi")
            .surname("User")
            .mail("test@test.de")
            .externalCompany("N.N.")
            .passwordHash("3ecf9427c31104fed1364eb3f640671ad22d02b73bdcc5aa78edaf33bbcd659d11c616c3097308547a10131c513d55cf939645fbe1d56e1931d030c18741c616")
            .roles(Role.USER)
            .build();    
    private Person p5 = new PersonBuilder()
            .id("personmanager")
            .prename("Frank")
            .surname("Schulze")
            .mail("test@test.de")
            .externalCompany("N.N.")
            .passwordHash("3ecf9427c31104fed1364eb3f640671ad22d02b73bdcc5aa78edaf33bbcd659d11c616c3097308547a10131c513d55cf939645fbe1d56e1931d030c18741c616")
            .roles(Role.PERSON_MANAGER)
            .build();
}
