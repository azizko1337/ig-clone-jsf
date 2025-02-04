package com.jsf.templatebeans;

import java.util.List;
import com.jsf.dao.UserDAO;
import com.jsf.entities.User;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.Flash;
import jakarta.faces.simplesecurity.ServerClient;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@RequestScoped
public class Suggestions {
	private static final String PAGE_PROFILE_PUBLIC = "/pages/profile_public?faces-redirect=true";
	
	@EJB
	UserDAO userDAO;
	
	@Inject
	Flash flash;
	
	public List<User> getList(){
		User loggedUser = ServerClient.getLoggedUser();
	    
	    if(loggedUser != null) {
			List<User> list = null;
			list = userDAO.getSuggestions(loggedUser);
			return list;
	    }
	    
	    return null;
	}
	
	public String profile(User user) {
		flash.put("profile", user);
		
		return PAGE_PROFILE_PUBLIC;
	}

}
