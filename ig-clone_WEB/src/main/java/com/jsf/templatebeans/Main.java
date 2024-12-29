package com.jsf.templatebeans;

import java.io.Serializable;

import com.jsf.entities.Post;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.Flash;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@RequestScoped
public class Main implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private static final String PAGE_INDEX = "/pages/index?faces-redirect=true";
	private static final String PAGE_LOGIN = "/pages/auth/login?faces-redirect=true";
	private static final String PAGE_PROFILE = "/pages/auth/profile?faces-redirect=true";
	private static final String PAGE_POST = "/pages/post?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;
	
	@Inject
	Flash flash;

	public String login() {
		return PAGE_LOGIN;
	}
	public String profile() {
		return PAGE_PROFILE;
	}
	public String addPost() {
		Post post = new Post();
		flash.put("post", post);
		
		return PAGE_POST;
	}
	public String index() {
		return PAGE_INDEX;
	}
}