package com.jsf.dao;

import java.util.List;

import com.jsf.entities.Follow;
import com.jsf.entities.UserRole;

import jakarta.persistence.PersistenceContext;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

@Stateless
public class UserRoleDAO {
    
    private final static String UNIT_NAME = "ig-clone";

    @PersistenceContext(unitName = UNIT_NAME)
    protected EntityManager em;

    public List<String> findRoleNamesByUserId(int userId) {
        TypedQuery<String> query = em.createQuery(
            "SELECT ur.role.name FROM UserRole ur WHERE ur.user.id = :userId", 
            String.class
        );
        query.setParameter("userId", userId);
        return query.getResultList();
    }
    
    public void create(UserRole userRole) {
		em.persist(userRole);
	}
}