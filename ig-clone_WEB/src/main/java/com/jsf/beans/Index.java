package com.jsf.beans;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import org.primefaces.model.file.UploadedFile;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.Flash;
import jakarta.faces.simplesecurity.RemoteClient;
import jakarta.faces.simplesecurity.ServerClient;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import com.jsf.dao.PostDAO;
import com.jsf.entities.Post;
import com.jsf.entities.User;
import com.jsf.dao.UserDAO;

@Named
@ViewScoped
public class Index implements Serializable{
	private static final String PAGE_LOGIN = "/pages/auth/login?faces-redirect=true";
	
	private String body;
	
	@EJB
	PostDAO postDAO;
	
	
	    private final int ITEMS_PER_PAGE = 4;
	    private int firstRow = 0;
	    
	    private boolean hasMorePosts = true;
	    private List<Integer> posts = new ArrayList<>();
	    
	    @PostConstruct
	    public void init() {
	        loadMorePosts();
	    }
	    
	    public void loadMorePosts() {
	        if (!hasMorePosts) {
	            return;
	        }

	        User loggedUser = ServerClient.getLoggedUser();
	        Map<String,Object> searchParams = new HashMap<>();
	        
	        if (body != null && body.length() > 0) {
	            searchParams.put("body", body);
	        }
	        
	        if (loggedUser != null) {
	            searchParams.put("feed", loggedUser.getId());
	        }
	        
	        List<Integer> newPostsIds = postDAO.getIdsList(searchParams, firstRow, ITEMS_PER_PAGE);
	        
	        if (newPostsIds == null || newPostsIds.isEmpty()) {
	            hasMorePosts = false;
	            return;
	        }

	        // Add only unique posts
	        for (Integer postId : newPostsIds) {
	        	posts.add(postId);
	        }
	        
	        firstRow += ITEMS_PER_PAGE;
	        hasMorePosts = newPostsIds.size() == ITEMS_PER_PAGE;
	    }
	    
	    public boolean isHasMorePosts() {
	        return hasMorePosts;
	    }
	
	public void search() {
		firstRow = 0;
		posts = new ArrayList<>();
		hasMorePosts = true;
		loadMorePosts();
	}
	    
	public List<Post> getPosts(){
		return postDAO.getFromIds(posts);
	}
	
	public String login() {
		return PAGE_LOGIN;
	}
	
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
}
