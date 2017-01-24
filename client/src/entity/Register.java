package entity;

import java.io.Serializable;

/**
 * This entity store the user details.
 * @author nire
 */
public class Register implements Serializable {
	
/**
* 	The constant serialVersionUID
*/	
private static final long serialVersionUID = 1L;


/**
 * The username.
 */
private String Username;


/**
 * The password.
 */
private String Password;


/**
 * The first name.
 */
private String FirstName;


/**
 * The last name,
 */
private String LastName;


/**
 * The constructor store the data into the entity.
 * @param Username - Gets The username.
 * @param Password - Gets The password.
 * @param FirstName - Gets The first name.
 * @param LastName - Gets The last name.
 */
public Register(String Username, String Password, String FirstName, String LastName)
{
	setUsername(Username);
	setPassword(Password);
	setFirstName(FirstName);
	setLastName(LastName);
}

/**
 * Getter for username
 * @return the username.
 */
public String getUsername() {
	return Username;
}


/** Setter for username.
 * @param username - Set the username.
 */
public void setUsername(String username) {
	Username = username;
}


/** Getter for password.
 * @return - The password.
 */
public String getPassword() {
	return Password;
}


/** Setter the password.
 * @param password - Set the password.
 */
public void setPassword(String password) {
	Password = password;
}


/** Getter for first name.
 * @return FirstName - The first name.
 */
public String getFirstName() {
	return FirstName;
}


/** Setter for first name.
 * @param firstName - Gets the first name.
 */
public void setFirstName(String firstName) {
	FirstName = firstName;
}


/** Getter for last name.
 * @return lastName - The last name.
 */
public String getLastName() {
	return LastName;
}


/** Setter for last name.
 * @param lastName - Gets the last name.
 */
public void setLastName(String lastName) {
	LastName = lastName;
}

}
