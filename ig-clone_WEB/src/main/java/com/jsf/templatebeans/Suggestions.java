package com.jsf.templatebeans;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jsf.dao.UserDAO;
import com.jsf.entities.User;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.simplesecurity.RemoteClient;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;

@Named
@RequestScoped
public class Suggestions {
	private static final String PAGE_PROFILE = "/pages/profile?faces-redirect=true";
	
	@EJB
	UserDAO userDAO;
	
	public List<User> getList(){
//		get User from session
		FacesContext context = FacesContext.getCurrentInstance();
	    HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
	    RemoteClient client = RemoteClient.load(request.getSession());
	    User loggedUser = null;
	    if(client != null) {
	    	loggedUser = (User)client.getDetails();
	    }
	    
	    if(loggedUser != null) {
			List<User> list = null;
			
			list = userDAO.getSuggestions(loggedUser);
			
			return list;
	    }
	    
	    return null;
	}

}