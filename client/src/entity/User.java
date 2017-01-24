package entity;
/**
 * User class store the Data of users in system
 * @author Sagivm
 *
 */
public class User extends Person {
	/**
	 * The username.
	 */
	private String username;
	/**
	 * The password.
	 */
	private String password;
	/**
	 * The account type.
	 */
	private AccountType accountType;
	/**
	 * The account status.
	 */
	private AccountStatus accountStatus;
	/**
	 * true if the user connected.
	 */
	private boolean connected;
	/**
	 * Initialize User without parameters
	 */
	public User()
	{
		super();
		
	}
	/**
	 * Initialize User with parameters. firstname,lastname,username are defined in parent
	 * class Person 
	 * @param firstname
	 * @param lastname
	 * @param username
	 * @param accountType
	 * @param accountStatus
	 */
	public User(String firstname, String lastname, String username, String password, String accountType, String accountStatus) {
		super(firstname,lastname,username);
		setPassword(password);
		setAccountType(accountType);
		setAccountStatus(accountStatus);
	}
	
	/**
	 * @return Users's account type
	 */
	public AccountType getAccountType() {
		return accountType;
	}
	
	/**
	 * Given a String object setAccountType defines user's account
	 * type using Enum class
	 * @param accountTypes
	 */
	public void setAccountType(String accountTypes) {
		switch(accountTypes)
		{
		case "Intrested":
			this.accountType=AccountType.Intrested;
			break;
		case "Monthly":
			this.accountType=AccountType.Monthly;
			break;
		case "Yearly":
			this.accountType=AccountType.Yearly;
			break;
		case "PerBook":
			this.accountType=AccountType.PerBook;
			break;
		case "PendingYearly":
			this.accountStatus=AccountStatus.PendingYearly;
			break;
		}
	}
	
	/**
	 * @return User's password.
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
	
	/**
	 * @return Users's account status
	 */
	public AccountStatus getAccountStatus() {
		return accountStatus;
	}
	/**
	 * Given a String object setAccountType defines user's account
	 * type using Enum class
	 * @param accountStatuss
	 */
	public void setAccountStatus(String accountStatuss) {
		switch(accountStatuss)
		{
		case "Standard":
			this.accountStatus=AccountStatus.Standard;
			break;
		case "PendingPerBook":
			this.accountStatus=AccountStatus.PendingPerBook;
			break;
		case "PendingMonthly":
			this.accountStatus=AccountStatus.PendingMonthly;
			break;
		case "PendingYearly":
			this.accountStatus=AccountStatus.PendingYearly;
			break;
		}
	}
	/**
	 * @return if user is connected
	 */
	public boolean getConnected()
	{
		return this.connected;
	}
	
	/**
	 * Define user's connection
	 * @param connected
	 */
	public void setConnected(boolean connected)
	{
		this.connected=connected;
	}
	

}
