package entity;

/**
 * Author class store the Data of Authors in system
 * @author itain
 *
 */
public class Author extends Person{
	/**
	 * Author constructor store the data.
	 * @author itain
	 * @param firstname Gets the firstname.
	 * @param lastname Gets the lastname.
	 * @param id Gets id.
	 */
	public Author(String id, String firstname, String lastname) {
		super(id, firstname, lastname);

	}
	
	/**
	 * empty Author constructor
	 * @author itain
	 */
	public Author() {
		super();
	}
	

	/** prints author's name
	 * @author itain
	 * @return full name of author
	 */
	public String toString() { 
	    return getFirstname()+" "+getLastname();
	} 
}



