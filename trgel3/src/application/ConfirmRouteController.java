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
import javafx.util.Callback;

public class ConfirmRouteController {

	@FXML
	private Button btn_message;

	@FXML
	private Button myMaps;

	@FXML
	private Button Back;

	@FXML
	private Button OneTimePurchase;

	@FXML
	private Button FixedTimePurchase;

	@FXML
	private ComboBox<String> comboBox;

	@FXML
	private TextField searchText = new TextField();

	@FXML
	private TableView<Route> searchTable;

	@FXML
	private TableColumn<Route, String> CityCol;

	@FXML
	private TableColumn<Route, String> DescriptionCol;

	@FXML
	private TableColumn<City, String> routeCol;

	@FXML
	private TableColumn<City, String> placeCol;

	@FXML
	private TableColumn<City, String> pathCol;

	@FXML
	private TableColumn<Route, String> UpdateCol;

	@FXML
	private TableView<Place> searchTable1;

	@FXML
	private TableColumn<Place, String> PlaceCol1;

	@FXML
	private TableColumn<Place, String> CityCol1;

	@FXML
	private TableColumn<Place, String> DescriptionCol1;

	@FXML
	private TableColumn<Place, String> routeCol1;

	private ObservableList<City> dataCity = FXCollections.observableArrayList();

	private ObservableList<Place> dataPlace = FXCollections.observableArrayList();

	private ObservableList<Route> dataRoute = FXCollections.observableArrayList();

	FilteredList<City> flCity = null;

	FilteredList<Route> flRoute = null;

	FilteredList<Place> flPlace = null;

	@FXML
	private Button searchCity;

	@FXML
	private Button searchPlace;

	@FXML
	private Button btn_AddLoc;
	
	@FXML
	private HBox hbox;
	
	TextField placeField= new TextField();

	@FXML
	void searchCityBtn(ActionEvent event) {
		searchPlace.getStyleClass().remove("addBobOk");
		searchCity.getStyleClass().removeAll("addBobOk, focus");
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
		searchPlace.getStyleClass().add("addBobOk");
		
		comboBox.getItems().add("City & place");

		searchTable.setVisible(false);
		searchTable.setDisable(true);

		searchTable1.setVisible(true);
		searchTable1.setDisable(false);
		
		comboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
		      @Override public void changed(ObservableValue<? extends String> selected, String old, String newVal) {
		          if (newVal != null) {
		            switch(newVal) {
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
					flPlace.setPredicate(
							p -> p.getPlaceName().toLowerCase().contains(placeField.getText().toLowerCase().trim()));
					break;
				}
			}
		});
	
