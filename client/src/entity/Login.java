package entity;

import java.io.Serializable;

/**
 * This an entity for presave login details. later this entity will send to server
 * with message.
 * @author nire
 */
public class Login implements Serializable {

/**
 * 	The constant serialVersionUID
 */
private static final long serialVersionUID = 1L;


/**
 * The username.
 */
private String username;


/**
 * The password.
 */
private String password;

/**
 * Login constructor store the data.
 * @param username Gets the username.
 * @param password Gets the password.
 */
public Login(String username, String password) {
	setUsername(username);
	setPassword(password);
}



/** Getter for username
 * @return The username
 */
public String getUsername() {
	return username;
}

/** Setter for username
 * @param username The username.
 */
public void setUsername(String username) {
	this.username = username;
}

/** Getter for password.
 * @return The password.
 */
public String getPassword() {
	return password;
}

/** Setter for password.
 * @param password The password.
 */
public void setPassword(String password) {
	this.password = password;
}
}
