package de.cube3d.rest;

import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/password")
public class PasswordRestController {
	
	@RequestMapping(value = "/confirm", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
	public String confirm(Model model) {		
		return model.getAttribute("message").toString();
	}
}
