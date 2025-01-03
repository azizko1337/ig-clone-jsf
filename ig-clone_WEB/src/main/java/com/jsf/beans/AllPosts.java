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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.primefaces.model.file.UploadedFile;

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
@RequestScoped
public class AllPosts{
	private static final String PAGE_LOGIN = "/pages/auth/login?faces-redirect=true";
	
	private String body;
	
	@EJB
	PostDAO postDAO;
	
	public List<Post> getPosts(){
		User loggedUser = ServerClient.getLoggedUser();
		
		List<Post> list = null;
		
		//1. Prepare search params
		Map<String,Object> searchParams = new HashMap<String, Object>();
		
		if (body != null && body.length() > 0){
			searchParams.put("body", body);
		}
		
		//2. Get list
		list = postDAO.getList(searchParams, 0, 10);
		
		return list;
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
