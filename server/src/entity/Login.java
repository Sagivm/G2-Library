package entity;

import java.io.Serializable;

import interfaces.StatementsIF;

public class Login implements Serializable, StatementsIF {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String Username;
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
	

	public String PrepareSelectStatement(int action) {
		if(action==1)
			return "SELECT * FROM clients WHERE username="+Username + " AND accountType<>'RegisterPending'";
		else
			return "SELECT * FROM workers WHERE username="+Username;
	}


	public String getUsername() {
		return Username;
	}

	public void setUsername(String username) {
		Username = username;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}

}
