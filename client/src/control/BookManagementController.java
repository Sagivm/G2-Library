package control;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;

import control.PendingRegistrationController.pendingUser;
import entity.Author;
import entity.GeneralMessages;
import entity.Message;
import entity.Register;
import entity.Validate;
import enums.ActionType;
import img.*;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.util.Callback;

/**
 * BookManagementController is the controller that manage all books, domains, subject and authors.
 * @author idanN
 */
/**
 * @author Idan
 *
 */
public class BookManagementController {

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
 * Label of book title of the selected book in the table view
 */
 @FXML
 private Label InfoTitle;

 /**
  * Label of book authors of the selected book in the table view
  */
 @FXML
 private Label InfoAuthors;

 /**
  * Label of book keywords of the selected book in the table view
  */
 @FXML
 private Label InfoKeywords;

 /**
  * Text Area of book summary of the selected book in the table view
  */
 @FXML
 private TextArea BookSummary;

 /**
  * Label of "title:" in the bottom of books table view
  */
 @FXML
 private Label TitleLabel;

 /**
  * Label of "authors:" in the bottom of books table view
  */
 @FXML
 private Label AuthorsLabel;

 /**
  * Label of "keywords:" in the bottom of books table view
  */
 @FXML
 private Label KeywordsLabel;

 /**
  * Label of "summary:" in the bottom of books table view
  */
 @FXML
 private Label SummaryLabel;
 
 /**
  * Label of book price of the selected book in the table view
  */
 @FXML
 private Label infoPrice;
 
 /**
  * Label of "price:" in the bottom of books table view
  */
 @FXML
 private Label PriceLabel;

 /**
  * Button to delete book
  */
 @FXML
 private Button delBtn;

 /**
  * Button to edit book
  */
 @FXML
 private Button editBtn;

 /**
  * Button to hide/unhide book
  */
 @FXML
 private Button hideBtn;
 
 /**
  * Button to add book
  */
 @FXML
 private Button addBookBtn;

 /**
  * Image of the book
  */
 @FXML
 private ImageView imageView;

 /**
  * Text field of the search book line
  */
 @FXML
 private TextField filterField;

 /**
  * Container of the main books in books tab
  */
 @FXML
 private AnchorPane mainPane;

 /**
  * Container of the add books in books tab
  */
 @FXML
 private AnchorPane addBookPane;
 
 /**
  * Container of the edit books in books tab
  */
 @FXML
 private AnchorPane editBookPane;
 
 // end mainpane

 // books tab - add book pane
 
 /**
  * Button of choose image to upload
  */
 @FXML
 private Button choosePicBtn;

 /**
  * Image of the uploaded image
  */
 @FXML
 private ImageView picBook;

 /**
  * Submit button to send book to server
  */
 @FXML
 private Button submitAddBook;

 /**
  * Back button to main books container
  */
 @FXML
 private Button backAddBook;

 /**
  * Button to clear add book form
  */
 @FXML
 private Button clearAddBook;

 /**
  * Text field of the added book title
  */
 @FXML
 private TextField addBookTitle;

 /**
  * List view of the added book authors
  */
 @FXML
 private ListView < String > addBookAuthorsList;

 /**
  * Text area of the added book keywords
  */
 @FXML
 private TextArea addBookKeywordsText;

 /**
  * List view of the added book languages
  */
 @FXML
 private ListView < String > addBookLanguageList;

 /**
  * List view of the added book subjects
  */
 @FXML
 private ListView < String > addBookSubjectsList;

 /**
  * Text area of the added book table of content
  */
 @FXML
 private TextArea addBookTableOfContent;

 /**
  * Text area of the added book summary
  */
 @FXML
 private TextArea addBookSummary;
 
 /**
  * Text field of the added book price
  */
 @FXML
 private TextField priceTextField;
 
 
//books tab - editbookpane
 
 /**
  * Button of choose image to upload
  */
@FXML
private Button editBookChoosePicBtn;

/**
 * Image of the uploaded image
 */
@FXML
private ImageView editBookpicBook;

/**
 * Submit button to send edited book to server
 */
@FXML
private Button submitEditBook;

/**
 * Back button to main books container
 */
@FXML
private Button backEditBook;

/**
 * Text field of the edited book title
 */
@FXML
private TextField editBookTitle;

/**
 * List view of the current autorhs of the book
 */
@FXML
private ListView < String > editBookAuthorsList;

/**
 * List view of the new autorhs of the book
 */
@FXML
private ListView < String > editBookAuthorsListSelected;

/**
 * Button to move authors from all avalibale authors list to book authors list
 */
@FXML
private Button editBookAuthorsLeft;

/**
 * Button to move authors from book authors list to all avalibale authors list
 */
@FXML
private Button editBookAuthorsRight;

/**
 * List view of the current subjects of the book
 */
@FXML
private ListView < String > editBookSubjectsList;

/**
 * List view of the new subjects of the book
 */
@FXML
private ListView < String > editBookSubjectsListSelected;

/**
 * Button to move authors from all avalibale subjects list to book subjects list
 */
@FXML
private Button editBookSubjectsLeft;

/**
 * Button to move authors from book subjects list to all avalibale subjects list
 */
@FXML
private Button editBookSubjectsRight;

/**
 * Text area of the edited book table of keywords
 */
@FXML
private TextArea editBookKeywordsText;

/**
 * List view of the edited book languages
 */
@FXML
private ListView < String > editBookLanguageList;

/**
 * List view of the edited book table of content
 */
@FXML
private TextArea editBookTableOfContent;

/**
 * List view of the edited book summary
 */
@FXML
private TextArea editBookSummary;

/**
 * List view of the edited book price
 */
@FXML
private TextField editBookPriceTextField;


// domains tab - mainDomainPane

/**
 * Container of the main domains in domains tab
 */
@FXML
private AnchorPane mainDomainPane;

/**
 * Container of the add domains in domains tab
 */
@FXML
private AnchorPane addDomainPane;

/**
 * Container of the edit domains in domains tab
 */
@FXML
private AnchorPane editDomainPane;

/**
 * Submit button to add domain
 */
@FXML
private Button addDomain;

/**
* TableView that show the domains.
*/
@FXML
private TableView<PropertyDomain> mainDomainTableView;

/**
* TableColumn of domain id
*/
@FXML
private TableColumn domainId;

/**
* TableColumn of domain name
*/
@FXML
private TableColumn domainName;

/**
* Text Field of added domain name
*/
@FXML
private TextField addDomainName;

/**
 * Button to add domain
 */
@FXML
private Button addDomainBack;

/**
 * Button to submit added domain to server
 */
@FXML
private Button addDomainSubmit;

/**
 * Button to clear domain add form
 */
@FXML
private Button addDomainClear;

/**
 * Button to edit domain from the table view of domains
 */
@FXML
private Button addDomainEditBtn;

/**
 * Button to delete domain from the table view of domains
 */
@FXML
private Button DomainDeleteBtn;

/**
 * Text Field of the edited domain name
 */
@FXML
private TextField editDomainName;

/**
 * Button to go back to main domains container from edit domain container
 */
@FXML
private Button editDomainBack;

/**
 * Button to submit the edited domain to server
 */
@FXML
private Button editDomainSubmit;

//subjects tab - mainDomainPane

/**
 * Container of the main supjects page in subjects tab
 */
@FXML
private AnchorPane mainSubjectPane;

/**
 * Container of the add supjects page in subjects tab
 */
@FXML
private AnchorPane addSubjectPane;

/**
 * Container of the edit supjects page in subjects tab
 */
@FXML
private AnchorPane editSubjectPane;

/**
 * Button to add book
 */
@FXML
private Button addSubject;

/**
 * Table view that contains the subjects in DB
 */
@FXML
private TableView<PropertySubject> mainSubjectTable;

/**
* Table Column of subject id
*/
@FXML
private TableColumn subjectId;

/**
* Table Column of subject name
*/
@FXML
private TableColumn subjectName;

/**
* Table Column of subject domain
*/
@FXML
private TableColumn subjectDomain;

/**
* Text Field of added subject name
*/
@FXML
private TextField addSubjectsName;

/**
* List View of domains list in add subject page
*/
@FXML
private ListView addSubjectsDomainsList;

/**
* Button to go back to main subjects page from add subject form
*/
@FXML
private Button addSubjetcsBack;

/**
* Button to submit added subject to server
*/
@FXML
private Button addSubjetcsSubmit;

/**
* Button to clear add subject form
*/
@FXML
private Button addSubjetcsClear;

/**
* Button to edit subject from the subjects table
*/
@FXML
private Button subjectsEditBtn;

/**
* Button to delete subject from DB
*/
@FXML
private Button subjectsDeleteBtn;

/**
* Text field of the edited subject name
*/
@FXML
private TextField editSubjectsName;

/**
* Button to go back to main subjects page from edit subject form
*/
@FXML
private Button editSubjetcsBack;

/**
* List of domains list in edit subject page
*/
@FXML
private ListView editSubjectsDomainsList;

/**
* Button to send to server the edited subject
*/
@FXML
private Button editSubjetcsSubmit;


//authors tab - mainDomainPane

/**
 * Container of the main authors in authors tab
 */
@FXML
private AnchorPane mainAuthorPane;

/**
 * Container of the add authors in authors tab
 */
@FXML
private AnchorPane addAuthorPane;

/**
 * Container of the edit authors in authors tab
 */
@FXML
private AnchorPane editAuthorPane;

/**
 * Submit button to add author
 */
@FXML
private Button addAuthor;

/**
* TableView that show the authors.
*/
@FXML
private TableView<PropertyAuthor> mainAuthorTableView;

/**
* Table Column of author id
*/
@FXML
private TableColumn authorId;

/**
* Table Column of author first name
*/
@FXML
private TableColumn authorFirstName;

/**
* Table Column of author last name 
*/
@FXML
private TableColumn authorLastName;

/**
* Text Field of added author first name
*/
@FXML
private TextField addAuthorFirstName;

/**
* Text Field of added author last name
*/
@FXML
private TextField addAuthorLastName;

/**
 * Button to go back to main authors page from add author page
 */
@FXML
private Button addAuthorBack;

/**
 * Button to submit added author to server
 */
@FXML
private Button addAuthorSubmit;

/**
 * Button to clear author add form
 */
@FXML
private Button addAuthorClear;

/**
 * Button to edit author from the table view of authors
 */
@FXML
private Button addAuthorEditBtn;

/**
 * Button to delete author from the table view of authors
 */
@FXML
private Button AuthorDeleteBtn;

/**
 * Text Field of the edited author first name
 */
@FXML
private TextField editAuthorFirstName;

/**
 * Text Field of the edited author last name
 */
@FXML
private TextField editAuthorLastName;

/**
 * Button to go back to main authors page from edit author container
 */
@FXML
private Button editAuthorBack;

/**
 * Button to submit the edited author to server
 */
@FXML
private Button editAuthorSubmit;

/**
 * static Array list of all the books from the DB.
 */
 public static ArrayList < String > BooksList; //itai - V
 
 /**
  * static Array list of all the subjects names from the DB.
  */
 public static ArrayList < String > subjectList; //itai - V
 
 /**
  * static Array list of all the authors Id from the DB.
  */
 public ArrayList < String > authorsId = null;
 
 /**
  * String to keep the ecoded choosen picture from computer
  */
 public String picStr="noPicture";
 
 /**
  * static Array list of all the authors from the DB.
  */
 public static ArrayList < Author > authorList; //itai - V
 
 /**
  * static Array list of all the selected authors in edit book page
  */
 public static ArrayList < Author > selectedAuthorsString; //itai - V
 
 /**
  * static Array list of the subjects of choose book to edit
  */
 public static ArrayList < String >subjectListOfBook; //itai - V
 
 /**
  * static Array list of all the selected subjects in edit book page
  */
 public static ArrayList < String > selectedSubjectString; //itai - V
 
 /**
  * static string that contains the language of edited book
  */
 public static String editBookLanguage;
 
 /**
  * static string that contains the table of content of edited book
  */
 public static String editBookTableOfContant; //itai - V
 
 /**
  * static Array list of all the domains from the DB.
  */
 public static ArrayList < String > domainsList; //itai - V
 
 /**
  * static Integer that contains the number of book of specific domain
  */
 public static int countBookByDomain; //itai - V
 
 /**
  * static Array list of all the subjects info from the DB.
  */
 public static ArrayList < String > subjectsList; //itai - V

 /**
  * static Integer that contains the number of book of specific subject
  */
 public static int countBookBySubject; //itai - V
 
 /**
  * static Integer that contains the number of book of specific author
  */
 public static int countBookOfUser; //itai - V

 
 /**
 * this attribute insert the data (books) to the table.
 */
 private ObservableList < PropertyBook > data = FXCollections.observableArrayList();
 
 /**
 * this attribute insert the data (filtered books by search) to the table.
 */
 private ObservableList < PropertyBook > filteredData = FXCollections.observableArrayList();

