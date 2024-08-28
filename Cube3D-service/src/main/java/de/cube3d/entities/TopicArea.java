package de.cube3d.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class TopicArea {

    @Id
    @Column(name = "NAME")
    private String name;
   
    @ManyToMany
    @JsonIgnoreProperties("topicAreas")
    private Set<Person> persons = new HashSet<>();

    public void addPerson(Person p) {
        this.persons.add(p);
        p.getTopicAreas().add(this);
    }

    public void removePerson(Person p) {
        this.persons.remove(p);
        p.getTopicAreas().remove(this);
    }

    public TopicArea() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TopicArea topicArea = (TopicArea) o;
        return Objects.equals(name, topicArea.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    // This ToString is implemented here instead of with lombok since topicArea has an n x m Relationship with persons and person will call topicareas to string method and topicarea will call persons to string method and so on
    @Override
    public String toString() {
        return "TopicArea{" +
                "name='" + name + '\'' +
                '}';
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Person> getPersons() {
		return persons;
	}

	public void setPersons(Set<Person> persons) {
		this.persons = persons;
	}
    
	public void update(TopicArea topicArea) {
		this.name = topicArea.getName();		
	}
}
