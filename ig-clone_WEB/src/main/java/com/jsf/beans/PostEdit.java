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
import java.util.Base64;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import org.primefaces.model.file.UploadedFile;

import jakarta.ejb.EJB;
import jakarta.faces.annotation.ManagedProperty;
import jakarta.faces.application.FacesMessage;
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
public class PostEdit implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final String PAGE_INDEX = "/pages/index?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;
	
	private Post post = new Post();
	private Post loaded = null;
	private UploadedFile uploadedFile;
	
	@Inject
	@ManagedProperty("#{txt}")
	private ResourceBundle txt;
	
	@EJB
	PostDAO postDAO;
	
	@EJB
	UserDAO userDAO;

	@Inject
	FacesContext context;

	@Inject
	Flash flash;
	
	@Inject
    ServletContext servletContext;
	
	public void onLoad() throws IOException {
		// 1. load person passed through session
		// HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
		// loaded = (Person) session.getAttribute("person");

		// 2. load person passed through flash
		loaded = (Post) flash.get("post");

		// cleaning: attribute received => delete it from session
		if (loaded != null) {
			setPost(loaded);
			// session.removeAttribute("person");
		} else {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, txt.getString("unexpectedError"), null));
			// if (!context.isPostback()) { //possible redirect
			// context.getExternalContext().redirect("personList.xhtml");
			// context.responseComplete();
			// }
		}
	}
	
	public String savePost() {
		User loggedUser = ServerClient.getLoggedUser();
		
		// no Person object passed
		if (loaded == null) {
			return PAGE_STAY_AT_THE_SAME;
		}
		
		if(getUploadedFile() == null && getPost().getId()==0) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, txt.getString("noImage"), null));
			return PAGE_STAY_AT_THE_SAME;
		}

		try {
			if (getPost().getUser() == null) {
                getPost().setUser(loggedUser);
            }
			
			if (getPost().getId() == 0) {
				// new record
				postDAO.create(getPost());
			} else {
				// existing record
				postDAO.merge(getPost());
			}
			
			handleUploadedFile();
			
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, txt.getString("unexpectedError"), null));
			
			return PAGE_INDEX;
		} catch (Exception e) {
			e.printStackTrace();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, txt.getString("unexpectedError"), null));
			return null;
		}

		
	}
	
	 public String removePost(){
//		remove post image
		Path postFolderPath = Paths.get(servletContext.getRealPath("/uploads"));
		Path filePath = postFolderPath.resolve(getPost().getId() + ".jpg");
		try { Files.deleteIfExists(filePath); } catch(IOException e) {}
		
		 
		postDAO.remove(getPost());
		
		return PAGE_INDEX;
	 }
	 
	 public void preview() {}
	
    private void handleUploadedFile() throws IOException {
    	if (getUploadedFile() != null) {
            // Ścieżka do folderu
            Path postFolderPath = Paths.get(servletContext.getRealPath("/uploads"));

            if (!Files.exists(postFolderPath)) {
                Files.createDirectories(postFolderPath);
            }
            
            Path filePath = postFolderPath.resolve(getPost().getId() + ".jpg");

            // Zapis pliku
            Files.copy(getUploadedFile().getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        }
    }
    public String getImageContentsAsBase64() {
        return Base64.getEncoder().encodeToString(uploadedFile.getContent());
    }
    
    public String index() {
    	return PAGE_INDEX;
    }

	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

	public UploadedFile getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(UploadedFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}
}
