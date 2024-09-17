package de.security.entities.builder;

import java.util.Set;

import org.apache.commons.lang3.builder.Builder;
import de.security.entities.Person;
import de.security.entities.enums.Role;

public class PersonBuilder implements Builder<Person>{

	private Person person;
	
	public PersonBuilder() {
		this.person = new Person();
	}

	public PersonBuilder id(String bid) {
		this.person.setId(bid);
		return this;
	}

	public PersonBuilder surname(String surname) {
		this.person.setSurname(surname);
		return this;
	}

	public PersonBuilder prename(String prename) {
		this.person.setPrename(prename);
		return this;
	}

	public PersonBuilder mail(String mail) {
		this.person.setMail(mail);
		return this;
	}

	public PersonBuilder externalCompany(String externalCompany) {
		this.person.setExternalCompany(externalCompany);
		return this;
	}

	public PersonBuilder passwordHash(String passwordHash) {
		this.person.setPasswordHash(passwordHash);
		return this;
	}

	public PersonBuilder roles(Set<Role> roles) {
		this.person.setRoles(roles);
		return this;
	}
	
	public PersonBuilder roles(Role... roles) {
		this.person.addRoles(roles);
		return this;
	}
	
	@Override
	public Person build() {
	     return person;
	}
	
}