 /**
 * initialize all the listener and data from the DB in the form on load.
 */
 @FXML
 private void initialize() {
  addBookPane.setVisible(false);
  editBookPane.setVisible(false);
  BookSummary.setStyle("-fx-focus-color: -fx-control-inner-background ; -fx-faint-focus-color: -fx-control-inner-background ; -fx-background-insets: 0;-fx-background-color: transparent, white, transparent, white;");
  TitleLabel.setVisible(false);
  AuthorsLabel.setVisible(false);
  KeywordsLabel.setVisible(false);
  SummaryLabel.setVisible(false);
  PriceLabel.setVisible(false);
  BookSummary.setVisible(false);
  delBtn.setVisible(false);
  editBtn.setVisible(false);
  hideBtn.setVisible(false);
  
  
  Message message = prepareGetBooksList(ActionType.GET_BOOK_LIST);
  try {
   ClientController.clientConnectionController.sendToServer(message);
  } catch (IOException e) {
	  e.printStackTrace();
	  actionOnError(ActionType.TERMINATE, GeneralMessages.UNNKNOWN_ERROR_DURING_SEND);
  }
  
  //itai
  Platform.runLater(new Runnable() {
		@Override
		public void run() {
			BookManagermentGetBookListRecv recv_getBooks = new BookManagermentGetBookListRecv();
				recv_getBooks.start();
				synchronized (recv_getBooks) {
					try{
						recv_getBooks.wait();
					}catch(InterruptedException e){
						e.printStackTrace();
					}
				}
		}});
		
  
  
  

  Message message2 = prepareGetAuthors(ActionType.GET_AUTHORS);
  try {
   ClientController.clientConnectionController.sendToServer(message2);
  } catch (Exception e1) {
   e1.printStackTrace();
  }
  
  //itai
  Platform.runLater(new Runnable() {
		@Override
		public void run() {
			BookManagermentGetAuthorsRecv recv_getAuthors = new BookManagermentGetAuthorsRecv();
				recv_getAuthors.start();
				synchronized (recv_getAuthors) {
					try{
						recv_getAuthors.wait();
					}catch(InterruptedException e){
						e.printStackTrace();
					}
					
					Platform.runLater(() -> {
						  
						  
						   String authors = "";
						   for (int i = 0; i < BooksList.size(); i += 9) {
						    if (i + 9 < BooksList.size() && BooksList.get(i).equals(BooksList.get(i + 9))) {
						     authors = authors + BooksList.get(i + 5) + ",";
						    } else {
						     if (authors.equals("")) {
						      String hide;
						      if (BooksList.get(i + 3).equals("0")) hide = "no";
						      else hide = "yes";
						      data.add(new PropertyBook(BooksList.get(i), BooksList.get(i + 1), BooksList.get(i + 2), hide, BooksList.get(i + 4), BooksList.get(i + 5), BooksList.get(i + 6), BooksList.get(i + 7), BooksList.get(i + 8)));
						      filteredData.add(new PropertyBook(BooksList.get(i), BooksList.get(i + 1), BooksList.get(i + 2), hide, BooksList.get(i + 4), BooksList.get(i + 5), BooksList.get(i + 6), BooksList.get(i + 7), BooksList.get(i + 8)));
						     } else {
						      String hide;
						      if (BooksList.get(i + 3).equals("0")) hide = "no";
						      else hide = "yes";
						      data.add(new PropertyBook(BooksList.get(i), BooksList.get(i + 1), BooksList.get(i + 2), hide, BooksList.get(i + 4), authors + BooksList.get(i + 5), BooksList.get(i + 6), BooksList.get(i + 7), BooksList.get(i + 8)));
						      filteredData.add(new PropertyBook(BooksList.get(i), BooksList.get(i + 1), BooksList.get(i + 2), hide, BooksList.get(i + 4), authors + BooksList.get(i + 5), BooksList.get(i + 6), BooksList.get(i + 7), BooksList.get(i + 8)));
						     }
						     authors = "";
						    }
						   }
						  });
					
					  Platform.runLater(() -> {
						   ArrayList < String > names = new ArrayList < String > ();
						   addBookAuthorsList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
						   //System.out.println(statush.get(1).getFirstname());
						   for (int i = 0; i < authorList.size(); i++) {
						    names.add(i, "(" + authorList.get(i).getId() + ")" + "\t" + authorList.get(i).getFirstname() + " " + authorList.get(i).getLastname());
						   }
						   //System.out.println(names.get(0));
						   ObservableList < String > authors = FXCollections.observableArrayList(names);
						   addBookAuthorsList.setItems(authors);

						  });
				}
		}});
  
  
  
  




  Message message3 = prepareGetSubjects(ActionType.GET_SUBJECTS);
  try {
   ClientController.clientConnectionController.sendToServer(message3);
  } catch (Exception e1) {
   e1.printStackTrace();
  }
  
  //itai
  Platform.runLater(new Runnable() {
		@Override
		public void run() {
				BookManagermentGetSubjectsRecv recv_getSubjects = new BookManagermentGetSubjectsRecv();
				recv_getSubjects.start();
				synchronized (recv_getSubjects) {
					try{
						recv_getSubjects.wait();
					}catch(InterruptedException e){
						e.printStackTrace();
					}
					
					  Platform.runLater(() -> {
						   addBookSubjectsList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
						   ObservableList < String > subjects = FXCollections.observableArrayList(subjectList);
						   addBookSubjectsList.setItems(subjects);

						  });
					  
					  BooksTableView.setEditable(true);

					  BookSn.setCellValueFactory(
					   new PropertyValueFactory < pendingUser, String > ("bookSn"));

					  BookTitle.setCellValueFactory(
					   new PropertyValueFactory < pendingUser, String > ("bookTitle"));

					  BookKeywords.setCellValueFactory(
					   new PropertyValueFactory < pendingUser, String > ("bookKeywords"));

					  BookHide.setCellValueFactory(
					   new PropertyValueFactory < pendingUser, String > ("bookHide"));

					  BookAuthors.setCellValueFactory(
					   new PropertyValueFactory < pendingUser, String > ("authorName"));
				}
				
				  BookSn.setStyle("-fx-alignment: CENTER;");
				  BookTitle.setStyle("-fx-alignment: CENTER;");
				  BookKeywords.setStyle("-fx-alignment: CENTER;");
				  BookHide.setStyle("-fx-alignment: CENTER;");
				  BookAuthors.setStyle("-fx-alignment: CENTER;");
				  
				  BooksTableView.setItems(filteredData);

				  BooksTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
				   if (newSelection != null) {
				    TitleLabel.setVisible(true);
				    AuthorsLabel.setVisible(true);
				    KeywordsLabel.setVisible(true);
				    SummaryLabel.setVisible(true);
				    PriceLabel.setVisible(true);
				    BookSummary.setVisible(true);
				    imageView.setVisible(true);
				    infoPrice.setVisible(true);
				    InfoAuthors.setVisible(true);
				    InfoKeywords.setVisible(true);
				    InfoTitle.setVisible(true);
				    delBtn.setVisible(true);
				    editBtn.setVisible(true);
				    hideBtn.setVisible(true);

				    if (newSelection.getBookHide().equals("yes"))
				     hideBtn.setText("Unhide Book");
				    else
				     hideBtn.setText("Hide Book");
				    
				    InfoTitle.setText(newSelection.getBookTitle());
				    InfoAuthors.setText(newSelection.getAuthorName());
				    InfoKeywords.setMaxWidth(170);
				    InfoKeywords.setWrapText(true);
				    InfoKeywords.setText(newSelection.getBookKeywords());
				    BookSummary.setStyle("-fx-focus-color: -fx-control-inner-background ; -fx-faint-focus-color: -fx-control-inner-background ; -fx-background-insets: 0;-fx-background-color: transparent, white, transparent, white;");
				    BookSummary.setMaxWidth(280);
				    BookSummary.setWrapText(true);
				    BookSummary.setText(newSelection.getBookSummary());
				    BookSummary.setEditable(false);
				    infoPrice.setText(newSelection.getBookPrice());

				    //show picture
				    if(newSelection.getBookImage()!=null){
				    String base64EncodedImage = newSelection.getBookImage();
				    byte[] imageInBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64EncodedImage);
				    BufferedImage imgbuf;
				    try {
				     imgbuf = ImageIO.read(new ByteArrayInputStream(imageInBytes));
				     Image image = SwingFXUtils.toFXImage(imgbuf, null);
				     imageView.setImage(image);
				    } catch (Exception e1) {
				     // TODO Auto-generated catch block
				     e1.printStackTrace();
				    }
				  }
				    else imageView.setImage(null);
				    //end show picture

				   }
				  });
				  
				  
				  
				  
		}});
		
  
  editBtn.setOnAction(e -> {
	  mainPane.setVisible(false);
	  addBookPane.setVisible(false);
	  editBookPane.setVisible(true);
	  PropertyBook selectedItem = BooksTableView.getSelectionModel().getSelectedItem();
	  editBookTitle.setText(selectedItem.getBookTitle());
	  editBookKeywordsText.setText(selectedItem.getBookKeywords());
	  editBookPriceTextField.setText(selectedItem.getBookPrice());
	  editBookSummary.setText(selectedItem.getBookSummary());  
	  if(selectedItem.getBookImage()!=null){
		  String base64EncodedImage = selectedItem.getBookImage();
		    byte[] imageInBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64EncodedImage);
		    BufferedImage imgbuf;
		    try {
		     imgbuf = ImageIO.read(new ByteArrayInputStream(imageInBytes));
		     Image image = SwingFXUtils.toFXImage(imgbuf, null);
		   editBookpicBook.setImage(image);
		    } catch (Exception e1) {
		        // TODO Auto-generated catch block
		        e1.printStackTrace();
		       }
	  }


	  
	  Message message4 = prepareGetAuthors(ActionType.GET_BOOK_AUTHORS,selectedItem.getBookSn());
	  Message message5 = prepareGetAuthors(ActionType.GET_AUTHORS);
	  Message message6 = prepareGetSubjects(ActionType.GET_BOOK_SUBJETCS,selectedItem.getBookSn());
	  Message message7 = prepareGetSubjects(ActionType.GET_SUBJECTS);
	  Message message8 = prepareGetLanguage(ActionType.GET_BOOK_LANGUAGE,selectedItem.getBookSn());
	  Message message9 = prepareGetTableOfContent(ActionType.GET_BOOK_TABLE_OF_CONTENT,selectedItem.getBookSn());

	  try {
	    ClientController.clientConnectionController.sendToServer(message4);
	    ClientController.clientConnectionController.sendToServer(message5);
	    ClientController.clientConnectionController.sendToServer(message6);
	    ClientController.clientConnectionController.sendToServer(message7);
	    ClientController.clientConnectionController.sendToServer(message8);
	    ClientController.clientConnectionController.sendToServer(message9);
	   } catch (Exception e1) {
	    e1.printStackTrace();
	   }

	  //itai
	  Platform.runLater(new Runnable() {
			@Override
			public void run() {
					BookManagermentGetBookAuthorsRecv recv_getBookAuthors = new BookManagermentGetBookAuthorsRecv();
					recv_getBookAuthors.start();
					synchronized (recv_getBookAuthors) {
						try{
							recv_getBookAuthors.wait();
						}catch(InterruptedException e){
							e.printStackTrace();
						}
					}
			}});
	  
	  
	  //itai
	  Platform.runLater(new Runnable() {
			@Override
			public void run() {
				BookManagermentGetBookAuthorsRecv2 recv_getAuthors = new BookManagermentGetBookAuthorsRecv2();
					recv_getAuthors.start();
					synchronized (recv_getAuthors) {
						try{
							recv_getAuthors.wait();
						}catch(InterruptedException e){
							e.printStackTrace();
						}
					}
			}});
	  
	  
	  //itai
	  Platform.runLater(new Runnable() {
			@Override
			public void run() {
					BookManagermentGetBookSubjectsRecv recv_getBookSubjects = new BookManagermentGetBookSubjectsRecv();
					recv_getBookSubjects.start();
					synchronized (recv_getBookSubjects) {
						try{
							recv_getBookSubjects.wait();
						}catch(InterruptedException e){
							e.printStackTrace();
						}
					}
			}});
		
		  
		  //itai
		  Platform.runLater(new Runnable() {
				@Override
				public void run() {
						BookManagermentGetSubjectsRecv2 recv_getSubjects = new BookManagermentGetSubjectsRecv2();
						recv_getSubjects.start();
						synchronized (recv_getSubjects) {
							try{
								recv_getSubjects.wait();
							}catch(InterruptedException e){
								e.printStackTrace();
							}
						}
				}});
		  
		  
		//itai
		  Platform.runLater(new Runnable() {
				@Override
				public void run() {
						BookManagermentGetBookLanguageRecv recv_getBookLanguages = new BookManagermentGetBookLanguageRecv();
						recv_getBookLanguages.start();
						synchronized (recv_getBookLanguages) {
							try{
								recv_getBookLanguages.wait();
							}catch(InterruptedException e){
								e.printStackTrace();
							}
						}
				}});
		
		  //itai
		  Platform.runLater(new Runnable() {
				@Override
				public void run() {
						BookManagermentGetBookTocRecv recv_getBookTableOfContent = new BookManagermentGetBookTocRecv();
						recv_getBookTableOfContent.start();
						synchronized (recv_getBookTableOfContent) {
							try{
								recv_getBookTableOfContent.wait();
							}catch(InterruptedException e){
								e.printStackTrace();
							}
						}
				}});
		  

	    ArrayList < String > names = new ArrayList < String > ();
	    editBookAuthorsList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	    //System.out.println(statush.get(1).getFirstname());
	    for (int i = 0; i < authorList.size(); i++) {
	    		names.add(i, "(" + authorList.get(i).getId() + ")" + "\t" + authorList.get(i).getFirstname() + " " + authorList.get(i).getLastname());
	    }
	    ArrayList < String > namesselected = new ArrayList < String > ();
	    editBookAuthorsListSelected.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	    for (int i = 0; i < selectedAuthorsString.size(); i++) {
	    	namesselected.add(i, "(" + selectedAuthorsString.get(i).getId() + ")" + "\t" + selectedAuthorsString.get(i).getFirstname() + " " + selectedAuthorsString.get(i).getLastname());
	    	names.remove(namesselected.get(i));
	    }

	    
	    ArrayList < String > subjects = new ArrayList < String > ();
	    editBookSubjectsList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	    //System.out.println(statush.get(1).getFirstname());
	    for (int i = 0; i < subjectListOfBook.size(); i++) {
	    	subjects.add(i, subjectListOfBook.get(i));
	    }
	    ArrayList < String > subjectsselected = new ArrayList < String > ();
	    editBookSubjectsListSelected.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	    
	    
	    //System.out.println(statush.get(1).getFirstname());
	    for (int i = 0; i < selectedSubjectString.size(); i++) {
	    	subjectsselected.add(i, selectedSubjectString.get(i));
	    	subjects.remove(subjectsselected.get(i));
	    }
	    

	    //System.out.println(names.get(0));
	    ObservableList < String > authors = FXCollections.observableArrayList(names);
	    ObservableList < String > selectedAuthors = FXCollections.observableArrayList(namesselected);
	    editBookAuthorsList.setItems(authors);    
	    editBookAuthorsListSelected.setItems(selectedAuthors);
	    
		editBookAuthorsLeft.setOnAction((ActionEvent event) -> {
		        String potential = editBookAuthorsList.getSelectionModel().getSelectedItem();
		        if (potential != null) {
		        	editBookAuthorsList.getSelectionModel().clearSelection();
		        	authors.remove(potential);
		        	selectedAuthors.add(potential);
		        }
		      });
		
		editBookAuthorsRight.setOnAction((ActionEvent event) -> {
	        String potential = editBookAuthorsListSelected.getSelectionModel().getSelectedItem();
	        if (potential != null) {
	        	editBookAuthorsListSelected.getSelectionModel().clearSelection();
	        	selectedAuthors.remove(potential);
	        	authors.add(potential);
	        }
	      });
		
		
	    ObservableList < String > allSubjects = FXCollections.observableArrayList(subjects);
	    ObservableList < String > selectedSubjects = FXCollections.observableArrayList(subjectsselected);
	    editBookSubjectsList.setItems(allSubjects);    
	    editBookSubjectsListSelected.setItems(selectedSubjects);
	    
	    editBookSubjectsLeft.setOnAction((ActionEvent event) -> {
		        String potential = editBookSubjectsList.getSelectionModel().getSelectedItem();
		        if (potential != null) {
		        	editBookSubjectsList.getSelectionModel().clearSelection();
		        	allSubjects.remove(potential);
		        	selectedSubjects.add(potential);
		        }
		      });
		
	    editBookSubjectsRight.setOnAction((ActionEvent event) -> {
	        String potential = editBookSubjectsListSelected.getSelectionModel().getSelectedItem();
	        if (potential != null) {
	        	editBookSubjectsListSelected.getSelectionModel().clearSelection();
	        	selectedSubjects.remove(potential);
	        	allSubjects.add(potential);
	        }
	      });


	  
	   
	   ObservableList < String > languages = FXCollections.observableArrayList(
			     "English", "Hebrew", "Russian", "Arabic");
	   editBookLanguageList.setItems(languages);
	   editBookLanguageList.getSelectionModel().select(editBookLanguage);
	   
	   editBookTableOfContent.setText(editBookTableOfContant); 

	  }); //end edit button


  delBtn.setOnAction(e -> {
   PropertyBook selectedItem = BooksTableView.getSelectionModel().getSelectedItem();
   BooksTableView.getItems().remove(selectedItem);
   Message messageDel = prepareDeleteBook(ActionType.DELETE_BOOK ,selectedItem.getBookSn());
   try {
   	ClientController.clientConnectionController.sendToServer(messageDel);
   } catch (IOException e1) {	
   	actionOnError(ActionType.TERMINATE,GeneralMessages.UNNKNOWN_ERROR_DURING_SEND);
   }
   
   /////////////////////////////////////////////////////////////////
   
   ///////////////////////////////////////////////////////////////////
   
   Platform.runLater(() -> {
   			data.remove(selectedItem);
   });
   //delete book
  });

  hideBtn.setOnAction(e -> {
   PropertyBook selectedItem = BooksTableView.getSelectionModel().getSelectedItem();
   if (selectedItem.getBookHide().equals("yes")) {

    Message message4 = prepareHideBook(ActionType.HIDE_BOOK, selectedItem.getBookSn(), "0");
    try {
     ClientController.clientConnectionController.sendToServer(message4);
    } catch (IOException e1) {
     e1.printStackTrace();
     actionOnError(ActionType.TERMINATE, GeneralMessages.UNNKNOWN_ERROR_DURING_SEND);
    }
    
    /////////////////////////////////////////////////////////////////
    
    ///////////////////////////////////////////////////////////////////
    
    
    Platform.runLater(() -> {
     selectedItem.setBookHide("no");
     hideBtn.setText("Hide Book");
    });
   } else {
    Message message5 = prepareHideBook(ActionType.HIDE_BOOK, selectedItem.getBookSn(), "1");
    try {
     ClientController.clientConnectionController.sendToServer(message5);
    } catch (IOException e1) {
    	e1.printStackTrace();
     actionOnError(ActionType.TERMINATE, GeneralMessages.UNNKNOWN_ERROR_DURING_SEND);
    }
    
    /////////////////////////////////////////////////////////////////
    
    ///////////////////////////////////////////////////////////////////
    
    Platform.runLater(() -> {
     selectedItem.setBookHide("yes");
     hideBtn.setText("Unhide Book");
    });
   }
   BookHide.setVisible(false);
   BookHide.setVisible(true);
  });



  filterField.textProperty().addListener(new ChangeListener < String > () {
   @Override
   public void changed(ObservableValue < ? extends String > observable,
    String oldValue, String newValue) {

    updateFilteredData();
   }
  });




   addBookBtn.setOnAction(e -> {
   mainPane.setVisible(false);
   addBookPane.setVisible(true);
   editBookPane.setVisible(false);
    ObservableList < String > languages = FXCollections.observableArrayList(
     "English", "Hebrew", "Russian", "Arabic");
   addBookLanguageList.setItems(languages);

   Message message4 = prepareGetAuthors(ActionType.GET_AUTHORS);
   try {
    ClientController.clientConnectionController.sendToServer(message4);
   } catch (Exception e1) {
    e1.printStackTrace();
   }
   
   //itai
   Platform.runLater(new Runnable() {
 		@Override
 		public void run() {
 				BookManagermentGetAuthorsRecv2 recv_getAuthors = new BookManagermentGetAuthorsRecv2();
 				recv_getAuthors.start();
 				synchronized (recv_getAuthors) {
 					try{
 						recv_getAuthors.wait();
 					}catch(InterruptedException e){
 						e.printStackTrace();
 					}
 				}
 		}});
   
   Platform.runLater(() -> {
    ArrayList < String > names = new ArrayList < String > ();
    addBookAuthorsList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    //System.out.println(statush.get(1).getFirstname());
    for (int i = 0; i < authorList.size(); i++) {
     names.add(i, "(" + authorList.get(i).getId() + ")" + "\t" + authorList.get(i).getFirstname() + " " + authorList.get(i).getLastname());
    }
    //System.out.println(names.get(0));
    ObservableList < String > authors = FXCollections.observableArrayList(names);
    addBookAuthorsList.setItems(authors);

   });



   Message message5 = prepareGetSubjects(ActionType.GET_SUBJECTS);
   try {
    ClientController.clientConnectionController.sendToServer(message5);
   } catch (Exception e1) {
    e1.printStackTrace();
   }
   
   //itai
   Platform.runLater(new Runnable() {
 		@Override
 		public void run() {
 				BookManagermentGetSubjectsRecv3 recv_getSubjects = new BookManagermentGetSubjectsRecv3();
 				recv_getSubjects.start();
 				synchronized (recv_getSubjects) {
 					try{
 						recv_getSubjects.wait();
 					}catch(InterruptedException e){
 						e.printStackTrace();
 					}
 				}
 		}});
   
   Platform.runLater(() -> {
    addBookSubjectsList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    ObservableList < String > subjects = FXCollections.observableArrayList(subjectList);
    addBookSubjectsList.setItems(subjects);

   });

  }); //endbtn







  //---------------------------------book add pane---------------------

  final FileChooser fileChooser = new FileChooser();

  choosePicBtn.setOnAction(e -> {
   configureFileChooser(fileChooser);
   File file = fileChooser.showOpenDialog(ScreenController.getStage());
   if (file != null) {
    //System.out.println(file.getAbsolutePath());
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    BufferedImage image;
    byte[] imageInBytes;
    try {
     image = ImageIO.read(file);
     //System.out.println(file.getAbsolutePath().substring(file.getAbsolutePath().indexOf(".")+1));
     ImageIO.write(image, "png", output);
     String base64EncodedImage = DatatypeConverter.printBase64Binary(output.toByteArray());
     imageInBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64EncodedImage);
     picStr = base64EncodedImage;
     //System.out.println(base64EncodedImage);
     BufferedImage imgbuf;
     imgbuf = ImageIO.read(new ByteArrayInputStream(imageInBytes));
     Image image1 = SwingFXUtils.toFXImage(imgbuf, null);
     picBook.setImage(image1);
    } catch (Exception e2) {
     actionOnError(ActionType.CONTINUE, "Your Picture format not suppoerted!");
     imageInBytes = null;
    }

   }
  });


  backAddBook.setOnAction(e -> {
   addBookTitle.setText("");
   addBookAuthorsList.getSelectionModel().clearSelection();
   addBookKeywordsText.setText("");
   addBookLanguageList.getSelectionModel().clearSelection();
   addBookSubjectsList.getSelectionModel().clearSelection();
   addBookTableOfContent.setText("");
   addBookSummary.setText("");
   priceTextField.setText("");
   picStr="noPicture";
   picBook.setImage(null);
   addBookPane.setVisible(false);
   editBookPane.setVisible(false);
   mainPane.setVisible(true);
  });

  clearAddBook.setOnAction(e -> {
   addBookTitle.setText("");
   addBookAuthorsList.getSelectionModel().clearSelection();
   addBookKeywordsText.setText("");
   addBookLanguageList.getSelectionModel().clearSelection();
   addBookSubjectsList.getSelectionModel().clearSelection();
   addBookTableOfContent.setText("");
   addBookSummary.setText("");
   priceTextField.setText("");
   picBook.setImage(null);
   picStr="noPicture";
  });

  submitAddBook.setOnAction(e -> {
   String TitleBook = null;
   TitleBook = addBookTitle.getText();

   ObservableList < String > Authors = addBookAuthorsList.getSelectionModel().getSelectedItems();
   String authorsId = "";
   for (int i = 0; i < Authors.size(); i++)
    authorsId += Authors.get(i).substring(Authors.get(i).indexOf("(") + 1, Authors.get(i).indexOf(")")) + "^";

   String keywords = null;
   keywords = addBookKeywordsText.getText();

   String language = null;
   language = addBookLanguageList.getSelectionModel().getSelectedItem();

   ObservableList < String > Subjects = addBookSubjectsList.getSelectionModel().getSelectedItems();
   String SubjectsList = "";
   for (int i = 0; i < Subjects.size(); i++)
    SubjectsList += Subjects.get(i).substring(Subjects.get(i).indexOf("(") + 1, Subjects.get(i).indexOf(")")) + "^";

   String tableOfContent = null;
   tableOfContent = addBookTableOfContent.getText();

   String summary = null;
   summary = addBookSummary.getText();
   
   String price = null;
   price = priceTextField.getText();

   String picture=picStr;
   
   if(price.matches("[-+]?[0-9]*\\.?[0-9]+")==false)
	   actionOnError(ActionType.CONTINUE, "The price must be number!");

   else if (language == null || authorsId.length() == 0 || SubjectsList.length() == 0 || TitleBook == null || keywords == null || tableOfContent == null || summary == null)
    actionOnError(ActionType.CONTINUE, "You must to fill the all fields!");

   else {
    Message message6 = prepareAddBook(ActionType.ADD_BOOK, Validate.fixText(TitleBook), authorsId, keywords, language, SubjectsList, tableOfContent, summary, picture, price);
    try {
     ClientController.clientConnectionController.sendToServer(message6);
    } catch (Exception e1) {
     e1.printStackTrace();
    }
    ////////////////////////////////////////////////////////////////
    
    ////////////////////////////////////////////////////////////////	
    
    Platform.runLater(() -> {
    actionOnError(ActionType.CONTINUE, "The book added successfully!");
    addBookTitle.setText("");
    addBookAuthorsList.getSelectionModel().clearSelection();
    addBookKeywordsText.setText("");
    addBookLanguageList.getSelectionModel().clearSelection();
    addBookSubjectsList.getSelectionModel().clearSelection();
    addBookTableOfContent.setText("");
    addBookSummary.setText("");
    picBook.setImage(null);
    picStr="noPicture";
    
    
    filterField.clear();
    filteredData.clear();
    Message message7 = prepareGetBooksList(ActionType.GET_BOOK_LIST);
    try {
     ClientController.clientConnectionController.sendToServer(message7);
    } catch (IOException e2) {
    	e2.printStackTrace();
     actionOnError(ActionType.TERMINATE, GeneralMessages.UNNKNOWN_ERROR_DURING_SEND);
    }
    
    //itai
    Platform.runLater(new Runnable() {
		@Override
		public void run() {
				BookManagermentGetBookListRecv2 recv_getBooks = new BookManagermentGetBookListRecv2();
				recv_getBooks.start();
				synchronized (recv_getBooks) {
					try{
						recv_getBooks.wait();
					}catch(InterruptedException e){
						e.printStackTrace();
					}
					
				    Platform.runLater(() -> {
				        String authors = "";
				        for (int i = 0; i < BooksList.size(); i += 9) {
				         if (i + 9 < BooksList.size() && BooksList.get(i).equals(BooksList.get(i + 9))) {
				          authors = authors + BooksList.get(i + 5) + ",";
				         } else {
				          if (authors.equals("")) {
				           String hide;
				           if (BooksList.get(i + 3).equals("0")) hide = "no";
				           else hide = "yes";
				           data.add(new PropertyBook(BooksList.get(i), BooksList.get(i + 1), BooksList.get(i + 2), hide, BooksList.get(i + 4), BooksList.get(i + 5), BooksList.get(i + 6), BooksList.get(i + 7), BooksList.get(i + 8)));
				           filteredData.add(new PropertyBook(BooksList.get(i), BooksList.get(i + 1), BooksList.get(i + 2), hide, BooksList.get(i + 4), BooksList.get(i + 5), BooksList.get(i + 6), BooksList.get(i + 7), BooksList.get(i + 8)));
				          } else {
				           String hide;
				           if (BooksList.get(i + 3).equals("0")) hide = "no";
				           else hide = "yes";
				           data.add(new PropertyBook(BooksList.get(i), BooksList.get(i + 1), BooksList.get(i + 2), hide, BooksList.get(i + 4), authors + BooksList.get(i + 5), BooksList.get(i + 6), BooksList.get(i + 7), BooksList.get(i + 8)));
				           filteredData.add(new PropertyBook(BooksList.get(i), BooksList.get(i + 1), BooksList.get(i + 2), hide, BooksList.get(i + 4), authors + BooksList.get(i + 5), BooksList.get(i + 6), BooksList.get(i + 7), BooksList.get(i + 8)));
				          }
				          authors = "";
				         }
				        }
				       });
				}
		}});
    


    
    addBookPane.setVisible(false);
    editBookPane.setVisible(false);
    mainPane.setVisible(true);
    });
   }
  }); //end submit button

 //---------------------------------book edit pane---------------------
  
  
  editBookChoosePicBtn.setOnAction(e -> {
	   configureFileChooser(fileChooser);
	   File file = fileChooser.showOpenDialog(ScreenController.getStage());
	   if (file != null) {
	    //System.out.println(file.getAbsolutePath());
	    ByteArrayOutputStream output = new ByteArrayOutputStream();
	    BufferedImage image;
	    byte[] imageInBytes;
	    try {
	     image = ImageIO.read(file);
	     //System.out.println(file.getAbsolutePath().substring(file.getAbsolutePath().indexOf(".")+1));
	     ImageIO.write(image, "png", output);
	     String base64EncodedImage = DatatypeConverter.printBase64Binary(output.toByteArray());
	     imageInBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64EncodedImage);
	     picStr = base64EncodedImage;
	     //System.out.println(base64EncodedImage);
	     BufferedImage imgbuf;
	     imgbuf = ImageIO.read(new ByteArrayInputStream(imageInBytes));
	     Image image1 = SwingFXUtils.toFXImage(imgbuf, null);
	     editBookpicBook.setImage(image1);
	    } catch (Exception e2) {
	     actionOnError(ActionType.CONTINUE, "Your Picture format not suppoerted!");
	     imageInBytes = null;
	    }

	   }
	  });
  
  backEditBook.setOnAction(e -> {
	   editBookTitle.setText("");
	   editBookAuthorsList.getSelectionModel().clearSelection();
	   editBookKeywordsText.setText("");
	   editBookLanguageList.getSelectionModel().clearSelection();
	   editBookSubjectsList.getSelectionModel().clearSelection();
	   editBookTableOfContent.setText("");
	   editBookSummary.setText("");
	   editBookPriceTextField.setText("");
	   picStr="noPicture";
	   editBookpicBook.setImage(null);
	   addBookPane.setVisible(false);
	   editBookPane.setVisible(false);
	   mainPane.setVisible(true);
	  });
  
  submitEditBook.setOnAction(e -> {
	  
	   PropertyBook selectedItem = BooksTableView.getSelectionModel().getSelectedItem();  
	   String Sn= selectedItem.getBookSn();
	  
	   String TitleBook = null;
	   TitleBook = editBookTitle.getText();

	   ObservableList < String > Authors = editBookAuthorsListSelected.getItems();
	   String authorsId = "";
	   for (int i = 0; i < Authors.size(); i++)
	    authorsId += Authors.get(i).substring(Authors.get(i).indexOf("(") + 1, Authors.get(i).indexOf(")")) + "^";

	   String keywords = null;
	   keywords = editBookKeywordsText.getText();

	   String language = null;
	   language = editBookLanguageList.getSelectionModel().getSelectedItem();

	   ObservableList < String > Subjects = editBookSubjectsListSelected.getItems();
	   String SubjectsList = "";
	   for (int i = 0; i < Subjects.size(); i++)
	    SubjectsList += Subjects.get(i).substring(Subjects.get(i).indexOf("(") + 1, Subjects.get(i).indexOf(")")) + "^";

	   String tableOfContent = null;
	   tableOfContent = editBookTableOfContent.getText();

	   String summary = null;
	   summary = editBookSummary.getText();
	   
	   String price = null;
	   price = editBookPriceTextField.getText();

	   String picture=picStr;
	   
	   if(price.matches("[-+]?[0-9]*\\.?[0-9]+")==false)
		   actionOnError(ActionType.CONTINUE, "The price must be number!");

	   else if (language == null || authorsId.length() == 0 || SubjectsList.length() == 0 || TitleBook.equals("")==true || keywords.equals("")==true || tableOfContent.equals("")==true || summary.equals("")==true)
	    actionOnError(ActionType.CONTINUE, "You must to fill the all fields!");

	   else {
	    Message message6 = prepareEditBook(ActionType.EDIT_BOOK, Sn, TitleBook, authorsId, keywords, language, SubjectsList, tableOfContent, summary, picture, price);
	    try {
	     ClientController.clientConnectionController.sendToServer(message6);
	    } catch (Exception e1) {
	     e1.printStackTrace();
	    }
	    
	    ////////////////////////////////////////////////////////////////
	    
	    
	    ////////////////////////////////////////////////////////////////

	    
	    Platform.runLater(() -> {
	    actionOnError(ActionType.CONTINUE, "The book added successfully!");
	    addBookTitle.setText("");
	    addBookAuthorsList.getSelectionModel().clearSelection();
	    addBookKeywordsText.setText("");
	    addBookLanguageList.getSelectionModel().clearSelection();
	    addBookSubjectsList.getSelectionModel().clearSelection();
	    addBookTableOfContent.setText("");
	    addBookSummary.setText("");
	    picBook.setImage(null);
	    picStr="noPicture";
	    
	    filterField.clear();
	    data.clear();
	    filteredData.clear();
	    Message message7 = prepareGetBooksList(ActionType.GET_BOOK_LIST);
	    try {
	     ClientController.clientConnectionController.sendToServer(message7);
	    } catch (IOException e2) {
	    	e2.printStackTrace();
	     actionOnError(ActionType.TERMINATE, GeneralMessages.UNNKNOWN_ERROR_DURING_SEND);
	    }
	    
	    //itai
	    Platform.runLater(new Runnable() {
			@Override
			public void run() {
					BookManagermentGetBookListRecv3 recv_getBooks = new BookManagermentGetBookListRecv3();
					recv_getBooks.start();
					synchronized (recv_getBooks) {
						try{
							recv_getBooks.wait();
						}catch(InterruptedException e){
							e.printStackTrace();
						}
						
						Platform.runLater(() -> {
						     String authors = "";
						     for (int i = 0; i < BooksList.size(); i += 9) {
						      if (i + 9 < BooksList.size() && BooksList.get(i).equals(BooksList.get(i + 9))) {
						       authors = authors + BooksList.get(i + 5) + ",";
						      } else {
						       if (authors.equals("")) {
						        String hide;
						        if (BooksList.get(i + 3).equals("0")) hide = "no";
						        else hide = "yes";
						        data.add(new PropertyBook(BooksList.get(i), BooksList.get(i + 1), BooksList.get(i + 2), hide, BooksList.get(i + 4), BooksList.get(i + 5), BooksList.get(i + 6), BooksList.get(i + 7), BooksList.get(i + 8)));
						        filteredData.add(new PropertyBook(BooksList.get(i), BooksList.get(i + 1), BooksList.get(i + 2), hide, BooksList.get(i + 4), BooksList.get(i + 5), BooksList.get(i + 6), BooksList.get(i + 7), BooksList.get(i + 8)));
						       } else {
						        String hide;
						        if (BooksList.get(i + 3).equals("0")) hide = "no";
						        else hide = "yes";
						        data.add(new PropertyBook(BooksList.get(i), BooksList.get(i + 1), BooksList.get(i + 2), hide, BooksList.get(i + 4), authors + BooksList.get(i + 5), BooksList.get(i + 6), BooksList.get(i + 7), BooksList.get(i + 8)));
						        filteredData.add(new PropertyBook(BooksList.get(i), BooksList.get(i + 1), BooksList.get(i + 2), hide, BooksList.get(i + 4), authors + BooksList.get(i + 5), BooksList.get(i + 6), BooksList.get(i + 7), BooksList.get(i + 8)));
						       }
						       authors = "";
						      }
						     }
						    });
					}
			}});
	    
	    

	    
	    TitleLabel.setVisible(false);
	    InfoTitle.setVisible(false);
	    AuthorsLabel.setVisible(false);
	    InfoAuthors.setVisible(false);
	    KeywordsLabel.setVisible(false);
	    SummaryLabel.setVisible(false);
	    imageView.setVisible(false);
	    InfoKeywords.setVisible(false);
	    infoPrice.setVisible(false);
	    PriceLabel.setVisible(false);
	    BookSummary.setVisible(false);
	    delBtn.setVisible(false);
	    editBtn.setVisible(false);
	    hideBtn.setVisible(false);
	    
	    addBookPane.setVisible(false);
	    editBookPane.setVisible(false);
	    mainPane.setVisible(true);
	    });
	   }
	  
  });
  
  
