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
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SingleEditRouteController {

	@FXML
	private ImageView route;

	@FXML
	private TableView<Route> routeTable;

	@FXML
	private TableColumn<Route, String> idCol;

	@FXML
	private TableColumn<Route, String> DescriptionCol;

	@FXML
	private Button Back;

	private ObservableList<Route> data = null;

	@FXML
	void backFunc(ActionEvent event) throws IOException {

		Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		URL url = getClass().getResource("EditRoutes.fxml");
		AnchorPane pane = FXMLLoader.load(url);

		Scene scene = new Scene(pane);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.setOnCloseRequest(e-> {
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

		routeTable.getColumns().clear();
		routeTable.setEditable(true);

		routeTable.setRowFactory(tv -> {
			TableRow<Route> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!row.isEmpty())) {
					Route routeRow = routeTable.getSelectionModel().getSelectedItem();

					String urll = routeRow.getLink();
					System.out.println(urll);
					// Globals.backLink = urll;
					Globals.route = routeRow;

					try {
						Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
						URL url = getClass().getResource("AddRLocations.fxml");
						Globals.backLink = "SingleEditRoute.fxml";
						AnchorPane pane;
						pane = FXMLLoader.load(url);
						Scene scene = new Scene(pane);
						scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
						primaryStage.setScene(scene);
						primaryStage.setOnCloseRequest(e-> {
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

//   	        	boolean backgroundLoading = true;
//   	        	 
//   	        	// The image is being loaded in the background
//   	        	Image image = new Image(url, backgroundLoading);
//    	        	
//   	        	map.ImageView(image);
				}
			});
			return row;
		});

		idCol.setStyle("-fx-alignment: CENTER;");
		idCol.setMinWidth(100);
		idCol.setCellValueFactory(new PropertyValueFactory<Route, String>("id"));

		DescriptionCol.setCellFactory(tc -> {
			TableCell<Route, String> cell = new TableCell<>();
			Text text = new Text();
			cell.setGraphic(text);
			cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
			text.wrappingWidthProperty().bind(DescriptionCol.widthProperty());
			text.textProperty().bind(cell.itemProperty());
			return cell;
		});
		DescriptionCol.setStyle("-fx-alignment: CENTER;");
		DescriptionCol.setMinWidth(150);
		DescriptionCol.setCellValueFactory(new PropertyValueFactory<Route, String>("Description"));

		FilteredList<Route> flCity = new FilteredList<Route>(data, p -> true);// Pass the data to a filtered list
		routeTable.setItems(flCity);// Set the table's items using the filtered list
		routeTable.getColumns().addAll(idCol, DescriptionCol);

	}

	public void buildData() throws UnknownHostException, IOException {
		@SuppressWarnings("resource")
		Socket socket = new Socket("localhost", 5555);
		data = FXCollections.observableArrayList();

		String[] get = new String[3];
		get[0] = "getRoutesCity";
		get[1] = Globals.city.getCity();
		get[2] = "-1";
		try {
			ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
			objectOutput.writeObject(get);
			try {
				ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());
				try {
					Object[] object = (Object[]) objectInput.readObject();
					for (int i = 1; i <= (int) object[0]; i++) {
						data.add((Route) object[i]);
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
		Socket socket = new Socket("localhost", 5555);
		try {
			ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
			objectOutput.writeObject(array);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
