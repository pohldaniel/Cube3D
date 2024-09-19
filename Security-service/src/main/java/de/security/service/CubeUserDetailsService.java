package de.security.service;

import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import de.security.CubeUserDetails;
import de.security.dao.PersonDao;
import de.security.entities.Person;

@Service
public class CubeUserDetailsService implements UserDetailsService {
	private PersonDao personDao = PersonDao.getInstance();
	
	@Override 
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { 
		Person person = personDao.findById(username);

		if(person == null) {
			 throw new UsernameNotFoundException("User not present");
		}
		
		return new CubeUserDetails.Builder()
				 .username(person.getId())
				 .password("{noop}" + person.getPasswordHash())
				 .firstname(person.getPrename())
				 .lastname(person.getSurname())
				 .authorities(person.getRoles().stream().map(p -> new SimpleGrantedAuthority(p.toString())).collect(Collectors.toList()))
				 .build();
	}
}
