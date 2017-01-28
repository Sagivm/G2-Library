package entity;

import java.io.Serializable;

/** FileEvent is the class that include the actual data when we transfer
 * the file.
 * @author nire
 * 
 */
public class FileEvent implements Serializable {

	
/**
 * Empty constructor.
 */
public FileEvent() {
}

/**
 * variable serialVersionUID for serializable.
 */
private static final long serialVersionUID = 1L;


/**
 * Destination directory.
 */
private String destinationDirectory;


/**
 * Source directory.
 */
private String sourceDirectory;

/**
 * File name.
 */
private String filename;


/**
 * File size.
 */
private long fileSize;

/**
 * File data (in bytes).
 */
private byte[] fileData;

/**
 * File status {Success,Failed}
 */
private String status;


/** Getter for DestinationDirectory.
 * @return The DestinationDirectory.
 */
public String getDestinationDirectory() {
return destinationDirectory;
}

/** Setter for DestinationDirectory.
 * @param destinationDirectory Gets the destinationDirectory.
 */
public void setDestinationDirectory(String destinationDirectory) {
this.destinationDirectory = destinationDirectory;
}

/** Getter for SourceDirectory.
 * @return The SourceDirectory.
 */
public String getSourceDirectory() {
return sourceDirectory;
}

/** Setter for SourceDirectory.
 * @param sourceDirectory Gets the sourceDirectory.
 */
public void setSourceDirectory(String sourceDirectory) {
this.sourceDirectory = sourceDirectory;
}

/** Getter for Filename.
 * @return The filename.
 */
public String getFilename() {
return filename;
}

/** Setter for Filename.
 * @param filename Gets the filename
 */
public void setFilename(String filename) {
this.filename = filename;
}

/** Getter for FileSize
 * @return The fileSize.
 */
public long getFileSize() {
return fileSize;
}

/** Setter for FileSize.
 * @param fileSize Gets the fileSize.
 */
public void setFileSize(long fileSize) {
this.fileSize = fileSize;
}

/** Getter for status
 * @return Gets the status.
 */
public String getStatus() {
return status;
}

/** Setter for status.
 * @param status the status.
 */
public void setStatus(String status) {
this.status = status;
}

/** Gets the FileData
 * @return the fileData.
 */
public byte[] getFileData() {
return fileData;
}

/** Setter for FileData.
 * @param fileData Gets the fileData.
 */
public void setFileData(byte[] fileData) {
this.fileData = fileData;
}
}