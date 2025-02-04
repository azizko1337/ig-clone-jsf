package com.jsf.beans;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.ResourceBundle;

import org.primefaces.model.file.UploadedFile;

import jakarta.ejb.EJB;
import jakarta.faces.annotation.ManagedProperty;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.Flash;
import jakarta.faces.simplesecurity.ServerClient;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.ServletContext;
import com.jsf.dao.PostDAO;
import com.jsf.entities.Post;
import com.jsf.entities.User;
import com.jsf.dao.UserDAO;

@Named
@ViewScoped
public class PostEdit implements Serializable {
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
	
	private static final long serialVersionUID = 1L;

	private static final String PAGE_INDEX = "/pages/index?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;
	
	private Post post = new Post();
	private Post loaded = null;
	private UploadedFile uploadedFile;
	
	public void onLoad() throws IOException {
		loaded = (Post) flash.get("post");

		if (loaded != null) {
			setPost(loaded);
		} else {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, txt.getString("unexpectedError"), null));
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
			
			getPost().setUpdatedAt(null);
			
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
		Path UPLOAD_PATH = Paths.get(context.getExternalContext().getInitParameter("UPLOAD_PATH")).resolve("/posts/");
		UPLOAD_PATH = UPLOAD_PATH.resolve(String.valueOf(getPost().getId()));
		 
		Path filePath = UPLOAD_PATH.resolve("image.jpg");
		try { Files.deleteIfExists(filePath); } catch(IOException e) {}
		
		 
		postDAO.remove(getPost());
		
		return PAGE_INDEX;
	 }
	 
	 public void preview() {}
	
    private void handleUploadedFile() throws IOException {
    	if (getUploadedFile() != null) {
    		Path UPLOAD_PATH = Paths.get(context.getExternalContext().getInitParameter("UPLOAD_PATH")).resolve("posts/");
    		UPLOAD_PATH = UPLOAD_PATH.resolve(String.valueOf(getPost().getId()));
    		
            if (!Files.exists(UPLOAD_PATH)) {
                Files.createDirectories(UPLOAD_PATH);
            }
            
            Path filePath = UPLOAD_PATH.resolve("image.jpg");

            // save file
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
