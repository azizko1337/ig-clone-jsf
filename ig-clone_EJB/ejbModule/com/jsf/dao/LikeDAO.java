package com.jsf.dao;

import java.util.List;

import com.jsf.entities.Follow;
import com.jsf.entities.Like;
import com.jsf.entities.Post;
import com.jsf.entities.User;

import jakarta.persistence.PersistenceContext;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

@Stateless
public class LikeDAO {
    
    private final static String UNIT_NAME = "ig-clone";

    @PersistenceContext(unitName = UNIT_NAME)
    protected EntityManager em;
    
    public boolean isLiked(Post post, User user) {
    	TypedQuery<Like> query = em.createQuery(
            "SELECT l FROM Like l WHERE l.user.id = :user_id AND l.post.id = :post_id", Like.class
        );
    	query.setParameter("post_id", post.getId());
        query.setParameter("user_id", user.getId());
        
        try {
        	 return query.getSingleResult() != null;
        }catch(NoResultException e) {
        	return false;
        }
       
    }

    public void toggle(Post post, User user) {
    	TypedQuery<Like> query = em.createQuery(
    		"SELECT l FROM Like l WHERE l.user.id = :user_id AND l.post.id = :post_id", Like.class
        );
    	query.setParameter("post_id", post.getId());
        query.setParameter("user_id", user.getId());

        Like result = null;
        try {
        	result = query.getSingleResult();
        }catch(NoResultException e) {}
        
        if (result != null) {
            remove(result);
        } else {
        	Like like = new Like();
        	like.setUser(user);
        	like.setPost(post);
        	
            create(like);
        }
    }
    
    public void create(Like like) {
		em.persist(like);
	}
    public void remove(Like like) {
		em.remove(em.merge(like));
	}
}
