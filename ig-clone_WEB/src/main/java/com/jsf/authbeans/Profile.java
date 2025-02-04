package com.jsf.authbeans;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;

import com.jsf.dao.UserDAO;
import com.jsf.dao.UserRoleDAO;
import com.jsf.entities.User;

import jakarta.faces.annotation.ManagedProperty;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.simplesecurity.Password;
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
	private static final long serialVersionUID = 1L;
	
	private static final String PAGE_LOGIN = "/pages/auth/login?faces-redirect=true";
	
	private User user;
	private String newPassword;
	private String repeatedNewPassword;
	private String currentPasswordEdit;
	private String currentPasswordDelete;
	
	@Inject
	@ManagedProperty("#{txt}")
	private ResourceBundle txt;
	
	@Inject
	UserDAO userDAO;
	
	@Inject
	UserRoleDAO userRoleDAO;
	
	public void onLoad() {
		User loggedUser = ServerClient.getLoggedUser();
		
		User user = new User();
		user.setId(loggedUser.getId());
		user.setNickname(loggedUser.getNickname());
		user.setFirstName(loggedUser.getFirstName());
		user.setLastName(loggedUser.getLastName());
		user.setEmail(loggedUser.getEmail());
		user.setPassword(loggedUser.getPassword());
		
		setUser(user);
	}
	
	
	public String logout(){
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		session.invalidate();
		return PAGE_LOGIN;
	}
	
	public void save() {
		User loggedUser = ServerClient.getLoggedUser();
		
		FacesContext ctx = FacesContext.getCurrentInstance();
		if(loggedUser.getPassword().equals(Password.hash(getCurrentPasswordEdit()))) {
			setCurrentPasswordEdit("");
			
			if(getNewPassword().length()>0) {
				if(getNewPassword().equals(getRepeatedNewPassword())) {
					if(getNewPassword().length() >= 4 && getNewPassword().length()<=128) {
						getUser().setPassword(Password.hash(getNewPassword()));
					}else {
						ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, txt.getString("validateNewPassword"), null));
						return;
					}
				}else {
					ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, txt.getString("validateRepeatedNewPassword"), null));
					return;
				}
			}
				
//			check if nickname has been changed, if yes check unique
			if(!loggedUser.getNickname().equals(getUser().getNickname())) {
				if(!userDAO.checkUniqueNickname(getUser().getNickname())){
					ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, txt.getString("nicknameOccupied"), null));
					return;
				}
			}
//			check if email... as above
			if(!loggedUser.getEmail().equals(getUser().getEmail())) {
				if(!userDAO.checkUniqueEmail(getUser().getEmail())){
					ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, txt.getString("emailOccupied"), null));
					return;
				}
			}
			
//			update updatedAt, updatedBy
			getUser().setUpdatedAt(null);
			getUser().setUser2(loggedUser);
			
			userDAO.merge(getUser());
			
//			update session (login)
			User newLoggedUser = userDAO.find(user.getId());
			RemoteClient<User> client = new RemoteClient<User>();
			client.setDetails(newLoggedUser);
			List<String> roles = userRoleDAO.findRoleNamesByUserId(newLoggedUser.getId());
			if (roles.size() > 0) {
				for (String role: roles) {
					client.getRoles().add(role);
				}
			}
			HttpServletRequest request = (HttpServletRequest) ctx.getExternalContext().getRequest();
			client.store(request);
			
			setNewPassword("");
			setRepeatedNewPassword("");
			
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, txt.getString("savedChanges"), null));
				
			
		}else {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, txt.getString("validateCurrentPassword"), null));
		}
	}
	
	public String delete() {
		if(user.getPassword().equals(Password.hash(getCurrentPasswordDelete()))) {
			userDAO.remove(user);
			return logout();
		}else {
			setCurrentPasswordDelete("");
			FacesContext ctx = FacesContext.getCurrentInstance();
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					txt.getString("validateCurrentPassword"), null));
			return null;
		}
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
