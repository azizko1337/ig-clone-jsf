package com.jsf.beans;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import com.jsf.dao.PostDAO;
import com.jsf.entities.Post;

@Named
@RequestScoped
public class AllPosts{
	private static final String PAGE_LOGIN = "/pages/auth/login?faces-redirect=true";
	
	private String body;
	
	@EJB
	PostDAO postDAO;
	
	public List<Post> getPosts(){
		List<Post> list = null;
		
		Map<String,Object> searchParams = new HashMap<String, Object>();
		
		if (body != null && body.length() > 0){
			searchParams.put("body", body);
		}
		list = postDAO.getList(searchParams, 0, 10);
		
		return list;
	}
	
	public String login() {
		return PAGE_LOGIN;
	}
	
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
}
