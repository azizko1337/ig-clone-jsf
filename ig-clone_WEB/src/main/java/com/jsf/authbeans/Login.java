package com.jsf.authbeans;

import java.util.List;
import java.util.ResourceBundle;

import com.jsf.dao.UserDAO;
import com.jsf.dao.UserRoleDAO;
import com.jsf.entities.User;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.annotation.ManagedProperty;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.simplesecurity.Password;
import jakarta.faces.simplesecurity.RemoteClient;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;

@Named
@RequestScoped
public class Login {
	private static final String PAGE_MAIN = "/pages/index?faces-redirect=true";
	private static final String PAGE_REGISTER = "/pages/auth/register?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;
	
	private String nickname;
	private String password;
	
	@Inject
	@ManagedProperty("#{txt}")
	private ResourceBundle txt;
	
	@Inject
	UserDAO userDAO;
	
	@Inject
	UserRoleDAO userRoleDAO;
	
	public String login() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		
		setPassword(Password.hash(getPassword()));
		User user = userDAO.login(nickname, password);
		
		if (user == null) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					txt.getString("invalidLogin"), null));
			setPassword("");
			return PAGE_STAY_AT_THE_SAME;
		}
		
		RemoteClient<User> client = new RemoteClient<User>();
		client.setDetails(user);
		
		List<String> roles = userRoleDAO.findRoleNamesByUserId(user.getId());
		
		
		if (roles.size() > 0) {
			for (String role: roles) {
				client.getRoles().add(role);
			}
		}
		
		HttpServletRequest request = (HttpServletRequest) ctx.getExternalContext().getRequest();
		client.store(request);

		return PAGE_MAIN;
	}
	
	public String register() {
		return PAGE_REGISTER;
	}
	
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