//---------------------------------domain main pane---------------------
  
  addDomainEditBtn.setVisible(false);
  DomainDeleteBtn.setVisible(false);
  mainDomainPane.setVisible(true);
  addDomainPane.setVisible(false);
  editDomainPane.setVisible(false);
  Message message4 = prepareGetDomainsWithId(ActionType.GET_DOMAINS_WITH_ID);
  ObservableList<PropertyDomain> dataDomains = FXCollections.observableArrayList();
  try {
   ClientController.clientConnectionController.sendToServer(message4);
  } catch (IOException e) {
	  e.printStackTrace();
   actionOnError(ActionType.TERMINATE, GeneralMessages.UNNKNOWN_ERROR_DURING_SEND);
  }
  
  //itai
  Platform.runLater(new Runnable() {
		@Override
		public void run() {
				BookManagermentGetDomainsWithIdRecv recv_getDomainsWithId = new BookManagermentGetDomainsWithIdRecv();
				recv_getDomainsWithId.start();
				synchronized (recv_getDomainsWithId) {
					try{
						recv_getDomainsWithId.wait();
					}catch(InterruptedException e){
						e.printStackTrace();
					}
					
					Platform.runLater(() -> {
						for(int i=0;i<domainsList.size();i+=2){
							dataDomains.add(new PropertyDomain(domainsList.get(i), domainsList.get(i+1)));
						}
				  });
				}
		}});
  

  
	domainId.setCellValueFactory(
            new PropertyValueFactory<PropertyDomain, String>("domainId"));

		domainName.setCellValueFactory(
            new PropertyValueFactory<PropertyDomain, String>("domainName"));
		
		domainId.setStyle( "-fx-alignment: CENTER;");
		domainName.setStyle( "-fx-alignment: CENTER;");
		mainDomainTableView.setItems(dataDomains);
		
		
		mainDomainTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
		 if (newSelection != null) {
			  addDomainEditBtn.setVisible(true);
			  DomainDeleteBtn.setVisible(true);
			  }
		});
		
		addDomainEditBtn.setOnAction(e -> {
			mainDomainPane.setVisible(false);
			addDomainPane.setVisible(false);
			editDomainPane.setVisible(true);
			PropertyDomain selectedItem = mainDomainTableView.getSelectionModel().getSelectedItem();
			editDomainName.setText(selectedItem.getDomainName());
		});
		
		editDomainBack.setOnAction(e -> {
			   editDomainName.setText("");
			   addDomainPane.setVisible(false);
			   editDomainPane.setVisible(false);
			   mainDomainPane.setVisible(true);
			  });
		
		editDomainSubmit.setOnAction(e -> {
			PropertyDomain selectedItem = mainDomainTableView.getSelectionModel().getSelectedItem(); 
			String DomainId = null;
			DomainId = selectedItem.getDomainId();
			String DomainName = null;
			DomainName = editDomainName.getText();
			
			//System.out.println(DomainId+" "+DomainName);

			
			if (DomainName.equals(""))
			    actionOnError(ActionType.CONTINUE, "You must to fill the all fields!");
			else{
				Message message5 = prepareEditDomain(ActionType.EDIT_DOMAIN, DomainId, DomainName);
				 try {
				     ClientController.clientConnectionController.sendToServer(message5);
				    } catch (Exception e1) {
				     e1.printStackTrace();
				    }
				 
				 	////////////////////////////////////////////
				 
				 	////////////////////////////////////////////
				 
				    Platform.runLater(() -> {
				    actionOnError(ActionType.CONTINUE, "The domain edited successfully!");
				    editDomainName.setText("");
				    
				    dataDomains.clear();
  				    Message message6 = prepareGetDomainsWithId(ActionType.GET_DOMAINS_WITH_ID);
  				    try {
  				     ClientController.clientConnectionController.sendToServer(message6);
  				    } catch (IOException e2) {
  				     actionOnError(ActionType.TERMINATE, GeneralMessages.UNNKNOWN_ERROR_DURING_SEND);
  				    }
  				    
  				  //itai
  				  Platform.runLater(new Runnable() {
  						@Override
  						public void run() {
  								BookManagermentGetDomainsWithIdRecv2 recv_getDomainsWithId = new BookManagermentGetDomainsWithIdRecv2();
  								recv_getDomainsWithId.start();
  								synchronized (recv_getDomainsWithId) {
  									try{
  										recv_getDomainsWithId.wait();
  									}catch(InterruptedException e){
  										e.printStackTrace();
  									}
  				  				    Platform.runLater(() -> {
  				  						for(int i=0;i<domainsList.size();i+=2){
  				  							dataDomains.add(new PropertyDomain(domainsList.get(i), domainsList.get(i+1)));
  				  						}
  				  				    });
  								}
  						}});
  				  
  				    


				    
				    addDomainPane.setVisible(false);
				    editDomainPane.setVisible(false);
				    mainDomainPane.setVisible(true);
			});
			}
		});

		
		
		addDomain.setOnAction(e -> {
   		   mainDomainPane.setVisible(false);
   		   addDomainPane.setVisible(true);
   		   editDomainPane.setVisible(false);
   		  });
		
		
		
		
		DomainDeleteBtn.setOnAction(e -> {
			PropertyDomain selectedItem = mainDomainTableView.getSelectionModel().getSelectedItem();
			String DomainId = selectedItem.getDomainId();
			Message message5 = prepareGetNumberBookAtDomain(ActionType.GET_NUMBER_BOOK_AT_DOMAIN ,DomainId);
			   try {
			   	ClientController.clientConnectionController.sendToServer(message5);
			   } catch (IOException e1) {	
			   	actionOnError(ActionType.TERMINATE,GeneralMessages.UNNKNOWN_ERROR_DURING_SEND);
			   }
			   
				  //itai
				  Platform.runLater(new Runnable() {
						@Override
						public void run() {
								BookManagermentGetNumberBookAtDomainRecv recv_getNumberBookAtDomain = new BookManagermentGetNumberBookAtDomainRecv();
								recv_getNumberBookAtDomain.start();
								synchronized (recv_getNumberBookAtDomain) {
									try{
										recv_getNumberBookAtDomain.wait();
									}catch(InterruptedException e){
										e.printStackTrace();
									}
									
									   Platform.runLater(() -> {
										   
											if(countBookByDomain>0)  
												actionOnError(ActionType.CONTINUE,"You can't to remove domain that contains books!");
											else{
												Message message6 = prepareGetNumberBookAtDomain(ActionType.DELETE_DOMAIN ,DomainId);
												try {
												   	ClientController.clientConnectionController.sendToServer(message6);
												   } catch (IOException e1) {	
												   	actionOnError(ActionType.TERMINATE,GeneralMessages.UNNKNOWN_ERROR_DURING_SEND);
												   }
												//////////////////////////////////
												
												//////////////////////////////////
												
												actionOnError(ActionType.CONTINUE, "The domain deleted successfully!");
											    dataDomains.clear();
							  				    Message message7 = prepareGetDomainsWithId(ActionType.GET_DOMAINS_WITH_ID);
							  				    try {
							  				     ClientController.clientConnectionController.sendToServer(message7);
							  				    } catch (IOException e2) {
							  				     actionOnError(ActionType.TERMINATE, GeneralMessages.UNNKNOWN_ERROR_DURING_SEND);
							  				    }
							  				    
							  				    
							  				  //itai
							  				  Platform.runLater(new Runnable() {
							  						@Override
							  						public void run() {
							  								BookManagermentGetDomainsWithIdRecv3 recv_getDomainsWithId = new BookManagermentGetDomainsWithIdRecv3();
							  								recv_getDomainsWithId.start();
							  								synchronized (recv_getDomainsWithId) {
							  									try{
							  										recv_getDomainsWithId.wait();
							  									}catch(InterruptedException e){
							  										e.printStackTrace();
							  									}
							  									
											  				    Platform.runLater(() -> {
											  						for(int i=0;i<domainsList.size();i+=2){
											  							dataDomains.add(new PropertyDomain(domainsList.get(i), domainsList.get(i+1)));
											  						}
											  				    });
							  								}
							  						}});

											}
											
										   });
								}
						}});

		});
   		
   		
  		 addDomainClear.setOnAction(e -> {
  			addDomainName.setText("");
  		 });
  		 
  		addDomainBack.setOnAction(e -> {
  			addDomainName.setText("");
  			mainDomainPane.setVisible(true);
    		addDomainPane.setVisible(false);
    		editDomainPane.setVisible(false);
  		 });
  		
  		addDomainSubmit.setOnAction(e -> {
  			String DomainName = null;
  			DomainName = addDomainName.getText();
  			//System.out.println(DomainName);
  			
  			if (DomainName.equals(""))
  			    actionOnError(ActionType.CONTINUE, "You must to fill the all fields!");
  			else {
  				
  				 Message message5 = prepareAddDomain(ActionType.ADD_DOMAIN, DomainName);
  				 try {
  				     ClientController.clientConnectionController.sendToServer(message5);
  				    } catch (Exception e1) {
  				     e1.printStackTrace();
  				    }
  				Platform.runLater(() -> {
  				    actionOnError(ActionType.CONTINUE, "The domain added successfully!");
  				    addDomainName.setText("");
  				    
  				    dataDomains.clear();
  				    Message message6 = prepareGetDomainsWithId(ActionType.GET_DOMAINS_WITH_ID);
  				    try {
  				     ClientController.clientConnectionController.sendToServer(message6);
  				    } catch (IOException e2) {
  				     actionOnError(ActionType.TERMINATE, GeneralMessages.UNNKNOWN_ERROR_DURING_SEND);
  				    }
  				    
  				  //itai
  				  Platform.runLater(new Runnable() {
  						@Override
  						public void run() {
  								BookManagermentGetDomainsWithIdRecv4 recv_getDomainsWithId = new BookManagermentGetDomainsWithIdRecv4();
  								recv_getDomainsWithId.start();
  								synchronized (recv_getDomainsWithId) {
  									try{
  										recv_getDomainsWithId.wait();
  									}catch(InterruptedException e){
  										e.printStackTrace();
  									}
  				  				    Platform.runLater(() -> {
  				  						for(int i=0;i<domainsList.size();i+=2){
  				  							dataDomains.add(new PropertyDomain(domainsList.get(i), domainsList.get(i+1)));
  				  						}
  				  				    });
  								}
  						}});
  				  
  				    


  				    
  				  mainDomainPane.setVisible(true);
  				  addDomainPane.setVisible(false);
  				  editDomainPane.setVisible(false);
  				    
  				    
  				});
  			}
  		});
	
