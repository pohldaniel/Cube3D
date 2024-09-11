package de.security.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import de.security.dao.PersonDao;
import de.security.entities.Person;

public class PasswordUserDetailsService implements UserDetailsService {
	private PersonDao personDao = PersonDao.getInstance();
	
	 @Override 
	 public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { 
		 Person person = personDao.findById(username);

		 if(person == null) {
			 throw new UsernameNotFoundException("User not present");
		 }

		 return User.builder()
				 .username(person.getId())
				 .password("{noop}" + person.getPasswordHash())
				 //.roles(person.getRole().toString())
				 .authorities(person.getRole().toString())
				 .build();
	 }
}
