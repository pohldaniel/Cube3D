package de.security.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.stereotype.Service;

import de.security.dao.PersonDao;
import de.security.entities.Person;

@Service
public class OidcUserInfoService {

	private PersonDao personDao = PersonDao.getInstance();
		
	public OidcUserInfo loadUser(String id) {
		Person person = personDao.findById(id);
		
		List<String> roles = Arrays.asList(person.getRole().toString(), "USER");	
		return OidcUserInfo.builder()
				.subject(person.getId())
				.claim("roles", roles.stream().distinct().collect(Collectors.toList())).build();
	}
}