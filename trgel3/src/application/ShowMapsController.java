package application;

import java.io.IOException;
import java.io.ObjectInputStream;
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
import javafx.scene.control.Control;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ShowMapsController {

	@FXML
	private TableView<Map> mapTable;

	@FXML
	private TableColumn<Map, String> IdCol;

	@FXML
	private TableColumn<Map, String> DescriptionCol;

	private ObservableList<Map> data = null;

	@FXML
	private Button Back;

	@FXML
	void backFunc(ActionEvent event) throws IOException {
		Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		URL url = getClass().getResource("MyMapsScene.fxml");
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
	public void initialize() throws UnknownHostException, IOException {
		buildData();

		mapTable.getColumns().clear();
		mapTable.setEditable(true);

		mapTable.setRowFactory(tv -> {
			TableRow<Map> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!row.isEmpty())) {
					Map mapRow = mapTable.getSelectionModel().getSelectedItem();
					Globals.map = mapRow;

					try {
						Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
						URL url = getClass().getResource("viewMapScene.fxml");
						Globals.backLink = "ShowMapsScene.fxml";
						AnchorPane pane;
						pane = FXMLLoader.load(url);
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
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			});
			return row;
		});

		IdCol.setStyle("-fx-alignment: CENTER;");
		IdCol.setMinWidth(100);
		IdCol.setCellValueFactory(new PropertyValueFactory<Map, String>("id"));

		DescriptionCol.setCellFactory(tc -> {
			TableCell<Map, String> cell = new TableCell<>();
			Text text = new Text();
			cell.setGraphic(text);
			cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
			text.wrappingWidthProperty().bind(DescriptionCol.widthProperty());
			text.textProperty().bind(cell.itemProperty());
			return cell;
		});

		DescriptionCol.setStyle("-fx-alignment: CENTER;");
		DescriptionCol.setMinWidth(150);
		DescriptionCol.setCellValueFactory(new PropertyValueFactory<Map, String>("Description"));

		FilteredList<Map> flCity = new FilteredList<Map>(data, p -> true);// Pass the data to a filtered list
		mapTable.setItems(flCity);// Set the table's items using the filtered list
		mapTable.getColumns().addAll(IdCol, DescriptionCol);

	}

	public void buildData() throws UnknownHostException, IOException {
		@SuppressWarnings("resource")
		Socket socket = new Socket(Globals.IpAddress, 5555);
		data = FXCollections.observableArrayList();

		String[] get = new String[3];
		get[0] = "getMyMaps";
		get[1] = Globals.FixedPurchase.getCity();
		get[2] = "0";
		try {
			ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
			objectOutput.writeObject(get);
			try {
				ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());
				try {
					Object[] object = (Object[]) objectInput.readObject();
					for (int i = 1; i <= (int) object[0]; i++) {
						data.add((Map) object[i]);
					}

				} catch (ClassNotFoundException e) {
					System.out.println("The title list has not come from the server");
					e.printStackTrace();
				}
			} catch (IOException e) {
				System.out.println("The socket for reading the object has problem");
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
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
