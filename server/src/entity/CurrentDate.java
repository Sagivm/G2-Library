package entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.mysql.jdbc.util.ResultSetUtil;

import control.DatabaseController;
//
/**
 * @author sagivm
 *Generates each passing day and each time the Db is starting to run new tuple for each book in 
 *book by date table representing book's stats in this day
 */
public class CurrentDate   {
	/**
	 * Date format for create new rows in sql table Book_by_date
	 */
	private String date;
	/**
	 * Constructor
	 * Calls the relevant functions for generating the new tuples
	 */
	public CurrentDate()
	{
		dateInitialize();
		newDay();
	}
	/**
	 * Checks if the date was already initialize in this session of the Db
	 * and if not checks the current date and will follow to generate tuples
	 */
	private void dateInitialize() {
		// fix multiply occurrence per date
		checkLastCrash();
		if (date == null) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("HH");
			this.date = (String) dateFormat.format(new Date());
			if(checkLastCrash())
				createNewDay();
		}
	}
private boolean checkLastCrash() {
	SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	String curDate = (String) dateFormat.format(new Date());
		try {
			ResultSet rs=DatabaseController.searchInDatabase(
					"SELECT * "
					+ "FROM book_by_date");
			while(rs.next())
			{
				if(rs.getString(2).equals(curDate))
					return false;			
			}
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
		
	}
	//
	/**
	 *Checks if the day has changed and if was 
	 *create new rows in sql table book by date for each book 
	 */
	private void newDay() {
		String date;
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH");
		date = (String) dateFormat.format(new Date());
		// compare to return 0 if equal
		if ((this.date.compareTo(date)) != 0 && date.compareTo("00") == 0) {
			endSubscriptoin();
			createNewDay();
			this.date=date;
		}

	}

	private void endSubscriptoin() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);    
        String yesterday=dateFormat.format(cal.getTime());
        try {
			DatabaseController.updateDatabase(""
					+ "UPDATE book_by_date "
					+ "SET accountType=Intrested and accountStatus=Standard and credit=0 "
					+ "WHERE endSubscription='"+yesterday+"';");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	/**
	 * Insert new rows in sql table book by date for each book 
	 */
	
	private void createNewDay()
	{
		ResultSet rs = null;
		try {
		rs=DatabaseController.searchInDatabase("SELECT * FROM books;");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			while(rs.next())
			{
				insertBookDateRow(rs,0,0);	
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**\
	 * Insert a single row to book by date with the current date
	 * @param rs- ResultSet of book by date  table with sn in the first index of rs
	 * @param search - number of searches for the new book
	 * @param purchase-number of purchases for the new book
	 * @throws SQLException
	 */
	public static void insertBookDateRow(ResultSet rs,int search,int purchase) throws SQLException
	{
		Date Currentdate=new Date();
		SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy");
		String date=(String)dateFormat.format(Currentdate);
		DatabaseController.addToDatabase("INSERT INTO book_by_date VALUES ("+rs.getInt(1)+",'"+date+"',"+search+","+purchase+");");
	}
	/**\
	 * Inc a single search row of book by date with the current date
	 * @param bookId- id of book
	 * @throws SQLException
	 */
	public static int IncSearchBookDateRow(String bookId) throws SQLException
	{
		Date Currentdate=new Date();
		SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy");
		String date=(String)dateFormat.format(Currentdate);
		Statement stmt = DatabaseController.connection.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT searchCount FROM book_by_date WHERE bookId='" + bookId + "' and date='"+date+"';");
		int count=0;
		while(rs.next()) //will do only once
			 count = Integer.parseInt(rs.getString(1));
		String serachCount=Integer.toString(count+1);
		DatabaseController.updateDatabase("UPDATE book_by_date SET searchCount='" + serachCount + "' WHERE bookId=+'"+bookId+"' and date='"+date+"';");
		return count;
	}
	/**\
	 * Inc a single purchase row of book by date with the current date
	 * @param bookId- id of book
	 * @param rs- ResultSet of book by date  table with sn in the first index of rs
	 * @throws SQLException
	 */
	public static int IncPurcahseBookDateRow(String bookId) throws SQLException
	{
		Date Currentdate=new Date();
		SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy");
		String date=(String)dateFormat.format(Currentdate);
		
		Statement stmt = DatabaseController.connection.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT purchaseCount FROM book_by_date WHERE bookId='" + bookId + "' and date='"+date+"';");
		int count=0;
		while(rs.next()) //will do only once
			 count = Integer.parseInt(rs.getString(1));
		String purchaseCount=Integer.toString(count+1);
		DatabaseController.updateDatabase("UPDATE book_by_date SET purchaseCount='" + purchaseCount + "' WHERE bookId=+'"+bookId+"' and date='"+date+"';");
		return count;
	}
}