		searchText.setOnKeyReleased(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent ke) {
				switch (comboBox.getValue()) {
				case "City":
					flPlace.setPredicate(
							p -> p.getCityName().toLowerCase().contains(searchText.getText().toLowerCase().trim()));
					break;
				case "Place":
					flPlace.setPredicate(
							p -> p.getPlaceName().toLowerCase().contains(searchText.getText().toLowerCase().trim()));
					break;
				case "Description":
					flPlace.setPredicate(
							p -> p.getDescription().toLowerCase().contains(searchText.getText().toLowerCase().trim()));
					break;
				case "City & place":
					flPlace.setPredicate(
							p -> p.getCityName().toLowerCase().contains(searchText.getText().toLowerCase().trim()));
					break;
				}
			}
		});

	}

	@FXML
	void showMymaps(ActionEvent event) throws IOException {

		Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		URL url = getClass().getResource("MyMapsScene.fxml");
		AnchorPane pane = FXMLLoader.load(url);
		Globals.backLink = "DefaultPage.fxml";
		Scene scene = new Scene(pane);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	@FXML
	void OnePurchase(ActionEvent event) {

	}

	@FXML
	void FixedPurchase(ActionEvent event) throws UnknownHostException, IOException {

		addDataBasetoMember();

		Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		URL url = getClass().getResource("mapCatalogScene.fxml");
		Globals.backLink = "DefaultPage.fxml";
		AnchorPane pane;
		try {
			pane = FXMLLoader.load(url);
			Scene scene = new Scene(pane);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() throws IOException, Exception {
		Globals.backLink = "MainPage.fxml";

		searchCity.getStyleClass().removeAll("addBobOk, focus");
		searchCity.getStyleClass().add("addBobOk");

		// buildData("city");
		buildData("place");
		buildData("route");

		comboBox.getItems().addAll("City", "Place", "Description");
		searchText.setPromptText("Write here");

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

		routeCol1.setStyle("-fx-alignment: CENTER;");
		routeCol1.setMinWidth(50);
		routeCol1.setCellValueFactory(new PropertyValueFactory<Place, String>("numOfmaps"));

		flPlace = new FilteredList<Place>(dataPlace, p -> true);// Pass the dataCity to a filtered list
		searchTable1.setItems(flPlace);// Set the table's items using the filtered list
		searchTable1.getColumns().addAll(PlaceCol1, CityCol1, DescriptionCol1, routeCol1);

		searchTable1.setVisible(false);
		searchTable1.setDisable(true);

		searchTable.getColumns().clear();
		searchTable.setEditable(true);

		searchTable.setRowFactory(tv -> {
			TableRow<Route> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 1 && (!row.isEmpty())) {
					Route RouteRow = searchTable.getSelectionModel().getSelectedItem();
					Globals.route = (Route) RouteRow;

				}
			});
			return row;
		});

		CityCol.setStyle("-fx-alignment: CENTER;");
		CityCol.setMinWidth(100);
		CityCol.setCellValueFactory(new PropertyValueFactory<Route, String>("city"));

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

		UpdateCol.setStyle("-fx-alignment: CENTER;");
		UpdateCol.setMinWidth(50);
		UpdateCol.setCellValueFactory(new PropertyValueFactory<>("numOfPaths"));
		Globals.ThereIsRouteUpdate = false;
		Callback<TableColumn<Route, String>, TableCell<Route, String>> cellFactory = new Callback<TableColumn<Route, String>, TableCell<Route, String>>() {

			@Override
			public TableCell<Route, String> call(TableColumn<Route, String> param) {
				// TODO Auto-generated method stub
				final TableCell<Route, String> cell = new TableCell<Route, String>() {

					final Button btn = new Button("New Update");

					@Override
					public void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setGraphic(null);
							setText(null);
						} else {
							Route route1 = getTableView().getItems().get(getIndex());
							if (route1.getNewUpdate() == 0) {
								btn.setText("Edit");

							} else
								Globals.ThereIsRouteUpdate = true;

							btn.setOnAction(event -> {
								Globals.route = route1;
								Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
								URL url = getClass().getResource("AddRLocations.fxml");
								Globals.backLink = "ConfirmRoute.fxml";
								AnchorPane pane;
								try {
									pane = FXMLLoader.load(url);
									Scene scene = new Scene(pane);
									scene.getStylesheets()
											.add(getClass().getResource("application.css").toExternalForm());
									primaryStage.setScene(scene);
									primaryStage.show();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							});
							setGraphic(btn);
							setText(null);
						}
					}
				};
				return cell;
			}
		};
		UpdateCol.setCellFactory(cellFactory);

		flRoute = new FilteredList<Route>(dataRoute, p -> true);// Pass the data to a filtered list
		searchTable.setItems(flRoute);// Set the table's items using the filtered list
		searchTable.getColumns().addAll(CityCol, DescriptionCol, UpdateCol);

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

		comboBox.getSelectionModel().selectFirst();

	}

	public void addDataBasetoMember() throws UnknownHostException, IOException {
		@SuppressWarnings("resource")
		Socket socket = new Socket("localhost", 5555);
		dataCity = FXCollections.observableArrayList();

		String[] set = new String[3];
		set[0] = "addCityToMember";
		set[1] = Globals.user.getUserName();
		set[2] = Globals.city.getCity();
		try {
			ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
			objectOutput.writeObject(set);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void buildData(String type) throws UnknownHostException, IOException {
		@SuppressWarnings("resource")
		Socket socket = new Socket("localhost", 5555);

		String[] get = new String[2];
		get[0] = "getCatalog";
		get[1] = "-1";
		if (type.equals("place")) {
			get[0] = "getPlaceCatalog";
		} else if (type.equals("route")) {
			get = new String[3];
			get[0] = "getRoutes";
			get[1] = Globals.city.getCity();
			get[2] = "-1";
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
					} else if (type.equals("route")) {
						for (int i = 1; i <= (int) object[0]; i++) {
							dataRoute.add((Route) object[i]);
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

	@FXML
	void backFunc(ActionEvent event) throws IOException {

		Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		URL url = getClass().getResource("ConfirmRoutes.fxml");
		AnchorPane pane = FXMLLoader.load(url);
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
		Globals.backLink = "ConfirmRoute.fxml";
		Scene scene = new Scene(pane);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();

	}

}
