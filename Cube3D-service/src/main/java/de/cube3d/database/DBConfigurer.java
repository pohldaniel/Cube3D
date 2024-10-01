package de.cube3d.database;

import de.cube3d.dao.ActionDao;
import de.cube3d.dao.PersonDao;
import de.cube3d.dao.TopicAreaDao;
import de.cube3d.entities.Action;
import de.cube3d.entities.Person;
import de.cube3d.entities.TopicArea;
import de.cube3d.entities.builder.ActionBuilder;
import de.cube3d.entities.builder.PersonBuilder;
import de.cube3d.entities.builder.TopicAreaBuilder;
import de.cube3d.entities.enums.Role;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.GregorianCalendar;

public class DBConfigurer {
	
	private PersonDao personDao = PersonDao.getInstance();
	private ActionDao actionDao = ActionDao.getInstance();
	private TopicAreaDao topicAreaDao = TopicAreaDao.getInstance();

    public static final ArrayList<Action> actionRepositoryContent = new ArrayList<Action>();
    public static final ArrayList<TopicArea> topicAreaRepositoryContent = new ArrayList<TopicArea>();
    public static final ArrayList<Person> personRepositoryContent = new ArrayList<Person>();

    public void emptyRepositories() {      
    	actionDao.deleteAll();
    	topicAreaDao.deleteAll();
    	personDao.deleteAll();       
        actionRepositoryContent.clear();
        topicAreaRepositoryContent.clear();
        personRepositoryContent.clear();
    }

   
    public void fillRepositories() {
        //ACTIONS FIRST
        Action a1Created = actionDao.save(a1);
        Action a2Created = actionDao.save(a2);
        Action a3Created = actionDao.save(a3);
        Action a4Created = actionDao.save(a4);
        Action a5Created = actionDao.save(a5);
        actionRepositoryContent.add(a1Created);
        actionRepositoryContent.add(a2Created);
        actionRepositoryContent.add(a3Created);
        actionRepositoryContent.add(a4Created);
        actionRepositoryContent.add(a5Created);

        //PERSONS FOURTH
        Person p1Created = personDao.save(p1); 
        Person p2Created = personDao.save(p2);
        Person p3Created = personDao.save(p3);
        Person p4Created = personDao.save(p4);
        Person p5Created = personDao.save(p5);
        Person p6Created = personDao.save(p6);
        Person p7Created = personDao.save(p7);
        personRepositoryContent.add(p1Created);
        personRepositoryContent.add(p2Created);
        personRepositoryContent.add(p3Created);
        personRepositoryContent.add(p4Created);
        personRepositoryContent.add(p5Created);
        personRepositoryContent.add(p6Created);
        personRepositoryContent.add(p7Created);

        //TOPICAREAS FIFTH
        TopicArea t1Created = topicAreaDao.save(t1);
        t2.addPerson(p1Created);
        t2.addPerson(p2Created);
        t2.addPerson(p3Created);
        t2.addPerson(p4Created);
        TopicArea t2Created = topicAreaDao.save(t2);
        TopicArea t3Created = topicAreaDao.save(t3);
        t4.addPerson(p1Created);
        t4.addPerson(p2Created);
        t4.addPerson(p3Created);
        t4.addPerson(p4Created);
        TopicArea t4Created = topicAreaDao.save(t4);
        TopicArea t5Created = topicAreaDao.save(t5);
        TopicArea t6Created = topicAreaDao.save(t6);
        TopicArea t7Created = topicAreaDao.save(t7);
        topicAreaRepositoryContent.add(t1Created);
        topicAreaRepositoryContent.add(t2Created);
        topicAreaRepositoryContent.add(t3Created);
        topicAreaRepositoryContent.add(t4Created);
        topicAreaRepositoryContent.add(t5Created);
        topicAreaRepositoryContent.add(t6Created);
        topicAreaRepositoryContent.add(t7Created);       
    }

    private Action a1 = new ActionBuilder()
            .name("Action 1")
            .startDate(new Timestamp(new GregorianCalendar(2019, 4, 1).getTimeInMillis()))
            .endDate(new Timestamp(new GregorianCalendar(2019, 4, 8).getTimeInMillis()))
            .finishedPlanning(true)
            .finished(false)
            .build();
    private Action a2 = new ActionBuilder()
            .name("Action 2")
            .startDate(new Timestamp(new GregorianCalendar(2019, 5, 1).getTimeInMillis()))
            .endDate(new Timestamp(new GregorianCalendar(2019, 5, 6).getTimeInMillis()))
            .finishedPlanning(false)
            .finished(false)
            .build();
    private Action a3 = new ActionBuilder()
            .name("Action 3")
            .startDate(new Timestamp(new GregorianCalendar(2019, 3, 1).getTimeInMillis()))
            .endDate(new Timestamp(new GregorianCalendar(2019, 3, 4).getTimeInMillis()))
            .finishedPlanning(true)
            .finished(true)
            .build();
    private Action a4 = new ActionBuilder()
            .name("Action 4")
            .startDate(new Timestamp(new GregorianCalendar(2019, 3, 1).getTimeInMillis()))
            .endDate(new Timestamp(new GregorianCalendar(2019, 3, 4).getTimeInMillis()))
            .finishedPlanning(false)
            .finished(true)
            .build();
    private Action a5 = new ActionBuilder()
            .name("Action 5")
            .startDate(new Timestamp(new GregorianCalendar(2019, 3, 1).getTimeInMillis()))
            .endDate(new Timestamp(new GregorianCalendar(2019, 3, 4).getTimeInMillis()))
            .finishedPlanning(false)
            .finished(true)
            .build();
    
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
            .roles(Role.ADMIN)
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
    
    private TopicArea t1 = new TopicAreaBuilder()
            .name("OMS")
            .build();
    private TopicArea t2 = new TopicAreaBuilder()
            .name("AIMS")
            .build();
    private TopicArea t3 = new TopicAreaBuilder()
            .name("Coffee")
            .build();
    private TopicArea t4 = new TopicAreaBuilder()
            .name("Elastic")
            .build();
    private TopicArea t5 = new TopicAreaBuilder()
            .name("Organisation")
            .build();
    private TopicArea t6 = new TopicAreaBuilder()
            .name("Cloud")
            .build();
    private TopicArea t7 = new TopicAreaBuilder()
            .name("Illnes")
            .build();
   
}
