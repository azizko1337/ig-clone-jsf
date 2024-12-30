package jakarta.faces.simplesecurity;

import com.jsf.entities.User;

import jakarta.faces.context.FacesContext;
import jakarta.servlet.http.HttpServletRequest;

public class ServerClient {
	public static User getLoggedUser() {
		FacesContext context = FacesContext.getCurrentInstance();
	    HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
	    RemoteClient client = RemoteClient.load(request.getSession());
	    
	    User loggedUser = null;
	    if(client != null) {
	    	loggedUser = (User)client.getDetails();
	    }
	    
	    return loggedUser;
	}
}
