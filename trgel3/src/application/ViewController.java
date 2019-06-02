package application;


import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import javafx.scene.control.Button;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ViewController  {
   @FXML
    private ImageView mapImage;
  
   @FXML
    private AnchorPane mainPane;

   @FXML
   private Button back;	
   
   @FXML
   	public void initialize() throws UnknownHostException, IOException {
	    mapImage = new ImageView(new Image(Globals.map.getLinkCustomer()));
	    mapImage.relocate(162 , 50);
    	mainPane.getChildren().add(mapImage);
    }
	
   @FXML
   void backFunc(ActionEvent event) throws IOException {
     	Stage primaryStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
     	URL url = getClass().getResource(Globals.backLink);
     	Globals.backLink = "MyMapsScene.fxml";

		AnchorPane pane =FXMLLoader.load(url);

		Scene scene = new Scene(pane);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
   }
     
}