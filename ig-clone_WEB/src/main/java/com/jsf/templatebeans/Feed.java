package com.jsf.templatebeans;

import java.io.Serializable;
import java.util.ResourceBundle;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.ejb.EJB;
import jakarta.faces.annotation.ManagedProperty;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.Flash;
import jakarta.faces.simplesecurity.ServerClient;
import jakarta.faces.view.ViewScoped;
import com.jsf.dao.CommentDAO;
import com.jsf.dao.LikeDAO;
import com.jsf.entities.Comment;
import com.jsf.entities.Post;
import com.jsf.entities.User;

@Named
@ViewScoped
public class Feed implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private static final String PAGE_POST_EDIT = "/pages/post?faces-redirect=true";
	private static final String PAGE_PROFILE_PUBLIC = "/pages/profile_public?faces-redirect=true";
	private static final String PAGE_LOGIN = "/pages/auth/login?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;
	
	private int extendedPost;
	private String newComment;
	
	@Inject
	@ManagedProperty("#{txt}")
	private ResourceBundle txt;
	
	@Inject
	ExternalContext extcontext;
	
	@Inject
	Flash flash;
	
	@Inject
	FacesContext context;
	
	@EJB
	CommentDAO commentDAO;
	
	@EJB
	LikeDAO likeDAO;
	
	public String addComment(Post post) {
		try {
			User loggedUser = ServerClient.getLoggedUser();
			if(loggedUser == null) {
				return PAGE_LOGIN;
			}
			
			Comment comment = new Comment();
			
			comment.setBody(getNewComment());
			comment.setPost(post);
			comment.setUser(loggedUser);
			
			commentDAO.create(comment);
			
			setNewComment("");
			
		}catch(Exception e) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, txt.getString("unexpectedError"), null));
		}
		
		return PAGE_STAY_AT_THE_SAME;
	}
	
	public void removeComment(Comment comment) {
		commentDAO.remove(comment);
	}
	
	public String like(Post post) {
		User loggedUser = ServerClient.getLoggedUser();
		if(loggedUser == null) {
			return PAGE_LOGIN;
		}
		
		likeDAO.toggle(post, loggedUser);
		return null;
	}
	
	public boolean isLiked(Post post) {
		User loggedUser = ServerClient.getLoggedUser();
		if(loggedUser == null) {
			return false;
		}
		
		return likeDAO.isLiked(post, loggedUser);
	}
	
	public void extendPost(Post post) {
		if(getExtendedPost() == post.getId()) {
			setExtendedPost(0);
		}else {
			setExtendedPost(post.getId());
		}
	}
	
	public String editPost(Post post){
		flash.put("post", post);
		
		return PAGE_POST_EDIT;
	}

	public String profile(User user) {
		flash.put("profile", user);
		
		return PAGE_PROFILE_PUBLIC;
	}

	public String getNewComment() {
		return newComment;
	}

	public void setNewComment(String newComment) {
		this.newComment = newComment;
	}

	public int getExtendedPost() {
		return extendedPost;
	}

	public void setExtendedPost(int extendedPost) {
		this.extendedPost = extendedPost;
	}
}
