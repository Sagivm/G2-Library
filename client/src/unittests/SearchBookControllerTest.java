package unittests;

import static org.junit.Assert.*;
import static org.hamcrest.core.IsNot.not;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.Before;
import org.junit.Rule;

import boundry.ClientUI;
import control.ClientConnectionController;
import control.ClientController;
import control.SearchBookController;
import control.SearchBookResultsController;
import entity.Author;
import entity.Book;
import control.HomepageUserController;
import control.LogoutController;
import entity.User;
import entity.Login;
import entity.Message;
import javafx.scene.control.Alert;
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
import ocsf.client.AbstractClient;
import javafx.scene.control.MultipleSelectionModel;

public class SearchBookControllerTest extends TestCase
{
	public static SearchBookController searchBook;
	public static SearchBookResultsController searchBookResult;
	public static ClientController clientCon;
	public static ClientConnectionController clientCC=null;
	public static ClientUI clientMain;
	public static HomepageUserController userMain;

	
	
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
    public static int disconnectedFlag;
    public static int getEmptyFieldAnswerFlag;
    public static boolean emptyFieldsFlag;
    public static int getSpecialCharacterAnswerFlag;
    public static boolean SpecialCharacterFlag;
    Login login;
    User user;  
    //
   
    @Before
	protected void setUp() throws Exception {
		super.setUp();
		
		clientMain = new ClientUI();
		clientMain.setTypeOfUser("User");
		
		clientMain.testMode = true;
		Login login = new Login("302659743","12345");
		if(clientCC!=null)
		{
			clientCC.closeConnection();
			clientCC = new ClientConnectionController(ClientController.IP_ADDRESS, ClientController.DEFAULT_PORT);
			clientCon = new ClientController();
			connectedFlag=0;
	        
			Message msg = clientCon.prepareLogin(ActionType.LOGIN,login);
	
			clientCC.sendToServer(msg);
			
			user = new User("Or","Koren","302659743","12345","PerBook","Standard");
			userMain=new HomepageUserController();
			userMain.setConnectedUser(user);
		}
		
        //while(connectedFlag==0)
            //System.out.print("");
	
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
		
		//new JFXPanel();
		searchBookResult = new SearchBookResultsController();
		resultsTable=new TableView<>();
		bookCol=new TableColumn<>();
		authorsCol=new TableColumn<>();
		languageCol=new TableColumn<>();
		domainsCol=new TableColumn<>();
		subjectsCol=new TableColumn<>();
		bookPageCol=new TableColumn<>();
		
		
		ArrayList<String> names=new ArrayList<String>();
		ArrayList<String> elementList = new ArrayList<String>();
		getAuthorsFlag=0;
		getDomainsFlag=0;
		

		Message msg1 = new Message(ActionType.GET_AUTHORS, elementList);
		
		if(clientCC==null)
			clientCC = new ClientConnectionController(ClientController.IP_ADDRESS, ClientController.DEFAULT_PORT);
		
		//TimeUnit.SECONDS.sleep(1);
		clientCC.sendToServer(msg1);


		while(getAuthorsFlag==0)
			System.out.print("");
		getAuthorsFlag=0;
		authorListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		for(int i=0 ; i< SearchBookController.authorList.size();i++)
			names.add(i, "("+SearchBookController.authorList.get(i).getId()+")"+"\t"+SearchBookController.authorList.get(i).getFirstname()+" "+SearchBookController.authorList.get(i).getLastname());
		ObservableList<String> items = FXCollections.observableArrayList(names);
		authorListView.setItems(items);	
		
		
		Message msg2 = new Message(ActionType.GET_DOMAINS, SearchBookController.domainList);
		clientCC.sendToServer(msg2);
		
		domainListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		while(getDomainsFlag==0)
			System.out.print("");
		getDomainsFlag=0;
		ObservableList<String> items2 = FXCollections.observableArrayList(SearchBookController.domainList);
		domainListView.setItems(items2);
	
		
		ObservableList<String> lanaguageOptions = FXCollections.observableArrayList("","Hebrew", "English", "Russian");
		languageComboBox.getItems().addAll(lanaguageOptions);
		languageComboBox.getSelectionModel().select(0);		

	}
    

