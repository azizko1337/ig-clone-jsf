package com.jsf.dao;

import java.util.List;

import com.jsf.entities.Comment;
import com.jsf.entities.Follow;
import com.jsf.entities.User;

import jakarta.persistence.PersistenceContext;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

@Stateless
public class CommentDAO {
    
    private final static String UNIT_NAME = "ig-clone";

    @PersistenceContext(unitName = UNIT_NAME)
    protected EntityManager em;
    
    public void create(Comment comment) {
		em.persist(comment);
	}
    public void remove(Comment comment) {
		em.remove(em.merge(comment));
	}
}
