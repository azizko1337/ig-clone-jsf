package com.jsf.dao;

import java.util.List;

import com.jsf.entities.Follow;
import com.jsf.entities.User;

import jakarta.persistence.PersistenceContext;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

@Stateless
public class FollowDAO {
    
    private final static String UNIT_NAME = "ig-clone";

    @PersistenceContext(unitName = UNIT_NAME)
    protected EntityManager em;
    
    public boolean isFollowing(User following_user, User followed_user) {
    	TypedQuery<Follow> query = em.createQuery(
            "SELECT f FROM Follow f WHERE f.user1.id = :following_user_id AND f.user2.id = :followed_user_id",
            Follow.class
        );
    	query.setParameter("following_user_id", following_user.getId());
        query.setParameter("followed_user_id", followed_user.getId());
        
        try {
        	 return query.getSingleResult() != null;
        }catch(NoResultException e) {
        	return false;
        }
       
    }

    public void toggle(Follow follow) {
    	TypedQuery<Follow> query = em.createQuery(
            "SELECT f FROM Follow f WHERE f.user1.id = :following_user_id AND f.user2.id = :followed_user_id",
            Follow.class
        );
        query.setParameter("following_user_id", follow.getUser1().getId());
        query.setParameter("followed_user_id", follow.getUser2().getId());

        Follow result = null;
        try {
        	result = query.getSingleResult();
        }catch(NoResultException e) {}
        
        if (result != null) {
            remove(result);
        } else {
            create(follow);
        }
    }
    
    public void create(Follow follow) {
		em.persist(follow);
	}
    public void remove(Follow follow) {
		em.remove(em.merge(follow));
	}
}
