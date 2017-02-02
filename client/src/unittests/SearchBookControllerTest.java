package unittests;

import static org.junit.Assert.*;
import static org.hamcrest.core.IsNot.not;

import java.io.IOException;
import java.util.ArrayList;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.Test;

import boundry.ClientUI;
import control.ClientConnectionController;
import control.ClientController;
import control.SearchBookController;
import control.SearchBookResultsController;
import entity.Author;
import entity.Book;

import entity.Login;
import entity.Message;

import entity.SearchBookResult;
import enums.ActionType;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import junit.framework.TestCase;


public class SearchBookControllerTest extends TestCase
{
	public static SearchBookController searchBook;
	public static SearchBookResultsController searchBookResult;
	public static ClientController clientCon;
	public static ClientConnectionController clientCC;
	

	
	
	/**
	 * enables user enter title to search 
	 */
	public TextField titleTextField;
	
	/**
	 * show authors from DB 
	 */
	public ListView<String> authorListView;
	
	/**
	 * show available languages 
	 */
	public ComboBox<String> languageComboBox;
	
	/**
	 * enables user enter summary to search 
	 */
	public TextArea summaryTextArea;
	
	/**
	 * enables user enter table of contents to search 
	 */
	public TextArea tocTextArea;
	
	/**
	 * shows domains from DB 
	 */
	public ListView<String> domainListView;
	
	/**
	 * enables user enter summary to search 
	 */
	public TextArea keywTextArea;
	
	/**
	 * starts books search
	 */
	public Button searchButton;
	
	/**
	 * clears all fields 
	 */
	public Button clearButton;
	
	/**
	 * makes an AND search with users data 
	 */
	public RadioButton andRadioButton;
	
	/**
	 * makes an OR search with users data 
	 */
	public RadioButton orRadioButton;
	
	/**
	 * make radio buttons connected so user can choose only one 
	 */
	public ToggleGroup searchGroup;
	
	/**
	 * shows results table  
	 */
	private TableView resultsTable;
	
	/**
	 * shows books titles 
	 */
	public static TableColumn bookCol;
	
	/**
	 * shows books authors 
	 */
	public TableColumn authorsCol;
	
	/**
	 * shows books language 
	 */
	public TableColumn languageCol;
	
	/**
	 * shows books domains 
	 */
	public TableColumn domainsCol;
	
	/**
	 * shows books subjects 
	 */
	private TableColumn subjectsCol;
	
	/**
	 * shows button to enter book's page 
	 */
	private TableColumn bookPageCol;
	
	
	
	public static int getAuthorsFlag;
	public static int getDomainsFlag;
	public static int getSearchResultsFlag;
	public static int dataFlag;
	public static int connectedFlag;
	