// end domain main pane
  		
  		
//---------------------------------subjects main pane---------------------
  		
  	  subjectsEditBtn.setVisible(false);
  	subjectsDeleteBtn.setVisible(false);
  	  mainSubjectPane.setVisible(true);
  	addSubjectPane.setVisible(false);
  	editSubjectPane.setVisible(false);
  	  Message message5 = prepareGetDomainsWithId(ActionType.GET_SUBJECTS_INFO);
  	  ObservableList<PropertySubject> dataSubjects = FXCollections.observableArrayList();
  	  try {
  	   ClientController.clientConnectionController.sendToServer(message5);
  	  } catch (IOException e) {
  		  e.printStackTrace();
  	   actionOnError(ActionType.TERMINATE, GeneralMessages.UNNKNOWN_ERROR_DURING_SEND);
  	  }
  	  
	  //itai
	  Platform.runLater(new Runnable() {
			@Override
			public void run() {
					BookManagermentGetSubjectsInfoRecv recv_getSubjectsInfo = new BookManagermentGetSubjectsInfoRecv();
					recv_getSubjectsInfo.start();
					synchronized (recv_getSubjectsInfo) {
						try{
							recv_getSubjectsInfo.wait();
						}catch(InterruptedException e){
							e.printStackTrace();
						}
						
				  		Platform.runLater(() -> {
				  			for(int i=0;i<subjectsList.size();i+=4){
				  				dataSubjects.add(new PropertySubject(subjectsList.get(i), subjectsList.get(i+1), "("+subjectsList.get(i+2)+")"+" "+subjectsList.get(i+3)));
				  			}
				  	  });
					}
			}});
  	  

  	  
  		subjectId.setCellValueFactory(
  	            new PropertyValueFactory<PropertyDomain, String>("subjectId"));

  		subjectName.setCellValueFactory(
  	            new PropertyValueFactory<PropertyDomain, String>("subjectName"));
  			
  		subjectDomain.setCellValueFactory(
  	  	            new PropertyValueFactory<PropertyDomain, String>("subjectDomain"));
  			
  		subjectId.setStyle( "-fx-alignment: CENTER;");
  		subjectName.setStyle( "-fx-alignment: CENTER;");
  		subjectDomain.setStyle( "-fx-alignment: CENTER;");
  		//System.out.println("asas");
  		mainSubjectTable.setItems(dataSubjects);
  			
  		
  		mainSubjectTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
  			 if (newSelection != null) {
  				subjectsEditBtn.setVisible(true);
  				subjectsDeleteBtn.setVisible(true);
  				  }
  			});
  				
  			
  		editSubjetcsBack.setOnAction(e -> {
  				   editSubjectsName.setText("");
  				   editSubjectsDomainsList.getSelectionModel().clearSelection();
  				 addSubjectPane.setVisible(false);
  				 editSubjectPane.setVisible(false);
  				 mainSubjectPane.setVisible(true);
  				  });
  		
  		subjectsEditBtn.setOnAction(e -> {
  			mainSubjectPane.setVisible(false);
  			addSubjectPane.setVisible(false);
  			editSubjectPane.setVisible(true);
  				PropertySubject selectedItem = mainSubjectTable.getSelectionModel().getSelectedItem(); 
  				editSubjectsName.setText(selectedItem.getSubjectName());
  				
  				Message message6 = prepareGetAuthors(ActionType.GET_DOMAINS_WITH_ID);
  				
  				try {
  			    ClientController.clientConnectionController.sendToServer(message6);
  			   } catch (Exception e1) {
  			    e1.printStackTrace();
  			   }
  				
  			  //itai
  			  Platform.runLater(new Runnable() {
  					@Override
  					public void run() {
  							BookManagermentGetDomainsWithIdRecv5 recv_getDomainsWithId = new BookManagermentGetDomainsWithIdRecv5();
  							recv_getDomainsWithId.start();
  							synchronized (recv_getDomainsWithId) {
  								try{
  									recv_getDomainsWithId.wait();
  								}catch(InterruptedException e){
  									e.printStackTrace();
  								}
  				  				Platform.runLater(() -> { 
  				   				 ArrayList < String > domains = new ArrayList < String > ();
  				   			    //System.out.println(statush.get(1).getFirstname());
  				   			    for (int i = 0; i < domainsList.size(); i+=2) {
  				   			    	domains.add("(" + domainsList.get(i) + ")" + " " + domainsList.get(i+1));
  				   			    }

  				   			  ObservableList < String > domainsList = FXCollections.observableArrayList(domains);
  				   			  editSubjectsDomainsList.setItems(domainsList);  
  				   			editSubjectsDomainsList.getSelectionModel().select(selectedItem.getSubjectDomain());
  				   			   
  				   			 });
  							}
  					}});		
  				

  				
  			});

  			
  				
  		addSubject.setOnAction(e2 -> {
  			mainSubjectPane.setVisible(false);
  			addSubjectPane.setVisible(true);
  			editSubjectPane.setVisible(false);
  			
  			Message message6 = prepareGetAuthors(ActionType.GET_DOMAINS_WITH_ID);
				
				try {
			    ClientController.clientConnectionController.sendToServer(message6);
			   } catch (Exception e1) {
			    e1.printStackTrace();
			   }
				
				  //itai
				  Platform.runLater(new Runnable() {
						@Override
						public void run() {
								BookManagermentGetDomainsWithIdRecv6 recv_getDomainsWithId = new BookManagermentGetDomainsWithIdRecv6();
								recv_getDomainsWithId.start();
								synchronized (recv_getDomainsWithId) {
									try{
										recv_getDomainsWithId.wait();
									}catch(InterruptedException e){
										e.printStackTrace();
									}
									Platform.runLater(() -> { 
										 ArrayList < String > domains = new ArrayList < String > ();
									    //System.out.println(statush.get(1).getFirstname());
									    for (int i = 0; i < domainsList.size(); i+=2) {
									    	domains.add("(" + domainsList.get(i) + ")" + " " + domainsList.get(i+1));
									    }

									  ObservableList < String > domainsList = FXCollections.observableArrayList(domains);
									  addSubjectsDomainsList.setItems(domainsList);  

										});
								}
						}});
				  
				//

			 });
  			
  			
  			
  			
  		subjectsDeleteBtn.setOnAction(e -> {
  				PropertySubject selectedItem = mainSubjectTable.getSelectionModel().getSelectedItem();
  				String subjectId = selectedItem.getSubjectId();
  				Message message6 = prepareGetNumberBookAtDomain(ActionType.GET_NUMBER_BOOK_AT_SUBJECT ,subjectId);
  				   try {
  				   	ClientController.clientConnectionController.sendToServer(message6);
  				   } catch (IOException e1) {	
  				   	actionOnError(ActionType.TERMINATE,GeneralMessages.UNNKNOWN_ERROR_DURING_SEND);
  				   }
  				   
  				  //itai
  				  Platform.runLater(new Runnable() {
  						@Override
  						public void run() {
  								BookManagermentGetNumberBookAtSubjectRecv recv_getNumberBookAtSubject = new BookManagermentGetNumberBookAtSubjectRecv();
  								recv_getNumberBookAtSubject.start();
  								synchronized (recv_getNumberBookAtSubject) {
  									try{
  										recv_getNumberBookAtSubject.wait();
  									}catch(InterruptedException e){
  										e.printStackTrace();
  									}
  								}
  						}});
  				  
  				   Platform.runLater(() -> {
  					if(countBookBySubject>0)  
  						actionOnError(ActionType.CONTINUE,"You can't to remove subject that contains books!");
  					else{
  						Message message7 = prepareGetNumberBookAtDomain(ActionType.DELETE_SUBJECT ,subjectId);
  						try {
  						   	ClientController.clientConnectionController.sendToServer(message7);
  						   } catch (IOException e1) {	
  						   	actionOnError(ActionType.TERMINATE,GeneralMessages.UNNKNOWN_ERROR_DURING_SEND);
  						   }
  						actionOnError(ActionType.CONTINUE, "The subject deleted successfully!");
  						
  						////////////////////////////////////////
  						
  						///////////////////////////////////////
  						
  						dataSubjects.clear();
  	  	  		  	  Message message8 = prepareGetDomainsWithId(ActionType.GET_SUBJECTS_INFO);
  	  	  	  	  try {
  	  	  	  	   ClientController.clientConnectionController.sendToServer(message8);
  	  	  	  	  } catch (IOException e2) {
  	  	  	  		  e2.printStackTrace();
  	  	  	  	   actionOnError(ActionType.TERMINATE, GeneralMessages.UNNKNOWN_ERROR_DURING_SEND);
  	  	  	  	  }
  	  	  	  	  
  	  		  //itai
  	  		  Platform.runLater(new Runnable() {
  	  				@Override
  	  				public void run() {
  	  						BookManagermentGetSubjectsInfoRecv2 recv_getSubjectsInfo = new BookManagermentGetSubjectsInfoRecv2();
  	  						recv_getSubjectsInfo.start();
  	  						synchronized (recv_getSubjectsInfo) {
  	  							try{
  	  								recv_getSubjectsInfo.wait();
  	  							}catch(InterruptedException e){
  	  								e.printStackTrace();
  	  							}
  	  	  	  	  	  		Platform.runLater(() -> {
  	    	  	  	  			for(int i=0;i<subjectsList.size();i+=4){
  	    	  	  	  				dataSubjects.add(new PropertySubject(subjectsList.get(i), subjectsList.get(i+1), "("+subjectsList.get(i+2)+")"+" "+subjectsList.get(i+3)));
  	    	  	  	  			}
  	    	  	  	  	  });
  	  						}
  	  				}});
  	  	  	  	  


  	  	  				mainSubjectTable.setItems(dataSubjects);
  						
  					}
  					
  				   });
  				 

  			});
  	   		
  		
  		addSubjetcsClear.setOnAction(e4 -> {
  			addSubjectsName.setText("");
  			addSubjectsDomainsList.getSelectionModel().clearSelection();
  	  		 });
  			 
  		addSubjetcsBack.setOnAction(e3 -> {
  			addSubjectsName.setText("");
  			addSubjectsDomainsList.getSelectionModel().clearSelection();
  			mainSubjectPane.setVisible(true);
  	  		addSubjectPane.setVisible(false);
  	  		editSubjectPane.setVisible(false);
  	  		 });
  			
  		addSubjetcsSubmit.setOnAction(e -> {
  	  			String SubjectName = null;
  	  			SubjectName = addSubjectsName.getText();
  	  			//System.out.println(SubjectName);
  	  			String SubjectDomain;
  	  			SubjectDomain = addSubjectsDomainsList.getSelectionModel().getSelectedItem().toString();
  	  			//System.out.println(SubjectDomain);
  	  			
  	  			if (SubjectName.equals("") || SubjectDomain.equals(""))
  	  			    actionOnError(ActionType.CONTINUE, "You must to fill the all fields!");
  	  			else {
  	  				 SubjectDomain=SubjectDomain.substring(SubjectDomain.indexOf("(")+1, SubjectDomain.indexOf(")"));
  	  				 Message message6 = prepareAddSubject(ActionType.ADD_SUBJECT, SubjectName, SubjectDomain);
  	  				 try {
  	  				     ClientController.clientConnectionController.sendToServer(message6);
  	  				    } catch (Exception e1) {
  	  				     e1.printStackTrace();
  	  				    }
  	  				 ////////////////////////////////////////////
  	  				 
  	  				 ///////////////////////////////////////////
  	  				 
  	  				Platform.runLater(() -> {
  	  				    actionOnError(ActionType.CONTINUE, "The subject added successfully!");
  	  				addSubjectsName.setText("");
  	  				addSubjectsDomainsList.getSelectionModel().clearSelection();
  	  				    
  	  				
  	  			dataSubjects.clear();
  	  		  	  Message message7 = prepareGetDomainsWithId(ActionType.GET_SUBJECTS_INFO);
  	  	  	  try {
  	  	  	   ClientController.clientConnectionController.sendToServer(message7);
  	  	  	  } catch (IOException e2) {
  	  	  		  e2.printStackTrace();
  	  	  	   actionOnError(ActionType.TERMINATE, GeneralMessages.UNNKNOWN_ERROR_DURING_SEND);
  	  	  	  }
  	  	  //itai
  	  	  Platform.runLater(new Runnable() {
  	  			@Override
  	  			public void run() {
  	  					BookManagermentGetSubjectsInfoRecv3 recv_getSubjectsInfo = new BookManagermentGetSubjectsInfoRecv3();
  	  					recv_getSubjectsInfo.start();
  	  					synchronized (recv_getSubjectsInfo) {
  	  						try{
  	  							recv_getSubjectsInfo.wait();
  	  						}catch(InterruptedException e){
  	  							e.printStackTrace();
  	  						}
  	  	  	  	  		Platform.runLater(() -> {
  	    	  	  			for(int i=0;i<subjectsList.size();i+=4){
  	    	  	  				dataSubjects.add(new PropertySubject(subjectsList.get(i), subjectsList.get(i+1), "("+subjectsList.get(i+2)+")"+" "+subjectsList.get(i+3)));
  	    	  	  			}
  	    	  	  	  });
  	  					}
  	  			}});
  	  	  	  


  	  				mainSubjectTable.setItems(dataSubjects);
  	  			mainSubjectPane.setVisible(true);
  	  				addSubjectPane.setVisible(false);
  	  			editSubjectPane.setVisible(false);
  	  				    
  	  				    
  	  				});
  	  			}
  	  		});
  		
  		
  		
  		editSubjetcsSubmit.setOnAction(e -> {
  			PropertySubject selectedItem = mainSubjectTable.getSelectionModel().getSelectedItem();
  			String subjectId = null;
  			subjectId = selectedItem.getSubjectId();
			String SubjectName = null;
			SubjectName = editSubjectsName.getText();
			String SubjectDomain = null;
			SubjectDomain = editSubjectsDomainsList.getSelectionModel().getSelectedItem().toString();
			
			if (SubjectName.equals("") || SubjectDomain.equals(""))
			    actionOnError(ActionType.CONTINUE, "You must to fill the all fields!");
			else{
				SubjectDomain=SubjectDomain.substring(SubjectDomain.charAt('(')+1, SubjectDomain.charAt(')'));
				Message message6 = prepareEditSubject(ActionType.EDIT_SUBJECT, subjectId, SubjectName, SubjectDomain);
				 try {
				     ClientController.clientConnectionController.sendToServer(message6);
				    } catch (Exception e1) {
				     e1.printStackTrace();
				    }
				 	//////////////////////////////////////
				 
				 	//////////////////////////////////////
				 
				    Platform.runLater(() -> {
				    actionOnError(ActionType.CONTINUE, "The subject edited successfully!");
				    editDomainName.setText("");
				    
				    
				    
				    dataSubjects.clear();
	  	  		  	  Message message7 = prepareGetDomainsWithId(ActionType.GET_SUBJECTS_INFO);
	  	  	  	  try {
	  	  	  	   ClientController.clientConnectionController.sendToServer(message7);
	  	  	  	  } catch (IOException e2) {
	  	  	  		  e2.printStackTrace();
	  	  	  	   actionOnError(ActionType.TERMINATE, GeneralMessages.UNNKNOWN_ERROR_DURING_SEND);
	  	  	  	  }
	  	  	  //itai
	  	  	  Platform.runLater(new Runnable() {
	  	  			@Override
	  	  			public void run() {
	  	  					BookManagermentGetSubjectsInfoRecv4 recv_getSubjectsInfo = new BookManagermentGetSubjectsInfoRecv4();
	  	  					recv_getSubjectsInfo.start();
	  	  					synchronized (recv_getSubjectsInfo) {
	  	  						try{
	  	  							recv_getSubjectsInfo.wait();
	  	  						}catch(InterruptedException e){
	  	  							e.printStackTrace();
	  	  						}
	  	  	  	  	  		Platform.runLater(() -> {
	  		  	  	  			for(int i=0;i<subjectsList.size();i+=4){
	  		  	  	  				dataSubjects.add(new PropertySubject(subjectsList.get(i), subjectsList.get(i+1), "("+subjectsList.get(i+2)+")"+" "+subjectsList.get(i+3)));
	  		  	  	  			}
	  		  	  	  	  });
	  	  					}
	  	  			}});
	  	  	  	  

	
	  	  				mainSubjectTable.setItems(dataSubjects);
	  	  			mainSubjectPane.setVisible(true);
	  	  				addSubjectPane.setVisible(false);
	  	  			editSubjectPane.setVisible(false);
			});
			}
  			
  		});
  	////////////////////////////////////////////////////
  		
  		
  	//---------------------------------authors main pane---------------------

  		
  		
  	  addAuthorEditBtn.setVisible(false);
  	  AuthorDeleteBtn.setVisible(false);
  	  mainAuthorPane.setVisible(true);
  	  addAuthorPane.setVisible(false);
  	  editAuthorPane.setVisible(false);
  	  Message message6 = prepareGetAuthors(ActionType.GET_AUTHORS);
  	  ObservableList<PropertyAuthor> dataAuthors = FXCollections.observableArrayList();
  	  try {
  	   ClientController.clientConnectionController.sendToServer(message6);
  	  } catch (IOException e) {
  		  e.printStackTrace();
  	   actionOnError(ActionType.TERMINATE, GeneralMessages.UNNKNOWN_ERROR_DURING_SEND);
  	  }
  	  
  	  //itai
  	  Platform.runLater(new Runnable() {
  			@Override
  			public void run() {
  				BookManagermentGetAuthorsRecv3 recv_getAuthors = new BookManagermentGetAuthorsRecv3();
  					recv_getAuthors.start();
  					synchronized (recv_getAuthors) {
  						try{
  							recv_getAuthors.wait();
  						}catch(InterruptedException e){
  							e.printStackTrace();
  						}
  				  		Platform.runLater(() -> {
  				  			for(int i=0;i<authorList.size();i++){
  				  				dataAuthors.add(new PropertyAuthor(authorList.get(i).getId(), authorList.get(i).getFirstname(), authorList.get(i).getLastname()));
  				  			}
  				  	  });
  					}
  			}});
  	  

  	  
  		authorId.setCellValueFactory(
  	            new PropertyValueFactory<PropertyDomain, String>("authorId"));

  		authorFirstName.setCellValueFactory(
  	            new PropertyValueFactory<PropertyDomain, String>("authorFirstName"));
  		
  		authorLastName.setCellValueFactory(
  	            new PropertyValueFactory<PropertyDomain, String>("authorLastName"));
  			
  		authorId.setStyle( "-fx-alignment: CENTER;");
  		authorFirstName.setStyle( "-fx-alignment: CENTER;");
  		authorLastName.setStyle( "-fx-alignment: CENTER;");;
  			mainAuthorTableView.setItems(dataAuthors);
  			
  		
  			mainAuthorTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
  			 if (newSelection != null) {
  				  addAuthorEditBtn.setVisible(true);
  				  AuthorDeleteBtn.setVisible(true);
  				  }
  			});
  					
  			addAuthorEditBtn.setOnAction(e -> {
  				mainAuthorPane.setVisible(false);
  				addAuthorPane.setVisible(false);
  				editAuthorPane.setVisible(true);
  				PropertyAuthor selectedItem = mainAuthorTableView.getSelectionModel().getSelectedItem();
  				editAuthorFirstName.setText(selectedItem.getAuthorFirstName());
  				editAuthorLastName.setText(selectedItem.getAuthorLastName());
  			});
  			
  			editAuthorBack.setOnAction(e -> {
  				   editAuthorFirstName.setText("");
  				 editAuthorLastName.setText("");
  				   addAuthorPane.setVisible(false);
  				   editAuthorPane.setVisible(false);
  				   mainAuthorPane.setVisible(true);
  				  });
  			
  			editAuthorSubmit.setOnAction(e -> {
  				PropertyAuthor selectedItem = mainAuthorTableView.getSelectionModel().getSelectedItem(); 
  				String AuthorId = null;
  				AuthorId = selectedItem.getAuthorId();
  				String AuthorFirstName = null;
  				AuthorFirstName = editAuthorFirstName.getText();
  				String AuthorLastName = null;
  				AuthorLastName = editAuthorLastName.getText();
  				

  				
  				if (AuthorFirstName.equals("") || AuthorLastName.equals(""))
  				    actionOnError(ActionType.CONTINUE, "You must to fill the all fields!");
  				else{
  					Message message7 = prepareEditAuthor(ActionType.EDIT_AUTHOR, AuthorId, AuthorFirstName, AuthorLastName);
  					 try {
  					     ClientController.clientConnectionController.sendToServer(message7);
  					    } catch (Exception e1) {
  					     e1.printStackTrace();
  					    }
  					 	////////////////////////////////////
  					 
  					 	///////////////////////////////////
  					 
  					    Platform.runLater(() -> {
  					    actionOnError(ActionType.CONTINUE, "The author edited successfully!");
  					    editAuthorFirstName.setText("");
  					    editAuthorLastName.setText("");
  					    
  					    dataAuthors.clear();
  	  				    Message message8 = prepareGetAuthors(ActionType.GET_AUTHORS);
  	  				    try {
  	  				     ClientController.clientConnectionController.sendToServer(message8);
  	  				    } catch (IOException e2) {
  	  				     actionOnError(ActionType.TERMINATE, GeneralMessages.UNNKNOWN_ERROR_DURING_SEND);
  	  				    }
  	  				    
  	  				  //itai
  	  				  Platform.runLater(new Runnable() {
  	  						@Override
  	  						public void run() {
  	  								BookManagermentGetAuthorsRecv4 recv_getAuthors = new BookManagermentGetAuthorsRecv4();
  	  								recv_getAuthors.start();
  	  								synchronized (recv_getAuthors) {
  	  									try{
  	  										recv_getAuthors.wait();
  	  									}catch(InterruptedException e){
  	  										e.printStackTrace();
  	  									}
  	  		  	  				    Platform.runLater(() -> {
  	  	  	  				    	for(int i=0;i<authorList.size();i++){
  	  	  	  				    			dataAuthors.add(new PropertyAuthor(authorList.get(i).getId(), authorList.get(i).getFirstname(), authorList.get(i).getLastname()));
  	  	  	  				    		}
  	  	  	  				    });

  	  								}
  	  						}});
  	  				    
  	  				    

  	  				    
  	  				filterField.clear();
  	  		    data.clear();
  	  		    filteredData.clear();
  	  		    Message message9 = prepareGetBooksList(ActionType.GET_BOOK_LIST);
  	  		    try {
  	  		     ClientController.clientConnectionController.sendToServer(message9);
  	  		    } catch (IOException e2) {
  	  		    	e2.printStackTrace();
  	  		     actionOnError(ActionType.TERMINATE, GeneralMessages.UNNKNOWN_ERROR_DURING_SEND);
  	  		    }
  	  		    
  	  		//itai
  	  		 Platform.runLater(new Runnable() {
  	  			@Override
  	  			public void run() {
  	  					BookManagermentGetBookListRecv4 recv_getBooks = new BookManagermentGetBookListRecv4();
  	  					recv_getBooks.start();
  	  					synchronized (recv_getBooks) {
  	  						try{
  	  							recv_getBooks.wait();
  	  						}catch(InterruptedException e){
  	  							e.printStackTrace();
  	  						}
  	  					Platform.runLater(() -> {
  	  	  	  		     String authors = "";
  	  	  	  		     for (int i = 0; i < BooksList.size(); i += 9) {
  	  	  	  		      if (i + 9 < BooksList.size() && BooksList.get(i).equals(BooksList.get(i + 9))) {
  	  	  	  		       authors = authors + BooksList.get(i + 5) + ",";
  	  	  	  		      } else {
  	  	  	  		       if (authors.equals("")) {
  	  	  	  		        String hide;
  	  	  	  		        if (BooksList.get(i + 3).equals("0")) hide = "no";
  	  	  	  		        else hide = "yes";
  	  	  	  		        data.add(new PropertyBook(BooksList.get(i), BooksList.get(i + 1), BooksList.get(i + 2), hide, BooksList.get(i + 4), BooksList.get(i + 5), BooksList.get(i + 6), BooksList.get(i + 7), BooksList.get(i + 8)));
  	  	  	  		        filteredData.add(new PropertyBook(BooksList.get(i), BooksList.get(i + 1), BooksList.get(i + 2), hide, BooksList.get(i + 4), BooksList.get(i + 5), BooksList.get(i + 6), BooksList.get(i + 7), BooksList.get(i + 8)));
  	  	  	  		       } else {
  	  	  	  		        String hide;
  	  	  	  		        if (BooksList.get(i + 3).equals("0")) hide = "no";
  	  	  	  		        else hide = "yes";
  	  	  	  		        data.add(new PropertyBook(BooksList.get(i), BooksList.get(i + 1), BooksList.get(i + 2), hide, BooksList.get(i + 4), authors + BooksList.get(i + 5), BooksList.get(i + 6), BooksList.get(i + 7), BooksList.get(i + 8)));
  	  	  	  		        filteredData.add(new PropertyBook(BooksList.get(i), BooksList.get(i + 1), BooksList.get(i + 2), hide, BooksList.get(i + 4), authors + BooksList.get(i + 5), BooksList.get(i + 6), BooksList.get(i + 7), BooksList.get(i + 8)));
  	  	  	  		       }
  	  	  	  		       authors = "";
  	  	  	  		      }
  	  	  	  		     }
  	  	  	  		    });
  	  					}
  	  			}});
  	  		    
  	  		    
 
  	  		    
  	  		    TitleLabel.setVisible(false);
  	  		    InfoTitle.setVisible(false);
  	  		    AuthorsLabel.setVisible(false);
  	  		    InfoAuthors.setVisible(false);
  	  		    KeywordsLabel.setVisible(false);
  	  		    SummaryLabel.setVisible(false);
  	  		    imageView.setVisible(false);
  	  		    InfoKeywords.setVisible(false);
  	  		    infoPrice.setVisible(false);
  	  		    PriceLabel.setVisible(false);
  	  		    BookSummary.setVisible(false);
  	  		    delBtn.setVisible(false);
  	  		    editBtn.setVisible(false);
  	  		    hideBtn.setVisible(false);
  					    
  					    addAuthorPane.setVisible(false);
  					    editAuthorPane.setVisible(false);
  					    mainAuthorPane.setVisible(true);
  				});
  				}
  			});

  			
  			
  			addAuthor.setOnAction(e -> {
  	   		   mainAuthorPane.setVisible(false);
  	   		   addAuthorPane.setVisible(true);
  	   		   editAuthorPane.setVisible(false);
  	   		  });
  			
  			
  			AuthorDeleteBtn.setOnAction(e -> {
  				PropertyAuthor selectedItem = mainAuthorTableView.getSelectionModel().getSelectedItem();
  				String AuthorId = selectedItem.getAuthorId();
  				Message message7 = prepareGetNumberBookOfAuthor(ActionType.GET_NUMBER_BOOK_OF_AUTHOR ,AuthorId);
  				   try {
  				   	ClientController.clientConnectionController.sendToServer(message7);
  				   } catch (IOException e1) {	
  				   	actionOnError(ActionType.TERMINATE,GeneralMessages.UNNKNOWN_ERROR_DURING_SEND);
  				   }
  				   
   				  //itai
   				  Platform.runLater(new Runnable() {
   						@Override
   						public void run() {
   								BookManagermentGetNumberBookOfAuthorRecv recv_getNumberBookOfAuthor = new BookManagermentGetNumberBookOfAuthorRecv();
   								recv_getNumberBookOfAuthor.start();
   								synchronized (recv_getNumberBookOfAuthor) {
   									try{
   										recv_getNumberBookOfAuthor.wait();
   									}catch(InterruptedException e){
   										e.printStackTrace();
   									}
   				  				   Platform.runLater(() -> {

   				  					   //System.out.println(countBookOfUser);
   				  					if(countBookOfUser>0)  
   				  						actionOnError(ActionType.CONTINUE,"You can't to remove author that contains books!");
   				  					else{
   				  						Message message8 = prepareGetNumberBookAtDomain(ActionType.DELETE_AUTHOR ,AuthorId);
   				  						try {
   				  						   	ClientController.clientConnectionController.sendToServer(message8);
   				  						   } catch (IOException e1) {	
   				  						   	actionOnError(ActionType.TERMINATE,GeneralMessages.UNNKNOWN_ERROR_DURING_SEND);
   				  						   }
   				  						actionOnError(ActionType.CONTINUE, "The author deleted successfully!");
   				  						dataAuthors.clear();
   				  	  				    Message message9 = prepareGetAuthors(ActionType.GET_AUTHORS);
   				  	  				    try {
   				  	  				     ClientController.clientConnectionController.sendToServer(message9);
   				  	  				    } catch (IOException e2) {
   				  	  				     actionOnError(ActionType.TERMINATE, GeneralMessages.UNNKNOWN_ERROR_DURING_SEND);
   				  	  				    }
   				  	  				  //itai
   				  	  				  Platform.runLater(new Runnable() {
   				  	  						@Override
   				  	  						public void run() {
   				  	  							BookManagermentGetAuthorsRecv5 recv_getAuthors = new BookManagermentGetAuthorsRecv5();
   				  	  								recv_getAuthors.start();
   				  	  								synchronized (recv_getAuthors) {
   				  	  									try{
   				  	  										recv_getAuthors.wait();
   				  	  									}catch(InterruptedException e){
   				  	  										e.printStackTrace();
   				  	  									}
   				  	  								}
   				  	  						}});
   				  	  				    
   				  	  				    Platform.runLater(() -> {
   				  	  				    for(int i=0;i<authorList.size();i++){
   								    			dataAuthors.add(new PropertyAuthor(authorList.get(i).getId(), authorList.get(i).getFirstname(), authorList.get(i).getLastname()));
   								    		}
   				  	  				    });

   				  						
   				  					}
   				  					
   				  				   });
   								}
   						}});
  				   

  				 

  			});
  	   		
  	   		
  	  		 addAuthorClear.setOnAction(e -> {
  	  			addAuthorFirstName.setText("");
  	  			addAuthorLastName.setText("");
  	  		 });
  	  		 
  	  		addAuthorBack.setOnAction(e -> {
  	  			addAuthorFirstName.setText("");
  	  			addAuthorLastName.setText("");
  	  			mainAuthorPane.setVisible(true);
  	    		addAuthorPane.setVisible(false);
  	    		editAuthorPane.setVisible(false);
  	  		 });
  	  		
  	  		addAuthorSubmit.setOnAction(e -> {
  	  			String AuthorFirstName = null;
  	  			AuthorFirstName = addAuthorFirstName.getText();
  	  			
  	  			String AuthorLastName = null;
  	  			AuthorLastName = addAuthorLastName.getText();
  	  			
  	  			if (AuthorFirstName.equals("") || AuthorLastName.equals(""))
  	  			    actionOnError(ActionType.CONTINUE, "You must to fill the all fields!");
  	  			else {
  	  				
  	  				 Message message7 = prepareAddAuthor(ActionType.ADD_AUTHOR, AuthorFirstName, AuthorLastName);
  	  				 try {
  	  				     ClientController.clientConnectionController.sendToServer(message7);
  	  				    } catch (Exception e1) {
  	  				     e1.printStackTrace();
  	  				    }
  	  				Platform.runLater(() -> {
  	  				    actionOnError(ActionType.CONTINUE, "The author added successfully!");
  	  				    addAuthorFirstName.setText("");
  	  				    addAuthorLastName.setText("");

  	  				    
  	  				    dataAuthors.clear();
  	  				    Message message8 = prepareGetAuthors(ActionType.GET_AUTHORS);
  	  				    try {
  	  				     ClientController.clientConnectionController.sendToServer(message8);
  	  				    } catch (IOException e2) {
  	  				     actionOnError(ActionType.TERMINATE, GeneralMessages.UNNKNOWN_ERROR_DURING_SEND);
  	  				    }
  	  				    
  	  				  //itai
  	  				  Platform.runLater(new Runnable() {
  	  						@Override
  	  						public void run() {
  	  								BookManagermentGetAuthorsRecv6 recv_getAuthors = new BookManagermentGetAuthorsRecv6();
  	  								recv_getAuthors.start();
  	  								synchronized (recv_getAuthors) {
  	  									try{
  	  										recv_getAuthors.wait();
  	  									}catch(InterruptedException e){
  	  										e.printStackTrace();
  	  									}
  	  		  	  				    Platform.runLater(() -> {
  	  	  	  				    	for(int i=0;i<authorList.size();i++){
  	  	  	  				    		dataAuthors.add(new PropertyAuthor(authorList.get(i).getId(), authorList.get(i).getFirstname(), authorList.get(i).getLastname()));
  	  	  	  				    	}
  	  	  	  				    });
  	  								}
  	  						}});
  	  				    


  	  				    
  	  				  mainAuthorPane.setVisible(true);
  	  				  addAuthorPane.setVisible(false);
  	  				  editAuthorPane.setVisible(false);
  	  				    
  	  				    
  	  				});
  	  			}
  	  		});
  		
  		
  		
  //////////////////////////////////////////////
	

 }

 // ---------------- functions -------------------
 

