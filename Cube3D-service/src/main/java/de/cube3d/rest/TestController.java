package de.cube3d.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/test")
public class TestController {
	
	@RequestMapping(value = "/execute", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> execute(){		
		ObjectMapper mapper = new ObjectMapper();						 			
		ObjectNode statusMap = mapper.createObjectNode();
		statusMap.put("message", "Hello From Cube3D");
		statusMap.put("status", "200");
		return ResponseEntity.ok(statusMap);	
	}
}
