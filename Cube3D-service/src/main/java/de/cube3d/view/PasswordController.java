package de.cube3d.view;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import de.cube3d.dao.PersonDao;
import de.cube3d.entities.Person;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/password")
public class PasswordController {

	private PersonDao personDao = PersonDao.getInstance();
	private String pepper = "sjddjw768wlsmj882z2rnknlahffajsdgw2mAW!sjhjsc9870asfj3f";
	
	@RequestMapping(value = "/change", method = RequestMethod.GET)
	public String change(Model model, @RequestParam(name = "token", defaultValue = "aaa") String token){				
		PasswordForm changePassword = new PasswordForm();
		model.addAttribute("passwordForm", changePassword);
		model.addAttribute("token", token);
		return "password_form";	
	}
	
	@RequestMapping(value = "/submit", method = RequestMethod.POST)
    public String checkPersonInfo(
    		@RequestParam(name = "token") String token, 
    		@Valid @ModelAttribute("passwordForm") PasswordForm passwordForm, BindingResult bindingResult, Model model, RedirectAttributes redirAttrs) {

        if (bindingResult.hasErrors()) {
        	return "password_form";
        }
       
        try {
        	Person user = personDao.findByPasswordResetToken(token);
            if(user != null) { 
                if (user.getPasswordResetTokenExpiryDate().after(Calendar.getInstance())) {
                    MessageDigest digest = MessageDigest.getInstance("SHA-512");
                    byte[] hash = digest.digest((passwordForm.getPassword() + pepper).getBytes());
                    String hexHash = String.format("%x", new BigInteger(1, hash));
                    if(hexHash.equals(user.getPasswordHash())) {
                        redirAttrs.addFlashAttribute("message", "The new password cannot be the old one");
                      	return "redirect:/password/confirm";                   	
                    }
                    user.setPasswordHash(hexHash);
                    user.setPasswordResetTokenExpiryDate(null);
                    user.setPasswordResetToken(null);
                    personDao.save(user);
                    redirAttrs.addFlashAttribute("message", "Set new password.");
                	return "redirect:/password/confirm";
                }
            	
            }
            redirAttrs.addFlashAttribute("message", "link expired");
            return "redirect:/password/confirm";
            
        } catch (Exception e) {
        	redirAttrs.addFlashAttribute("message", "link expired");
            return "redirect:/password/confirm";
        }
      
    }
	
	public static String getTimeStamp(Date date, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        //dateFormat.setTimeZone(TimeZone.getDefault());//server timezone
        dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
        return dateFormat.format(date);
	}
}