/**
 * Open file chooser in the user home directory
 * @param fileChooser
 */
private static void configureFileChooser(final FileChooser fileChooser) {
  fileChooser.setTitle("View Pictures");
  fileChooser.setInitialDirectory(
   new File(System.getProperty("user.home"))
  );
 }



 /**
 * Update books table view after change the search input (filteredTextField string)
 */
private void updateFilteredData() {
  filteredData.clear();

  for (PropertyBook b: data) {
   if (matchesFilter(b)) {
    filteredData.add(b);
   }
  }

  // Must re-sort table after items changed
  reapplyTableSortOrder();
 }



 /**
  * this function help to updateFilteredData function by searching in the data (books table) the appropriate string that user search
 * @param book 
 * @return true- success, false - not success
 */
private boolean matchesFilter(PropertyBook book) {
  String filterString = filterField.getText();
  if (filterString == null || filterString.isEmpty()) {
   // No filter --> Add all.
   return true;
  }

  String lowerCaseFilterString = filterString.toLowerCase();

  if (book.getBookSn().toLowerCase().indexOf(lowerCaseFilterString) != -1) {
   return true;
  }
  if (book.getBookTitle().toLowerCase().indexOf(lowerCaseFilterString) != -1) {
   return true;
  }
  if (book.getAuthorName().toLowerCase().indexOf(lowerCaseFilterString) != -1) {
   return true;
  }
  if (book.getBookKeywords().toLowerCase().indexOf(lowerCaseFilterString) != -1) {
   return true;
  }

  return false; // Does not match
 }


 /**
 * Sort the data of books after user search and filter data
 */
