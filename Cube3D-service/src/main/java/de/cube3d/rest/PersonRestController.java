package de.cube3d.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import de.cube3d.dao.PersonDao;
import de.cube3d.entities.Person;
import de.cube3d.entities.enums.Role;

@RestController
@RequestMapping("/restAPI")
public class PersonRestController {

	private Logger log = LoggerFactory.getLogger(PersonRestController.class);	
	private PersonDao personDao = PersonDao.getInstance();
    
    @GetMapping(value = "/persons/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<Person>> getAllPersons() {  	    	
    	Iterable<Person> persons = personDao.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(persons);
    }

    @GetMapping(value = "/persons/getById", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getById(String id) {
        log.info("/persons/getByBid was called");
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(personDao.findById(id));
        } catch (NoSuchElementException e) {
            log.info("/persons/getByBid failed with error message: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Person nicht gefunden");
        }
    }

    /**requestAttribute is set at RestAPIFilter.java*/
    @PostMapping(value = "/persons/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createPerson(@RequestBody Person person, @RequestAttribute("roles") List<Role> roles) {
        log.info("/persons/create was called");
        ObjectMapper mapper = new ObjectMapper(); 
    	ObjectNode statusMap = mapper.createObjectNode();
        
        if (!roles.contains(Role.ADMIN)) {
        	statusMap.put("message", "No access granted for this action");       
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(statusMap);
        }
        
        try {      	
            if(personDao.findById(person.getId()) == null) {
	            Person createdPerson = personDao.save(person);
	            String token = UUID.randomUUID().toString();
            
	            Calendar calendar = Calendar.getInstance();
	            calendar.add(GregorianCalendar.HOUR, 24);
	            createdPerson.setPasswordResetToken(token);
	            createdPerson.setPasswordResetTokenExpiryDate(calendar);
	            createdPerson = personDao.save(createdPerson);
	            
	            return ResponseEntity.status(HttpStatus.CREATED).body(statusMap);
            }else {
            	statusMap.put("message", "Person already exist");            	
            	return ResponseEntity.status(HttpStatus.CONFLICT).body(statusMap);
            }
                   
        } catch (IllegalArgumentException e) {
            log.info("/persons/create failed with error message: " + e.getMessage());
            statusMap.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(statusMap);
        }
    }

    @PostMapping(value = "/persons/delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> deleteAction(@RequestBody Person person) {
        log.info("/persons/delete was called");
        try {
            Person p = personDao.findById(person.getId());
            personDao.delete(p);
        } catch (Exception e) {
            log.info("/persons/delete failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    
    @RequestMapping(value = "/persons/update", method = RequestMethod.POST)
    public ResponseEntity<Object> update(@RequestBody Person person, @RequestAttribute("roles") List<Role> roles) {
    	log.info("/persons/update was called");
    	
    	if (!roles.contains(Role.ADMIN) && !roles.contains(Role.PERSON_MANAGER)) {
     		ObjectMapper mapper = new ObjectMapper(); 
         	ObjectNode statusMap = mapper.createObjectNode();            
     		statusMap.put("message", "No access granted for this action");       
     		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(statusMap);
     	}
    	
    	try {
    		Person storedPerson = personDao.findById(person.getId());
    		person.setPasswordHash(storedPerson.getPasswordHash());
    		personDao.save(person);	
    		ObjectMapper mapper = new ObjectMapper(); 
         	ObjectNode statusMap = mapper.createObjectNode();            
     		statusMap.put("status", 200);       
    		return ResponseEntity.status(HttpStatus.OK).body(statusMap);
    	}catch (Exception e) {
     		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
     	}
    	
    }
    
    @RequestMapping(value = "/persons/generate", method = RequestMethod.POST)
    public ResponseEntity<Object> generate(@RequestBody Person person, @RequestAttribute("roles") List<Role> roles) {
    	log.info("/persons/generate was called");
    	  
    	if (!roles.contains(Role.ADMIN) && !roles.contains(Role.PERSON_MANAGER)) {
     		ObjectMapper mapper = new ObjectMapper(); 
         	ObjectNode statusMap = mapper.createObjectNode();
     		statusMap.put("message", "No access granted for this action");       
     		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(statusMap);
     	}
     	
     	try {
     		Person user = personDao.findById(person.getId());
     		String token = UUID.randomUUID().toString();           
     		Calendar calendar = Calendar.getInstance();
     		calendar.add(GregorianCalendar.HOUR, 24);
     		user.setPasswordResetToken(token);
     		user.setPasswordResetTokenExpiryDate(calendar);
     		personDao.save(user);
     		return ResponseEntity.status(HttpStatus.CREATED).build();
     	}catch (Exception e) {
     		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
     	}     	
    }

    @RequestMapping(value = "/persons/remove", method = RequestMethod.POST)
    public ResponseEntity<Object> remove(@RequestBody Person person, @RequestAttribute("roles") List<Role> roles) {
    	log.info("/persons/remove was called");
    	
     	if (!roles.contains(Role.ADMIN) && !roles.contains(Role.PERSON_MANAGER)) {
     		ObjectMapper mapper = new ObjectMapper(); 
         	ObjectNode statusMap = mapper.createObjectNode();            
     		statusMap.put("message", "No access granted for this action");       
     		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(statusMap);
     	}
     	
     	try {
     		Person user = personDao.findById(person.getId());
     		user.setPasswordResetTokenExpiryDate(null);
     		user.setPasswordResetToken(null);
     		personDao.save(user);
     		return ResponseEntity.status(HttpStatus.CREATED).build();
     	}catch (Exception e) {
     		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
     	}     	
    }
}
