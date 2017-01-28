package control;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

import control.UserReportController.Purchase;
import entity.Author;
import entity.Book;
import entity.Message;
import entity.SearchBookResult;
import entity.User;
import enums.ActionType;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * @author sagivm
 *Displays book popularity report containing book's data and the number of 
 *purchases for this book. The data is  displayed in Tableview form.
 *The operator has a choice to display this book against all of the books 
 *in the Db or against books with the same domains of his choosing.
 */
public class BookPopularityReportController implements Initializable {

	/**
	 * Main table in the Popularity report screen
	 */

	@FXML
	private TableView<Popularity> table;

	/**
	 * Data returned from Db will be inserted here
	 */
	public static ArrayList<String> data;

	/**
	 * Book id column in the table
	 */
	@FXML
	private TableColumn<Popularity, Integer> bookIdColumn;
	/**
	 * Title column in the table
	 */
	@FXML
	private TableColumn<Popularity, String> titleColumn;
	/**
	 * Author column in the table
	 */
	@FXML
	private TableColumn<Popularity, String> authorColumn;

	/**
	 * Language column in the table
	 */
	@FXML
	private TableColumn<Popularity, String> languageColumn;
	/**
	 * Number of purchases column in the table
	 */
	@FXML
	private TableColumn<Popularity, Integer> purchaseColumn;

	/**
	 * If chosen displays book against all books
	 */
	@FXML
	private RadioButton allBooksRadio;
	/**
	 * If chosen displays book against selected domain
	 */
	@FXML
	private RadioButton domainRadio;
	/**
	 * A list of all the Domains that the book have
	 */
	@FXML
	private ListView<String> domains;

