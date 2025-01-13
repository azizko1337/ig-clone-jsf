package com.jsf.beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jsf.dao.FollowDAO;
import com.jsf.dao.UserDAO;
import com.jsf.entities.Follow;
import com.jsf.entities.User;

import jakarta.ejb.EJB;
import jakarta.faces.context.Flash;
import jakarta.faces.simplesecurity.ServerClient;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class Search implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private static final String PAGE_PROFILE_PUBLIC = "/pages/profile_public?faces-redirect=true";
	private String query;
	
	@Inject
	Flash flash;
	
	@EJB
	UserDAO userDAO;
	
	@EJB
	FollowDAO followDAO;
	
	
	
	public void onLoad() {
		setQuery((String) flash.get("query"));
	}
	
	
	public List<User> getList(){
		List<User> list = null;
		
		Map<String,Object> searchParams = new HashMap<String, Object>();
		
		searchParams.put("nickname", getQuery());
		searchParams.put("firstName", getQuery());
		searchParams.put("lastName", getQuery());
		
		list = userDAO.getList(searchParams);
		
		return list;
	}
	
	public String profile(User user) {
		flash.put("profile", user);
		
		return PAGE_PROFILE_PUBLIC;
	}
	
	public boolean isFollowing(User user) {
		User loggedUser = ServerClient.getLoggedUser();
	    
		return followDAO.isFollowing(loggedUser, user);
	}
	
	public void toggleFollow(User user) {
	    User loggedUser = ServerClient.getLoggedUser();
		
		Follow follow = new Follow();
		follow.setUser1(loggedUser);
		follow.setUser2(user);
		
		followDAO.toggle(follow);
	}
	
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
}
