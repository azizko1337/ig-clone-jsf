package com.jsf.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ejb.EJB;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.Flash;
import jakarta.servlet.http.HttpSession;

import com.jsf.dao.PostDAO;
import com.jsf.entities.Post;

@Named
@RequestScoped
public class PostList {
	private static final String PAGE_POST_EDIT = "post?faces-redirect=true";
	
	private String body;
	
	@Inject
	ExternalContext extcontext;
	
	@Inject
	Flash flash;
	
	@EJB
	PostDAO postDAO;
	
	public List<Post> getList(){
		List<Post> list = null;
		
		//1. Prepare search params
		Map<String,Object> searchParams = new HashMap<String, Object>();
		
		if (body != null && body.length() > 0){
			searchParams.put("body", body);
		}
		
		//2. Get list
		list = postDAO.getList(searchParams);
		
		return list;
	}
	
	public String addPost(){
		Post post = new Post();
		
		//1. Pass object through session
		//HttpSession session = (HttpSession) extcontext.getSession(true);
		//session.setAttribute("person", person);
		
		//2. Pass object through flash	
		flash.put("post", post);
		
		return PAGE_POST_EDIT;
	}
	
	public String editPost(Post post){
		//1. Pass object through session
		//HttpSession session = (HttpSession) extcontext.getSession(true);
		//session.setAttribute("person", person);
		
		//2. Pass object through flash 
		flash.put("post", post);
		
		return PAGE_POST_EDIT;
	}

	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
}
