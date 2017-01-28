package entity;

import javafx.beans.property.SimpleStringProperty;

/**
 * This entity store the search book result details.
 * @author itain
 */
public class SearchBookResult {
	
	/**
	 * serial number of book
	 */
    private SimpleStringProperty bookSn;
    
	/**
	 * title of book
	 */
    private SimpleStringProperty bookTitle;
    
	/**
	 * language of book
	 */
    private SimpleStringProperty bookLanguage;
    
	/**
	 * summary of book
	 */
    private SimpleStringProperty bookSummary;
    
	/**
	 * table Of Content of book
	 */
    private SimpleStringProperty bookToc;
    
	/**
	 * key words of book
	 */
    private SimpleStringProperty bookKeywords;
    
	/**
	 *authors of book
	 */
    private SimpleStringProperty bookAuthors;
    
	/**
	 *subjects of book
	 */
    private SimpleStringProperty bookSubjects;
    
	/**
	 *domains of book
	 */
    private SimpleStringProperty bookDomains;
    
	/**
	 *price of book
	 */
    private SimpleStringProperty bookPrice;

    
	/**
	 * Book constructor store the data.
	 * @author itain
	 * @param bookSn Gets the sn.
	 * @param bookTitle Gets the title.
	 * @param bookLanguage Gets the title.
	 * @param bookSummary Gets the title.
	 * @param bookToc Gets the tableOfContent.
	 * @param bookKeywords Gets the keywords.
	 * @param bookPrice Gets the price.
	 * @param bookAuthors Gets the authors.
	 * @param bookDomains Gets the domains.
	 * @param bookSubjects Gets the subjects.
	 */
    public SearchBookResult(String bookSn, String bookTitle, String bookLanguage, String bookSummary, String bookToc, String bookKeywords, String bookAuthors, String bookSubjects, String bookDomains, String bookPrice) {
        this.bookSn = new SimpleStringProperty(bookSn);
        this.bookTitle = new SimpleStringProperty(bookTitle);
        this.bookLanguage = new SimpleStringProperty(bookLanguage);
        this.bookSummary = new SimpleStringProperty(bookSummary);
        this.bookToc = new SimpleStringProperty(bookToc);
        this.bookKeywords = new SimpleStringProperty(bookKeywords);
        this.bookAuthors = new SimpleStringProperty(bookAuthors);
        this.bookSubjects = new SimpleStringProperty(bookSubjects);
        this.bookDomains = new SimpleStringProperty(bookDomains);
        this.bookPrice = new SimpleStringProperty(bookPrice);
    }

	/** Getter for sn
	 * @author itain
	 * @return sn the sn of book
	 */
	public String getBookSn() {
		return bookSn.get();
	}
	
	
	/** Getter for title
	 * @author itain
	 * @return title
	 */
	public String getBookTitle() {
		return bookTitle.get();
	}

	
	/** Getter for language
	 * @author itain
	 * @return language
	 */
	public String getBookLanguage() {
		return bookLanguage.get();
	}

	
	/** Getter for summary
	 * @author itain
	 * @return summary
	 */
	public String getBookSummary() {
		return bookSummary.get();
	}
	
	
	/** Getter for TableOfContents
	 * @author itain
	 * @return table of contents
	 */
	public String getBookToc() {
		return bookToc.get();
	}

	
	/** Getter for keywords
	 * @author itain
	 * @return keywords
	 */
	public String getBookKeywords() {
		return bookKeywords.get();
	}

	/** Getter for authors
	 * @author itain
	 * @return authors
	 */
	public String getBookAuthors() {
		return bookAuthors.get();
	}

	/** Getter for subjects
	 * @author itain
	 * @return subjects
	 */
	public String getBookSubjects() {
		return bookSubjects.get();
	}

	/** Getter for domains
	 * @author itain
	 * @return domains
	 */
	public String getBookDomains() {
		return bookDomains.get();
	}
	
	/** Getter for price
	 * @author itain
	 * @return book price
	 */
	public String getBookPrice() {
		return bookPrice.get();
	}

	
	/** Setter for sn
	 * @author itain
	 *@param bookSn the sn of book
	 */
	public void setBookSn(String bookSn) {
		this.bookSn.set(bookSn);
	}

	/** Setter for title
	 * @author itain
	 * @param bookTitle the title of book
	 */
	public void setBookTitle(String bookTitle) {
		this.bookTitle.set(bookTitle);
	}

	/** Setter for language
	 * @author itain
	 * @param bookLanguage the language of book
	 */
	public void setBookLanguague(String bookLanguage) {
		this.bookLanguage.set(bookLanguage);
	}
	
	
	/** Setter for summary
	 * @author itain
	 * @param bookSummary the summary of book
	 */
	public void setBookSummary(String bookSummary) {
		this.bookSummary.set(bookSummary);
	}

	/** Setter for TableOfContents
	 * @author itain
	 * @param bookToc the Table Of Contents of book
	 */
	public void setBookToc(String bookToc) {
		this.bookToc.set(bookToc);
	}

	/** Setter for keywords
	 * @author itain
	 * @param bookKeywords the keywords of book
	 */
	public void setBookKeywords(String bookKeywords) {
		this.bookKeywords.set(bookKeywords);
	}

	/** Setter for authors
	 * @author itain
	 * @param bookAuthors authors
	 */
	public void setAuthors(String bookAuthors) {
		this.bookAuthors.set(bookAuthors);
	}

	/** Setter for subjects
	 * @author itain
	 * @param bookSubjects subjects
	 */
	public void setSubjects(String bookSubjects) {
		this.bookSubjects.set(bookSubjects);
	}

	/** Setter for domains
	 * @author itain
	 * @param bookDomains domains
	 */
	public void setDomains(String bookDomains) {
		this.bookDomains.set(bookDomains);
	}

	/** Setter for price
	 * @author itain
	 * @param bookPrice the price of book
	 */
	public void setPrice(String bookPrice) {
		this.bookPrice.set(bookPrice);
	}


}
