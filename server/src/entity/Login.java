package entity;

import java.io.Serializable;

import interfaces.StatementsIF;

public class Login implements Serializable, StatementsIF {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	/**
	 * User name.
	 */
	private String Username;
	/**
	 * Password.
	 */
	private String Password;
	
	
	public Login(String Username, String Password) {
		this.setUsername(Username);
		this.setPassword(Password);
	}

	@Override
	public String PrepareAddStatement() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String PrepareDeleteStatement() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String prepareUpdateStatement() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String PrepareSelectStatement() {
		
		return "SELECT * FROM clients WHERE username="+Username;
	}
	

	/**The SQL statement.
	 * @param action If client or worker.
	 * @return The SQL statement.
	 */
	public String PrepareSelectStatement(int action) {
		if(action==1)
			return "SELECT * FROM clients WHERE username="+Username + " AND accountType<>'RegisterPending'";
		else
			return "SELECT * FROM workers WHERE username="+Username;
	}


	/**Getter for username.
	 * @return Username.
	 */
	public String getUsername() {
		return Username;
	}

	/**Setter for username.
	 * @param username The username.
	 */
	public void setUsername(String username) {
		Username = username;
	}

	/**Getter for password.
	 * @return The password.
	 */
	public String getPassword() {
		return Password;
	}

	/**Setter for password.
	 * @param password The password.
	 */
	public void setPassword(String password) {
		Password = password;
	}

}
