package control;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import javax.imageio.ImageIO;

import boundry.ClientUI;
import entity.GeneralMessages;
import entity.Message;
import entity.Review;
import entity.ScreensInfo;
import entity.SearchBookResult;
import entity.User;
import entity.Validate;
import enums.ActionType;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

/** ExternalPaymentController Responsible to make payments.
 * @author ork
 */
public class ExternalPaymentController {
	
	/**
	 * This label display the product name.
	 */
	@FXML
	private Label lblProduct;
	
	/**
	 * This label display the price of the product.
	 */
	@FXML
	private Label lblPrice;
	
	/**
	 * Choose to pay with visa.
	 */
	@FXML 
	private RadioButton rbVisa;
	
	/**
	 * Choose to pay with Master Card.
	 */
	@FXML
	private RadioButton rbMasterCard;
	
	/**
	 * Choose to pay with American Express.
	 */
	@FXML
	private RadioButton rbAmex;
	
	/**
	 * TextField for insert the Card Number.
	 */
	@FXML
	private TextField txtCardNum;
	
	/**
	 * TextField for insert the Card Exp.Month.
	 */
	@FXML
	private TextField txtMonth;
	
	/**
	 * TextField for insert the Card Exp.Year.
	 */
	@FXML
	private TextField txtYear;
	
	/**
	 * TextField for insert the Card CVV.
	 */
	@FXML
	private TextField txtCVV;
	
	/**
	 * TextField for insert the name of the card holder.
	 */
	@FXML
	private TextField txtName;
	
	/**
	 * TextField for insert the ID of the card holder.
	 */
	@FXML
	private TextField txtID;
	
	private static int action = 0;
	
	/**
	 * Get the product name from the previous screen.
	 */
	private static String product;
	
	/**
	 * Get the product price from the previous screen.
	 */
	private static String price;
	
	/**
	 * static reference of user home page.
	 */
	private static HomepageUserController userMain;
	
	/**
	 * Saves the book entity information.
	 */
	public static SearchBookResult searchedBookPage;
	
	/**
	 * Get answer from DB if success.
	 */
	public static boolean success = false;
	
	final ToggleGroup group = new ToggleGroup();
	
	/**
	 * Initialize FX components.
	 */
	@FXML
	public void initialize() 
	{
		rbVisa.setToggleGroup(group);
		rbVisa.setSelected(false);
		rbMasterCard.setToggleGroup(group);
		rbMasterCard.setSelected(false);
		rbAmex.setToggleGroup(group);
		rbAmex.setSelected(false);
		
		Service<Void> service = new Service<Void>() {
	        @Override
	        protected Task<Void> createTask() {
	            return new Task<Void>() {           
	                @Override
	                protected Void call() throws Exception {                
	                    final CountDownLatch latch = new CountDownLatch(1);
	                    Platform.runLater(new Runnable() {                          
	                        @Override
	                        public void run() {
		                        lblProduct.setText(product);
		                        lblPrice.setText(price 	+ " \u20AA");   	
							}
	                        });
	                     latch.await();                      
	                     return null;
	                   }
	                };
	            }
	        };
	        service.start();
		
	}
	
	
	/**
	 * When pressed, checks validation of all fields and call method makePurchase
	 * On PayementController with TRUE and action to do.
	 * @param event
	 * @throws IOException
	 */
	@FXML
	public void btnPayPressed(ActionEvent event) throws IOException
	{ 
		if (txtCardNum.getText().equals("") || txtMonth.getText().equals("") || txtYear.getText().equals("") ||
			txtCVV.getText().equals("") || txtName.getText().equals("") || txtID.getText().equals("") || (!rbVisa.isSelected() & !rbMasterCard.isSelected() & !rbAmex.isSelected()) )
			{
				actionOnError(ActionType.CONTINUE,GeneralMessages.EMPTY_FIELDS);
				return;
			}
		
		if (Validate.cardNumberValidate(txtCardNum.getText()) == false)
		{
			actionOnError(ActionType.CONTINUE,GeneralMessages.MUST_INCLUDE_ONLY_DIGIT_CARD);
			return;
		}
		else if(txtCardNum.getLength() < 7 || txtCardNum.getLength() > 20 )
		{
			actionOnError(ActionType.CONTINUE,GeneralMessages.WRONG_COUNT_OF_DIGIT_CARD);
			return;
		}
		
		if (Validate.twoDigitValidate(txtMonth.getText()) == false || Validate.twoDigitValidate(txtYear.getText()) == false  || Integer.parseInt(txtMonth.getText())<1 || Integer.parseInt(txtMonth.getText())>12)
		{
			actionOnError(ActionType.CONTINUE,GeneralMessages.INVALID_DATE);
			return;
		}
		
		if (Validate.cvvValidate(txtCVV.getText()) == false)
		{
			actionOnError(ActionType.CONTINUE,GeneralMessages.CVV_INVALID);
			return;
		}
		
		if (Validate.nameValidateCharactersOnly(txtName.getText()) == false)
		{
			actionOnError(ActionType.CONTINUE,GeneralMessages.MUST_INCLUDE_ONLY_CHARACTERS);
			return;
		}
		
		if (Validate.usernameValidate(txtID.getText()) == false)
		{
			actionOnError(ActionType.CONTINUE,GeneralMessages.MUST_INCLUDE_ONLY_DIGITS_ID);
			return;
		}
		
		PaymentController purchase = new PaymentController();
		purchase.makePurchase(true, action);
	}
	
	/**
	 * When pressed, call method makePurchase On PayementController with FALSE and action to do.
	 * @param event
	 * @throws IOException
	 */
	@FXML
	public void btnCancelPressed(ActionEvent event) throws IOException
	{ 
		PaymentController purchase = new PaymentController();
		purchase.makePurchase(false, action);
		
	}
	
	/**
	 * Setter of product.
	 * @param product
	 */
	public void setProduct(String product)
	{
		this.product = product;
	}
	
	/**
	 * Setter of price.
	 * @param price
	 */
	public void setPrice(String price)
	{
		this.price = price;
	}
	
	/**
	 * Setter of action.
	 * @param action
	 */
	public void setAction(int action)
	{
		this.action = action;
	}
	
	/* (non-Javadoc)
	 * @see interfaces.ScreensIF#actionOnError(enums.ActionType, java.lang.String)
	 */
	public void actionOnError(ActionType type, String errorCode) {
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Error");
		alert.setHeaderText(null);
		alert.setContentText(errorCode);
		alert.showAndWait();
		if (type == ActionType.TERMINATE)
		{
			Platform.exit();
			System.exit(1);
		}
		if (type == ActionType.CONTINUE)
			return;
	}

}
