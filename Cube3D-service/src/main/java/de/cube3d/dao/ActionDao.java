package de.cube3d.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.cube3d.entities.Action;
import de.cube3d.utils.HibernateUtil;

public class ActionDao {
	
	private static final Logger LOG = LoggerFactory.getLogger(ActionDao.class);
	private static ActionDao instance;
	
	private ActionDao() {}
	 
	public static ActionDao getInstance () {
	    if (ActionDao.instance == null) {
	    	ActionDao.instance = new ActionDao();
	    }
	    return ActionDao.instance;
	}
	
	public Action save(Action action) {
		Session session = null;
		Transaction transaction = null;
        try {
        	session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            
            Action _action = action.getName() == null ? null : session.find(Action.class, action.getName());
            if(_action == null) session.persist(action); else _action.update(action);
            
            transaction.commit();
            return action;           
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOG.error("ActionDao save: " + e.getMessage());
            return null;          
        } finally {
			 session.close();
		}
    }
    
    public void delete(Action action) {
    	Session session = null;
		Transaction transaction = null;
        try {
        	session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.remove(action);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }           
            LOG.error("ActionDao delete: " + e.getMessage());
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
            session.createMutationQuery("DELETE From Action").executeUpdate(); 
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }           
            LOG.error("ActionDao deleteAll: " + e.getMessage());
        } finally {
        	session.close();
		}
    }
    
    public List<Action> findAll() {
    	List<Action> list = null;
    	Session session = null;
    	try {   	
    		session = HibernateUtil.getSessionFactory().openSession();
    		list = session.createQuery("SELECT a FROM Action a", Action.class).getResultList(); 
    	}catch (Exception e) {	 
    		 LOG.error("ActionDao findAll: " + e.getMessage());
        } finally {
        	session.close();
		}
    	return list;      
    }
    
    public Action findById(String name){
    	Action action = null;  
    	Session session = null;
    	try  {
    		session = HibernateUtil.getSessionFactory().openSession();
    		action = session.get(Action.class, name);
        } catch (Exception e) {
        	LOG.error("TopicAreaDao findById: " + e.getMessage());
        } finally {
        	session.close();
		}	
    	return action;
    }
}
