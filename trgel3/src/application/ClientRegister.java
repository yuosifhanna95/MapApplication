package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
//import javafx.stage.StageStyle;
import javafx.scene.Scene;
//import javafx.scene.paint.Color;

import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
public class ClientRegister extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			URL url = getClass().getResource("RegisterScene.fxml");
			AnchorPane pane = FXMLLoader.load( url );
			Scene scene = new Scene( pane );
			//scene.setFill(Color.BLACK);
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle( "Registeration" );
			//primaryStage.initStyle(StageStyle.UNIFIED);
			primaryStage.sizeToScene();
			
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
