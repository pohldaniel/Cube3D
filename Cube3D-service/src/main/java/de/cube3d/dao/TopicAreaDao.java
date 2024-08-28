package de.cube3d.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.cube3d.entities.TopicArea;
import de.cube3d.utils.HibernateUtil;

public class TopicAreaDao {
	
	private static final Logger LOG = LoggerFactory.getLogger(TopicAreaDao.class);
	private static TopicAreaDao instance;
	
	private TopicAreaDao() {}
	 
	public static TopicAreaDao getInstance () {
	    if (TopicAreaDao.instance == null) {
	    	TopicAreaDao.instance = new TopicAreaDao();
	    }
	    return TopicAreaDao.instance;
	}
	
	public TopicArea save(TopicArea topicArea) {
		Session session = null;
		Transaction transaction = null;
        try {
        	session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            
            TopicArea _topicArea = topicArea.getName() == null ? null : session.find(TopicArea.class, topicArea.getName());
            if(_topicArea == null) session.persist(topicArea); else _topicArea.update(topicArea);
            
            transaction.commit();
            return topicArea;           
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOG.error("TopicAreaDao save: " + e.getMessage());
            return null;          
        } finally {
			 session.close();
		}
    }
    
    public void delete(TopicArea topicArea) {
    	Session session = null;
		Transaction transaction = null;
        try {
        	session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.remove(topicArea);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }           
            LOG.error("TopicAreaDao delete: " + e.getMessage());
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
            session.createMutationQuery("DELETE From TopicArea").executeUpdate(); 
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }           
            LOG.error("TopicAreaDao deleteAll: " + e.getMessage());
        } finally {
        	session.close();
		}
    }
    
    public List<TopicArea> findAll() {
    	List<TopicArea> list = null;
    	Session session = null;
    	try {   	
    		session = HibernateUtil.getSessionFactory().openSession();
    		list = session.createQuery("SELECT a FROM TopicArea a", TopicArea.class).getResultList(); 
    	}catch (Exception e) {	 
    		 LOG.error("TopicAreaDao findAll: " + e.getMessage());
        } finally {
        	session.close();
		}
    	return list;      
    }
    
    public TopicArea findById(String name){
    	TopicArea topicArea = null;  
    	Session session = null;
    	try  {
    		session = HibernateUtil.getSessionFactory().openSession();
    		topicArea = session.get(TopicArea.class, name);
        } catch (Exception e) {
        	LOG.error("TopicAreaDao findById: " + e.getMessage());
        } finally {
        	session.close();
		}	
    	return topicArea;
    }
}
