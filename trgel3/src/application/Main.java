package application;

import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Globals.MODE = 0;
			URL url = getClass().getResource("MainPage.fxml");
			AnchorPane pane = FXMLLoader.load(url);
			
			Scene scene = new Scene(pane);
			scene.getStylesheets().add(getClass().getResource("styleMain.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Globals.IpAddress = "127.0.0.1";
		launch(args);
	}
}