    /*
    protected void tearDown() throws Exception{
        disconnectedFlag=0;
        LogoutController logoutCon = new LogoutController();
        Message msg = logoutCon.prepareLogout(ActionType.LOGOUT, login);
        try {
            clientCC.sendToServer(msg);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        while(disconnectedFlag==0)
            System.out.print("");
    }
    */


	
	/* test AND search of author and language */
	@Test
	public void testAndSearch() {
        Book book1 = new Book(27,"", "", "", "", "","","","", "");
        Book book2 = new Book(28,"", "", "", "", "","","","", "");
        Book book3 = new Book(29,"", "", "", "", "","","","", "");
        Book book4 = new Book(30,"", "", "", "", "","","","", "");
        ArrayList<Book> expected = new ArrayList<Book>();
        expected.add(book1); expected.add(book4); expected.add(book2); expected.add(book3); 
        
        
        authorListView.getSelectionModel().select(10);
        languageComboBox.getSelectionModel().select(2);
        setUpSearchAndResultControllers();
        
        getSearchResultsFlag=0;
        try {
            searchBook.searchButtonPressed(null);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
 
        while(getSearchResultsFlag==0)
            System.out.print("");
        getSearchResultsFlag=0;
        ArrayList<Book> result = getSearchResults();
        if(result.size()==expected.size())
            for(int i=0;i<result.size();i++)
                assertEquals(result.get(i),expected.get(i));
        
        
    }
    
 
    
 
    /* test OR search of 2 authors */
    public void testOrSearch() {
        Book book1 = new Book(18,"", "", "", "", "","","","", "");
        Book book2 = new Book(27,"", "", "", "", "","","","", "");
        Book book3 = new Book(28,"", "", "", "", "","","","", "");
        Book book4 = new Book(29,"", "", "", "", "","","","", "");
        Book book5 = new Book(30,"", "", "", "", "","","","", "");
        ArrayList<Book> expected = new ArrayList<Book>();
        expected.add(book1); expected.add(book2); expected.add(book5); expected.add(book3); expected.add(book4); 
        
        searchGroup.selectToggle(orRadioButton);
        authorListView.getSelectionModel().select(0);
        authorListView.getSelectionModel().select(10);
        
        setUpSearchAndResultControllers();
        getSearchResultsFlag=0;
        try {
            searchBook.searchButtonPressed(null);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        /*
        while(getSearchResultsFlag==0)
            System.out.print(""); 
            */
        getSearchResultsFlag=0;
        ArrayList<Book> result = getSearchResults();
        if(result.size()==expected.size())
            for(int i=0;i<result.size();i++)
                assertEquals(result.get(i),expected.get(i));
    }
    
    
    @Test
    //test a search by title
    public void testSearchByTitle() {
        Book book1 = new Book(18,"", "", "", "", "","","","", "");
        ArrayList<Book> expected = new ArrayList<Book>();
        expected.add(book1);
        
        titleTextField.setText("harry");
        setUpSearchAndResultControllers();
        
        getSearchResultsFlag=0;
        try {
            searchBook.searchButtonPressed(null);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
 
        while(getSearchResultsFlag==0)
            System.out.print(""); 
        getSearchResultsFlag=0;
        ArrayList<Book> result = getSearchResults();
        if(result.size()==expected.size())  
            for(int i=0;i<result.size();i++)
                assertEquals(result.get(i),expected.get(i));
    }
    
    
    /* test a search by language */
    public void testSearchByLanguage() {
        Book book1 = new Book(19,"", "", "", "", "","","","", "");
        ArrayList<Book> expected = new ArrayList<Book>();
        expected.add(book1);
        
        languageComboBox.getSelectionModel().select(3);
        setUpSearchAndResultControllers();
        
        getSearchResultsFlag=0;
        try {
            searchBook.searchButtonPressed(null);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
 
        while(getSearchResultsFlag==0)
            System.out.print(""); 
        getSearchResultsFlag=0;
        ArrayList<Book> result = getSearchResults();
        if(result.size()==expected.size())  
            for(int i=0;i<result.size();i++)
                assertEquals(result.get(i),expected.get(i));
    }
    
    /* test a search by summary */
    public void testSearchBySummary() {
        Book book1 = new Book(21,"", "", "", "", "","","","", "");
        ArrayList<Book> expected = new ArrayList<Book>();
        expected.add(book1);
        
        summaryTextArea.setText("algebra");
        setUpSearchAndResultControllers();
        
        getSearchResultsFlag=0;
        try {
            searchBook.searchButtonPressed(null);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
 
        while(getSearchResultsFlag==0)
            System.out.print(""); 
        getSearchResultsFlag=0;
        ArrayList<Book> result = getSearchResults();
        if(result.size()==expected.size())  
            for(int i=0;i<result.size();i++)
                assertEquals(result.get(i),expected.get(i));
    }
    
    /* test a search by Table Of Contents */
    public void testSearchByTableOfContents() {
        Book book1 = new Book(19,"", "", "", "", "","","","", "");
        Book book2 = new Book(25,"", "", "", "", "","","","", "");
        Book book3 = new Book(21,"", "", "", "", "","","","", "");
        ArrayList<Book> expected = new ArrayList<Book>();
        expected.add(book3); expected.add(book1); expected.add(book2);
        
        tocTextArea.setText("introduction");
        setUpSearchAndResultControllers();
        
        getSearchResultsFlag=0;
        try {
            searchBook.searchButtonPressed(null);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
 
        while(getSearchResultsFlag==0)
            System.out.print(""); 
        getSearchResultsFlag=0;
        ArrayList<Book> result = getSearchResults();
        if(result.size()==expected.size())  
            for(int i=0;i<result.size();i++)
                assertEquals(result.get(i),expected.get(i));
    }
    
    /* test a search by key words */
    public void testSearchByKeyWords() {
        Book book1 = new Book(27,"", "", "", "", "","","","", "");
        Book book2 = new Book(28,"", "", "", "", "","","","", "");
        Book book3 = new Book(29,"", "", "", "", "","","","", "");
        ArrayList<Book> expected = new ArrayList<Book>();
        expected.add(book1); expected.add(book2); expected.add(book3);
        
        keywTextArea.setText("travel, europe");
        setUpSearchAndResultControllers();
        
        getSearchResultsFlag=0;
        try {
            searchBook.searchButtonPressed(null);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
 
        while(getSearchResultsFlag==0)
            System.out.print(""); 
        getSearchResultsFlag=0;
        ArrayList<Book> result = getSearchResults();
        if(result.size()==expected.size())  
            for(int i=0;i<result.size();i++)
                assertEquals(result.get(i),expected.get(i));
    }
    
    /* test a search by a single author */
    public void testSearchBySingleAuthor() {
        Book book1 = new Book(26,"", "", "", "", "","","","", "");
        ArrayList<Book> expected = new ArrayList<Book>();
        expected.add(book1);
        
        authorListView.getSelectionModel().select(8);
        setUpSearchAndResultControllers();
        
        getSearchResultsFlag=0;
        try {
            searchBook.searchButtonPressed(null);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
 
        while(getSearchResultsFlag==0)
            System.out.print(""); 
        getSearchResultsFlag=0;
        ArrayList<Book> result = getSearchResults();
        if(result.size()==expected.size())  
            for(int i=0;i<result.size();i++)
                assertEquals(result.get(i),expected.get(i));
    }
    
    /* test a search by multiple authors */
    public void testSearchByMultipleAuthors() {
        Book book1 = new Book(18,"", "", "", "", "","","","", "");
        Book book2 = new Book(19,"", "", "", "", "","","","", "");
        ArrayList<Book> expected = new ArrayList<Book>();
        expected.add(book1); expected.add(book2);
        
        authorListView.getSelectionModel().select(0);
        authorListView.getSelectionModel().select(1);
        searchGroup.selectToggle(orRadioButton);
        
        setUpSearchAndResultControllers();
        
        getSearchResultsFlag=0;
        try {
            searchBook.searchButtonPressed(null);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
 
        while(getSearchResultsFlag==0)
            System.out.print(""); 
        getSearchResultsFlag=0;
        ArrayList<Book> result = getSearchResults();
        if(result.size()==expected.size())  
            for(int i=0;i<result.size();i++)
                assertEquals(result.get(i),expected.get(i));
    }
    
    /* test a search by a single domain */
    public void testSearchBySingleDomain() {
        Book book1 = new Book(18,"", "", "", "", "","","","", "");
        Book book2 = new Book(19,"", "", "", "", "","","","", "");
        ArrayList<Book> expected = new ArrayList<Book>();
        expected.add(book1); expected.add(book2);
        
        authorListView.getSelectionModel().select(0);
        authorListView.getSelectionModel().select(1);
        searchGroup.selectToggle(orRadioButton);
        
        setUpSearchAndResultControllers();
        
        getSearchResultsFlag=0;
        try {
            searchBook.searchButtonPressed(null);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
 
        while(getSearchResultsFlag==0)
            System.out.print(""); 
        getSearchResultsFlag=0;
        ArrayList<Book> result = getSearchResults();
        if(result.size()==expected.size())  
            for(int i=0;i<result.size();i++)
                assertEquals(result.get(i),expected.get(i));
    }
    
    /* test a search by multiple domains */
    public void testSearchByMultipleDomain() {
        Book book1 = new Book(18,"", "", "", "", "","","","", "");
        Book book2 = new Book(22,"", "", "", "", "","","","", "");
        Book book3 = new Book(19,"", "", "", "", "","","","", "");
        ArrayList<Book> expected = new ArrayList<Book>();
        expected.add(book2); expected.add(book1);  expected.add(book3);
        
        domainListView.getSelectionModel().select(0); //magic
        domainListView.getSelectionModel().select(1); //mystery
        searchGroup.selectToggle(orRadioButton);
        
        setUpSearchAndResultControllers();
        getSearchResultsFlag=0;
        try {
            searchBook.searchButtonPressed(null);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
 
        while(getSearchResultsFlag==0)
            System.out.print(""); 
        getSearchResultsFlag=0;
        ArrayList<Book> result = getSearchResults();
        if(result.size()==expected.size())  
            for(int i=0;i<result.size();i++)
                assertEquals(result.get(i),expected.get(i));
    }
    
    /* test empty fields case */
    public void testEmptyFields() {
        emptyFieldsFlag=false;
        boolean expected=true;
        setUpSearchAndResultControllers();
        try {
            searchBook.searchButtonPressed(null);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        while(getEmptyFieldAnswerFlag==0)
            System.out.print("");
            
        getEmptyFieldAnswerFlag=0;
        boolean result=emptyFieldsFlag;
        assertEquals(expected, result);
    }
    
    /* test special character '^' case */
    public void testSpecialCharacter() {
        SpecialCharacterFlag=false;
        boolean expected=true;
        
        titleTextField.setText("beit^ar");
        setUpSearchAndResultControllers();
        try {
            searchBook.searchButtonPressed(null);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        while(getSpecialCharacterAnswerFlag==0)
            System.out.print("");
            
        getEmptyFieldAnswerFlag=0;
        boolean result=SpecialCharacterFlag;
        assertEquals(expected, result);
    }
    
    
    /* no result case */
    public void testNoResult() {
        ArrayList<Book> expected = new ArrayList<Book>();
        
        titleTextField.setText("beitar");
        setUpSearchAndResultControllers();
        
        getSearchResultsFlag=0;
        try {
            searchBook.searchButtonPressed(null);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
 
        while(getSearchResultsFlag==0)
            System.out.print(""); 
        getSearchResultsFlag=0;
        ArrayList<Book> result = getSearchResults();
        if(result.size()==expected.size())  
            for(int i=0;i<result.size();i++)
                assertEquals(result.get(i),expected.get(i));
    }

    private void setUpSearchAndResultControllers() {
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

    }
    
    private ArrayList<Book> getSearchResults()
    {
        dataFlag=0;
        if(searchBookResult.data.size()!=0)
            searchBookResult.data.clear();
        searchBookResult.initialize();
        while(dataFlag==0)
            System.out.print("");
 
        dataFlag=0;
        ObservableList<SearchBookResult> res = searchBookResult.data;
        ArrayList<Book> result = new ArrayList<Book>();
        for(int i=0;i<res.size();i++)
            result.add(new Book(Integer.parseInt(res.get(i).getBookSn()),res.get(i).getBookTitle(),res.get(i).getBookLanguage(),res.get(i).getBookSummary(), res.get(i).getBookToc(), res.get(i).getBookKeywords(), res.get(i).getBookPrice(), res.get(i).getBookAuthors(), res.get(i).getBookDomains(), res.get(i).getBookSubjects()));
 
        return result;
    }
}




