package entity;

/**
 * This entity stores subject's details.
 * @author itain
 */
public class Subject {
	/**
	 * id of subject
	 */
	private int id;
	
	/**
	 * name of domain
	 */
	private String name;
	
	/**
	 * number of books under this subject
	 */
	private int booksCount;
	
	/**
	 * subject's domain id
	 */
	private int domain;
	

	/**
	 * Subject constructor store the data.
	 * @author itain
	 * @param id Gets the id.
	 * @param name Gets the name.
	 * @param booksCount Gets the number of books under this subject.
	 * @param domain Gets the number of books under this subject.
	 
	 */
	public Subject(int id, String name, int booksCount, int domain) {
		this.id = id;
		this.name = name;
		this.booksCount = booksCount;
		this.domain = domain;
		
		
	}
	
	/**
	 * empty subject constructor.
	 * @author itain
	  */ 
	public Subject() {

	}

	/** Getter for id
	 * @author itain
	 * @return id
	 */
	public int getId() {
		return id;
	}

	/** Setter for id
	 * @author itain
	 * @param id id of subject
	 */
	public void setId(int id) {
		this.id=id;
	}

	/** Getter for name
	 * @author itain
	 * @return name of subject
	 */
	public String getName() {
		return name;
	}

	/** Setter for name
	 * @author itain
	 * @param name the name of subject
	 */
	public void setName(String name) {
		this.name = name;
	}

	/** Getter for booksCount
	 * @author itain
	 * @return booksCount
	 */
	public int getBooksCount() {
		return booksCount;
	}

	/** Setter for booksCount
	 * @author itain
	 * @param booksCount number of books under this subject
	 */
	public void setBooksCount(int booksCount) {
		this.booksCount = booksCount;
	}

	/** Getter for domain
	 * @author itain
	 * @return domain
	 */
	public int getDomain() {
		return domain;
	}

	/** Setter for domain
	 * @author itain
	 * @param domain id of subject's domain
	 */
	public void setDomain(int domain) {
		this.domain = domain;
	}
	
	
}
