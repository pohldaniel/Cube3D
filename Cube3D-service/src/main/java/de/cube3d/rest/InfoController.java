package de.cube3d.rest;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import ch.qos.logback.classic.Level;

@RestController
@RequestMapping("/cube/restAPI")
public class InfoController {
	
	private static final Logger LOG = LoggerFactory.getLogger(InfoController.class);	
	ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
	
	@RequestMapping(value = "/info/getLogLevel", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getLogLevel() {
		LOG.info("/info/getLogLevel was called");

		ObjectMapper mapper = new ObjectMapper();	
		ObjectNode statusMap = mapper.createObjectNode();
		statusMap.put("message", root.getLevel().levelStr);
		
		return ResponseEntity.ok(statusMap);	
	}
	
	@PostMapping(value = "/info/setLogLevel", consumes = {MediaType.APPLICATION_JSON_VALUE})
	public @ResponseBody ResponseEntity<Void> setLogLevel(@RequestBody JsonNode json) {
		LOG.info("/info/setLogLevel was called");
		
		if(json.get("logLevel").asText().equalsIgnoreCase("INFO")) {
			root.setLevel(Level.INFO);
		}
		
		if(json.get("logLevel").asText().equalsIgnoreCase("ERROR")) {
			root.setLevel(Level.WARN);
		}
		
		return ResponseEntity.ok().build();	
	}
}
