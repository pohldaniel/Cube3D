package de.security.rest;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class DownloadRestController {

	@RequestMapping(value = "download", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> viewCheckExcel() throws IOException {
	    File file = new File("actionmanager.p12");
	    ByteArrayInputStream in = new ByteArrayInputStream(Files.readAllBytes(file.toPath()));	    
	    HttpHeaders headers = new HttpHeaders();
	    headers.add("Content-Disposition", "attachment; filename=actionmanager.p12");
	    return ResponseEntity.ok().headers(headers).body(new InputStreamResource(in));
	}
}
