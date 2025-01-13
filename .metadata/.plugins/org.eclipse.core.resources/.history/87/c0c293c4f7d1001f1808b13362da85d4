package com.jsf.authbeans;

import java.util.ResourceBundle;

import com.jsf.dao.UserDAO;
import com.jsf.dao.UserRoleDAO;
import com.jsf.entities.Role;
import com.jsf.entities.User;
import com.jsf.entities.UserRole;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.annotation.ManagedProperty;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.simplesecurity.Password;
import jakarta.inject.Inject;
import jakarta.inject.Named;



@Named
@RequestScoped
public class Register {
	private static final String PAGE_LOGIN = "/pages/auth/login?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;
	
	private User user = new User();
	private String repeatedPassword;
	
	@Inject
	@ManagedProperty("#{txt}")
	private ResourceBundle txt;
	
	@EJB
	UserDAO userDAO;
	
	@EJB
	UserRoleDAO userRoleDAO;
	
	public String register() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		
		try {
			if(!user.getPassword().equals(repeatedPassword)) {
				ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, txt.getString("validateRepeatedPassword"), null));
				return PAGE_STAY_AT_THE_SAME;
			}
			
			if(!userDAO.checkUniqueEmail(getUser().getEmail())) {
				ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, txt.getString("emailOccupied"), null));
				return PAGE_STAY_AT_THE_SAME;
			}
			if(!userDAO.checkUniqueNickname(getUser().getNickname())) {
				ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, txt.getString("emailOccupied"), null));
				return PAGE_STAY_AT_THE_SAME;
			}
			
			user.setPassword(Password.hash(user.getPassword()));
			userDAO.create(user);
			
			UserRole userRole = new UserRole();
			userRole.setRole(new Role("user"));
			userRole.setUser(userDAO.find(user.getId()));
			
			userRoleDAO.create(userRole);
			
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, txt.getString("successfulRegistered"), null));
			return PAGE_LOGIN;
		} catch (Exception e) {
			e.printStackTrace();
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, txt.getString("unexpectedError"), null));
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
