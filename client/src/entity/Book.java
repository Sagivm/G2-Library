package entity;

import java.util.ArrayList;

/**
 * This entity stores book's details.
 * @author itain
 */
public class Book {
	
	/**
	 * serial number of book
	 */
	private int sn;
	
	/**
	 * title of book
	 */
	private String title;
	
	/**
	 * language of book
	 */
	private String language;
	
	/**
	 * summary of book
	 */
	private String summary;
	
	/**
	 * table Of Content of book
	 */
	private String tableOfContent;
	
	/**
	 * key words of book
	 */
	private String keywords;
	
	/**
	 * price of book
	 */
	private String price;
	
	/**
	 * book is hidden from catalog or not
	 */
	private boolean hide;
	
	
	/**
	 *authors of book
	 */
	private ArrayList <String> authors;
	
	/**
	 *authors of book
	 */
	private ArrayList <Author> authorsList;
	
	
	/**
	 *domains of book
	 */
	private ArrayList <String> domains;
	
	/**
	 *subjects of book
	 */
	private ArrayList <String> subjects;
	
	/**
	 *domains of book
	 */
	private ArrayList <Domain> domainsList;
	
	/**
	 *subjects of book
	 */
	private ArrayList <Subject> subjectsList;



	/**
	 * Book constructor store the data.
	 * @author itain
	 * @param sn Gets the sn.
	 * @param title Gets the title.
	 * @param language Gets the language.
	 * @param summary Gets the summary.
	 * @param tableOfContent Gets the tableOfContent.
	 * @param keywords Gets the keywords.
	 * @param price Gets the price.
	 * @param authors Gets the authors.
	 * @param domains Gets the domains.
	 * @param subjects Gets the subjects.
	 */
	
	public Book(int sn, String title, String language, String summary, String tableOfContent, String keywords,
			String price, ArrayList <String> authors, ArrayList <String> domains, ArrayList <String> subjects) 
	{
		
		this.sn = sn;
		this.title = title;
		this.language = language;
		this.summary = summary;
		this.tableOfContent = tableOfContent;
		this.keywords = keywords;
		this.price = price;
		this.hide = false;
		this.authors=authors;
		this.domains=domains;
		this.subjects=subjects;
	}
	
	
	/**
	 * Book constructor store the data.
	 * @author itain
	 * @param sn Gets the sn.
	 * @param title Gets the title.
	 * @param language Gets the language.
	 * @param summary Gets the summary.
	 * @param tableOfContent Gets the tableOfContent.
	 * @param keywords Gets the keywords.
	 * @param price Gets the price.
	 * @param authorsList Gets the authors.
	 */
	public Book(int sn,String title, String language, String summary, String tableOfContent, String keywords,
			String price, ArrayList <Author> authorsList) 
	{
		this.sn = sn;
		this.title = title;
		this.language = language;
		this.summary = summary;
		this.tableOfContent = tableOfContent;
		this.keywords = keywords;
		this.price = price;
		this.hide = false;
		this.authorsList=authorsList;
		this.domains=domains;
		this.subjects=subjects;
	}
	

	/**
	 * empty Book constructor
	 * @author itain
	 */
	public Book() {

	}

	/** Getter for sn
	 * @author itain
	 * @return sn the sn of book
	 */
	public int getSn() {
		return sn;
	}
	
	

	/** Setter for sn
	 * @author itain
	 *@param sn the sn of book
	 */
	public void setSn(int sn) {
		this.sn = sn;
	}

	/** Getter for title
	 * @author itain
	 * @return title
	 */
	public String getTitle() {
		return title;
	}
	
	/** Setter for title
	 * @author itain
	 * @param title the title of book
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/** Getter for language
	 * @author itain
	 * @return language
	 */
	public String getLanguage() {
		return language;
	}
	
	/** Setter for language
	 * @author itain
	 * @param language the language of book
	 */
	public void setLanguage(String language) {
		this.language = language;
	}
	
	/** Getter for summary
	 * @author itain
	 * @return summary
	 */
	public String getSummary() {
		return summary;
	}
	
	/** Setter for summary
	 * @author itain
	 * @param summary the summary of book
	 */
	public void setSummary(String summary) {
		this.summary = summary;
	}
	
	/** Getter for TableOfContents
	 * @author itain
	 * @return table of contents
	 */
	public String getTableOfContent() {
		return tableOfContent;
	}
	
	/** Setter for TableOfContents
	 * @author itain
	 * @param TableOfContents the Table Of Contents of book
	 */
	public void setTableOfContent(String tableOfContent) {
		this.tableOfContent = tableOfContent;
	}
	
	/** Getter for keywords
	 * @author itain
	 * @return keywords
	 */
	public String getKeywords() {
		return keywords;
	}
	
	/** Setter for keywords
	 * @author itain
	 * @param keywords the keywords of book
	 */
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	
	/** Getter for price
	 * @author itain
	 * @return price
	 */
	public String getPrice() {
		return price;
	}
	
	/** Setter for price
	 * @author itain
	 * @param price the price of book
	 */
	public void setPrice(String price) {
		this.price = price;
	}
	
	/** Getter for hide
	 * @author itain
	 * @return hide
	 */
	public boolean isHide() {
		return hide;
	}
	
	/** Setter for hide
	 * @author itain
	 * @param hide if book is hidden from catalog or not
	 */
	public void setHide(boolean hide) {
		this.hide = hide;
	}


	/** Getter for authors
	 * @author itain
	 * @return authors
	 */
	public ArrayList<String> getAuthors() {
		return authors;
	}
	
	/** Getter for authors
	 * @author itain
	 * @return authors
	 */
	public ArrayList<Author> getAuthorsList() {
		return authorsList;
	}

	/** Setter for authors
	 * @author itain
	 * @param authors list of authors
	 */
	public void setAuthors(ArrayList<String> authors) {
		this.authors = authors;
	}
	
	/** Setter for authors
	 * @author itain
	 * @param authors list of authors
	 */
	public void setAuthorsList(ArrayList<Author> authorsList) {
		this.authorsList = authorsList;
	}

	/** Getter for domains
	 * @author itain
	 * @return domains
	 */
	public ArrayList<String> getDomains() {
		return domains;
	}

	/** Setter for domains
	 * @author itain
	 * @param domains list of domains
	 */
	public void setDomains(ArrayList<String> domains) {
		this.domains = domains;
	}

	/** Getter for subjects
	 * @author itain
	 * @return subjects
	 */
	public ArrayList<String> getSubjects() {
		return subjects;
	}

	/** Setter for subjects
	 * @author itain
	 * @param subjects list of subjects
	 */
	public void setSubjects(ArrayList<String> subjects) {
		this.subjects = subjects;
	}

	/** Getter for domains
	 * @author itain
	 * @return domains
	 */
	public ArrayList<Domain> getDomainsList() {
		return domainsList;
	}
	
	
	/** Setter for domains
	 * @author itain
	 * @param domains list of domains
	 */
	public void setDomainsList(ArrayList<Domain> domainsList) {
		this.domainsList = domainsList;
	}

	/** Getter for subjects
	 * @author itain
	 * @return subjects
	 */
	public ArrayList<Subject> getSubjectsList() {
		return subjectsList;
	}

	/** Setter for subjects
	 * @author itain
	 * @param subjects list of subjects
	 */
	public void setSubjectsList(ArrayList<Subject> subjectsList) {
		this.subjectsList = subjectsList;
	}
	
	

	
}
