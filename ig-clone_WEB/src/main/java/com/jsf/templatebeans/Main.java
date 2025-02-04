package com.jsf.templatebeans;

import java.io.Serializable;

import com.jsf.entities.Post;
import com.jsf.entities.User;

import jakarta.faces.context.Flash;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class Main implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private static final String PAGE_INDEX = "/pages/index?faces-redirect=true";
	private static final String PAGE_LOGIN = "/pages/auth/login?faces-redirect=true";
	private static final String PAGE_PROFILE = "/pages/auth/profile?faces-redirect=true";
	private static final String PAGE_PROFILE_PUBLIC = "/pages/profile_public?faces-redirect=true";
	private static final String PAGE_POST = "/pages/post?faces-redirect=true";
	private static final String PAGE_SEARCH = "/pages/search?faces-redirect=true";
	private static final String PAGE_MODERATOR_PANEL = "/pages/moderator/all_posts?faces-redirect=true";
	
	@Inject
	Flash flash;
	
	private String nickname;

	public String login() {
		return PAGE_LOGIN;
	}
	public String profile() {
		return PAGE_PROFILE;
	}
	public String profile(User user) {
		flash.put("profile", user);
		
		return PAGE_PROFILE_PUBLIC;
	}
	public String search() {
		flash.put("query", nickname);
		
		return PAGE_SEARCH;
	}
	
	public String moderatorPanel() {
		return PAGE_MODERATOR_PANEL;
	}
	
	public String addPost() {
		Post post = new Post();
		flash.put("post", post);
		
		return PAGE_POST;
	}
	public String index() {
		return PAGE_INDEX;
	}
	
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
}
