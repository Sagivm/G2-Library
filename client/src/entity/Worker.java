package entity;
/**
 * User class store the Data of workers in system
 * @author Ork
 *
 */
public class Worker extends Person {
	
	/**
	 * The username
	 */
	private String username;
	/**
	 * The password.
	 */
	private String password;
	/**
	 * The email
	 */
	private String email;
	/**
	 * The job
	 */
	private String job;
	/**
	 * The department
	 */
	private String department;
	
	//private boolean connected;
	
	/**
	 * Initialize Worker without parameters
	 */
	public Worker()
	{
		super();
		
	}
	
	/** Initialize Worker with parameters. firstname,lastname,username are defined in parent
	 * @param firstname
	 * @param lastname
	 * @param username
	 * @param email
	 * @param job
	 * @param department
	 */
	public Worker(String firstname, String lastname, String username,String password, String email, String job, String department) {
		super(firstname,lastname,username);
		setPassword(password);
		setEmail(email);
		setJob(job);
		setDepratment(department);
	}
	
	/**
	 * @return User's pasword.
	 */
	public String getPassword()
	{
		return password;
	}
	
	/**
	 * @param password
	 */
	public void setPassword(String password)
	{
		this.password = password;
	}
	
	/** Getter for Email.
	 * @return The email.
	 */
	public String getEmail()
	{
		return email;
	}
	
	/** Getter for job.
	 * @return The job.
	 */
	public String getJob()
	{
		return job;
	}
	
	/** Getter for depratment.
	 * @return The depratment.
	 */
	public String getDepratment()
	{
		return department;
	}
	
	/** Setter for Email.
	 * @param email
	 */
	public void setEmail(String email)
	{
		this.email = email;
	}
	
	/** Setter for job.
	 * @param job
	 */
	public void setJob(String job)
	{
		this.job = job;
	}
	
	/** Setter for department.
	 * @param department
	 */
	public void setDepratment(String department)
	{
		this.department = department;
	}

}
