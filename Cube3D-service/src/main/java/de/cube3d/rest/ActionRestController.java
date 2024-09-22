package de.cube3d.rest;

import de.cube3d.dao.ActionDao;
import de.cube3d.entities.Action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/restAPI")
public class ActionRestController {

	private Logger log = LoggerFactory.getLogger( ActionRestController.class);
	private ActionDao actionDao = ActionDao.getInstance();
    
    @GetMapping(value = "/actions/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getAllActions() {
        log.info("/actions/getAll was called");
        return ResponseEntity.status(HttpStatus.OK).body(actionDao.findAll());
    }

    @PostMapping(value = "/actions/create")
    public ResponseEntity<Object> createAction(@RequestBody Action action) {
        log.info("/actions/create was called");
        Action newAction;
        try {
            newAction = new Action(action);
           
            return ResponseEntity.status(HttpStatus.CREATED).body(actionDao.save(newAction));
        } catch (IllegalArgumentException e) {
            log.info("/actions/create failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping(value = "/actions/duplicate")
    public ResponseEntity<Object> duplicateAction(@RequestBody Map<String, Action> resp) {
        log.info("/actions/duplicate was called");
        try {
            Action newAction = resp.get("newAction");

            if (actionDao.findById(newAction.getName()) != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An action with this name already exists.");
            }
            Action duplicatedAction = new Action(newAction);
           
            return ResponseEntity.status(HttpStatus.CREATED).body(actionDao.save(duplicatedAction));
        } catch (Exception e) {
            log.info("/actions/duplicate failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping(value = "/actions/delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> deleteAction(@RequestBody Action action) {
        log.info("/actions/delete was called");
        try {
            Action a = actionDao.findById(action.getName());
            actionDao.delete(a);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            log.info("/actions/delete failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
