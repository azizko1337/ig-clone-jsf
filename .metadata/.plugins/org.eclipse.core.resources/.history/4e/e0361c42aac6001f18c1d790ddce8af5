package jakarta.faces.simplesecurity;

import java.security.MessageDigest;

public class Password {
	public static String hash(String password) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
			messageDigest.update(password.getBytes());
			return new String(messageDigest.digest());
		}
		catch(Exception e) {
			e.printStackTrace();
			return "";
		}
	}
}
