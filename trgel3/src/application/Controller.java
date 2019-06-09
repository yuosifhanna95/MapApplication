package application;
/**
 * Sample Skeleton for 'MainPage.fxml' Controller Class
 */

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Controller {

	@FXML // fx:id="Password"
	private TextField Password; // Value injected by FXMLLoader

	@FXML // fx:id="Username"
	private TextField Username; // Value injected by FXMLLoader

	@FXML // fx:id="Login"
	private Button Login; // Value injected by FXMLLoaderv

	@FXML // fx:id="SignUp"
	private Button SignUp; // Value injected by FXMLLoader

	@FXML // fx:id="Catalog"
	private Button Catalog; // Value injected by FXMLLoader

	@FXML
	private Label Messege;

	private ObservableList<City> dataCities = null;

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() throws IOException, Exception {

		Font font = Font.loadFont(Controller.class.getResource("Quicksand.ttf").toExternalForm(), 12);
		Password.setFont(font);
		Username.setFont(font);

	}

	@FXML
	void SignUpFunc(ActionEvent event) throws IOException {

		Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		URL url = getClass().getResource("RegisterScene.fxml");
		AnchorPane pane = FXMLLoader.load(url);

		Scene scene = new Scene(pane);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	@FXML
	void LoginFunc(ActionEvent event) throws IOException, ClassNotFoundException {

		ObjectInputStream objectInput;
		Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		@SuppressWarnings("resource")
		Object data;
		String[] array = new String[3];
		array[0] = "Login";
		array[1] = Username.getText();
		array[2] = Password.getText();

		Socket socket = new Socket("localhost", 5555);
		ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
		out.writeObject(array);

		objectInput = new ObjectInputStream(socket.getInputStream());
		data = objectInput.readObject();
		System.out.println(((Object[]) data)[0]);

		if (((Object[]) (data))[0] instanceof Boolean) {
			if((int) ((Object[]) (data))[2] == 1) {
				Messege.setText("The user is already logged in");
				Globals.user = null;
			}
			else if ((Boolean) ((Object[]) (data))[0]) {

				Globals.user = (User) ((Object[]) (data))[1];
				URL url = getClass().getResource("DefaultPage.fxml");
				if (Globals.user.getType().equals("member")) {
					Globals.MODE = 2;
				} else if (Globals.user.getType().equals("oneTime")) {
					Globals.MODE = 1;
				} else if (Globals.user.getType().equals("employee")) {
					Globals.MODE = 3;
					url = getClass().getResource("EmployeePage.fxml");
				} else if (Globals.user.getType().equals("manager")) {
					Globals.MODE = 4;
					url = getClass().getResource("ManagerPage.fxml");
				}

				AnchorPane pane = FXMLLoader.load(url);

				Scene scene = new Scene(pane);
				scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				primaryStage.setScene(scene);
				primaryStage.show();
				primaryStage.setOnCloseRequest(e-> {
					try {
						logOut();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				});

			} else {
				Messege.setText("Username or password is wrong");
				Globals.user = null;
			}
		}
	}

	private Object logOut() throws UnknownHostException, IOException {
		String[] array = new String[3];
		array[0] = "LogOut";
		array[1] = Username.getText();
		array[2] = Password.getText();
		
		@SuppressWarnings("resource")
		Socket socket = new Socket("localhost", 5555);
		try {
			ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
			objectOutput.writeObject(array);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@FXML
	void CatalogFunc(ActionEvent event) throws IOException {

		Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		URL url = getClass().getResource("catalogScene.fxml");
		AnchorPane pane = FXMLLoader.load(url);

		Scene scene = new Scene(pane);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	public void buildData() throws UnknownHostException, IOException {
		@SuppressWarnings("resource")
		String[] array = new String[3];
		array[0] = "Login";
		array[1] = Username.getText();
		array[2] = Password.getText();

		Socket socket = new Socket("localhost", 5555);
		ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
		out.writeObject(array);

		try {
			ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());
			try {
				Object[] object = (Object[]) objectInput.readObject();
				for (int i = 1; i <= (int) object[0]; i++) {
					// data.add((City) object[i]);
				}

			} catch (ClassNotFoundException e) {
				System.out.println("The title list has not come from the server");
				e.printStackTrace();
			}
		} catch (IOException e) {
			System.out.println("The socket for reading the object has problem");
			e.printStackTrace();
		}
	}
}
