package com.jsf.controllers;

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

import javax.imageio.ImageIO;

import org.primefaces.model.file.UploadedFile;

import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.Flash;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;

import com.jsf.dao.PostDAO;
import com.jsf.entities.Post;
import com.jsf.dao.UserDAO;

@Named
@ViewScoped
public class PostEdit implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final String PAGE_POST_LIST = "index?faces-redirect=true";
	
	private Post post = new Post();
	private Post loaded = null;
	private UploadedFile uploadedFile;
	
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
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Błędne użycie systemu", null));
			// if (!context.isPostback()) { //possible redirect
			// context.getExternalContext().redirect("personList.xhtml");
			// context.responseComplete();
			// }
		}
	}
	
	public String savePost() {
		// no Person object passed
		if (loaded == null) {
			return null;
		}

		try {
			if (getPost().getUser() == null) {
                getPost().setUser(userDAO.find(15));
            }
			
			if (getPost().getId() == 0) {
				// new record
				postDAO.create(getPost());
			} else {
				// existing record
				postDAO.merge(getPost());
			}
			
			handleUploadedFile();
		} catch (Exception e) {
			e.printStackTrace();
			context.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "wystąpił błąd podczas zapisu", null));
			return null;
		}

		return PAGE_POST_LIST;
	}
	
	 public String removePost() {
    	postDAO.remove(getPost());
    	return PAGE_POST_LIST;
	 }
	
    private void handleUploadedFile() throws IOException {
    	if (getUploadedFile() != null) {
            String fileName = getUploadedFile().getFileName();

            // Ścieżka do folderu
            String baseUploadPath = servletContext.getRealPath("/uploads");
            Path postFolderPath = Paths.get(baseUploadPath);

            if (!Files.exists(postFolderPath)) {
                Files.createDirectories(postFolderPath);
            }
            
            Path filePath = postFolderPath.resolve(getPost().getId() + ".jpg");

            // Zapis pliku
            Files.copy(getUploadedFile().getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        }
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
