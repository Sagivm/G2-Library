package unittests;

import static org.junit.Assert.*;
import static org.hamcrest.core.IsNot.not;
import java.util.ArrayList;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import control.SearchBookController;
import entity.Book;
import entity.GeneralMessages;
import entity.Message;
import enums.ActionType;
import junit.framework.TestCase;

public class SearchBookControllerTest extends TestCase
{
	Book searchInputs;
	
	private Book bookHarryPotter;
	private ArrayList<String> authorsHarryPotter;
	private ArrayList<String> domainsHarryPotter;
	
	private Book bookLordOftheRings;
	private ArrayList<String> authorsLordOfTheRings;
	private ArrayList<String> domainsLordOfTheRings;
	
	//initiate 2 books
	protected void setUp() throws Exception {
		super.setUp();
	
		authorsHarryPotter = new ArrayList<String>();
		authorsHarryPotter.add("JK Rowling");
		authorsHarryPotter.add("Nir Efrati");
		domainsHarryPotter = new ArrayList<String>();
		domainsHarryPotter.add("Magic");
		domainsHarryPotter.add("Fantasy");
		bookHarryPotter = new Book(1, "Harry Potter", "English", "young magician", "The Boy Who Lived..3 The Vanishing Glass..25" , "harry, potter, magician, magic, troll", "25.6", authorsHarryPotter, domainsHarryPotter, new ArrayList<String>());
		
		authorsLordOfTheRings = new ArrayList<String>();
		authorsLordOfTheRings.add("JRR Tolkien");
		domainsLordOfTheRings = new ArrayList<String>();
		domainsLordOfTheRings.add("Magic");
		domainsLordOfTheRings.add("Medieval");
		bookLordOftheRings = new Book(2, "Lord Of the Rings", "Hebrew", "precious ring", "The shadow of the past..4  The black riders..32" , "ring, elf, hobbit, orc", "34.2", authorsLordOfTheRings, domainsLordOfTheRings, new ArrayList<String>());

	}

	//test AND search of author and language
	public void testAndSearch() {
		SearchBookController searchBook = new SearchBookController();
		ArrayList<String> authors = new ArrayList<String>();
		authors.add("JK Rowling");
		searchInputs = new Book(0, "", "English", "", "", "", "", authors, new ArrayList<String>(), new ArrayList<String>());
		
		Book expected = bookHarryPotter;
		
		Message output = searchBook.prepareSerachBook(ActionType.SEARCH_BOOK_AND, searchInputs);
		String resultAuthor = output.getElementsList().get(6);
		String resultLanguage = output.getElementsList().get(1);
		
		assertThat(expected.getAuthors().toString(), CoreMatchers.containsString(resultAuthor));
		assertEquals(expected.getLanguage(), resultLanguage);
	}
	
	//test OR search of 2 authors
	public void testOrSearch() {
		SearchBookController searchBook = new SearchBookController();
		ArrayList<String> authors = new ArrayList<String>();
		authors.add("JK Rowling");
		authors.add("JRR Tolkien");
		searchInputs = new Book(0, "", "", "", "", "", "", authors, new ArrayList<String>(), new ArrayList<String>());
		Message output = searchBook.prepareSerachBook(ActionType.SEARCH_BOOK_OR, searchInputs);
		String resultHarry = output.getElementsList().get(6);
		String resultLord = output.getElementsList().get(7);
		String expectedHarry = bookHarryPotter.getAuthors().toString();
		String expectedLord  = bookLordOftheRings.getAuthors().toString();
		
		assertThat(expectedHarry, CoreMatchers.containsString(resultHarry));
		assertThat(expectedLord, CoreMatchers.containsString(resultLord));
	}
	
	//test a search by title
	public void testSearchByTitle() {
		SearchBookController searchBook = new SearchBookController();
		searchInputs = new Book(0, "harry", "", "", "", "", "", new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>());
		Message output = searchBook.prepareSerachBook(ActionType.SEARCH_BOOK_AND, searchInputs);
		String result = output.getElementsList().get(0).toLowerCase();
		String expected = bookHarryPotter.getTitle().toLowerCase();
		
		assertThat(expected, CoreMatchers.containsString(result));
	}
	
	//test a search by language
	public void testSearchByLanguage() {
		SearchBookController searchBook = new SearchBookController();
		searchInputs = new Book(0, "", "English", "", "", "", "", new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>());
		Message output = searchBook.prepareSerachBook(ActionType.SEARCH_BOOK_AND, bookHarryPotter);
		String result = output.getElementsList().get(1);
		String expected = bookHarryPotter.getLanguage();
		
		assertEquals(expected, result);
	}
	
	//test a search by summary
	public void testSearchBySummary() {
		SearchBookController searchBook = new SearchBookController();
		searchInputs = new Book(0, "", "", "young", "", "", "", new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>());
		Message output = searchBook.prepareSerachBook(ActionType.SEARCH_BOOK_AND, searchInputs);
		String result = output.getElementsList().get(2);
		String expected = bookHarryPotter.getSummary();
		
		assertThat(expected, CoreMatchers.containsString(result));
	}
	
