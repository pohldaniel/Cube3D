package de.security.view;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import de.security.CubeUserDetails;

@Controller
public class CertificateController {
	
	@GetMapping("/cert")
    public String certlogin(Model model) {
    	LoginForm loginForm = new LoginForm();
    	loginForm.setUsername("jnicolai");
    	//loginForm.setPassword("xxx");
		model.addAttribute("loginForm", loginForm);
        return "cert_login";
    }
	
	@GetMapping("/cert/download_page")
    public String homepage(@AuthenticationPrincipal CubeUserDetails userDetails) {		
        return "download_page";
    }
}
