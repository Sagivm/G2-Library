package control;

import entity.Login;
import entity.Message;
import enums.ActionType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


/** ScreenController exchange screens. the class presave "pointer" to stage.
 * stage "point" to the current screen now, so with nice idea we set a new value and load
 * new screen.
 * @author nire
 */
public class ScreenController {
	
	/**
	 * Point to current stage.
	 */
	private static Stage stage;
	
	
	/**
	 * Point to current instance. later we will use it to get the fxml files location.
	 */
	private static ScreenController instance;
	
	
	/**
	 * ScreenController initialize the instance.
	 */
	public ScreenController()
	{
		instance=this;
	}

	/**
	 * This function actually replace the stages. we can think about it like context switch.
	 * @param fxml Gets the screen's path.
	 * @param title Gets the screen's title.
	 * @return Pointer to parent.
	 * @throws Exception Exception.
	 */ 
	public Parent replaceSceneContent(String fxml, String title) throws Exception {
        Parent page = (Parent) FXMLLoader.load(ScreenController.class.getResource(fxml));
        Scene scene = stage.getScene();
        stage.setTitle(title);
        if (scene == null) {
            scene = new Scene(page);
            stage.setScene(scene);
        } else {
            stage.getScene().setRoot(page);
        }
        stage.sizeToScene();
        return page;
    }	
	
	/**
	 * Getter for stage
	 * @return Get the stage.
	 */
	public static Stage getStage() {
		return stage;
	}
	
	
	/**
	 * Setter for stage.
	 * @param stage Set the stage.
	 */
	public static void setStage(Stage stage) {
		ScreenController.stage = stage;
	}
	
	
	/**
	 * Getter for instance
	 * @return Instance to the current instance.
	 */
	public static ScreenController getInstance() {
		return instance;
	}
	
	
	/** Setter for instance
	 * @param instance Set the instance.
	 */
	public static void setInstance(ScreenController instance) {
		ScreenController.instance = instance;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#finalize()
	 */
	protected void finalize() throws Throwable
	{
	         super.finalize();
	}
	
}
