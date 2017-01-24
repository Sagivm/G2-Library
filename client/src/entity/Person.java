package entity;

/**
 * Person class represents a human in the system 
 * @author Sagivm
 *
 */
public class Person {
	/**
	 * Persons's first name
	 */
	private String firstname;
	/**
	 * Persons's last name
	 */
	private String lastname;
	private  String username;
	/**
	 * Initialize person without parameters
	 */
	public Person()
	{
		
	}
	/**
	 * Initialize person with parameters
	 * @param firstname
	 * @param lastname
	 * @param username
	 */
	public Person(String firstname, String lastname,String username) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.username = username;
	}
	/**
	 * @return Persons's first Name
	 */
	public String getFirstname() {
		return firstname;
	}
	
	/**
	 * Sets person's first name
	 * @param firstname
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	/**
	 * @return Persons's last name
	 */
	public String getLastname() {
		return lastname;
	}
	/**
	 * Sets person's last name
	 * @param lastname
	 */
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	/**
	 * @return Persons's username
	 */
	public String getId() {
		return username;
	}
	/**
	 * Sets person's username
	 * @param username
	 */
	public void setId(String username) {
		this.username = username;
	}
	

}
