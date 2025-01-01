package com.jsf.authbeans;

import com.jsf.dao.UserDAO;
import com.jsf.dao.UserRoleDAO;
import com.jsf.entities.Role;
import com.jsf.entities.User;
import com.jsf.entities.UserRole;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.simplesecurity.Password;
import jakarta.inject.Inject;
import jakarta.inject.Named;



@Named
@RequestScoped
public class Register {
	
	private static final String PAGE_INDEX = "/pages/index?faces-redirect=true";
	private static final String PAGE_LOGIN = "/pages/auth/login?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;
	
	private User user = new User();
	private String repeatedPassword;
	
	@EJB
	UserDAO userDAO;
	
	@EJB
	UserRoleDAO userRoleDAO;
	
	@Inject
	FacesContext context;
	
	public String register() {
		try {
			if(!user.getPassword().equals(repeatedPassword)) {
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hasła nie są takie same.", null));
				return PAGE_STAY_AT_THE_SAME;
			}
			
			user.setPassword(Password.hash(user.getPassword()));
			userDAO.create(user);
			
			UserRole userRole = new UserRole();
			userRole.setRole(new Role("user"));
			userRole.setUser(userDAO.find(user.getId()));
			
			userRoleDAO.create(userRole);
			
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Pomyślnie zarejestrowano.", null));
			return PAGE_LOGIN;
		} catch (Exception e) {
			e.printStackTrace();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Wystąpił błąd podczas zapisu.", null));
			return PAGE_STAY_AT_THE_SAME;
		}
	}

	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getRepeatedPassword() {
		return repeatedPassword;
	}
	public void setRepeatedPassword(String repeatedPassword) {
		this.repeatedPassword = repeatedPassword;
	}
}
