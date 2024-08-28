package de.cube3d.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PreRemove;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import de.cube3d.entities.enums.Role;

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

    @Column(name = "ROLE")
    private Role role;

    @Column(name = "PASSWORD_RESET_TOKEN")
    private String passwordResetToken;

    @JsonFormat(pattern="yyyy-MM-dd' 'HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PASSWORD_RESET_TOKEN_EXPIRY_DATE")
    private Calendar passwordResetTokenExpiryDate;

    @JsonIgnoreProperties("persons")
    @ManyToMany(mappedBy = "persons", fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
	@Cascade(org.hibernate.annotations.CascadeType.PERSIST)
    private Set<TopicArea> topicAreas = new HashSet<>();

    public Person() {
    }

    @PreRemove
    private void removeTopicAreas() {
        for (TopicArea topicArea : topicAreas) {
            topicArea.getPersons().remove(this);
        }
    }

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
                role == person.role ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, surname, prename, mail, externalCompany, role);
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
                ", role=" + role +
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

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
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

	public Set<TopicArea> getTopicAreas() {
		return topicAreas;
	}

	public void setTopicAreas(Set<TopicArea> topicAreas) {
		this.topicAreas = topicAreas;
	}
    
	public void update(Person person) {
		this.surname = person.getSurname();
		this.prename = person.getPrename();
		this.mail = person.getMail();
		
		this.externalCompany = person.getExternalCompany();
		this.passwordHash = person.getPasswordHash();
		this.role = person.getRole();

		this.passwordResetToken = person.getPasswordResetToken();
		this.passwordResetTokenExpiryDate = person.getPasswordResetTokenExpiryDate();
	}
    
}