private void reapplyTableSortOrder() {
  ArrayList < TableColumn < PropertyBook, ? >> sortOrder = new ArrayList < > (BooksTableView.getSortOrder());
  BooksTableView.getSortOrder().clear();
  BooksTableView.getSortOrder().addAll(sortOrder);
 }


 /**
  * Prepare message to get books list from server
 * @param type - the type of the action that server need to do
 * @return message
 */
public Message prepareGetBooksList(ActionType type) {
  Message message = new Message();
  message.setType(type);
  return message;
 }
 

 /**
  * Prepare message to get amount of books by specific domain
 * @param type - the type of the action that server need to do
 * @param domainId - ID of domain
 * @return message
 */
private Message prepareGetNumberBookAtDomain(ActionType type, String domainId) {
	  Message message = new Message();
	  ArrayList < String > elementsList = new ArrayList < String > ();
	  elementsList.add(domainId);
	  message.setType(type);
	  message.setElementsList(elementsList);
	  return message;
	 }
 

 /**
  * Prepare message to get amount of books by specific author
 * @param type - the type of the action that server need to do
 * @param authorId -ID of author
 * @return message
 */
private Message prepareGetNumberBookOfAuthor(ActionType type, String authorId) {
	  Message message = new Message();
	  ArrayList < String > elementsList = new ArrayList < String > ();
	  elementsList.add(authorId);
	  message.setType(type);
	  message.setElementsList(elementsList);
	  return message;
	 }
 
 /**
  * Prepare message to edit specific domain
 * @param type - the type of the action that server need to do
 * @param domainId - ID of domain
 * @param DomainName - the new domain name
 * @return message
 */
