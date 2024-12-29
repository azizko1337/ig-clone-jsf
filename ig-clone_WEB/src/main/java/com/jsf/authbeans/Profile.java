package com.jsf.authbeans;

import java.util.List;

import com.jsf.dao.UserDAO;
import com.jsf.dao.UserRoleDAO;
import com.jsf.entities.User;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.simplesecurity.RemoteClient;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Named
@RequestScoped
public class Profile {
	private static final String PAGE_MAIN = "/pages/index?faces-redirect=true";
	private static final String PAGE_REGISTER = "/pages/auth/register?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;
	private static final String PAGE_LOGIN = "/pages/auth/login?faces-redirect=true";
	
	
	@Inject
	UserDAO userDAO;
	
	@Inject
	UserRoleDAO userRoleDAO;
	
	
	public String logout(){
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		session.invalidate();
		return PAGE_LOGIN;
	}
}
