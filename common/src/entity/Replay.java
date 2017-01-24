package entity;

import java.io.Serializable;
import java.util.ArrayList;

import enums.ActionType;

/**
 * This class include the replay from server to user.
 * @author nire
 *
 */
public class Replay implements Serializable {
	
/**
* variable serialVersionUID for serializable.
*/
private static final long serialVersionUID = 1L;

/**
 * Kind of action.
 */
private ActionType type;


/**
 * Kind of response.
 */
private boolean sucess;

/**
 * action to do when replay
 */
private int action;

/**
 * Show general messages
 */
private String GnrlMsg;

/**
 * Array list of elements to return to the Client.
 */
private ArrayList<String> elementsList;



/**
 * Type of receive message.
 */
private ActionType transmitType;



/**
 * The FileEvent.
 */
private FileEvent fileEvent;


/** Replay constructor that initialize the attributes.
 * @param type - Gets the type of action.
 * @param sucess - Gets the kind of response from SQL.
 */
public Replay(ActionType type, boolean sucess)
{
	setType(type);
	setSucess(sucess);
	setTransmitType(ActionType.UNICAST);
}

/** Replay constructor that initialize the attributes for file sending.
 * @param type - Gets the type of action.
 * @param fileEvent - Gets the file.
 */
public Replay(ActionType type, FileEvent fileEvent) {
	setType(type);
	setFileEvent(fileEvent);
	setTransmitType(ActionType.UNICAST);
}


/** Replay constructor that initialize the attributes.
 * @param type - Gets the type of action.
 * @param action - Gets the action.
 */
public Replay(ActionType type, int action)
{
	this.action=action;
	this.type=type;
	setTransmitType(ActionType.UNICAST);
}



/** Replay constructor that initialize the attributes.
 * @param type - Gets the type of action.
 * @param sucess - Gets if the action true\false.
 * @param transmitType - Gets the transmit type.
 * @param elementsList - Gets the elements list.
 */
public Replay(ActionType type, boolean sucess,ActionType transmitType, ArrayList<String> elementsList)
{
	setType(type);
	setSucess(sucess);
	setElementsList(elementsList);
	setTransmitType(transmitType);
}


/** Replay constructor that initialize the attributes.
 * @param type - Gets the type of action.
 * @param sucess - Gets the kind of response from SQL.
 * @param action - Gets a sub-action to do.
 * @param elementsList - Gets the elements list that will be decrypt in the client.
 */
public Replay(ActionType type, boolean sucess,ArrayList<String> elementsList)
{
	setType(type);
	setSucess(sucess);
	setElementsList(elementsList);
	setTransmitType(ActionType.UNICAST);
}

/** Replay constructor that initialize the attributes.
 * @param type - Gets the type of action.
 * @param sucess - Gets the kind of response from SQL.
 * @param action - Gets a sub-action to do.
 * @param elementsList - Gets the elements list that will be decrypt in the client.
 */
public Replay(ActionType type, boolean sucess,int action,ArrayList<String> elementsList)
{
	setType(type);
	setSucess(sucess);
	setAction(action);
	setElementsList(elementsList);
	setTransmitType(ActionType.UNICAST);
}


/** Replay constructor that initialize the attributes.
 * @param type - Gets the type of action.
 * @param sucess - Gets the kind of response from SQL.
 * @param msg - Gets a message.
 */
public Replay(ActionType type, boolean sucess,String msg)
{
	setType(type);
	setSucess(sucess);
	setGnrlMsg(msg);
	setTransmitType(ActionType.UNICAST);
}

/** Getter for action type.
 * @return - the type attribute.
 */
public ActionType getType() {
	return type;
}

/** Setter for action type.
 * @param type - Set the value into type.
 */
public void setType(ActionType type) {
	this.type = type;
}


/** Getter for sucess.
 * @return - the sucess attribute.
 */
public boolean getSucess() {
	return sucess;
}

/** Getter for action
 * @return - the action attribute.
 */
public int getAction() {
	return action;
}

/** Getter for GeneralMessages
 * @return - the GeneralMessages attribute.
 */
public String getGnrlMsg() {
	return GnrlMsg;
}

/** Getter for elements list.
 * @return - Gets the elements list.
 */
public ArrayList<String> getElementsList() {
	return elementsList;
}

/** Setter for sucess.
 * @param sucess - Set the value into sucess.
 */
public void setSucess(boolean sucess) {
	this.sucess = sucess;
}	

/** Setter for action
 * @param action - Set the value into action
 */
public void setAction(int action) {
	this.action = action;
}

/** Setter for GeneralMessages
 * @param msg
 */
public void setGnrlMsg(String msg) {
	this.GnrlMsg = msg;
}

/** Setter for elements list.
 * @param elementsList - Set the elementsList.
 */
public void setElementsList(ArrayList<String> elementsList) {
	this.elementsList = elementsList;
}


/** Getter for transmit type.
 * @return the transmit type.
 */
public ActionType getTransmitType() {
	return transmitType;
}

/** Setter for transmit type.
 * @param Gets the transmit type.
 */
public void setTransmitType(ActionType transmitType) {
	this.transmitType = transmitType;
}

/** Getter for FileEvent.
 * @return - The fileEvent.
 */
public FileEvent getFileEvent() {
	return fileEvent;
}

/** Setter for FileEvent.
 * @param fileEvent - Gets the fileEvent.
 */
public void setFileEvent(FileEvent fileEvent) {
	this.fileEvent = fileEvent;
}
	
}
