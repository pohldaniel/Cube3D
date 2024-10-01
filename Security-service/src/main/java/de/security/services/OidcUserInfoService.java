package de.security.services;

import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.stereotype.Service;

import de.security.dao.PersonDao;
import de.security.entities.Person;

@Service
public class OidcUserInfoService {

	private PersonDao personDao = PersonDao.getInstance();	
	public OidcUserInfo loadUser(String id) {
		Person person = personDao.findById(id);			
		return OidcUserInfo.builder()
				.subject(person.getId())
				.claim("roles", person.getRoles()).build();
	}
}