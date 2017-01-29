package control.unitests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import org.junit.Test;
import control.SearchBookController;
import entity.Book;
import entity.Message;
import enums.ActionType;
import junit.framework.TestCase;

public class SearchBookControllerTest extends TestCase
{
	
	private Book book_and;
	private Book book_or;
	
	protected void setUp() throws Exception {
		super.setUp();
		book_and = new Book(0, "Harry Potter", "", "", "" , "", "", new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>());
		book_or = new Book(0, "", "", "", "" , "", "", new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>());

	}

	
	public void testAndSearch() {
		SearchBookController searchBook = new SearchBookController();
		Message output = searchBook.prepareSerachBook(ActionType.SEARCH_BOOK_AND, book_and);
		assertEquals(ActionType.SEARCH_BOOK_AND, output.getType());
	}
	

	public void testOrSearch() {
		SearchBookController searchBook = new SearchBookController();
		Message output = searchBook.prepareSerachBook(ActionType.SEARCH_BOOK_OR, book_or);
		assertEquals(ActionType.SEARCH_BOOK_OR, output.getType());
	}
	
	public void testSearchByTitle() {
		SearchBookController searchBook = new SearchBookController();
		Message output = searchBook.prepareSerachBook(ActionType.SEARCH_BOOK_AND, book_and);
		assertEquals("Harry Potter", output.getElementsList().get(0));
	}
	
	

}



