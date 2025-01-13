package com.jsf.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.simplesecurity.ServerClient;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import com.jsf.dao.PostDAO;
import com.jsf.entities.Post;
import com.jsf.entities.User;

@Named
@ViewScoped
public class Index implements Serializable{
	private static final long serialVersionUID = 1L;

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
