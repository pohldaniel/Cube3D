package de.cube3d.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import de.cube3d.dao.PersonDao;
import de.cube3d.dao.TopicAreaDao;
import de.cube3d.entities.Person;
import de.cube3d.entities.TopicArea;

@RestController
@RequestMapping("/cube/restAPI")
public class TopicAreaRestController {

	private Logger log = LoggerFactory.getLogger(TopicAreaRestController.class);
	
	private TopicAreaDao topicAreaDao = TopicAreaDao.getInstance();
    private PersonDao personDao = PersonDao.getInstance();

    @GetMapping(value = "/topicareas/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getAllTopicAreas() {
        log.info("/topicareas/getAll was called");
        return ResponseEntity.status(HttpStatus.OK).body(topicAreaDao.findAll());
    }

    @PostMapping(value = "/topicareas/create")
    public ResponseEntity<Object> createTopicArea(@RequestBody TopicArea topicArea) {
        log.info("/topicareas/create was called");
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(topicAreaDao.save(topicArea));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @PostMapping(value = "/topicareas/addPerson")
    public ResponseEntity<Object> addPerson(@RequestBody Map<String, String> request) {
        log.info("/topicareas/addPerson was called");
        try {
            if (topicAreaDao.findById(request.get("topicAreaName")) == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Themengebiet nicht gefunden");
            if (personDao.findById(request.get("bid")) == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Person nicht gefunden");
            TopicArea topicArea = topicAreaDao.findById(request.get("topicAreaName"));
            Person person = personDao.findById(request.get("bid"));
            topicArea.addPerson(person);
            topicAreaDao.save(topicArea);
            return ResponseEntity.status(HttpStatus.CREATED).body(topicArea);
        } catch (Exception e) {
            log.info("/topicareas/addPerson failed with error message: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping(value = "/topicareas/removePerson")
    public ResponseEntity<Object> removePerson(@RequestBody Map<String, String> request) {
        log.info("/topicareas/removePerson was called");
        try {
            TopicArea t = topicAreaDao.findById(request.get("topicAreaName"));
            Person p = personDao.findById(request.get("bid"));
            t.removePerson(p);
            topicAreaDao.save(t);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            log.info("/topicareas/delete failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping(value = "/topicareas/delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> deleteAction(@RequestBody TopicArea topicArea) {
        log.info("/topicareas/delete was called");
        try {
            TopicArea t = topicAreaDao.findById(topicArea.getName());
            topicAreaDao.delete(t);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            log.info("/topicareas/delete failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
