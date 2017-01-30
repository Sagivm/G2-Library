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
	
	private Book bookAlgebra;
	private ArrayList<String> authorsAlgebra;
	private ArrayList<String> domainsAlgebra;
	
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
		
		authorsAlgebra = new ArrayList<String>();
		authorsAlgebra.add("Charles Hall");
		authorsAlgebra.add("Basia Kennedy");
		domainsAlgebra = new ArrayList<String>();
		domainsAlgebra.add("Mathematics");
		bookAlgebra = new Book(3, "Algebra 1", "Russian", "prepare students for the new Algebra I (Common Core) Regents exam", "Foundations For Algebra..5  Solving Equations..15" , "math, teaching, algebra", "45.7", authorsAlgebra, domainsAlgebra, new ArrayList<String>());
	}

	/* test AND search of author and language */
	public void testAndSearch() {
		//make a new search
		SearchBookController searchBook = new SearchBookController();
		ArrayList<String> authors = new ArrayList<String>();
		authors.add("JK Rowling");
		searchInputs = new Book(0, "", "English", "", "", "", "", authors, new ArrayList<String>(), new ArrayList<String>());
		Message output = searchBook.prepareSerachBook(ActionType.SEARCH_BOOK_AND, searchInputs);
		String resultAuthor = output.getElementsList().get(6);
		String resultLanguage = output.getElementsList().get(1);
		
		//expected and unexpected results
		String expectedAuthors = bookHarryPotter.getAuthors().toString();
		String expectedLanguage = bookHarryPotter.getLanguage();
		String unExpectedAuthorsAlgebra = bookAlgebra.getAuthors().toString();
		String unExpectedLanguageAlgebra = bookAlgebra.getLanguage();
		String unExpectedAuthorsLord = bookLordOftheRings.getAuthors().toString();
		String unExpectedLanguageLord = bookLordOftheRings.getLanguage();
		
		//found match with book 'Harry Potter'
		assertThat(expectedAuthors, CoreMatchers.containsString(resultAuthor));
		assertEquals(expectedLanguage, resultLanguage);
		
		//didn't find match with books 'Algebra 1' and "Lord of the rings"
		assertThat(unExpectedAuthorsAlgebra, not(CoreMatchers.containsString(resultAuthor)));
		assertNotEquals(unExpectedLanguageAlgebra, resultLanguage);
		
		assertThat(unExpectedAuthorsLord, not(CoreMatchers.containsString(resultAuthor)));
		assertNotEquals(unExpectedLanguageLord, resultLanguage);
	}
	
	/* test OR search of 2 authors */
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
		
		String unExpectedAlgebra = bookAlgebra.getAuthors().toString();
		
		//found match with books 'Harry Potter' and 'Lord of the rings'
		assertThat(expectedHarry, CoreMatchers.containsString(resultHarry));
		assertThat(expectedLord, CoreMatchers.containsString(resultLord));
		
		//didn't find match with book 'Algebra 1'
		assertThat(unExpectedAlgebra, not(CoreMatchers.containsString(resultHarry)));
		assertThat(unExpectedAlgebra, not(CoreMatchers.containsString(resultLord)));
	}
	
	/* test a search by title */
	public void testSearchByTitle() {
		SearchBookController searchBook = new SearchBookController();
		searchInputs = new Book(0, "harry", "", "", "", "", "", new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>());
		Message output = searchBook.prepareSerachBook(ActionType.SEARCH_BOOK_AND, searchInputs);
		String result = output.getElementsList().get(0).toLowerCase();
		String expected = bookHarryPotter.getTitle().toLowerCase();
		String unExpectedAlgebra = bookAlgebra.getTitle().toLowerCase();
		String unExpectedLord = bookLordOftheRings.getTitle().toLowerCase();
		
		//found match with book 'Harry Potter'
		assertThat(expected, CoreMatchers.containsString(result));
		
		//didn't find match with books 'Algebra 1' and 'Lord of the rings"
		assertThat(unExpectedAlgebra, not(CoreMatchers.containsString(result)));
		assertThat(unExpectedLord, not(CoreMatchers.containsString(result)));
	}
	
	/* test a search by language */
	public void testSearchByLanguage() {
		SearchBookController searchBook = new SearchBookController();
		searchInputs = new Book(0, "", "English", "", "", "", "", new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>());
		Message output = searchBook.prepareSerachBook(ActionType.SEARCH_BOOK_AND, searchInputs);
		String result = output.getElementsList().get(1);
		String expected = bookHarryPotter.getLanguage();
		String unExpectedAlgebra = bookAlgebra.getLanguage();
		String unExpectedLord = bookLordOftheRings.getLanguage();
		
		//found match with book 'Harry Potter'
		assertEquals(expected, result);
		
		//didn't find match with book 'Algebra 1' and 'Lord of the rings"
		assertNotEquals(unExpectedAlgebra, result);
		assertNotEquals(unExpectedLord, result);
	}
	
	/* test a search by summary */
	public void testSearchBySummary() {
		SearchBookController searchBook = new SearchBookController();
		searchInputs = new Book(0, "", "", "young", "", "", "", new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>());
		Message output = searchBook.prepareSerachBook(ActionType.SEARCH_BOOK_AND, searchInputs);
		String result = output.getElementsList().get(2).toLowerCase();
		String expected = bookHarryPotter.getSummary().toLowerCase();
		String unExpectedAlgebra = bookAlgebra.getSummary().toLowerCase();
		String unExpectedLord = bookLordOftheRings.getSummary().toLowerCase();
		
		//found match with book 'Harry Potter'
		assertThat(expected, CoreMatchers.containsString(result));
		
		//didn't find match with books 'Algebra 1' and 'Lord of the rings"
		assertNotEquals(unExpectedAlgebra, result);
		assertNotEquals(unExpectedLord, result);
	}
	
	/* test a search by Table Of Contents */
	public void testSearchByTableOfContents() {
		SearchBookController searchBook = new SearchBookController();
		searchInputs = new Book(0, "", "", "", "glass", "", "", new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>());
		Message output = searchBook.prepareSerachBook(ActionType.SEARCH_BOOK_AND, searchInputs);
		String result = output.getElementsList().get(3).toLowerCase();
		String expected = bookHarryPotter.getTableOfContent().toLowerCase(); 
		String unExpectedAlgebra = bookAlgebra.getTableOfContent().toLowerCase();
		String unExpectedLord = bookLordOftheRings.getTableOfContent().toLowerCase();
		
		//found match with book 'Harry Potter'
		assertThat(expected, CoreMatchers.containsString(result));
		
		//didn't find match with books 'Algebra 1' and 'Lord of the rings"
		assertThat(unExpectedAlgebra, not(CoreMatchers.containsString(result)));
		assertThat(unExpectedLord, not(CoreMatchers.containsString(result)));
	}
	
	/* test a search by key words */
	public void testSearchByKeyWords() {
		SearchBookController searchBook = new SearchBookController();
		searchInputs = new Book(0, "", "", "", "", "troll, elf", "", new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>());
		Message output = searchBook.prepareSerachBook(ActionType.SEARCH_BOOK_OR, searchInputs);
		String[] result = new String[2];
		result = output.getElementsList().get(4).toLowerCase().split("\\,");		
		String expectedHarry = bookHarryPotter.getKeywords().toLowerCase();
		String expectedLord = bookLordOftheRings.getKeywords().toLowerCase();
		String unExpectedAlgebra = bookAlgebra.getKeywords().toLowerCase();
		
		//found match with books 'Harry Potter' and "Lord of the rings"
		assertThat(expectedHarry, CoreMatchers.containsString(result[0]));
		assertThat(expectedLord, CoreMatchers.containsString(result[1]));
		
		//didn't find match with book 'Algebra 1'
		assertThat(unExpectedAlgebra, not(CoreMatchers.containsString(result[1])));
	}
	
	/* test a search by a single author */
	public void testSearchBySingleAuthor() {
		SearchBookController searchBook = new SearchBookController();
		ArrayList<String> authors = new ArrayList<String>();
		authors.add("JK Rowling");
		searchInputs = new Book(0, "", "", "", "", "", "", authors, new ArrayList<String>(), new ArrayList<String>());
		Message output = searchBook.prepareSerachBook(ActionType.SEARCH_BOOK_AND, searchInputs);
		String result = output.getElementsList().get(6);
		String expected = bookHarryPotter.getAuthors().toString();
		String unExpectedLord = bookLordOftheRings.getAuthors().toString();
		String unExpectedAlgebra = bookAlgebra.getAuthors().toString();
		
		//found match with book 'Harry Potter'
		assertThat(expected, CoreMatchers.containsString(result));
		
		//didn't find match with books 'Algebra 1' and 'Lord of the rings'
		assertThat(unExpectedLord, not(CoreMatchers.containsString(result)));
		assertThat(unExpectedAlgebra, not(CoreMatchers.containsString(result)));
	}
	
	/* test a search by multiple authors */
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
		String unExpectedAlgebra = bookAlgebra.getAuthors().toString();
		
		//found match with books 'Harry Potter' and 'Lord of the rings'
		assertThat(expectedHarry, CoreMatchers.containsString(result1));
		assertThat(expectedLord, CoreMatchers.containsString(result2));
		
		//didn't find match with book 'Algebra 1'
		assertThat(unExpectedAlgebra, not(CoreMatchers.containsString(result1)));
		assertThat(unExpectedAlgebra, not(CoreMatchers.containsString(result2)));
	}
	
	/* test a search by a single domain */
	public void testSearchBySingleDomain() {
		SearchBookController searchBook = new SearchBookController();
		ArrayList<String> domains = new ArrayList<String>();
		domains.add("Fantasy");
		searchInputs = new Book(0, "", "", "", "", "", "", new ArrayList<String>(), domains, new ArrayList<String>());
		Message output = searchBook.prepareSerachBook(ActionType.SEARCH_BOOK_AND, searchInputs);
		String result = output.getElementsList().get(7);
		String expected = bookHarryPotter.getDomains().toString();
		String unExpectedLord = bookLordOftheRings.getDomains().toString();
		String unExpectedAlgebra = bookAlgebra.getDomains().toString();
		
		//found match with book 'Harry Potter'
		assertThat(expected, CoreMatchers.containsString(result));
		
		//didn't find match with books 'Algebra 1' and 'Lord of the rings'
		assertThat(unExpectedAlgebra, not(CoreMatchers.containsString(result)));
		assertThat(unExpectedLord, not(CoreMatchers.containsString(result)));
	}
	
	/* test a search by multiple authors */
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
		String unExpectedAlgebra = bookAlgebra.getDomains().toString();
		
		//found match with books 'Harry Potter' and 'Lord of the rings'
		assertThat(expected1, CoreMatchers.containsString(result1));
		assertThat(expected2, CoreMatchers.containsString(result2));
		
		//didn't find match with book 'Algebra 1'
		assertThat(unExpectedAlgebra, not(CoreMatchers.containsString(result1)));
		assertThat(unExpectedAlgebra, not(CoreMatchers.containsString(result2)));
	}
	
	/* test empty fields case */
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
	
	
	/* no result case */
	public void testNoResult() {
		SearchBookController searchBook = new SearchBookController();
		searchInputs = new Book(0, "java", "", "", "", "", "", new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>());
		Message output = searchBook.prepareSerachBook(ActionType.SEARCH_BOOK_AND, searchInputs);
		String result = output.getElementsList().get(0).toLowerCase();
		String unExpectedHarry =  bookHarryPotter.getTitle().toLowerCase();
		String unExpectedLord =  bookLordOftheRings.getTitle().toLowerCase();
		String unExpectedAlgebra =  bookAlgebra.getTitle().toLowerCase();
		
		//didn't find any match
		assertThat(unExpectedHarry, not(CoreMatchers.containsString(result)));
		assertThat(unExpectedLord, not(CoreMatchers.containsString(result)));
		assertThat(unExpectedAlgebra, not(CoreMatchers.containsString(result)));
	}

}



