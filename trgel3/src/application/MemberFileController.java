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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

public class MemberFileController {

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
	private TableView<User> searchTable;

	@FXML
	private TableColumn<User, String> fnameCol;

	@FXML
	private TableColumn<User, String> lnameCol;

	@FXML
	private TableColumn<User, String> pnumberCol;

	@FXML
	private TableColumn<User, String> emailCol;

	@FXML
	private TableColumn<User, String> usernameCol;
	
	@FXML
	private TableColumn<User, String> passwordCol;
	
	@FXML
	private TableColumn<User, String> typeCol;
	
	@FXML
	private TableColumn<User, String> EditCol;

	private TableColumn buttonCol;
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

	private ObservableList<User> dataUser = FXCollections.observableArrayList();

	private ObservableList<Place> dataPlace = FXCollections.observableArrayList();

	FilteredList<User> flUser = null;

	FilteredList<Place> flPlace = null;

	@FXML
	private Button searchCity;

	@FXML
	private Button searchPlace;

	@FXML
	private Button btn_AddLoc;

	@FXML
	void searchCityBtn(ActionEvent event) {
		searchPlace.getStyleClass().remove("addBobOk");
		searchCity.getStyleClass().removeAll("addBobOk, focus");
		searchCity.getStyleClass().add("addBobOk");

		searchTable1.setVisible(false);
		searchTable1.setDisable(true);

		searchTable.setVisible(true);
		searchTable.setDisable(false);

		searchText.setOnKeyReleased(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent ke) {
				switch (comboBox.getValue()) {
				case "City":
					flUser.setPredicate(
							p -> p.getUserName().toLowerCase().contains(searchText.getText().toLowerCase().trim()));
					break;
				case "Place":
					flUser.setPredicate(
							p -> p.getEmail().toLowerCase().contains(searchText.getText().toLowerCase().trim()));
					break;
				case "Description":
					flUser.setPredicate(
							p -> p.getPhoneNumber().toLowerCase().contains(searchText.getText().toLowerCase().trim()));
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

		searchTable.setVisible(false);
		searchTable.setDisable(true);

		searchTable1.setVisible(true);
		searchTable1.setDisable(false);

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
	}

	@SuppressWarnings("unchecked")
	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() throws IOException, Exception {
		Globals.backLink = "MainPage.fxml";

		//searchCity.getStyleClass().removeAll("addBobOk, focus");
		//searchCity.getStyleClass().add("addBobOk");

		buildData("user");
		
		comboBox.getItems().addAll("City", "Place", "Description");
		searchText.setPromptText("Write here");

		flPlace = new FilteredList<Place>(dataPlace, p -> true);// Pass the dataCity to a filtered list

		searchTable.getColumns().clear();
		searchTable.setEditable(true);

		searchTable.setRowFactory(tv -> {
			TableRow<User> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 1 && (!row.isEmpty())) {
					User userRow = searchTable.getSelectionModel().getSelectedItem();
					//Globals.user = (User) userRow;

				}
			});
			return row;
		});

