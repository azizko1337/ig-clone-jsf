package com.jsf.authbeans;

import java.io.Serializable;
import java.util.List;

import com.jsf.dao.UserDAO;
import com.jsf.dao.UserRoleDAO;
import com.jsf.entities.User;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.simplesecurity.RemoteClient;
import jakarta.faces.simplesecurity.ServerClient;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Named
@ViewScoped
public class Profile implements Serializable{
	private static final String PAGE_MAIN = "/pages/index?faces-redirect=true";
	private static final String PAGE_REGISTER = "/pages/auth/register?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;
	private static final String PAGE_LOGIN = "/pages/auth/login?faces-redirect=true";
	
	private User user;
	private String newPassword;
	private String repeatedNewPassword;
	private String currentPasswordEdit;
	private String currentPasswordDelete;
	
	
	@Inject
	UserDAO userDAO;
	
	@Inject
	UserRoleDAO userRoleDAO;
	
	public void onLoad() {
		setUser(ServerClient.getLoggedUser());
	}
	
	
	public String logout(){
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		session.invalidate();
		return PAGE_LOGIN;
	}
	
	public void save() {
		
	}
	
	public String delete() {
		return null;
	}


	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public String getRepeatedNewPassword() {
		return repeatedNewPassword;
	}
	public void setRepeatedNewPassword(String repeatedNewPassword) {
		this.repeatedNewPassword = repeatedNewPassword;
	}
	public String getCurrentPasswordEdit() {
		return currentPasswordEdit;
	}
	public void setCurrentPasswordEdit(String currentPasswordEdit) {
		this.currentPasswordEdit = currentPasswordEdit;
	}
	public String getCurrentPasswordDelete() {
		return currentPasswordDelete;
	}
	public void setCurrentPasswordDelete(String currentPasswordDelete) {
		this.currentPasswordDelete = currentPasswordDelete;
	}
}
