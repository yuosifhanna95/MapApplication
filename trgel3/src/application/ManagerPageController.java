package application;

import java.io.IOException;
import java.net.URL;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ManagerPageController {

	@FXML
	private Button btn_AddMap;

	@FXML
	private Button btn_message;

	@FXML
	private Button Back;

	@FXML
	private Button btn_confirmMaps;

	@FXML
	private Button btn_editMaps;

	@FXML
	private Button memberfile;
	 @FXML
	    private Button Prices;
	 @FXML
	    private Button Reports;
	 
	 
	 @FXML
	    void showreportbtn(ActionEvent event) throws IOException{
		 
		 Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			URL url = getClass().getResource("ShowReports.fxml");
			AnchorPane pane = FXMLLoader.load(url);

			Scene scene = new Scene(pane);
			scene.getStylesheets().add(getClass().getResource("styleMain.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		 
	 }
	 
	 @FXML
	    void changeprice(ActionEvent event) throws IOException{
		 Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			URL url = getClass().getResource("Changeprices.fxml");
			AnchorPane pane = FXMLLoader.load(url);

			Scene scene = new Scene(pane);
			scene.getStylesheets().add(getClass().getResource("styleMain.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
	    }

	@FXML
	void AddMapFunc(ActionEvent event) throws IOException {
		Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		URL url = getClass().getResource("AddMap.fxml");
		AnchorPane pane = FXMLLoader.load(url);
		Globals.backLink = "ManagerPage.fxml";
		Scene scene = new Scene(pane);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@FXML
	void confirmMapsFunc(ActionEvent event) throws IOException {
		Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		URL url = getClass().getResource("ConfirmMaps.fxml");
		AnchorPane pane = FXMLLoader.load(url);
		Globals.backLink = "ManagerPage.fxml";
		Scene scene = new Scene(pane);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	@FXML
	void MembersFileFunc(ActionEvent event) throws IOException {
		Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		URL url = getClass().getResource("MemberFile.fxml");
		AnchorPane pane = FXMLLoader.load(url);
		Globals.backLink = "ManagerPage.fxml";
		Scene scene = new Scene(pane);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() throws IOException, Exception {

		if (Globals.MODE < 3) {
			btn_editMaps.setVisible(false);
		}

	}

	@FXML
	void backFunc(ActionEvent event) throws IOException {

		Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		URL url = getClass().getResource("MainPage.fxml");
		AnchorPane pane = FXMLLoader.load(url);

		Scene scene = new Scene(pane);
		scene.getStylesheets().add(getClass().getResource("styleMain.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@FXML
	void editmaps(ActionEvent event) throws IOException {

		Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		URL url = getClass().getResource("EditMaps.fxml");
		AnchorPane pane = FXMLLoader.load(url);
		Globals.backLink = "ManagerPage.fxml";
		Scene scene = new Scene(pane);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@FXML
	void messageFunc(ActionEvent event) throws IOException {

		Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		URL url = getClass().getResource("Messages.fxml");
		AnchorPane pane = FXMLLoader.load(url);
		Globals.backLink = "ManagerPage.fxml";
		Scene scene = new Scene(pane);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();

	}

}
