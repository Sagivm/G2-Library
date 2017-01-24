package entity;

import java.io.Serializable;
import java.util.ArrayList;

import com.sun.prism.PixelFormat.DataType;

import enums.ActionType;


/**
 * This class include the message that send to server, and implements Serializable 
 * because the message send from client to the server.
 * @author nire
 */
public class Message implements Serializable {
	
/**
 * variable serialVersionUID for serializable.
 */
private static final long serialVersionUID = 1L;


/**
 * Data type
 */
private ActionType Type;


/**
 * Array list of elements to query server and database.
 */
private ArrayList<String> elementsList;


/** Message constructor to initialize the attributes.
 * @param type - Gets the kind of the process to perform.
 * @param elementsList - Gets the elements list that will be decrypt in the server.
 */
public Message(ActionType type, ArrayList<String> elementsList)
{
	setType(type);
	setElementsList(elementsList);
}


/**
 * Empty constructor.
 */
public Message() {
	
}


public Message(ActionType type) {
	setType(type);
}


/** Getter for attribute type.
 * @return - The type.
 */
public ActionType getType() {
	return Type;
}


/** Setter for attribute  type.
 * @param type - Sets the type.
 */
public void setType(ActionType type) {
	this.Type = type;
}


/** Getter for elements list.
 * @return - Gets the elements list.
 */
public ArrayList<String> getElementsList() {
	return elementsList;
}


/** Setter for elements list.
 * @param elementsList - Set the elementsList.
 */
public void setElementsList(ArrayList<String> elementsList) {
	this.elementsList = elementsList;
}

}
