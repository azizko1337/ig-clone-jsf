package com.jsf.dao;

import com.jsf.entities.Post;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import java.util.List;
import java.util.Map;

@Stateless
public class PostDAO {
    private final static String UNIT_NAME = "ig-clone";

    @PersistenceContext(unitName = UNIT_NAME)
    protected EntityManager em;

    // Create
    public void create(Post post) {
        em.persist(post);
    }

    // Update (Merge)
    public Post merge(Post post) {
        return em.merge(post);
    }

    // Delete
    public void remove(Post post) {
        em.remove(em.merge(post));
    }

    // Find by ID
    public Post find(Object id) {
        return em.find(Post.class, id);
    }

    // Retrieve all posts
    public List<Post> getFullList() {
        List<Post> list = null;

        Query query = em.createQuery("SELECT p FROM Post p ORDER BY p.createdAt DESC");

        try {
            list = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // Retrieve posts with search parameters
    public List<Post> getList(Map<String, Object> searchParams) {
        List<Post> list = null;

        String select = "SELECT p ";
        String from = "FROM Post p ";
        String where = "";
        String orderby = "ORDER BY p.createdAt DESC";

        // Filter by user ID
        Integer userId = (Integer) searchParams.get("userId");
        if (userId != null) {
            where += (where.isEmpty() ? "WHERE " : "AND ");
            where += "p.user.id = :userId ";
        }

        // Filter by content (body)
        String body = (String) searchParams.get("body");
        if (body != null) {
            where += (where.isEmpty() ? "WHERE " : "AND ");
            where += "p.body like :body ";
        }

        // Build query
        Query query = em.createQuery(select + from + where + orderby);

        // Set parameters
        if (userId != null) {
            query.setParameter("userId", userId);
        }
        if (body != null) {
            query.setParameter("body", body + "%");
        }

        // Execute query
        try {
            list = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}