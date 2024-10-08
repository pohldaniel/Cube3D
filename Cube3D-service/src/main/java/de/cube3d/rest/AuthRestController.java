package de.cube3d.rest;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import de.cube3d.components.CubePasswordEncoder;
import de.cube3d.dao.PersonDao;
import de.cube3d.entities.Person;
import de.cube3d.services.JwtService;

@RestController
@ConfigurationProperties
@RequestMapping("/auth")
public class AuthRestController {
	
	private Logger log = LoggerFactory.getLogger(AuthRestController.class);
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private CubePasswordEncoder cubePasswordEncoder;
	
	private PersonDao personDao = PersonDao.getInstance();
	
	@RequestMapping(value = "/token", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> token(@RequestBody Person person){
		log.info("/auth/token was called");
		ObjectMapper mapper = new ObjectMapper(); 
		ObjectNode tokenMap = mapper.createObjectNode();
		tokenMap.put("token", jwtService.getJwtForUser(person.getId()));    
		tokenMap.put("user", jwtService.getUsername(tokenMap.get("token").asText()));   
		return ResponseEntity.status(HttpStatus.OK).body(tokenMap);
	}
	
	@PostMapping(value = "/authenticate")
    public ResponseEntity<Object> authenticatePerson(@RequestBody Map<String, String> resp) {
        log.info("/auth/authenticate was called");
        try {
            Person optionalPerson = personDao.findById(resp.get("id"));         
            if (optionalPerson != null) {
                Person person = optionalPerson;
                
                if (cubePasswordEncoder.matches(resp.get("password"), person.getPasswordHash())) {
                    return ResponseEntity.status(HttpStatus.ACCEPTED).body(person);
                }else {
                	ObjectMapper mapper = new ObjectMapper(); 
                 	ObjectNode statusMap = mapper.createObjectNode();
             		statusMap.put("message", "Wrong Password");   
             		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(statusMap);
                }
            }else {
            	ObjectMapper mapper = new ObjectMapper(); 
             	ObjectNode statusMap = mapper.createObjectNode();
         		statusMap.put("message", resp.get("id") + " is not registered at Cube3D");   
         		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(statusMap);
          }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
      
    }
	
	@RequestMapping(value = "/refresh", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> refresh(@RequestBody Map<String, String> resp){
		log.info("/auth/refresh was called");
		ObjectMapper mapper = new ObjectMapper(); 
		ObjectNode tokenMap = mapper.createObjectNode();
		tokenMap.put("token", jwtService.refreshToken(resp.get("token"), false));    
		tokenMap.put("user", jwtService.getUsername(tokenMap.get("token").asText())); 
		return ResponseEntity.status(HttpStatus.OK).body(tokenMap);
	}
}