	//test a search by Table Of Contents
	public void testSearchByTableOfContents() {
		SearchBookController searchBook = new SearchBookController();
		searchInputs = new Book(0, "", "", "", "vanishing glass", "", "", new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>());
		Message output = searchBook.prepareSerachBook(ActionType.SEARCH_BOOK_AND, searchInputs);
		String result = output.getElementsList().get(3).toLowerCase();
		String expected = bookHarryPotter.getTableOfContent().toLowerCase(); 
		
		assertThat(expected, CoreMatchers.containsString(result));
	}
	
	//test a search by key words
	public void testSearchByKeyWords() {
		SearchBookController searchBook = new SearchBookController();
		searchInputs = new Book(0, "", "", "", "", "Troll, Elf", "", new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>());
		Message output = searchBook.prepareSerachBook(ActionType.SEARCH_BOOK_OR, searchInputs);
		String[] result = new String[2];
		result = output.getElementsList().get(4).toLowerCase().split("\\,");		
		String expectedHarry = bookHarryPotter.getKeywords().toLowerCase();
		String expectedLord = bookLordOftheRings.getKeywords().toLowerCase();
		
		assertThat(expectedHarry, CoreMatchers.containsString(result[0]));
		assertThat(expectedLord, CoreMatchers.containsString(result[1]));
	}
	
	//test a search by a single author
	public void testSearchBySingleAuthor() {
		SearchBookController searchBook = new SearchBookController();
		ArrayList<String> authors = new ArrayList<String>();
		authors.add("JK Rowling");
		searchInputs = new Book(0, "", "", "", "", "", "", authors, new ArrayList<String>(), new ArrayList<String>());
		Message output = searchBook.prepareSerachBook(ActionType.SEARCH_BOOK_AND, searchInputs);
		String result = output.getElementsList().get(6);
		String expected = bookHarryPotter.getAuthors().toString();
		
		assertThat(expected, CoreMatchers.containsString(result));
	}
	
	//test a search by multiple authors
	public void testSearchByMultipleAuthors() {
		SearchBookController searchBook = new SearchBookController();
		ArrayList<String> authors = new ArrayList<String>();
		authors.add("JK Rowling");
		authors.add("JRR Tolkien");
		searchInputs = new Book(0, "", "", "", "", "", "", authors, new ArrayList<String>(), new ArrayList<String>());
		Message output = searchBook.prepareSerachBook(ActionType.SEARCH_BOOK_OR, searchInputs);
		String result1 = output.getElementsList().get(6);
		String result2 = output.getElementsList().get(7);
		
		String expectedHarry = bookHarryPotter.getAuthors().toString();
		String expectedLord = bookLordOftheRings.getAuthors().toString();
		
		assertThat(expectedHarry, CoreMatchers.containsString(result1));
		assertThat(expectedLord, CoreMatchers.containsString(result2));

	}
	
	//test a search by a single domain
	public void testSearchBySingleDomain() {
		SearchBookController searchBook = new SearchBookController();
		ArrayList<String> domains = new ArrayList<String>();
		domains.add("Fantasy");
		searchInputs = new Book(0, "", "", "", "", "", "", new ArrayList<String>(), domains, new ArrayList<String>());
		Message output = searchBook.prepareSerachBook(ActionType.SEARCH_BOOK_AND, searchInputs);
		String result = output.getElementsList().get(7);
		String expected = bookHarryPotter.getDomains().toString();
		
		assertThat(expected, CoreMatchers.containsString(result));
	}
	
	//test a search by multiple authors
	public void testSearchByMultipleDomain() {
		SearchBookController searchBook = new SearchBookController();
		ArrayList<String> domains = new ArrayList<String>();
		domains.add("Fantasy");
		domains.add("Magic");
		searchInputs = new Book(0, "", "", "", "", "", "", new ArrayList<String>(), domains, new ArrayList<String>());
		Message output = searchBook.prepareSerachBook(ActionType.SEARCH_BOOK_OR, searchInputs);
		String result1 = output.getElementsList().get(7);
		String result2 = output.getElementsList().get(8);
		String expected1 = bookHarryPotter.getDomains().toString();
		String expected2 = bookLordOftheRings.getDomains().toString();
		
		assertThat(expected1, CoreMatchers.containsString(result1));
		assertThat(expected2, CoreMatchers.containsString(result2));
	}
	
	//test empty fields case
	public void testEmptyFields() {
		SearchBookController searchBook = new SearchBookController();
		searchInputs = new Book(0, "", "", "", "", "", "", new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>());
		Message output = searchBook.prepareSerachBook(ActionType.SEARCH_BOOK_AND, searchInputs);
		ArrayList<String> result=output.getElementsList();
		ArrayList<String> expected = new ArrayList<String>();
		for(int i=0;i<5;i++)
			expected.add("");
		for(int i=0;i<2;i++)
			expected.add("0");				
		
		assertEquals(expected, result);
	}
	
	
	//no result case
	public void testNoResult() {
		SearchBookController searchBook = new SearchBookController();
		searchInputs = new Book(0, "algebra", "", "", "", "", "", new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>());
		Message output = searchBook.prepareSerachBook(ActionType.SEARCH_BOOK_AND, searchInputs);
		String result = output.getElementsList().get(0).toLowerCase();
		String unexpected1 =  bookHarryPotter.getTitle().toLowerCase();
		String unexpected2 =  bookLordOftheRings.getTitle().toLowerCase();
		
		assertThat(unexpected1, not(CoreMatchers.containsString(result)));
		assertThat(unexpected2, not(CoreMatchers.containsString(result)));
	}

}