	protected void setUp() throws Exception {
		super.setUp();
		Login login = new Login("123123123","123123");
		clientCC = new ClientConnectionController(ClientController.IP_ADDRESS, ClientController.DEFAULT_PORT);
		clientCon = new ClientController();
		connectedFlag=0;
		Message message = clientCon.prepareLogin(ActionType.LOGIN,login);
		/*
		while(connectedFlag==0)
			System.out.print("");
		*/
		clientCC.sendToServer(message);
	
		new JFXPanel();
		searchBook = new SearchBookController();
		titleTextField = new TextField();
		authorListView = new ListView<String>();
		languageComboBox = new ComboBox<String>();
		summaryTextArea = new TextArea();
		tocTextArea = new TextArea();
		domainListView = new ListView<String>();
		keywTextArea = new TextArea();
		searchButton = new Button();
		clearButton = new Button();
		andRadioButton = new RadioButton("AND");
		orRadioButton = new RadioButton("OR");
		searchGroup = new ToggleGroup();
		
		andRadioButton.setToggleGroup(searchGroup);
		orRadioButton.setToggleGroup(searchGroup);
		searchGroup.selectToggle(andRadioButton);
		
		new JFXPanel();
		searchBookResult = new SearchBookResultsController();
		resultsTable=new TableView<>();
		bookCol=new TableColumn<>();
		authorsCol=new TableColumn<>();
		languageCol=new TableColumn<>();
		domainsCol=new TableColumn<>();
		subjectsCol=new TableColumn<>();
		bookPageCol=new TableColumn<>();
		
		
		ArrayList<String> names=new ArrayList<String>();
		getAuthorsFlag=0;
		getDomainsFlag=0;
		ArrayList<String> elementList = new ArrayList<String>();

		Message msg1 = new Message(ActionType.GET_AUTHORS, elementList);
		clientCC.sendToServer(msg1);


		authorListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		while(getAuthorsFlag==0)
			System.out.print("");
		for(int i=0 ; i< SearchBookController.authorList.size();i++)
			names.add(i, "("+SearchBookController.authorList.get(i).getId()+")"+"\t"+SearchBookController.authorList.get(i).getFirstname()+" "+SearchBookController.authorList.get(i).getLastname());
		ObservableList<String> items = FXCollections.observableArrayList(names);
		authorListView.setItems(items);	
		
		
		Message msg2 = new Message(ActionType.GET_DOMAINS, SearchBookController.domainList);
		clientCC.sendToServer(msg2);
		
		domainListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		while(getDomainsFlag==0)
			System.out.print("");
		ObservableList<String> items2 = FXCollections.observableArrayList(SearchBookController.domainList);
		domainListView.setItems(items2);
	
		
		ObservableList<String> lanaguageOptions = FXCollections.observableArrayList("","Hebrew", "English", "Russian");
		languageComboBox.getItems().addAll(lanaguageOptions);
		languageComboBox.getSelectionModel().select(0);		
		
		
		
	}
	

	
	/* test AND search of author and language */
	@Test
	public void testAndSearch() {
		Book book1 = new Book(18,"", "", "", "", "","","","", "");
		ArrayList<Book> expected = new ArrayList<Book>();
		expected.add(book1);
		
		titleTextField.setText("harry");
		searchBook.titleTextField=titleTextField;
		searchBook.authorListView=authorListView;
		searchBook.languageComboBox = languageComboBox;
		searchBook.summaryTextArea = summaryTextArea;
		searchBook.tocTextArea = tocTextArea;
		searchBook.domainListView = domainListView;
		searchBook.keywTextArea = keywTextArea;
		searchBook.searchButton = searchButton;
		searchBook.clearButton = clearButton;
		searchBook.andRadioButton = andRadioButton;
		searchBook.orRadioButton = orRadioButton;
		searchBook.searchGroup = searchGroup;

		searchBookResult.resultsTable=resultsTable;
		searchBookResult.bookCol=bookCol;
		searchBookResult.authorsCol=authorsCol;
		searchBookResult.languageCol=languageCol;
		searchBookResult.domainsCol=domainsCol;
		searchBookResult.subjectsCol=subjectsCol;
		searchBookResult.bookPageCol=bookPageCol;
		
		getSearchResultsFlag=0;
		try {
			searchBook.searchButtonPressed(null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		while(getSearchResultsFlag==0)
			System.out.print("");
		dataFlag=0;
		searchBookResult.initialize();
		while(dataFlag==0)
			System.out.print("");
		
		ObservableList<SearchBookResult> res = SearchBookResultsController.data;
		ArrayList<Book> result = new ArrayList<Book>();
		for(int i=0;i<res.size();i++)
			result.add(new Book(Integer.parseInt(res.get(i).getBookSn()),res.get(i).getBookTitle(),res.get(i).getBookLanguage(),res.get(i).getBookSummary(), res.get(i).getBookToc(), res.get(i).getBookKeywords(), res.get(i).getBookPrice(), res.get(i).getBookAuthors(), res.get(i).getBookDomains(), res.get(i).getBookSubjects()));
		
		assertEquals(result,expected);
		/*
		assertEquals(expected.get(0).getSn(), result.get(0).getSn());
		assertEquals(expected.get(0).getTitle(), result.get(0).getTitle());
		assertEquals(expected.get(0).getSummary(), result.get(0).getSummary());
		assertEquals(expected.get(0).getTableOfContent(), result.get(0).getTableOfContent());
		assertEquals(expected.get(0).getKeywords(), result.get(0).getKeywords());
		assertEquals(expected.get(0).getPrice(), result.get(0).getPrice());
		assertEquals(expected.get(0).getAuthorsString(), result.get(0).getAuthorsString());
		assertEquals(expected.get(0).getDomainsString(), result.get(0).getDomainsString());
		assertEquals(expected.get(0).getSubjectsString(), result.get(0).getSubjectsString());
		*/
		/*
		searchBook.titleTextField.setText("harry");
		searchBook.languageComboBox.getSelectionModel().select(1);
		System.out.println("test6");
		searchBook.searchButton.getOnAction();
		String resultTitle=booksRes.bookCol.toString();
		String resultLanguage=booksRes.bookCol.toString();
		String expectedTitle = "Harry Potter";
		String expectedLanguage="English";
		assertThat(resultTitle, CoreMatchers.containsString(expectedTitle));
		assertThat(resultLanguage, CoreMatchers.containsString(expectedLanguage));
		*/
	}
	
//
//	
//	/* test OR search of 2 authors */
//	public void testOrSearch() {
//		SearchBookController searchBook = new SearchBookController();
//		ArrayList<String> authors = new ArrayList<String>();
//		authors.add("JK Rowling");
//		authors.add("JRR Tolkien");
//		searchInputs = new Book(0, "", "", "", "", "", "", authors, new ArrayList<String>(), new ArrayList<String>());
//		Message output = searchBook.prepareSerachBook(ActionType.SEARCH_BOOK_OR, searchInputs);
//		String resultHarry = output.getElementsList().get(6);
//		String resultLord = output.getElementsList().get(7);
//		String expectedHarry = bookHarryPotter.getAuthors().toString();
//		String expectedLord  = bookLordOftheRings.getAuthors().toString();
//		
//		String unExpectedAlgebra = bookAlgebra.getAuthors().toString();
//		
//		//found match with books 'Harry Potter' and 'Lord of the rings'
//		assertThat(expectedHarry, CoreMatchers.containsString(resultHarry));
//		assertThat(expectedLord, CoreMatchers.containsString(resultLord));
//		
//		//didn't find match with book 'Algebra 1'
//		assertThat(unExpectedAlgebra, not(CoreMatchers.containsString(resultHarry)));
//		assertThat(unExpectedAlgebra, not(CoreMatchers.containsString(resultLord)));
//	}
//	
//	/* test a search by title */
//	public void testSearchByTitle() {
//		SearchBookController searchBook = new SearchBookController();
//		searchInputs = new Book(0, "harry", "", "", "", "", "", new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>());
//		Message output = searchBook.prepareSerachBook(ActionType.SEARCH_BOOK_AND, searchInputs);
//		String result = output.getElementsList().get(0).toLowerCase();
//		String expected = bookHarryPotter.getTitle().toLowerCase();
//		String unExpectedAlgebra = bookAlgebra.getTitle().toLowerCase();
//		String unExpectedLord = bookLordOftheRings.getTitle().toLowerCase();
//		
//		//found match with book 'Harry Potter'
//		assertThat(expected, CoreMatchers.containsString(result));
//		
//		//didn't find match with books 'Algebra 1' and 'Lord of the rings"
//		assertThat(unExpectedAlgebra, not(CoreMatchers.containsString(result)));
//		assertThat(unExpectedLord, not(CoreMatchers.containsString(result)));
//	}
//	
//	/* test a search by language */
//	public void testSearchByLanguage() {
//		SearchBookController searchBook = new SearchBookController();
//		searchInputs = new Book(0, "", "English", "", "", "", "", new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>());
//		Message output = searchBook.prepareSerachBook(ActionType.SEARCH_BOOK_AND, searchInputs);
//		String result = output.getElementsList().get(1);
//		String expected = bookHarryPotter.getLanguage();
//		String unExpectedAlgebra = bookAlgebra.getLanguage();
//		String unExpectedLord = bookLordOftheRings.getLanguage();
//		
//		//found match with book 'Harry Potter'
//		assertEquals(expected, result);
//		
//		//didn't find match with book 'Algebra 1' and 'Lord of the rings"
//		assertNotEquals(unExpectedAlgebra, result);
//		assertNotEquals(unExpectedLord, result);
//	}
//	
//	/* test a search by summary */
//	public void testSearchBySummary() {
//		SearchBookController searchBook = new SearchBookController();
//		searchInputs = new Book(0, "", "", "young", "", "", "", new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>());
//		Message output = searchBook.prepareSerachBook(ActionType.SEARCH_BOOK_AND, searchInputs);
//		String result = output.getElementsList().get(2).toLowerCase();
//		String expected = bookHarryPotter.getSummary().toLowerCase();
//		String unExpectedAlgebra = bookAlgebra.getSummary().toLowerCase();
//		String unExpectedLord = bookLordOftheRings.getSummary().toLowerCase();
//		
//		//found match with book 'Harry Potter'
//		assertThat(expected, CoreMatchers.containsString(result));
//		
//		//didn't find match with books 'Algebra 1' and 'Lord of the rings"
//		assertNotEquals(unExpectedAlgebra, result);
//		assertNotEquals(unExpectedLord, result);
//	}
//	
//	/* test a search by Table Of Contents */
//	public void testSearchByTableOfContents() {
//		SearchBookController searchBook = new SearchBookController();
//		searchInputs = new Book(0, "", "", "", "glass", "", "", new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>());
//		Message output = searchBook.prepareSerachBook(ActionType.SEARCH_BOOK_AND, searchInputs);
//		String result = output.getElementsList().get(3).toLowerCase();
//		String expected = bookHarryPotter.getTableOfContent().toLowerCase(); 
//		String unExpectedAlgebra = bookAlgebra.getTableOfContent().toLowerCase();
//		String unExpectedLord = bookLordOftheRings.getTableOfContent().toLowerCase();
//		
//		//found match with book 'Harry Potter'
//		assertThat(expected, CoreMatchers.containsString(result));
//		
//		//didn't find match with books 'Algebra 1' and 'Lord of the rings"
//		assertThat(unExpectedAlgebra, not(CoreMatchers.containsString(result)));
//		assertThat(unExpectedLord, not(CoreMatchers.containsString(result)));
//	}
//	
//	/* test a search by key words */
//	public void testSearchByKeyWords() {
//		SearchBookController searchBook = new SearchBookController();
//		searchInputs = new Book(0, "", "", "", "", "troll, elf", "", new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>());
//		Message output = searchBook.prepareSerachBook(ActionType.SEARCH_BOOK_OR, searchInputs);
//		String[] result = new String[2];
//		result = output.getElementsList().get(4).toLowerCase().split("\\,");		
//		String expectedHarry = bookHarryPotter.getKeywords().toLowerCase();
//		String expectedLord = bookLordOftheRings.getKeywords().toLowerCase();
//		String unExpectedAlgebra = bookAlgebra.getKeywords().toLowerCase();
//		
//		//found match with books 'Harry Potter' and "Lord of the rings"
//		assertThat(expectedHarry, CoreMatchers.containsString(result[0]));
//		assertThat(expectedLord, CoreMatchers.containsString(result[1]));
//		
//		//didn't find match with book 'Algebra 1'
//		assertThat(unExpectedAlgebra, not(CoreMatchers.containsString(result[1])));
//	}
//	
//	/* test a search by a single author */
//	public void testSearchBySingleAuthor() {
//		SearchBookController searchBook = new SearchBookController();
//		ArrayList<String> authors = new ArrayList<String>();
//		authors.add("JK Rowling");
//		searchInputs = new Book(0, "", "", "", "", "", "", authors, new ArrayList<String>(), new ArrayList<String>());
//		Message output = searchBook.prepareSerachBook(ActionType.SEARCH_BOOK_AND, searchInputs);
//		String result = output.getElementsList().get(6);
//		String expected = bookHarryPotter.getAuthors().toString();
//		String unExpectedLord = bookLordOftheRings.getAuthors().toString();
//		String unExpectedAlgebra = bookAlgebra.getAuthors().toString();
//		
//		//found match with book 'Harry Potter'
//		assertThat(expected, CoreMatchers.containsString(result));
//		
//		//didn't find match with books 'Algebra 1' and 'Lord of the rings'
//		assertThat(unExpectedLord, not(CoreMatchers.containsString(result)));
//		assertThat(unExpectedAlgebra, not(CoreMatchers.containsString(result)));
//	}
//	
//	/* test a search by multiple authors */
//	public void testSearchByMultipleAuthors() {
//		SearchBookController searchBook = new SearchBookController();
//		ArrayList<String> authors = new ArrayList<String>();
//		authors.add("JK Rowling");
//		authors.add("JRR Tolkien");
//		searchInputs = new Book(0, "", "", "", "", "", "", authors, new ArrayList<String>(), new ArrayList<String>());
//		Message output = searchBook.prepareSerachBook(ActionType.SEARCH_BOOK_OR, searchInputs);
//		String result1 = output.getElementsList().get(6);
//		String result2 = output.getElementsList().get(7);
//		
//		String expectedHarry = bookHarryPotter.getAuthors().toString();
//		String expectedLord = bookLordOftheRings.getAuthors().toString();
//		String unExpectedAlgebra = bookAlgebra.getAuthors().toString();
//		
//		//found match with books 'Harry Potter' and 'Lord of the rings'
//		assertThat(expectedHarry, CoreMatchers.containsString(result1));
//		assertThat(expectedLord, CoreMatchers.containsString(result2));
//		
//		//didn't find match with book 'Algebra 1'
//		assertThat(unExpectedAlgebra, not(CoreMatchers.containsString(result1)));
//		assertThat(unExpectedAlgebra, not(CoreMatchers.containsString(result2)));
//	}
//	
//	/* test a search by a single domain */
//	public void testSearchBySingleDomain() {
//		SearchBookController searchBook = new SearchBookController();
//		ArrayList<String> domains = new ArrayList<String>();
//		domains.add("Fantasy");
//		searchInputs = new Book(0, "", "", "", "", "", "", new ArrayList<String>(), domains, new ArrayList<String>());
//		Message output = searchBook.prepareSerachBook(ActionType.SEARCH_BOOK_AND, searchInputs);
//		String result = output.getElementsList().get(7);
//		String expected = bookHarryPotter.getDomains().toString();
//		String unExpectedLord = bookLordOftheRings.getDomains().toString();
//		String unExpectedAlgebra = bookAlgebra.getDomains().toString();
//		
//		//found match with book 'Harry Potter'
//		assertThat(expected, CoreMatchers.containsString(result));
//		
//		//didn't find match with books 'Algebra 1' and 'Lord of the rings'
//		assertThat(unExpectedAlgebra, not(CoreMatchers.containsString(result)));
//		assertThat(unExpectedLord, not(CoreMatchers.containsString(result)));
//	}
//	
//	/* test a search by multiple authors */
//	public void testSearchByMultipleDomain() {
//		SearchBookController searchBook = new SearchBookController();
//		ArrayList<String> domains = new ArrayList<String>();
//		domains.add("Fantasy");
//		domains.add("Magic");
//		searchInputs = new Book(0, "", "", "", "", "", "", new ArrayList<String>(), domains, new ArrayList<String>());
//		Message output = searchBook.prepareSerachBook(ActionType.SEARCH_BOOK_OR, searchInputs);
//		String result1 = output.getElementsList().get(7);
//		String result2 = output.getElementsList().get(8);
//		String expected1 = bookHarryPotter.getDomains().toString();
//		String expected2 = bookLordOftheRings.getDomains().toString();
//		String unExpectedAlgebra = bookAlgebra.getDomains().toString();
//		
//		//found match with books 'Harry Potter' and 'Lord of the rings'
//		assertThat(expected1, CoreMatchers.containsString(result1));
//		assertThat(expected2, CoreMatchers.containsString(result2));
//		
//		//didn't find match with book 'Algebra 1'
//		assertThat(unExpectedAlgebra, not(CoreMatchers.containsString(result1)));
//		assertThat(unExpectedAlgebra, not(CoreMatchers.containsString(result2)));
//	}
//	
//	/* test empty fields case */
//	public void testEmptyFields() {
//		SearchBookController searchBook = new SearchBookController();
//		searchInputs = new Book(0, "", "", "", "", "", "", new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>());
//		Message output = searchBook.prepareSerachBook(ActionType.SEARCH_BOOK_AND, searchInputs);
//		ArrayList<String> result=output.getElementsList();
//		ArrayList<String> expected = new ArrayList<String>();
//		for(int i=0;i<5;i++)
//			expected.add("");
//		for(int i=0;i<2;i++)
//			expected.add("0");				
//		
//		assertEquals(expected, result);
//	}
//	
//	
//	/* no result case */
//	public void testNoResult() {
//		SearchBookController searchBook = new SearchBookController();
//		searchInputs = new Book(0, "java", "", "", "", "", "", new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>());
//		Message output = searchBook.prepareSerachBook(ActionType.SEARCH_BOOK_AND, searchInputs);
//		String result = output.getElementsList().get(0).toLowerCase();
//		String unExpectedHarry =  bookHarryPotter.getTitle().toLowerCase();
//		String unExpectedLord =  bookLordOftheRings.getTitle().toLowerCase();
//		String unExpectedAlgebra =  bookAlgebra.getTitle().toLowerCase();
//		
//		//didn't find any match
//		assertThat(unExpectedHarry, not(CoreMatchers.containsString(result)));
//		assertThat(unExpectedLord, not(CoreMatchers.containsString(result)));
//		assertThat(unExpectedAlgebra, not(CoreMatchers.containsString(result)));
//	}
	
	

}


/** This class makes sure the information from the server was received successfully.
 * @author itain
 */
class SearchBookAuthorsRecv extends Thread{
	
	/**
	 * Get true after receiving values from DB.
	 */
	public static boolean canContinue = false;
	
	@Override
	public void run() {
		synchronized (this) {
        	while(canContinue == false)
    		{
        		System.out.print("");
    		}
        	canContinue = false;
			notify();
		}
	}
	
}

/** This class makes sure the information from the server was received successfully.
 * @author itain
 */
class SearchBookDomainsRecv extends Thread{
	
	/**
	 * Get true after receiving values from DB.
	 */
	public static boolean canContinue = false;
	
	@Override
	public void run() {
		synchronized (this) {
        	while(canContinue == false)
    		{
        		System.out.print("");
    		}
        	canContinue = false;
			notify();
		}
	}
	
}

