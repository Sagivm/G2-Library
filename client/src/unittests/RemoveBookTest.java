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
import control.BookManagementController;
import control.BookManagementController.PropertyAuthor;
import control.BookManagementController.PropertyBook;
import control.BookManagementController.PropertyDomain;
import control.BookManagementController.PropertySubject;
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
import javafx.scene.control.Label;
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
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import junit.framework.TestCase;



public class RemoveBookTest extends TestCase {
	
	public static BookManagementController bookManagementController;
	public static ClientController clientCon;
	public static ClientConnectionController clientCC;
	public static ClientUI LibrarianMain;
	 /**
	 * Container of the global pane
	 */
	@FXML
	 private AnchorPane globalPane;

	 // Books Tab - main pane
		
	 /**
	 * TableView that show the books.
	 */
	@FXML
	 private TableView < PropertyBook > BooksTableView;

	 /**
	 * TableColumn of book sn
	 */
	@FXML
	 private TableColumn BookSn;

	 /**
	 * TableColumn of book title
	 */
	@FXML
	 private TableColumn BookTitle;

	/**
	* TableColumn of book authors
	*/
	 @FXML
	 private TableColumn BookAuthors;

	 /**
	 * TableColumn of book keywords
	 */
	 @FXML
	 private TableColumn BookKeywords;

	 /**
	 * TableColumn of book state of hide
	 */
	 @FXML
	 private TableColumn BookHide;
	 
	 /**
	  * Button to delete book
	  */
	 @FXML
	 private Button delBtn;

	 /**
	  * Button to edit book
	  */
	 @FXML
	 private TextField filterField;
	 //busy wait flags
	 public static boolean bookRemovedFlag=false;
	 public static boolean connectedFlag=false;
	 
	 
	 protected void setUp() throws Exception{
		 super.setUp();
		 LibrarianMain.testMode=true;
		 Login login = new Login("123123123","123123");
			clientCC = new ClientConnectionController(ClientController.IP_ADDRESS, ClientController.DEFAULT_PORT);
			clientCon = new ClientController();
			Message message = clientCon.prepareLogin(ActionType.LOGIN,login);
			clientCC.sendToServer(message);
			while(connectedFlag==false)
				System.out.print("");
			new JFXPanel();
			RemoveBookTest.bookManagementController =new BookManagementController();
			this.BooksTableView=new TableView<>();
			this.BookSn= new TableColumn();
			this.BookTitle= new TableColumn();
			this.BookAuthors= new TableColumn();
			this.BookKeywords= new TableColumn();
			this.BookHide= new TableColumn();
			this.delBtn= new Button();
			this.filterField = new TextField();
			
	 }
	 public void MergeTestSubjects()
	 {
		 bookManagementController.BooksTableView=this.BooksTableView;
		 bookManagementController.BookSn=this.BookSn;
		 bookManagementController.BookTitle=this.BookTitle;
		 bookManagementController.BookAuthors=this.BookAuthors;
		 bookManagementController.BookKeywords=this.BookKeywords;
		 bookManagementController.BookHide=this.BookHide;
		 bookManagementController.delBtn=this.delBtn;
		 bookManagementController.filterField=this.filterField;
	 }
	 public void testDelete()
	 {
		 try {
			//setUp();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 System.out.println("sa");
		 BookManagementController.PropertyBook selectedItem = new BookManagementController.PropertyBook("19","Out of Order","","","","","","","");
		 MergeTestSubjects();
		 bookManagementController.initialize();
		 bookManagementController.BooksTableView.getSelectionModel().select(selectedItem);
		 bookManagementController.PressedDelete(null);
		 while(!bookRemovedFlag)
		 {
			 System.out.print("");
		 }
		 assertEquals(bookRemovedFlag,true);
		 
	 }
}
