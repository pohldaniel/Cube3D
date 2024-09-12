package de.security.view;

import java.security.Principal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

	@GetMapping("/login")
    public String login(Model model, Principal principal, RedirectAttributes redirAttrs) {
		if(principal == null) {
			redirAttrs.addFlashAttribute("message", "User not registered");
			 return "redirect:/message";
		}
		
		
    	UserDetails currentUser = (UserDetails) ((Authentication) principal).getPrincipal();
    	LoginForm loginForm = new LoginForm();
    	loginForm.setUsername(currentUser.getUsername());
    	loginForm.setPassword("default");
		model.addAttribute("loginForm", loginForm);
        return "login";
    }
	
	/*@RequestMapping(value = "/perform_login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = {MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> performLogin(@RequestParam Map<String, String> body) {
		return ResponseEntity.ok("perform_login");
	}
	
	@RequestMapping(path = "/authenticate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = {MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	  public ResponseEntity<String> authenticate(@RequestParam Map<String, String> body) throws Exception {
		
		//UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken("admin", "default");

        try {
            Authentication authentication = new UsernamePasswordAuthenticationToken("admin", "default");
        	SecurityContextHolder.getContext().setAuthentication(authentication);
            return ResponseEntity.ok("Authentication successful");
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
		
		
	}*/
}
