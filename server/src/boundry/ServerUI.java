package boundry;

import entity.ScreensInfo;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * ServerUI execute the GUI Window & main function, and start to run the server.
 * @author nire
 *
 */
public class ServerUI extends Application {

	/**
	 * Main function.
	 */
	public static void main(String args[])
	{
		launch(ServerUI.class, args);
	}

    /* (non-Javadoc)
     * @see javafx.application.Application#start(javafx.stage.Stage)
     */
    @Override
	   public void start(Stage stage) throws Exception {
		    try{
		    Parent root = FXMLLoader.load(getClass().getResource(ScreensInfo.SERVER_SCREEN));
		    stage.setTitle(ScreensInfo.SERVER_TITLE);
		    stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
	            @Override
	            public void handle(WindowEvent t) {
	                Platform.exit();
	                System.exit(0);
	            }
	        }); 
		    stage.setScene(new Scene(root));
		    stage.setResizable(false);
		    stage.show();
		    }
		    catch(Exception e){
		        System.out.print(e);
		    }      
		}
    

}