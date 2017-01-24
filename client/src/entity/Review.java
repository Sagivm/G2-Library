package entity;

/**
 *  This entity stores Review's details.
 * @author ork
 *
 */
public class Review {
	
	/**
	 * The id of the review.
	 */
	private String reviewID;
	
	/**
	 * The username of the user that wrote the review.
	 */
	private String username;
	
	/**
	 * The first name of the user that wrote the review.
	 */
	private String firstName;
	
	/**
	 * The last name of the user that wrote the review.
	 */
	private String lastName;
	
	/**
	 * The book title.
	 */
	private String bookTitle;
	
	/**
	 * The content of the review.
	 */
	private String reviewContent;
	
	/**
	 * The date of the review.
	 */
	private String reviewDate;
	
	/**
	 * The constructor of the review entity.
	 * 
	 * @param reviewID
	 * @param username
	 * @param firstName
	 * @param lastName
	 * @param bookTitle
	 * @param reviewContent
	 * @param reviewDate
	 */
	public Review(String reviewID,String username,String firstName,String lastName,String bookTitle,String reviewContent,String reviewDate)
	{
		this.reviewID = reviewID;
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.bookTitle = bookTitle;
		this.reviewContent = reviewContent;
		this.reviewDate = reviewDate;
	}
	
	/** 
	 * Getter for reviewID.
	 * @return
	 */
	public String getReviewID()
	{
		return reviewID;
	}
	
	/**
	 * Getter for username.
	 * @return
	 */
	public String getUsername()
	{
		return username;
	}
	
	/**
	 * Getter for firstName.
	 * @return
	 */
	public String getFirstName()
	{
		return firstName;
	}
	
	/**
	 * Getter for lastName.
	 * @return
	 */
	public String getLastName()
	{
		return lastName;
	}
	
	/**
	 * Getter for bookTitle.
	 * @return
	 */
	public String getBookTitle()
	{
		return bookTitle;
	}
	
	/**
	 * Getter for reviewContent.
	 * @return
	 */
	public String getReviewContent()
	{
		return reviewContent;
	}
	
	/**
	 * Getter for reviewDate.
	 * @return
	 */
	public String getReviewDate()
	{
		return reviewDate;
	}

}
