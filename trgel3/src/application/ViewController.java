package application;
import java.io.IOException;
import java.net.UnknownHostException;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
/**
*
* @author zoranpavlovic.blogspot.com
*/
public class ViewController  {

    @FXML
    private ImageView map;
  
    @FXML
    private Pane paneView;
    @FXML
   	public void initialize() throws UnknownHostException, IOException {
//    	paneView.getChildren().clear();
//    	Image image = new Image("/home/ranin/Documents/semester6/handasatTo5na/Project/London2.png");
//    	javafx.scene.image.ImageView imageview= new javafx.scene.image.ImageView(image);
//    	imageview.setFitWidth(300);
//    	imageview.setFitHeight(300);
//    	
//    	paneView.getChildren().add(imageview);
    }
	
	
     
}