	/**
	 * A list of all the Domains names that the book have
	 */
	@FXML
	public static ArrayList<String> domainsdata;
	/**
	 * Displays Book title
	 */
	@FXML
	private Label titleLabel;
	/**
	 * Displays Book title
	 */
	@FXML
	private SearchBookResult SelectedBook;
	/**
	 * group of the radio buttons
	 */
	@FXML
	private final ToggleGroup group = new ToggleGroup();
	/**
	 * ArrayList containing the data in Popularity form
	 */
	private ArrayList<Popularity> list;
	/**
	 * ArrayList containing the data in Popularity form acording to the
	 * selection
	 */
	private ArrayList<Popularity> specificList;
	/**
	 * Arraylist containing the prices for all the books 
	 */
	public static ArrayList<String> priceList;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// test
		this.SelectedBook = BookPageController.searchedBookPage;
		//
		//initializeLabel();
		initializeDomains();
		initializeTable();
		initializeprice();
		// TODO Auto-generated method stub

	}

	/**
	 * Gets book's prices from the DB
	 */
	private void initializeprice() {
		ArrayList<String> elementsList = new ArrayList<String>();
		elementsList.add(String.valueOf(SelectedBook.getBookSn()));
		Message message = new Message(ActionType.GET_TOTAL_PRICE, elementsList);
		try {

			ClientController.clientConnectionController.sendToServer(message);

		} catch (IOException e) {

			// actionToDisplay("Warning",ActionType.CONTINUE,GeneralMessages.UNNKNOWN_ERROR_DURING_SEND);
		}
		
		  //itai
		  Platform.runLater(new Runnable() {
				@Override
				public void run() {
					BookPopularityReportGetTotalPriceRecv recv_getTotalPrice = new BookPopularityReportGetTotalPriceRecv();
					recv_getTotalPrice.start();
						synchronized (recv_getTotalPrice) {
							try{
								recv_getTotalPrice.wait();
							}catch(InterruptedException e){
								e.printStackTrace();
							}
						}
				}});

	}
	/**
	 * Initialize the labels that are stored in the page
	 */
	private void initializeLabel() {
		titleLabel.setText(SelectedBook.getBookSn() + ") " + SelectedBook.getBookTitle() + " Popularity Report");

	}

	/**
	 * Fill Domain list with the domains of the book
	 */
	private void initializeDomains() {
		ArrayList<String> elementsList = new ArrayList<String>();
		elementsList.add(String.valueOf(SelectedBook.getBookSn()));
		Message message = new Message(ActionType.GETDOMAINSSPECIFIC, elementsList);
		try {

			ClientController.clientConnectionController.sendToServer(message);

		} catch (IOException e) {

			// actionToDisplay("Warning",ActionType.CONTINUE,GeneralMessages.UNNKNOWN_ERROR_DURING_SEND);
		}
		
		  //itai
		  Platform.runLater(new Runnable() {
				@Override
				public void run() {
					BookPopularityReportGetDomainSpecificRecv recv_getDomainSpecific = new BookPopularityReportGetDomainSpecificRecv();
					recv_getDomainSpecific.start();
						synchronized (recv_getDomainSpecific) {
							try{
								recv_getDomainSpecific.wait();
							}catch(InterruptedException e){
								e.printStackTrace();
							}
						}
				}});
		
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {

				try {
					ObservableList<String> items = FXCollections.observableArrayList(domainsdata);
					domains.setItems(items);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	/**
	 * Fill Table with the data that
	 */
	private void initializeTable() {
		ArrayList<String> elementsList = new ArrayList<String>();
		Message message = new Message(ActionType.POPULARITYREPORT, elementsList);
		try {
			ClientController.clientConnectionController.sendToServer(message);

		} catch (IOException e) {

			// actionToDisplay("Warning",ActionType.CONTINUE,GeneralMessages.UNNKNOWN_ERROR_DURING_SEND);
		}
		
		  //itai
		  Platform.runLater(new Runnable() {
				@Override
				public void run() {
					BookPopularityReportPopularityReportRecv recv_popularityReport = new BookPopularityReportPopularityReportRecv();
					recv_popularityReport.start();
						synchronized (recv_popularityReport) {
							try{
								recv_popularityReport.wait();
							}catch(InterruptedException e){
								e.printStackTrace();
							}
						}
				}});
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {

				try {
					allBooksRadio.setSelected(true);
					displaySettings(new ActionEvent());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		});

	}

	/**
	 * After selecting the list that will be displayed ,books with 2 or more
	 * domains are merged into one single row
	 */
	private void mergeDuplicatesDomain() {
		boolean dup[] = new boolean[specificList.size()];
		for (int i = 0; i < dup.length; i++)
			dup[i] = false;
		for (int i = 0; i < specificList.size(); i++) {
			for (int j = i + 1; j < specificList.size(); j++) {
				if (specificList.get(i).getId()==(specificList.get(j).getId())
						&& !specificList.get(i).getDomain().equals(specificList.get(j).getDomain()) && i != j) {
					specificList.get(i)
							.setDomain(specificList.get(i).getDomain() + " & " + specificList.get(j).getDomain());
					specificList.remove(j);
				}
			}
		}

	}
	/**
	 * After selecting the list that will be displayed ,books with 2 or more
	 * authors are merged into one single row
	 */
	private void mergeDuplicatesAuthors() {
		boolean dup[] = new boolean[list.size()];
		for (int i = 0; i < dup.length; i++)
			dup[i] = false;
		ArrayList<Purchase> temp = new ArrayList<Purchase>();
		for (int i = 0; i < list.size(); i++) {
			for (int j = i + 1; j < list.size(); j++) {
				if (list.get(i).getId()==list.get(j).getId()
						&& list.get(i).getDomain().equals(list.get(j).getDomain())
						&& !list.get(i).getAuthor().equals(list.get(j).getAuthor()) && i != j) {
					list.get(i).setAuthor(list.get(i).getAuthor() + " & " + list.get(j).getAuthor());
					list.remove(j);
				}
			}
		}
	}

	/**
	 * Fill up the tables depending on the selected preferences
	 */
	@FXML
	private void displaySettings(ActionEvent event) {
		if(list!=null)
			list.clear();
		removeAllRows();
		arrangelist();
		mergeDuplicatesAuthors();
		/*
		for(int i=0;i<list.size();i++)
			System.out.println(list.get(i).getTitle()+" "+list.get(i).getDomain());
			*/
		ObservableList<String> selectedDomains = domains.getSelectionModel().getSelectedItems();
		/*
		System.out.println(selectedDomains.get(0));
		*/
		removeAllRows();
		if (domainRadio.isSelected()) {
			displaydomains(selectedDomains);
		}
		if (allBooksRadio.isSelected())
			displayallbooks();

	}

	/**
	 * Clear table for change settings
	 */
	public void removeAllRows() {
		for (int i = 0; i < table.getItems().size(); i++) {
			table.getItems().clear();
		}
	}

	/**
	 * Calling displaybooks() declaring that we will display all of the books
	 */
	private void displayallbooks() {
		specificList=new ArrayList<Popularity>(list);
		displaybooks();
	}
//
	/**
	 * Calling displaybooks() declaring that we will display all the books that
	 * have the selected domain
	 */
	private void displaydomains(ObservableList<String> selectedDomains) {
		ArrayList<String> dom = new ArrayList<String>(selectedDomains);
		//System.out.println(list.size());
		for (int i = 0; i < list.size(); i++) {
			if (selectedDomains.contains(list.get(i).domain)) {
				specificList.add(list.get(i));
			}
		}
		displaybooks();

	}

	/**
	 * Generates the list that will be displayed base on the settings
	 * selected by the manager
	 */
	private void displaybooks() {
		mergeDuplicatesDomain();
		
		//Double merge duplicate discrete
		for(int i=0;i<specificList.size();i++)
		{
			for(int j=0;j<specificList.size();j++)
				if(specificList.get(i).getId()==specificList.get(j).getId()&&i!=j)
					specificList.remove(j);
		}
		/*for (int i=0;i<priceList.size();i++)
			System.out.println(priceList.get(i));*/
		setPrice();
		//System.out.println();
		for (int i=0;i<priceList.size();i++)
			//System.out.println(specificList.get(i).getPurchase());
		purchaseColumn.setSortType(TableColumn.SortType.DESCENDING);
		bookIdColumn.setCellValueFactory(new PropertyValueFactory<Popularity, Integer>("id"));
		titleColumn.setCellValueFactory(new PropertyValueFactory<Popularity, String>("title"));
		authorColumn.setCellValueFactory(new PropertyValueFactory<Popularity, String>("author"));
		languageColumn.setCellValueFactory(new PropertyValueFactory<Popularity, String>("language"));
		purchaseColumn.setCellValueFactory(new PropertyValueFactory<Popularity, Integer>("purchase"));
		ObservableList<Popularity> items = FXCollections.observableArrayList(specificList);
		table.setItems(items);
		table.getSortOrder().add(purchaseColumn);
		specificList.clear();
	}
	/**
	 * Merges the list that will be displayed with the specific list that will be displayed 
	 */
	private void setPrice() {
		String split[];

		for (int i = 0; i < priceList.size(); i++) {
			split = priceList.get(i).split("\\^");
			for (int j = 0; j < specificList.size(); j++) {
				if (split[0].equals(String.valueOf(specificList.get(j).getId()))) {
					specificList.get(j).setPurchase(Integer.valueOf(split[1]));

				}
			}
		}

	}

	/**
	 * Transfer the list from the DB to ArrayList<Purchase>
	 */
	private void arrangelist() {
		list = new ArrayList<Popularity>();
		String datasplit[] = new String[5];
		for (int i = 0; i < data.size(); i++) {
			datasplit = data.get(i).split("\\^");
			list.add(new Popularity(datasplit));
		}

	}

	/**
	 * Contains all the relevant data for the table
	 * 
	 * @author sagivm
	 *
	 */
	public class Popularity {
		/**
		 * Book's id
		 */
		private int id;
		/**
		 * Book's title
		 */
		private String title;
		/**
		 * Book's author
		 */
		private String author;
		/**
		 * Book's language
		 */
		private String language;
		/**
		 * Book's #purchase
		 */
		private int purchase;
		/**
		 * Book's domain
		 */
		private String domain;

		/**
		 * Constructor
		 * 
		 * @param split
		 *            String array containing the 6 attributes of Purchase in
		 *            the order id,title,author,language,#purchase and domain
		 */
		public Popularity(String split[]) {
			this.id = Integer.valueOf(split[0]);
			this.title = new String(split[1]);
			this.author = new String(split[2]);
			this.language = new String(split[3]);
			this.purchase = 0;
			this.domain = split[4];
		}

		/**
		 * Getter
		 * @return book's Id
		 */
		public int getId() {
			return id;
		}

		/**
		 * Setter
		 * sets bookId
		 * @param id The id of the book
		 */
		public void setId(int id) {
			this.id = id;
		}

		/**
		 * Getter
		 * @return book's title
		 */
		public String getTitle() {
			return title;
		}

		/**
		 * Setter
		 * sets book's title
		 * @param title The book's title
		 */
		public void setTitle(String title) {
			this.title = title;
		}
		/**
		 * Getter
		 * @return book's author
		 */
		public String getAuthor() {
			return author;
		}
		/**
		 * Setter
		 * sets book's author
		 * @param author The book's author
		 */
		public void setAuthor(String author) {
			this.author = author;
		}

		/**
		 * Getter
		 * @return book's language
		 */
		public String getLanguage() {
			return language;
		}
		/**
		 * Setter
		 * sets book's language
		 * @param language The book's language
		 */
		public void setLanguage(String language) {
			this.language = language;
		}

		/**
		 * Getter
		 * @return the number of purchases for each book
		 */
		public int getPurchase() {
			return purchase;
		}
		/**
		 * Setter
		 * sets number of purchases of the book
		 * @param purchase The of purchases of the book
		 */
		public void setPurchase(int purchase) {
			this.purchase = purchase;
		}

		/**
		 * Getter
		 * @return book's domain
		 */
		public String getDomain() {
			return domain;
		}
		/**
		 * Setter
		 * sets book's domain
		 * @param domain The book's domain
		 */
		public void setDomain(String domain) {
			this.domain = domain;
		}
	}

}


/** This class makes sure the information from the server was received successfully.
 * @author itain
 */
class BookPopularityReportGetTotalPriceRecv extends Thread{
	
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
class BookPopularityReportGetDomainSpecificRecv extends Thread{
	
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
class BookPopularityReportPopularityReportRecv extends Thread{
	
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