private Message prepareEditDomain(ActionType type, String domainId, String DomainName) {
	  Message message = new Message();
	  ArrayList < String > elementsList = new ArrayList < String > ();
	  elementsList.add(domainId);
	  elementsList.add(DomainName);
	  message.setType(type);
	  message.setElementsList(elementsList);
	  return message;
	 }
 
 /**
  * Prepare message to edit specific subject
 * @param type - the type of the action that server need to do
 * @param subjectId - ID of subject
 * @param subjectName - new subject name
 * @param domainId - new ID of domain
 * @return message
 */
private Message prepareEditSubject(ActionType type, String subjectId, String subjectName, String domainId) {
	  Message message = new Message();
	  ArrayList < String > elementsList = new ArrayList < String > ();
	  elementsList.add(subjectId);
	  elementsList.add(subjectName);
	  elementsList.add(domainId);
	  message.setType(type);
	  message.setElementsList(elementsList);
	  return message;
	 }
 
 /**
  * Prepare message to edit specific author
 * @param type - the type of the action that server need to do
 * @param AuthorId - ID of author
 * @param AuthorFirstName - new first name
 * @param AuthorLastName - new last name
 * @return message
 */
private Message prepareEditAuthor(ActionType type, String AuthorId, String AuthorFirstName, String AuthorLastName) {
	  Message message = new Message();
	  ArrayList < String > elementsList = new ArrayList < String > ();
	  elementsList.add(AuthorId);
	  elementsList.add(AuthorFirstName);
	  elementsList.add(AuthorLastName);
	  message.setType(type);
	  message.setElementsList(elementsList);
	  return message;
	 }

 
 /**
  * Prepare message to get domains with thier ID
 * @param type - the type of the action that server need to do
 * @return message
 */
public Message prepareGetDomainsWithId(ActionType type) {
	  Message message = new Message();
	  message.setType(type);
	  return message;
	 }

 /**
  * Prepare message to add book
 * @param type - the type of the action that server need to do
 * @param titleBook - title of book
 * @param authorsId2 - ID's of the authors
 * @param keywords - keywords of the book
 * @param language - language of the book
 * @param subjectsList2 - ID's of the subjects
 * @param tableOfContent - table of content of the book
 * @param summary - summary of the book
 * @param picture - picture of the book (base64 encoded)
 * @param price - price of the book
 * @return message
 */
public Message prepareAddBook(ActionType type, String titleBook, String authorsId2, String keywords, String language, String subjectsList2, String tableOfContent, String summary, String picture, String price) {
  Message message = new Message();
  ArrayList < String > elementsList = new ArrayList < String > ();
  elementsList.add(titleBook);
  elementsList.add(authorsId2);
  elementsList.add(keywords);
  elementsList.add(language);
  elementsList.add(subjectsList2);
  elementsList.add(tableOfContent);
  elementsList.add(summary);
  elementsList.add(picture);
  elementsList.add(price);
  message.setType(type);
  message.setElementsList(elementsList);
  return message;
 }
 
 /**
  * Prepare message to add domain
 * @param type - the type of the action that server need to do
 * @param domainName - name of domain
 * @return message
 */
	private Message prepareAddDomain(ActionType type, String domainName) {
		  Message message = new Message();
		  ArrayList < String > elementsList = new ArrayList < String > ();
		  elementsList.add(domainName);
		  message.setType(type);
		  message.setElementsList(elementsList);
		  return message;
	}
	
	/**
	 * Prepare message to add author
	 * @param type - the type of the action that server need to do
	 * @param AuthorFirstName - first name
	 * @param AuthorLastName - last name
	 * @return message
	 */
	private Message prepareAddAuthor(ActionType type, String AuthorFirstName, String AuthorLastName) {
		  Message message = new Message();
		  ArrayList < String > elementsList = new ArrayList < String > ();
		  elementsList.add(AuthorFirstName);
		  elementsList.add(AuthorLastName);
		  message.setType(type);
		  message.setElementsList(elementsList);
		  return message;
	}
	
	/**
	 * Prepare message to add subject
	 * @param type - the type of the action that server need to do
	 * @param subjectName - subject name
	 * @param subjectDomain - domain of the subject
	 * @return message
	 */
	private Message prepareAddSubject(ActionType type, String subjectName, String subjectDomain) {
		  Message message = new Message();
		  ArrayList < String > elementsList = new ArrayList < String > ();
		  elementsList.add(subjectName);
		  elementsList.add(subjectDomain);
		  message.setType(type);
		  message.setElementsList(elementsList);
		  return message;
	}
 
 /**
  * Prepare message to edit book
 * @param type - the type of the action that server need to do
 * @param Sn - the SN of the edited book
 * @param titleBook - new title book
 * @param authorsId2 - new ID's of authors
 * @param keywords - new keywords
 * @param language - new language
 * @param subjectsList2 - new subjects
 * @param tableOfContent - new table of content
 * @param summary - new summary
 * @param picture - new picture (base64 encoded)
 * @param price - new price
 * @return message
 */
public Message prepareEditBook(ActionType type, String Sn, String titleBook, String authorsId2, String keywords, String language, String subjectsList2, String tableOfContent, String summary, String picture, String price) {
	  Message message = new Message();
	  ArrayList < String > elementsList = new ArrayList < String > ();
	  elementsList.add(titleBook);
	  elementsList.add(authorsId2);
	  elementsList.add(keywords);
	  elementsList.add(language);
	  elementsList.add(subjectsList2);
	  elementsList.add(tableOfContent);
	  elementsList.add(summary);
	  elementsList.add(picture);
	  elementsList.add(price);
	  elementsList.add(Sn);
	  message.setType(type);
	  message.setElementsList(elementsList);
	  return message;
	 }

 /**
  * Prepare message to get all available authors
 * @param type - the type of the action that server need to do
 * @return message
 */
public Message prepareGetAuthors(ActionType type) {
  Message message = new Message();
  message.setType(type);
  return message;
 }
 
 /**
  * Prepare message to get authors of specific book
 * @param type - the type of the action that server need to do
 * @param bookSn - SN of book
 * @return message
 */
public Message prepareGetAuthors(ActionType type,String bookSn) {
	  Message message = new Message();
	  ArrayList < String > elementsList = new ArrayList < String > ();
	  elementsList.add(bookSn);
	  message.setType(type);
	  message.setElementsList(elementsList);
	  return message;
	 }
 
 /**
  * Prepare message to get language of specific book
 * @param type - the type of the action that server need to do
 * @param bookSn - SN of book
 * @return message
 */
public Message prepareGetLanguage(ActionType type,String bookSn) {
	  Message message = new Message();
	  ArrayList < String > elementsList = new ArrayList < String > ();
	  elementsList.add(bookSn);
	  message.setType(type);
	  message.setElementsList(elementsList);
	  return message;
	 }
 
