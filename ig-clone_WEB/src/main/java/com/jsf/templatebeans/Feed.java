package com.jsf.templatebeans;

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
public class Feed {
	private static final String PAGE_POST_EDIT = "/pages/post?faces-redirect=true";
	
	@Inject
	ExternalContext extcontext;
	
	@Inject
	Flash flash;
	
	@EJB
	PostDAO postDAO;
	
	
	
	public String editPost(Post post){
		//1. Pass object through session
		//HttpSession session = (HttpSession) extcontext.getSession(true);
		//session.setAttribute("person", person);
		
		//2. Pass object through flash 
		flash.put("post", post);
		
		return PAGE_POST_EDIT;
	}


}
