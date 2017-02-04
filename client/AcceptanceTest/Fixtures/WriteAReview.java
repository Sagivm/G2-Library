package Fixtures;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import control.WriteReviewController;
import boundry.ClientUI;
import control.BookPageController;
import control.ClientConnectionController;
import control.ClientController;
import control.HomepageUserController;
import control.SearchBookController;
import control.SearchBookResultsController;
import entity.Login;
import entity.Message;
import entity.SearchBookResult;
import entity.User;
import enums.ActionType;
import fit.ActionFixture;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;

public class WriteAReview extends ActionFixture{
	
	public static SearchBookController searchBook;
	public static SearchBookResultsController searchBookResult;
	public static ClientController clientCon;
	public static ClientConnectionController clientCC;
	public static ClientUI clientMain;
	public static BookPageController bookPage;
	
	public static boolean getResultFormServer = false;
	
	public static boolean getResult = false;
	
	public TextArea txtAreaReview;
	
	public Tab writeReviewTab;
	
	public String textToCompare;
	
	WriteReviewController wr;
	User user;
	SearchBookResult book;
	
	public void setUP() throws Exception 
	{
		clientMain = new ClientUI();
		clientMain.setTypeOfUser("User");
		clientMain.testMode = true;
		Login login = new Login("302659743","12345");
		
		clientCC = new ClientConnectionController(ClientController.IP_ADDRESS, ClientController.DEFAULT_PORT);
		clientCon = new ClientController();
		Message message = clientCon.prepareLogin(ActionType.LOGIN,login);

		clientCC.sendToServer(message);
		
		user = new User("Or","Koren","302659743","12345","PerBook","Standard");
		HomepageUserController userMain = new HomepageUserController();
		userMain.setConnectedUser(user);
		book = new SearchBookResult("25", "JAVA on a cup of coffee", "English", "Summary", "TOC", "key1,key2", "Meir Sela", "Programming", "Computers", "47");
		wr.book = book;
		
		new JFXPanel();	//BookPageController
		bookPage = new BookPageController();
		writeReviewTab = new Tab();
		bookPage.searchedBookPage = book;
		
		new JFXPanel();	//WriteReviewController
		wr = new WriteReviewController();
		txtAreaReview = new TextArea();
		
	}
	
	public boolean checkWriteTab()
	{
		if(bookPage.writeReviewTab.isDisabled() == true)
			return true;
		return false;
	}
	
	public void pressClearButton() throws IOException
	{
		wr.ClearkButtonPressed(null);
	}
	
	public void setTextToCompare(String text)
	{
		textToCompare = text;
	}
	
	public boolean compareText()
	{
		if(wr.txtAreaReview.getText().equals(textToCompare))
			return true;
		return false;
	}
	
	public void setReviewText(String str)
	{
		txtAreaReview.setText(str);
		wr.txtAreaReview = txtAreaReview;
	}
	
	public void sendReview() throws IOException
	{
		wr.submitButtonPressed(null);
	}
	
	public boolean testSendEmptyReview() throws InterruptedException
	{
		while(getResult == false)
		{
			System.out.print("");
		}
		return wr.success;
	}
	
	public boolean testSendReview() throws InterruptedException
	{
		while(getResultFormServer == false)
		{
			System.out.print("");
		}
		return wr.success;
	}

}