/**
 * Prepare message to get table of content of specific book
* @param type - the type of the action that server need to do
* @param bookSn - SN of book
* @return message
*/
public Message prepareGetTableOfContent(ActionType type,String bookSn) {
	  Message message = new Message();
	  ArrayList < String > elementsList = new ArrayList < String > ();
	  elementsList.add(bookSn);
	  message.setType(type);
	  message.setElementsList(elementsList);
	  return message;
	 }
 

/**
 * Prepare message to get subjects of specific book
* @param type - the type of the action that server need to do
* @param bookSn - SN of book
* @return message
*/
public Message prepareGetSubjects(ActionType type,String bookSn) {
		  Message message = new Message();
		  ArrayList < String > elementsList = new ArrayList < String > ();
		  elementsList.add(bookSn);
		  message.setType(type);
		  message.setElementsList(elementsList);
		  return message;
		 }

 /**
  * Prepare message to get all available subjects
 * @param type - the type of the action that server need to do
 * @return message
 */
public Message prepareGetSubjects(ActionType type) {
  Message message = new Message();
  message.setType(type);
  return message;
 }

 /**
  * Prepare message to delete specific book
 * @param type - the type of the action that server need to do
 * @param sn - The SN of book
 * @return message
 */
public Message prepareDeleteBook(ActionType type, String sn) {
  Message message = new Message();
  ArrayList < String > elementsList = new ArrayList < String > ();
  elementsList.add(sn);
  message.setType(type);
  message.setElementsList(elementsList);
  return message;
 }


 /**
  * Prepare message to hide/unhide specific book
 * @param type - the type of the action that server need to do
 * @param sn - The SN of book
 * @param hide - hide or unhide operation
 * @return message
 */
public Message prepareHideBook(ActionType type, String sn, String hide) {
  Message message = new Message();
  ArrayList < String > elementsList = new ArrayList < String > ();
  elementsList.add(sn);
  elementsList.add(hide);
  message.setType(type);
  message.setElementsList(elementsList);
  return message;
 }

 /**
  * Prepare message create alert popup
 * @param type - the type of the action that server need to do
 * @param errorCode - the error to show
 */
public void actionOnError(ActionType type, String errorCode) {

  Alert alert = new Alert(AlertType.INFORMATION);
  alert.setTitle("Error");
  alert.setHeaderText(null);
  alert.setContentText(errorCode);
  alert.showAndWait();
  if (type == ActionType.TERMINATE) {
   Platform.exit();
   System.exit(1);
  }
  if (type == ActionType.CONTINUE)
   return;
 }


 /**
  * inner class that contains propery strings of book attributes
 * @author Idan
 *
 */
public static class PropertyBook {

  /**
 * String Property that contains SN of book.
 */
private final SimpleStringProperty bookSn;
  
  /**
 * String Property that contains title of book.
 */
private final SimpleStringProperty bookTitle;
  
  /**
 * String Property that contains keywords of book.
 */
private final SimpleStringProperty bookKeywords;
  
  /**
 * String Property that contains hide state (yes or no) of book.
 */
private final SimpleStringProperty bookHide;
  
  /**
 * String Property that contains author ID of book.
 */
private final SimpleStringProperty authorId;
  
  /**
 * String Property that contains author name of book.
 */
private final SimpleStringProperty authorName;
  
  /**
 * String Property that contains summary of book.
 */
private final SimpleStringProperty bookSummary;
  
  /**
 * String Property that contains image (base64 encoded) of book.
 */
private final SimpleStringProperty bookImage;
  
  /**
 * String Property that contains price of book.
 */
private final SimpleStringProperty bookPrice;
  

  /**
   * PropertyBook constructor store the data.
 * @param bookSn - Gets the SN of book.
 * @param bookTitle - Gets the title of book.
 * @param bookKeywords - Gets the keywords of book.
 * @param bookHide - Gets the hide state of book.
 * @param authorId - Gets the author ID of book.
 * @param authorName - Gets the author name of book.
 * @param bookSummary - Gets the summary of book.
 * @param bookImage - Gets the image of book.
 * @param bookPrice - Gets the price of book.
 */
private PropertyBook(String bookSn, String bookTitle, String bookKeywords, String bookHide, String authorId, String authorName, String bookSummary, String bookImage, String bookPrice) {
   this.bookSn = new SimpleStringProperty(bookSn);
   this.bookTitle = new SimpleStringProperty(bookTitle);
   this.bookKeywords = new SimpleStringProperty(bookKeywords);
   this.bookHide = new SimpleStringProperty(bookHide);
   this.authorId = new SimpleStringProperty(authorId);
   this.authorName = new SimpleStringProperty(authorName);
   this.bookSummary = new SimpleStringProperty(bookSummary);
   this.bookImage = new SimpleStringProperty(bookImage);
   this.bookPrice = new SimpleStringProperty(bookPrice);
  }

  /**
   * Getter for SN.
 * @return
 */
public String getBookSn() {
   return bookSn.get();
  }

  /**
   * Getter for title.
 * @return
 */
public String getBookTitle() {
   return bookTitle.get();
  }

  /**
   * Getter for keywords.
 * @return
 */
public String getBookKeywords() {
   return bookKeywords.get();
  }

  /**
   * Getter for hide state.
 * @return
 */
public String getBookHide() {
   return bookHide.get();
  }

  /**
   * Getter for author ID.
 * @return
 */
public String getAuthorId() {
   return authorId.get();
  }

  /**
   * Getter for author name.
 * @return
 */
public String getAuthorName() {
   return authorName.get();
  }

  /**
   * Getter for summary.
 * @return
 */
public String getBookSummary() {
   return bookSummary.get();
  }

  /**
   * Getter for image.
 * @return
 */
public String getBookImage() {
   return bookImage.get();
  }
  
  /**
   * Getter for price.
 * @return
 */
public String getBookPrice() {
	   return bookPrice.get();
	  }


  /**
   * Getter for SN.
 * @param bookSn
 */
public void setBookSn(String bookSn) {
   this.bookSn.set(bookSn);
  }

  /**
   * Setter for title
 * @param bookTitle
 */
public void setBookTitle(String bookTitle) {
   this.bookTitle.set(bookTitle);
  }

  /**
   * Setter for keywords
 * @param bookKeywords
 */
public void setBookKeywords(String bookKeywords) {
   this.bookKeywords.set(bookKeywords);
  }

  /**
   * Setter for hide state
 * @param bookHide
 */
public void setBookHide(String bookHide) {
   this.bookHide.set(bookHide);
  }

  /**
   * Setter for author ID
 * @param authorId
 */
public void setAuthorId(String authorId) {
   this.authorId.set(authorId);
  }

  /**
   * Setter for author name
 * @param authorName
 */
public void setAuthorName(String authorName) {
   this.authorName.set(authorName);
  }

  /**
   * Setter for summary
 * @param bookSummary
 */
public void setBookSummary(String bookSummary) {
   this.bookSummary.set(bookSummary);
  }

  /**
   * Setter for image
 * @param bookImage
 */
public void setBookImage(String bookImage) {
   this.bookImage.set(bookImage);
  }
  
  /**
   * Setter for price
 * @param bookPrice
 */
public void setBookPrice(String bookPrice) {
   this.bookPrice.set(bookPrice);
  }


 }

/**
 * inner class that contains propery strings of domain attributes
* @author Idan
*
*/
 public static class PropertyDomain {

	  /**
	  * String Property that contains ID of domain
	  */
	private final SimpleStringProperty domainId;
	/**
	  * String Property that contains domain name
	  */
	private final SimpleStringProperty domainName;


	  /**
	   * PropertyDomain constructor store the data.
	 * @param domainId - Gets the ID of domain.
	 * @param domainName - Gets the name of domain
	 */
	private PropertyDomain(String domainId, String domainName) {
	   this.domainId = new SimpleStringProperty(domainId);
	   this.domainName = new SimpleStringProperty(domainName);
	  }


	  /**
	   * Getter for domain ID.
	 * @return
	 */
	public String getDomainId() {
		   return domainId.get();
	  }
	  
	  /**
	   * Getter for domain name. 
	 * @return
	 */
	public String getDomainName() {
		   return domainName.get();
	  }

	  /**
	   * Setter for domain ID.
	 * @param domainId
	 */
	public void setDomainId(String domainId) {
	   this.domainId.set(domainId);
	  }
	  
	  /**
	   * Setter for domain name.
	 * @param domainName
	 */
	public void setDomainName(String domainName) {
		   this.domainName.set(domainName);
	  }

	 }
 
 
 /**
  * inner class that contains propery strings of subjects attributes
 * @author Idan
 *
 */
 public static class PropertySubject {

	  /**
	 * String Property that contains ID of subject 
	 */
	private final SimpleStringProperty subjectId;
	  
	  /**
	 * String Property that contains subject name
	 */
	private final SimpleStringProperty subjectName;
	  
	  /**
	 * String Property that contains the domain of subject
	 */
	private final SimpleStringProperty subjectDomain;


	  /**
	   * PropertySubject constructor store the data.
	 * @param subjectId - Gets the ID of subject.
	 * @param subjectName - Gets the name of subject.
	 * @param subjectDomain - Gets the domain of subject.
	 */
	private PropertySubject(String subjectId, String subjectName, String subjectDomain) {
	   this.subjectId = new SimpleStringProperty(subjectId);
	   this.subjectName = new SimpleStringProperty(subjectName);
	   this.subjectDomain = new SimpleStringProperty(subjectDomain);
	  }


	  /**
	   * Getter for subject ID.
	 * @return
	 */
	public String getSubjectId() {
		   return subjectId.get();
	  }
	  
	  /**
	   * Getter for subject name.
	 * @return
	 */
	public String getSubjectName() {
		   return subjectName.get();
	  }
	  
	  /**
	   * Getter for domain of subject.
	 * @return
	 */
	public String getSubjectDomain() {
		   return subjectDomain.get();
	  }

	  /**
	   * Setter for subject ID.
	 * @param subjectId
	 */
	public void setSubjectId(String subjectId) {
	   this.subjectId.set(subjectId);
	  }
	  
	  /**
	   * Setter for subject name.
	 * @param subjectName
	 */
	public void setSubjectName(String subjectName) {
		   this.subjectName.set(subjectName);
	  }
	  
	  /**
	   * Setter for domain of subject.
	 * @param subjectDomain
	 */
	public void setSubjectDomain(String subjectDomain) {
		   this.subjectDomain.set(subjectDomain);
	  }
	  

	 }
 
 /**
  * inner class that contains propery strings of author attributes
 * @author Idan
 *
 */
 public static class PropertyAuthor {

	 /**
	  * String Property that contains ID of author
	  */
	  private final SimpleStringProperty authorId;
	  
	  /**
	 * String Property that contains first name of author
	 */
	private final SimpleStringProperty authorFirstName;
	  
	  /**
	 * String Property that contains last name of author
	 */
	private final SimpleStringProperty authorLastName;



	  /**
	   * PropertyAuthor constructor store the data.
	 * @param authorId
	 * @param authorFirstName
	 * @param authorLastName
	 */
	private PropertyAuthor(String authorId, String authorFirstName, String authorLastName) {
	   this.authorId = new SimpleStringProperty(authorId);
	   this.authorFirstName = new SimpleStringProperty(authorFirstName);
	   this.authorLastName = new SimpleStringProperty(authorLastName);
	  }


	  /**
	   * Getter for author ID.
	 * @return
	 */
	public String getAuthorId() {
		   return authorId.get();
	  }
	  
	  /**
	   * Getter for author first name.
	 * @return
	 */
	public String getAuthorFirstName() {
		   return authorFirstName.get();
	  }
	  
	  /**
	   * Getter for author last name.
	 * @return
	 */
	public String getAuthorLastName() {
		   return authorLastName.get();
	  }

	  /**
	   * Setter for author ID.
	 * @param authorId
	 */
	public void setDomainId(String authorId) {
	   this.authorId.set(authorId);
	  }
	  
	  /**
	   * Setter for author first name.
	 * @param authorName
	 */
	public void setAuthorFirstName(String authorFirstName) {
		   this.authorFirstName.set(authorFirstName);
	  }
	  
	  /**
	   * Setter for author last name.
	 * @param authorName
	 */
	public void setAuthorLastName(String authorLastName) {
		   this.authorLastName.set(authorLastName);
	  }

	 }
 

}

/** This class makes sure the information from the server was received successfully.
 * @author itain
 */
class BookManagermentGetBookListRecv extends Thread{
	
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
class BookManagermentGetAuthorsRecv extends Thread{
	
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
class BookManagermentGetSubjectsRecv extends Thread{
	
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
class BookManagermentGetBookAuthorsRecv extends Thread{
	
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
class BookManagermentGetBookAuthorsRecv2 extends Thread{
	
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
class BookManagermentGetBookSubjectsRecv extends Thread{
	
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
class BookManagermentGetSubjectsRecv2 extends Thread{
	
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
class BookManagermentGetBookLanguageRecv extends Thread{
	
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
class BookManagermentGetBookTocRecv extends Thread{
	
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
class BookManagermentGetAuthorsRecv2 extends Thread{
	
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
class BookManagermentGetSubjectsRecv3 extends Thread{
	
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
class BookManagermentGetBookListRecv2 extends Thread{
	
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
class BookManagermentGetBookListRecv3 extends Thread{
	
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
class BookManagermentGetDomainsWithIdRecv extends Thread{
	
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
class BookManagermentGetDomainsWithIdRecv2 extends Thread{
	
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
class BookManagermentGetDomainsWithIdRecv3 extends Thread{
	
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
class BookManagermentGetNumberBookAtDomainRecv extends Thread{
	
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
class BookManagermentGetDomainsWithIdRecv4 extends Thread{
	
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
class BookManagermentGetSubjectsInfoRecv extends Thread{
	
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
class BookManagermentGetDomainsWithIdRecv5 extends Thread{
	
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
class BookManagermentGetDomainsWithIdRecv6 extends Thread{
	
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
class BookManagermentGetNumberBookAtSubjectRecv extends Thread{
	
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
class BookManagermentGetSubjectsInfoRecv2 extends Thread{
	
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
class BookManagermentGetSubjectsInfoRecv3 extends Thread{
	
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
class BookManagermentGetSubjectsInfoRecv4 extends Thread{
	
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
class BookManagermentGetAuthorsRecv3 extends Thread{
	
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
class BookManagermentGetAuthorsRecv4 extends Thread{
	
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
class BookManagermentGetBookListRecv4 extends Thread{
	
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
class BookManagermentGetNumberBookOfAuthorRecv extends Thread{
	
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
class BookManagermentGetAuthorsRecv5 extends Thread{
	
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
class BookManagermentGetAuthorsRecv6 extends Thread{
	
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
class BookManagermentGetDomainsRecv extends Thread{
	
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