		fnameCol.setStyle("-fx-alignment: CENTER;");
		fnameCol.setMinWidth(100);
		fnameCol.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));

		usernameCol.setCellFactory(tc -> {
			TableCell<User, String> cell = new TableCell<>();
			Text text = new Text();
			cell.setGraphic(text);
			cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
			text.wrappingWidthProperty().bind(usernameCol.widthProperty());
			text.textProperty().bind(cell.itemProperty());
			return cell;
		});
		usernameCol.setStyle("-fx-alignment: CENTER;");
		usernameCol.setMinWidth(150);
		usernameCol.setCellValueFactory(new PropertyValueFactory<User, String>("userName"));

		lnameCol.setStyle("-fx-alignment: CENTER;");
		lnameCol.setMinWidth(50);
		lnameCol.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));

		pnumberCol.setStyle("-fx-alignment: CENTER;");
		pnumberCol.setMinWidth(50);
		pnumberCol.setCellValueFactory(new PropertyValueFactory<User, String>("phoneNumber"));

		emailCol.setStyle("-fx-alignment: CENTER;");
		emailCol.setMinWidth(50);
		emailCol.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
		
		passwordCol.setStyle("-fx-alignment: CENTER;");
		passwordCol.setMinWidth(50);
		passwordCol.setCellValueFactory(new PropertyValueFactory<User, String>("password"));
		
		typeCol.setStyle("-fx-alignment: CENTER;");
		typeCol.setMinWidth(50);
		typeCol.setCellValueFactory(new PropertyValueFactory<User, String>("type"));
		
		EditCol.setStyle("-fx-alignment: CENTER;");
		EditCol.setMinWidth(50);
		EditCol.setCellValueFactory(new PropertyValueFactory<>("type"));

		Callback<TableColumn<User, String>, TableCell<User, String>> cellFactory = new Callback<TableColumn<User, String>, TableCell<User, String>>() {

			@Override
			public TableCell<User, String> call(TableColumn<User, String> param) {
				// TODO Auto-generated method stub
				final TableCell<User, String> cell = new TableCell<User, String>() {

					final Button btn = new Button("Edit");

					@Override
					public void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setGraphic(null);
							setText(null);
						} else {
							User user1 = getTableView().getItems().get(getIndex());
							//if (!city1.getNewUpdate()) {
							//	btn.setText("Edit");
								//Globals.ThereIsCityUpdate = false;
							//} else
								//Globals.ThereIsCityUpdate = true;

							btn.setOnAction(event -> {
								//Globals.city = city1;
								Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
								URL url = getClass().getResource("ConfirmMap.fxml");
								AnchorPane pane;
								try {
									pane = FXMLLoader.load(url);
									Scene scene = new Scene(pane);
									scene.getStylesheets()
											.add(getClass().getResource("application.css").toExternalForm());
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

							});
							setGraphic(btn);
							setText(null);
						}
					}
				};
				return cell;
			}
		};
		EditCol.setCellFactory(cellFactory);

		flUser = new FilteredList<User>(dataUser, p -> true);// Pass the data to a filtered list
		searchTable.setItems(flUser);// Set the table's items using the filtered list
		searchTable.getColumns().addAll(fnameCol, lnameCol, pnumberCol, emailCol, usernameCol, passwordCol,typeCol,EditCol);

		searchText.setOnKeyReleased(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent ke) {
				switch (comboBox.getValue()) {
				case "City":
					flUser.setPredicate(
							p -> p.getUserName().toLowerCase().contains(searchText.getText().toLowerCase().trim()));
					break;
				case "Place":
					flUser.setPredicate(
							p -> p.getEmail().toLowerCase().contains(searchText.getText().toLowerCase().trim()));
					break;
				case "Description":
					flUser.setPredicate(
							p -> p.getPhoneNumber().toLowerCase().contains(searchText.getText().toLowerCase().trim()));
					break;
				}
			}
		});

		comboBox.getSelectionModel().selectFirst();

	}

	public void addDataBasetoMember() throws UnknownHostException, IOException {
		@SuppressWarnings("resource")
		Socket socket = new Socket("localhost", 5555);
		dataUser = FXCollections.observableArrayList();

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

		String[] get = new String[1];
		get[0] = "getUsers";
		if (type.equals("place")) {
			get[0] = "getPlaceCatalog";
		}

		try {
			ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
			objectOutput.writeObject(get);
			try {
				ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());
				try {
					Object[] object = (Object[]) objectInput.readObject();
					if (type.equals("user")) {
						for (int i = 1; i <= (int) object[0]; i++) {
							dataUser.add((User) object[i]);
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
		URL url = getClass().getResource("ManagerPage.fxml");
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

	@FXML
	void addLoc(ActionEvent event) throws IOException {

		Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		URL url = getClass().getResource("AddLocations.fxml");
		AnchorPane pane = FXMLLoader.load(url);
		Globals.backLink = "DefaultPage.fxml";
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

	@FXML
	void messageFunc(ActionEvent event) throws IOException {

		Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		URL url = getClass().getResource("Messages.fxml");
		AnchorPane pane = FXMLLoader.load(url);
		Globals.backLink = "ConfirmMaps.fxml";
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
