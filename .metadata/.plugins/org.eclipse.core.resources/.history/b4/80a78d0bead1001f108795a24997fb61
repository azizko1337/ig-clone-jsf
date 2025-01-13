package com.jsf.beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.ejb.EJB;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.Flash;
import jakarta.faces.simplesecurity.ServerClient;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import com.jsf.dao.FollowDAO;
import com.jsf.dao.PostDAO;
import com.jsf.entities.Follow;
import com.jsf.entities.Post;
import com.jsf.entities.User;

@Named
@ViewScoped
public class ProfilePublic implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private static final String PAGE_ERROR_NOT_FOUND = "/pages/error/not_found?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;
	
	private String body;
	private User user;
	
	@Inject
	FacesContext context;
	
	@Inject
	Flash flash;
	
	@EJB
	PostDAO postDAO;
	
	@EJB
	FollowDAO followDAO;
	
	public String onLoad() throws IOException {
		User loaded = (User) flash.get("profile");

		if (loaded != null) {
			setUser(loaded);
			return PAGE_STAY_AT_THE_SAME;
		} else {
		    return PAGE_ERROR_NOT_FOUND;
		}
	}

	public boolean isFollowing() {
		User loggedUser = ServerClient.getLoggedUser();
	    
		return followDAO.isFollowing(loggedUser, user);
	}
	
	public void toggleFollow() {
	    User loggedUser = ServerClient.getLoggedUser();
		
		Follow follow = new Follow();
		follow.setUser1(loggedUser);
		follow.setUser2(user);
		
		followDAO.toggle(follow);
	}
	
	public List<Post> getPosts(){
		if(user == null) return null;
		
		List<Post> list = null;
		

		Map<String,Object> searchParams = new HashMap<String, Object>();
		if (body != null && body.length() > 0){
			searchParams.put("body", body);
		}
		searchParams.put("userId", user.getId());
		list = postDAO.getList(searchParams, 0, 10);
		
		return list;
	}
	
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}

	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
}
