package application;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class CatalogController {

	@FXML
	private ComboBox<String> comboBox;
	@FXML
	private TextField searchText = new TextField();
	@FXML
	private TableView<City> searchTable;
	@FXML
	private TableColumn<City, String> CityCol;

	@FXML
	private TableColumn<City, String> DescriptionCol;

	@FXML
	private TableColumn<City, String> mapCol;

	@FXML
	private TableColumn<City, String> placeCol;

	@FXML
	private TableColumn<City, String> pathCol;

	@FXML
	private TableView<Place> searchTable1;

	@FXML
	private TableColumn<Place, String> PlaceCol1;

	@FXML
	private TableColumn<Place, String> CityCol1;

	@FXML
	private TableColumn<Place, String> DescriptionCol1;

	@FXML
	private TableColumn<Place, String> mapCol1;

	private ObservableList<City> dataCity = FXCollections.observableArrayList();

	private ObservableList<Place> dataPlace = FXCollections.observableArrayList();

	private ObservableList<Place> dataPlace2 = FXCollections.observableArrayList();

	@FXML
	private Button back;

	@FXML
	private Button searchCity;

	@FXML
	private HBox hbox;

	TextField placeField = new TextField();

	@FXML
	private Button view;

	@FXML
	private Button searchPlace;

	FilteredList<City> flCity = null;

	FilteredList<Place> flPlace = null;
	FilteredList<Place> flPlace1 = null;
	FilteredList<Place> flPlace2 = null;

	@FXML
	void viewMaps(ActionEvent event) throws IOException {
		Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		URL url = getClass().getResource("ShowMapsCatalogScene.fxml");
		Globals.backLink = "catalogScene.fxml";
		if (searchTable.isVisible()) {
			Globals.SearchOp = "City-";
			Globals.Searchfilter = searchText.getText();
		} else if (searchTable1.isVisible()) {
			Globals.SearchOp = "Place-";
			if (comboBox.getSelectionModel().getSelectedItem().equals("City & place"))
				Globals.Searchfilter = placeField.getText();
			else
				Globals.Searchfilter = searchText.getText();
		}
		Globals.SearchOp += comboBox.getSelectionModel().getSelectedItem();
		if (flPlace2 != null)
			Globals.Fplaces2 = flPlace2;
		else
			Globals.Fplaces2 = flPlace;
		Globals.Fplaces = flPlace;
		Globals.Fplaces1 = flPlace1;
		AnchorPane pane;
		pane = FXMLLoader.load(url);

		Scene scene = new Scene(pane);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
//		primaryStage.setOnCloseRequest(e -> {
//			try {
//				logOut();
//			} catch (IOException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//		});
		primaryStage.show();

	}

	@FXML
	void searchCityBtn(ActionEvent event) {
		searchPlace.getStyleClass().remove("addBobOk");
		searchCity.getStyleClass().removeAll("addBobOk, focus");
		searchCity.getStyleClass().remove("addBobOk");
		searchCity.getStyleClass().add("addBobOk");

		searchText.setPrefWidth(200);
		hbox.getChildren().remove(placeField);

		searchTable1.setVisible(false);
		searchTable1.setDisable(true);

		searchTable.setVisible(true);
		searchTable.setDisable(false);

		comboBox.getItems().remove("City & place");

		searchText.setOnKeyReleased(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent ke) {
				switch (comboBox.getValue()) {
				case "City":
					flCity.setPredicate(
							p -> p.getCity().toLowerCase().contains(searchText.getText().toLowerCase().trim()));
					break;
				case "Place":
					flCity.setPredicate(
							p -> p.getPlaces().toLowerCase().contains(searchText.getText().toLowerCase().trim()));
					break;
				case "Description":
					flCity.setPredicate(
							p -> p.getDescription().toLowerCase().contains(searchText.getText().toLowerCase().trim()));
					break;
				}
			}
		});

	}

	@FXML
	void searchPlaceBtn(ActionEvent event) throws UnknownHostException, IOException {
		searchCity.getStyleClass().remove("addBobOk");
		searchPlace.getStyleClass().removeAll("addBobOk, focus");
		searchPlace.getStyleClass().remove("addBobOk");
		searchPlace.getStyleClass().add("addBobOk");

		comboBox.getItems().remove("City & place");
		comboBox.getItems().add("City & place");

		searchTable.setVisible(false);
		searchTable.setDisable(true);

		searchTable1.setVisible(true);
		searchTable1.setDisable(false);

		comboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> selected, String old, String newVal) {
				if (newVal != null) {
					switch (newVal) {
					case "City & place":
						hbox.getChildren().remove(placeField);
						searchText.setPrefWidth(100);
						placeField.setPrefWidth(100);
						searchText.setPromptText("City");
						placeField.setPromptText("Place");
						hbox.getChildren().addAll(placeField);
						break;
					case "City":
						searchText.setPrefWidth(200);
						searchText.setPromptText("City");
						hbox.getChildren().remove(placeField);
						break;
					case "Description":
						searchText.setPrefWidth(200);
						searchText.setPromptText("Description");
						hbox.getChildren().remove(placeField);
						break;
					case "Place":
						searchText.setPrefWidth(200);
						searchText.setPromptText("Place");
						hbox.getChildren().remove(placeField);
						break;
					}
				}
			}
		});

		placeField.setOnKeyReleased(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent ke) {
				switch (comboBox.getValue()) {
				case "City & place":
					flPlace1.setPredicate(
							p -> p.getPlaceName().toLowerCase().contains(placeField.getText().toLowerCase().trim()));
					flPlace2 = new FilteredList<Place>(flPlace1, p -> true);
					flPlace2.setPredicate(
							p -> p.getCityName().toLowerCase().contains(searchText.getText().toLowerCase().trim()));
					searchTable1.setItems(flPlace2);
					// SearchCombination();
					break;
				}
			}
		});

		searchText.setOnKeyReleased(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent ke) {
				switch (comboBox.getValue()) {
				case "City":
					flPlace1.setPredicate(
							p -> p.getCityName().toLowerCase().contains(searchText.getText().toLowerCase().trim()));
					flPlace2 = new FilteredList<Place>(flPlace1, p -> true);
					flPlace2.setPredicate(
							p -> p.getPlaceName().toLowerCase().contains(placeField.getText().toLowerCase().trim()));
					break;
				case "Place":
					flPlace1.setPredicate(
							p -> p.getPlaceName().toLowerCase().contains(searchText.getText().toLowerCase().trim()));
					break;
				case "Description":
					flPlace1.setPredicate(
							p -> p.getDescription().toLowerCase().contains(searchText.getText().toLowerCase().trim()));
					break;
				case "City & place":
					flPlace1.setPredicate(
							p -> p.getCityName().toLowerCase().contains(searchText.getText().toLowerCase().trim()));
					flPlace2 = new FilteredList<Place>(flPlace1, p -> true);
					flPlace2.setPredicate(
							p -> p.getPlaceName().toLowerCase().contains(placeField.getText().toLowerCase().trim()));
					searchTable1.setItems(flPlace2);

					break;
				}
			}
		});

	}

	@FXML
	void backFunc(ActionEvent event) throws IOException {
		Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		URL url = getClass().getResource("MainPage.fxml");
		AnchorPane pane = FXMLLoader.load(url);

		Scene scene = new Scene(pane);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@SuppressWarnings("unchecked")
	@FXML
	public void initialize() throws UnknownHostException, IOException {
		searchCity.getStyleClass().removeAll("addBobOk, focus");
		searchCity.getStyleClass().add("addBobOk");

		buildData("city");
		buildData("place");
		buildData("Oplace");

		searchTable.setRowFactory(ctv -> {
			TableRow<City> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 1 && (!row.isEmpty())) {
					City cityRow = searchTable.getSelectionModel().getSelectedItem();
					Globals.city = (City) cityRow;
				}
			});
			return row;
		});
		searchTable1.setRowFactory(ctv -> {
			TableRow<Place> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 1 && (!row.isEmpty())) {
					Place placeRow = searchTable1.getSelectionModel().getSelectedItem();
					Globals.place = (Place) placeRow;
					Globals.cityName = placeRow.getCityName();
				}
			});
			return row;
		});

		comboBox.getItems().addAll("City", "Place", "Description");
		searchText.setPromptText("City");

		searchTable1.getColumns().clear();
		searchTable1.setEditable(true);

		PlaceCol1.setStyle("-fx-alignment: CENTER;");
		PlaceCol1.setMinWidth(100);
		PlaceCol1.setCellValueFactory(new PropertyValueFactory<Place, String>("PlaceName"));

		CityCol1.setStyle("-fx-alignment: CENTER;");
		CityCol1.setMinWidth(100);
		CityCol1.setCellValueFactory(new PropertyValueFactory<Place, String>("CityName"));

		DescriptionCol1.setCellFactory(tc -> {
			TableCell<Place, String> cell = new TableCell<>();
			Text text = new Text();
			cell.setGraphic(text);
			cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
			text.wrappingWidthProperty().bind(DescriptionCol.widthProperty());
			text.textProperty().bind(cell.itemProperty());
			return cell;
		});

		DescriptionCol1.setStyle("-fx-alignment: CENTER;");
		DescriptionCol1.setMinWidth(150);
		DescriptionCol1.setCellValueFactory(new PropertyValueFactory<Place, String>("Description"));

		mapCol1.setStyle("-fx-alignment: CENTER;");
		mapCol1.setMinWidth(50);
		mapCol1.setCellValueFactory(new PropertyValueFactory<Place, String>("numOfmaps"));

		flPlace = new FilteredList<Place>(dataPlace, p -> true);
		flPlace1 = new FilteredList<Place>(dataPlace2, p -> true);
		// flPlace1 = new FilteredList<Place>(dataPlace, p -> true);
		// Pass the dataCity to a filtered list
		searchTable1.setItems(flPlace1);// Set the table's items using the filtered list
		searchTable1.getColumns().addAll(PlaceCol1, CityCol1, DescriptionCol1, mapCol1);

		searchTable1.setVisible(false);
		searchTable1.setDisable(true);

		searchTable.getColumns().clear();
		searchTable.setEditable(true);

		CityCol.setStyle("-fx-alignment: CENTER;");
		CityCol.setMinWidth(100);
		CityCol.setCellValueFactory(new PropertyValueFactory<City, String>("city"));

		DescriptionCol.setCellFactory(tc -> {
			TableCell<City, String> cell = new TableCell<>();
			Text text = new Text();
			cell.setGraphic(text);
			cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
			text.wrappingWidthProperty().bind(DescriptionCol.widthProperty());
			text.textProperty().bind(cell.itemProperty());
			return cell;
		});
		DescriptionCol.setStyle("-fx-alignment: CENTER;");
		DescriptionCol.setMinWidth(150);
		DescriptionCol.setCellValueFactory(new PropertyValueFactory<City, String>("Description"));

		mapCol.setStyle("-fx-alignment: CENTER;");
		mapCol.setMinWidth(50);
		mapCol.setCellValueFactory(new PropertyValueFactory<City, String>("numOfMaps"));

		placeCol.setStyle("-fx-alignment: CENTER;");
		placeCol.setMinWidth(50);
		placeCol.setCellValueFactory(new PropertyValueFactory<City, String>("numOfPlaces"));

		pathCol.setStyle("-fx-alignment: CENTER;");
		pathCol.setMinWidth(50);
		pathCol.setCellValueFactory(new PropertyValueFactory<City, String>("numOfPaths"));

		flCity = new FilteredList<City>(dataCity, p -> true);// Pass the dataCity to a filtered list
		searchTable.setItems(flCity);// Set the table's items using the filtered list
		searchTable.getColumns().addAll(CityCol, DescriptionCol, mapCol, placeCol, pathCol);

		searchText.setOnKeyReleased(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent ke) {
				switch (comboBox.getValue()) {
				case "City":
					flCity.setPredicate(
							p -> p.getCity().toLowerCase().contains(searchText.getText().toLowerCase().trim()));
					flPlace2 = new FilteredList<Place>(dataPlace, p -> true);
					flPlace2.setPredicate(
							p -> p.getPlaceName().toLowerCase().contains(searchText.getText().toLowerCase().trim()));
					break;
				case "Place":
					flCity.setPredicate(
							p -> p.getPlaces().toLowerCase().contains(searchText.getText().toLowerCase().trim()));
					flPlace2 = new FilteredList<Place>(dataPlace, p -> true);
					flPlace2.setPredicate(
							p -> p.getPlaceName().toLowerCase().contains(searchText.getText().toLowerCase().trim()));
					break;
				case "Description":
					flCity.setPredicate(
							p -> p.getDescription().toLowerCase().contains(searchText.getText().toLowerCase().trim()));
					break;
				}
			}
		});

		comboBox.getSelectionModel().selectFirst();

	}

	public void buildData(String type) throws UnknownHostException, IOException {
		@SuppressWarnings("resource")
		Socket socket = new Socket(Globals.IpAddress, 5555);

		String[] get = new String[4];
		get[0] = "getCatalog";
		get[1] = "0";
		if (type.equals("place")) {
			get[0] = "getPlaceCatalog";
			// get[0] = "getPlaces";
			// get[1] = null;
			// get[2] = null;
			get[1] = "0";
		} else if (type.equals("Oplace")) {
			get[0] = "getOPlaceCatalog";
			// get[0] = "getPlaces";
			// get[1] = null;
			// get[2] = null;
			get[1] = "0";
		}
		try {
			ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
			objectOutput.writeObject(get);
			try {
				ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());
				try {
					Object[] object = (Object[]) objectInput.readObject();
					if (type.equals("city")) {
						for (int i = 1; i <= (int) object[0]; i++) {
							dataCity.add((City) object[i]);
						}
					} else if (type.equals("place")) {
						for (int i = 1; i <= (int) object[0]; i++) {
							dataPlace.add((Place) object[i]);

						}
					} else {
						for (int i = 1; i <= (int) object[0]; i++) {
							dataPlace2.add((Place) object[i]);

						}
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

	public void SearchCombination() {

		String place = placeField.getText();
		String city = searchText.getText();

		flPlace1 = new FilteredList<Place>(dataPlace, p -> true);
		flPlace1.setPredicate(p -> p.getPlaceName().toLowerCase().contains(placeField.getText().toLowerCase().trim()));
		// searchTable1.setItems(flPlace1);
		flPlace2 = new FilteredList<Place>(flPlace1, p -> true);
		flPlace2.setPredicate(p -> p.getCityName().toLowerCase().contains(searchText.getText().toLowerCase().trim()));
		searchTable1.setItems(flPlace2);
		System.out.println(place + "'" + city);

	}

}
