package application;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MemberHistoryController {

	private static ObservableList<HistoryOfUser> historyData = FXCollections.observableArrayList();

	@FXML
	private TableView<HistoryOfUser> historyTable;

	@FXML
	private TableColumn<HistoryOfUser, String> cityCol;

	@FXML
	private TableColumn<HistoryOfUser, String> typeCol;

	@FXML
	private TableColumn<HistoryOfUser, String> dateCol;

	User userInfo = Globals.user;

	@FXML
	private Button back;

	@FXML
	void onBack(ActionEvent event) throws IOException {
		Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		URL url = getClass().getResource(Globals.backLink);
		AnchorPane pane = FXMLLoader.load(url);
		Scene scene = new Scene(pane);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.setOnCloseRequest(e -> {
			try {
				logOut();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		primaryStage.show();
	}

	@SuppressWarnings("unchecked")
	@FXML
	void initialize() throws IOException, Exception {

		makeUserHistory();

		historyTable.getColumns().clear();
		historyTable.setEditable(true);

		cityCol.setStyle("-fx-alignment: CENTER;");
		cityCol.setMinWidth(100);
		cityCol.setCellValueFactory(new PropertyValueFactory<HistoryOfUser, String>("city"));

		typeCol.setStyle("-fx-alignment: CENTER;");
		typeCol.setMinWidth(100);
		typeCol.setCellValueFactory(new PropertyValueFactory<HistoryOfUser, String>("type"));

		dateCol.setStyle("-fx-alignment: CENTER;");
		dateCol.setMinWidth(100);
		dateCol.setCellValueFactory(new PropertyValueFactory<HistoryOfUser, String>("date"));

		FilteredList<HistoryOfUser> flCity = new FilteredList<HistoryOfUser>(historyData, p -> true);// Pass the data to
																										// a filtered
																										// list
		historyTable.setItems(flCity);// Set the table's items using the filtered list
		historyTable.getColumns().addAll(cityCol, typeCol, dateCol);
	}

	private void makeUserHistory() {
		String History = Globals.userView.getHistory();
		historyData = FXCollections.observableArrayList();
		String[] arrOfHistory = History.split("#|\\,");
		String type;
		if (History.indexOf(",") >= 0) {
			for (int i = 0; i < arrOfHistory.length; i++) {

				if (arrOfHistory[i + 1].equals("OT")) {
					type = "One time purchase";
				} else
					type = "Fixed time purchase";

				historyData.add(new HistoryOfUser(arrOfHistory[i], type, arrOfHistory[i + 3]));
				i += 3;
			}
		}
	}

	private Object logOut() throws UnknownHostException, IOException {
		String[] array = new String[3];
		array[0] = "LogOut";
		array[1] = Globals.user.getUserName();
		array[2] = Globals.user.getPassword();

		@SuppressWarnings("resource")
		Socket socket = new Socket(Globals.IpAddress, 5555);
		try {
			ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
			objectOutput.writeObject(array);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

}