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
	
	public TextArea txtAreaReview;
	
	public Button btnSubmit;
	
	public Tab writeReviewTab;
	
	WriteReviewController wr;
	User user;
	SearchBookResult book;
	
	public void setUP() throws Exception 
	{
		Login login = new Login("302659743","12345");
		
		clientCC = new ClientConnectionController(ClientController.IP_ADDRESS, ClientController.DEFAULT_PORT);
		clientCon = new ClientController();
		Message message = clientCon.prepareLogin(ActionType.LOGIN,login);

		clientCC.sendToServer(message);
		
		user = new User("Or","Koren","302659743","12345","PerBook","Standard");
		HomepageUserController userMain = new HomepageUserController();
		userMain.setConnectedUser(user);
		book = new SearchBookResult("23", "Steve Jobs", "English", "Summary", "TOC", "key1,key2", "Walter Isaacson", "Biography", "Computers", "34.9");
		wr.book = book;
		clientMain = new ClientUI();
		clientMain.setTypeOfUser("User");
		
		new JFXPanel();	//BookPageController
		bookPage = new BookPageController();
		writeReviewTab = new Tab();
		//bookPage.writeReviewTab = writeReviewTab;
		bookPage.searchedBookPage = book;
		
		
		new JFXPanel();	//WriteReviewController
		wr = new WriteReviewController();
		txtAreaReview = new TextArea();
		btnSubmit = new Button();
		
	}
	
	public boolean checkWriteTab()
	{
		writeReviewTab = bookPage.writeReviewTab;
		if(writeReviewTab.isDisabled() == true)
			return true;
		return false;
	}
	
	public void setReviewText(String str)
	{
		txtAreaReview.setText(str);
		wr.txtAreaReview = txtAreaReview;
		//txtAreaReview.setText(str);
	}
	
	public void sendReview() throws IOException
	{
		//wr.submitButtonPressed(event);
		wr.submitButtonPressed(null);
		//wr.btnSubmit.fire();
		//btnSubmit.fire();
	}
	
	public boolean testReview() throws InterruptedException
	{
		//System.out.println(writeReviewTab.isDisabled());
		//if(bookPage.writeReviewTab.isDisabled() == true)
			//return false;
		
		//TimeUnit.SECONDS.sleep(1);
		while(getResultFormServer == false)
		{
			System.out.print("");
		}
		return wr.success;
	}

}
