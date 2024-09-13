package de.security.view;

import java.security.Principal;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
}
