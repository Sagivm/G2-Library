package Fixtures;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import boundry.ClientUI;
import control.ClientConnectionController;
import control.ClientController;
import control.HomepageLibrarianController;
import control.BookManagementController;
import entity.Login;
import entity.Message;
import entity.Worker;
import entity.Validate;
import enums.ActionType;
import fit.ActionFixture;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.Button;
import javafx.scene.control.Labeled;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class AddBook extends ActionFixture{
	
	public static ClientController clientCon;
	public static ClientConnectionController clientCC;
	public static ClientUI clientMain;
	public static BookManagementController bookManagement;
	public static Worker worker;
	
	public static boolean getResultFormServer = false;
	public static boolean testMode = false;
	
	public TextField addBookTitle;
	public TextArea addBookKeywordsText;
	public TextArea addBookTableOfContent;
	public TextArea addBookSummary;
	public TextField priceTextField;
	public ListView addBookAuthorsList;
	public ListView addBookLanguageList;
	public ListView addBookSubjectsList;
	public Button submitAddBook;
	
	public Tab BooksTab;
	
	
	public void setUP() throws Exception 
	{
		clientMain = new ClientUI();
		clientMain.setTypeOfUser("Librarian");
		clientMain.testMode = true;
		
		testMode=true;
		
		Login login = new Login("123123123","123123");
		
		clientCC = new ClientConnectionController(ClientController.IP_ADDRESS, ClientController.DEFAULT_PORT);
		clientCon = new ClientController();
		Message message = clientCon.prepareLogin(ActionType.LOGIN,login);

		clientCC.sendToServer(message);
		

		worker = new Worker("Avi","Sofer","123123123","123123","Avisofer@walla.com","Librarian","1");
		HomepageLibrarianController librerianMain = new HomepageLibrarianController();

		librerianMain.setConnectedlibrarian(worker);


		
		new JFXPanel();	//BookPageController
		bookManagement = new BookManagementController();
		
		
		addBookTitle = new TextField();
		addBookKeywordsText = new TextArea();
		addBookTableOfContent = new TextArea();
		addBookSummary = new TextArea();
		priceTextField = new TextField();
		addBookAuthorsList = new ListView<>();
		addBookLanguageList = new ListView<>();
		addBookSubjectsList = new ListView<>();
		submitAddBook = new Button();

		
		
	}
	
	public void setBookTitle(String str)
	{
		addBookTitle.setText(str);
		bookManagement.addBookTitle = addBookTitle;
	}
	
	public void setBookKeywords(String str)
	{
		addBookKeywordsText.setText(str);
		bookManagement.addBookKeywordsText = addBookKeywordsText;
	}
	
	public void setBookTableOfContent(String str)
	{
		addBookTableOfContent.setText(str);
		bookManagement.addBookTableOfContent = addBookTableOfContent;
	}
	
	public void setBookSummary(String str)
	{
		addBookSummary.setText(str);
		bookManagement.addBookSummary = addBookSummary;
	}
	
	public void setBookPrice(String str)
	{
		priceTextField.setText(str);
		bookManagement.priceTextField = priceTextField;
	}
	
	public void setBookAuthor()
	{
		ObservableList < String > Authors = FXCollections.observableArrayList();
		Authors.add("1");
		addBookAuthorsList.setItems(Authors);
		//addBookAuthorsList.getSelectionModel().select(0);
		bookManagement.addBookAuthorsList = addBookAuthorsList;
	}
	
	public void setBookLanguage()
	{
		ObservableList < String > Languages = FXCollections.observableArrayList();
		Languages.add("English");
		addBookLanguageList.setItems(Languages);
		addBookLanguageList.getSelectionModel().select(0);
		bookManagement.addBookLanguageList = addBookLanguageList;
	}
	
	public void setBookSubject()
	{
		ObservableList < String > SubjectsList = FXCollections.observableArrayList();
		SubjectsList.add("1");
		addBookSubjectsList.setItems(SubjectsList);
		//addBookSubjectsList.getSelectionModel().select(0);
		bookManagement.addBookSubjectsList = addBookSubjectsList;
	}
	
	
	public boolean sendBook() throws IOException
	{
		try{
		bookManagement.submitButtonPressed(null);
		return true;
		}
		catch(Exception e){
			return false;
		}
	}
	

}
