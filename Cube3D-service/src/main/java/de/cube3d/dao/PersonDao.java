package de.cube3d.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.cube3d.entities.Person;
import de.cube3d.utils.HibernateUtil;

public class PersonDao {
	
	private static final Logger LOG = LoggerFactory.getLogger(PersonDao.class);
	private static PersonDao instance;
	
	private PersonDao() {}
	 
	public static PersonDao getInstance () {
	    if (PersonDao.instance == null) {
	    	PersonDao.instance = new PersonDao();
	    }
	    return PersonDao.instance;
	}
	
	public Person save(Person person) {
		Session session = null;
		Transaction transaction = null;
        try {
        	session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            
            Person _person = person.getId() == null ? null : session.find(Person.class, person.getId());
            if(_person == null) session.persist(person); else _person.update(person);
            
            transaction.commit();
            return person;           
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOG.error("PersonDao save: " + e.getMessage());
            return null;          
        } finally {
			 session.close();
		}
    }
    
    public void delete(Person person) {
    	Session session = null;
		Transaction transaction = null;
        try {
        	session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.remove(person);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }           
            LOG.error("PersonDao delete: " + e.getMessage());
        } finally {
        	session.close();
		}
    }
    
    public void deleteAll() {
    	Session session = null;
		Transaction transaction = null;
        try {
        	session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.createMutationQuery("DELETE From Person").executeUpdate(); 
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }           
            LOG.error("PersonDao deleteAll: " + e.getMessage());
        } finally {
        	session.close();
		}
    }
    
    public List<Person> findAll() {
    	List<Person> list = null;
    	Session session = null;
    	try {   	
    		session = HibernateUtil.getSessionFactory().openSession();
    		list = session.createQuery("SELECT a FROM Person a", Person.class).getResultList(); 
    	}catch (Exception e) {	 
    		 LOG.error("PersonDao findAll: " + e.getMessage());
        } finally {
        	session.close();
		}
    	return list;      
    }
    
    public Person findById(String id){
    	Person person = null;  
    	Session session = null;
    	try  {
    		session = HibernateUtil.getSessionFactory().openSession();
            person = session.get(Person.class, id);
        } catch (Exception e) {
        	LOG.error("PersonDao findById: " + e.getMessage());
        } finally {
        	session.close();
		}	
    	return person;
    }
    
    public Person findByPasswordResetToken(String passwordResetToken){
    	Person person = null;
    	Session session = null;
    	try {   
    		session = HibernateUtil.getSessionFactory().openSession();
    		Query<Person> query = session.createQuery("SELECT a FROM Person AS a WHERE a.passwordResetToken = :passwordResetToken", Person.class);    		   			
    		query.setParameter("passwordResetToken", passwordResetToken);
    		query.setFirstResult(0);
    		query.setMaxResults(1); 
    		person = query.uniqueResult();
    		
    		session.close();
    	} catch (Exception e) {
        	LOG.error("PersonDao passwortResetToken: " + e.getMessage());
        } finally {
        	session.close();
		}		
    	return person;
    }
}
