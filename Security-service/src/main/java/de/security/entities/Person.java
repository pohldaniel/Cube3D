package de.security.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import de.security.entities.enums.Role;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.JoinColumn;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Person {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "SURNAME")
    private String surname;

    @Column(name = "PRENAME")
    private String prename;

    @Column(name = "MAIL")
    private String mail;

    @Column(name = "EXTERNAL_COMPANY")
    private String externalCompany;

    @JsonIgnore
    @Column(name = "PASSWORD_HASH")
    private String passwordHash;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "ROLE", joinColumns = @JoinColumn(name = "PERSON_ID"))
    @Column(name = "ROLE", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    @Column(name = "PASSWORD_RESET_TOKEN")
    private String passwordResetToken;

    @JsonFormat(pattern="yyyy-MM-dd' 'HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PASSWORD_RESET_TOKEN_EXPIRY_DATE")
    private Calendar passwordResetTokenExpiryDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return  Objects.equals(id, person.id) &&
                Objects.equals(surname, person.surname) &&
                Objects.equals(prename, person.prename) &&
                Objects.equals(mail, person.mail) &&
                Objects.equals(externalCompany, person.externalCompany) &&
                roles == person.roles ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, surname, prename, mail, externalCompany, roles);
    }

    @Override
    public String toString() {
        return "Person{" +
                "id='" + id + '\'' +
                ", surname='" + surname + '\'' +
                ", prename='" + prename + '\'' +
                ", mail='" + mail + '\'' +
                ", externalCompany='" + externalCompany + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", role=" + roles +
                '}';
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getPrename() {
		return prename;
	}

	public void setPrename(String prename) {
		this.prename = prename;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getExternalCompany() {
		return externalCompany;
	}

	public void setExternalCompany(String externalCompany) {
		this.externalCompany = externalCompany;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
	public void addRoles(Role... roles) {
		
		if(this.roles == null)
			this.roles = new HashSet<Role>();
		
		for (Role role : roles) {
			this.roles.add(role);
		}
	}

	public String getPasswordResetToken() {
		return passwordResetToken;
	}

	public void setPasswordResetToken(String passwordResetToken) {
		this.passwordResetToken = passwordResetToken;
	}

	public Calendar getPasswordResetTokenExpiryDate() {
		return passwordResetTokenExpiryDate;
	}

	public void setPasswordResetTokenExpiryDate(Calendar passwordResetTokenExpiryDate) {
		if(passwordResetTokenExpiryDate != null)
			passwordResetTokenExpiryDate.set(Calendar.MILLISECOND, 0);
		this.passwordResetTokenExpiryDate = passwordResetTokenExpiryDate;
	}
  
	public void update(Person person) {
		this.surname = person.getSurname();
		this.prename = person.getPrename();
		this.mail = person.getMail();
		
		this.externalCompany = person.getExternalCompany();
		this.passwordHash = person.getPasswordHash();
		this.roles = person.getRoles();

		this.passwordResetToken = person.getPasswordResetToken();
		this.passwordResetTokenExpiryDate = person.getPasswordResetTokenExpiryDate();
	}
    
}
