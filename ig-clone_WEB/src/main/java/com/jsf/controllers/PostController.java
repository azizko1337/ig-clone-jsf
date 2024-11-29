package com.jsf.controllers;

import com.jsf.dao.PostDAO;
import com.jsf.entities.Post;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.view.ViewScoped;

import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class PostController implements Serializable {

    @EJB
    private PostDAO postDAO;

    public List<Post> getAllPosts() {
    	List<Post> posts = postDAO.getFullList();
        return posts;
    }
}
