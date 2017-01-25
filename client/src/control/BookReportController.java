package control;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import entity.Message;
import entity.SearchBookResult;
import enums.ActionType;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

/**
 * @author sagivm
 *Displays book's number of searches and purchases for each date in the book's 
 *life span displayed in an histographic graph
 */
public class BookReportController implements Initializable {

	/**
	 * Title containing the name of the book
	 */
	@FXML
	private Label titleLabel;
	/**
	 * The book that the report will be done on
	 */
	@FXML
	private SearchBookResult selectedBook;
	/**
	 * The data that was retrieved from the DB
	 */
	public static ArrayList<String> data;
	/**
	 * BarChart to display the date
	 */
	@FXML private BarChart<String, Integer> barChart;
	/**
	 * Search bar containing the number of searches for a book for a specific date
	 */
	@FXML private XYChart.Series<String, Integer> search ;
	/**
	 * Purchase bar containing the number of searches for a book for a specific date
	 */
	@FXML private XYChart.Series<String, Integer> purchase ;
	/**
	 * A list containing the data in BookByDate form
	 */
	private ArrayList<BookByDate> list;
	/**
	 * Date axis in the bar chart
	 */
	@FXML
	private CategoryAxis dateAxis;
	/**
	 * Numbers axis in the bar chart
	 */
	@FXML
	private NumberAxis numbersAxis;
	/* (non-Javadoc)
	 * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.selectedBook=BookPageController.searchedBookPage;
		initializeChart();
		// TODO Auto-generated method stub

	}
	

	/**
	 * Send a request to the DB For the relevant fields for the chart.
	 * Forwards to receive for displaying
	 */
	private void initializeChart() {
		ArrayList<String> elementsList = new ArrayList<String>();
		elementsList.add(String.valueOf(selectedBook.getBookSn()));
		System.out.println(selectedBook.getBookSn());
		Message message = new Message(ActionType.BOOKREPORT, elementsList);
		try {
			ClientController.clientConnectionController.sendToServer(message);

		} catch (IOException e) {

			// actionToDisplay("Warning",ActionType.CONTINUE,GeneralMessages.UNNKNOWN_ERROR_DURING_SEND);
		}
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				BookReportRecv recv_bookReport = new BookReportRecv();
				recv_bookReport.start();
				synchronized (recv_bookReport) {
					try{
						recv_bookReport.wait();
					}catch(InterruptedException e){
						e.printStackTrace();
					}
					receive();
				}
			}});
		
		//receive();
	}

	/**
	 * Receive the data arrange it in BookByDate form and transfer to displayChart
	 */
	private void receive() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {

				try {
					System.out.println(data.get(0));
					
					displayChart();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			
		});
		
	}
	/**
	 * Fill the Chart bars with the relevant data containing number of searches and purchases for each date
	 */
	@SuppressWarnings("unchecked")
	private void displayChart() {
		arrangelist();
		search=createSearchDate();
		search.setName("Search");
		
		purchase=createPurchasehDate();
		purchase.setName("Purchase");
		barChart.getData().addAll(search,purchase);
	}
	/**
	 * Transfer Data from the Db into search series
	 * @return series filled with search counters per day
	 */
	private XYChart.Series<String, Integer> createSearchDate() {
        XYChart.Series<String,Integer> series = new XYChart.Series<String,Integer>();

        for (int i = 0; i < list.size(); i++) {
            XYChart.Data<String, Integer> searchData = new XYChart.Data<String,Integer>(list.get(i).getDate(),list.get(i).getSearch());
            series.getData().add(searchData);
        }

        return series;
    }
	/**
	 * Transfer Data from the Db into purchase series
	 * @return series filled with purchase counters per day
	 */
	private XYChart.Series<String, Integer> createPurchasehDate() {
        XYChart.Series<String,Integer> series = new XYChart.Series<String,Integer>();

        for (int i = 0; i < list.size(); i++) {
            XYChart.Data<String, Integer> searchData = new XYChart.Data<String,Integer>(list.get(i).getDate(),list.get(i).getPurchase());
            series.getData().add(searchData);
        }

        return series;
    }
	/**
	 * Transfer the list from the DB to ArrayList<BookByDate> 
	 */
	private void arrangelist()
	{
		list=new ArrayList<BookByDate>();
		String datasplit[]=new String[3];
		for(int i=0;i<data.size();i++)
		{
			datasplit=data.get(i).split("\\^");
			list.add(new BookByDate(datasplit));
		}
		
	}
	/**
	 * Data type for containing the data from DB
	 * @author sagivm
	 *
	 */
	class BookByDate
	{
		/**
		 * Date
		 */
		private String date;
		/**
		 * number of searches for the book on a specific date
		 */
		private int search;
		/**
		 * number of searches for the book on a specific date
		 */
		private int purchase;
		/**
		 * constructor
		 * @param split String array containing the 6 attributes of Purchase in the order
		 * date,search,purchase
		 */
		public BookByDate(String split[])
		{
			this.date=split[0];
			this.search=Integer.parseInt(split[1]);
			this.purchase=Integer.parseInt(split[2]);
		}
		
		/**
		 * Constructor
		 */
		public BookByDate() {
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Date's Getter
		 * @return A specific date 
		 */
		public String getDate() {
			return date;
		}

		/**
		 * Date's Setter
		 * @param date - A specific date
		 */
		public void setDate(String date) {
			this.date = date;
		}
		/**
		 * Search's Getter
		 * @return number of searches for a specific date
		 */
		public int getSearch() {
			return search;
		}
		/**
		 * Search's Setter
		 * @param date - number of searches for a specific date 
		 */
		public void setSearch(int search) {
			this.search = search;
		}
		/**
		 * Purchases's Getter
		 * @return number of purchases for a specific date
		 */
		public int getPurchase() {
			return purchase;
		}
		/**
		 * Purchases's Setter
		 * @param date - number of purchases for a specific date
		 */
		public void setPurchase(int purchase) {
			this.purchase = purchase;
		}
	}

}


/** This class makes sure the information from the server was received successfully.
 * @author itain
 */
class BookReportRecv extends Thread{
	
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
