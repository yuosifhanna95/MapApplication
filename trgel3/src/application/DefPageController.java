package application;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DefPageController {

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

	private static ObservableList<Map> dataMaps = FXCollections.observableArrayList();

	private static ObservableList<Route> dataRoutes = FXCollections.observableArrayList();

	FilteredList<City> flCity = null;

	FilteredList<Place> flPlace = null;

	@FXML
	private Button searchCity;

	@FXML
	private Button searchPlace;

	@FXML
	private Button btn_AddLoc;

	private static DirectoryChooser directoryChooser;

	static File dir;

	static TextField area;

	@FXML
	private ImageView MainImage;

	@FXML
	private AnchorPane mainPane;

	@FXML
	private Label Lab;

	private Label[] labels = new Label[10];
	private ImageView[] locations = new ImageView[10];
	private Place[] Places = new Place[10];
	private ImagePlace[] ImagePlaces = new ImagePlace[10];
	private int Counter = 0;
	private double aspect = 0.75f;

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
		primaryStage.show();

	}

	@FXML
	void OnePurchase(ActionEvent event) {
		final Stage onePurchaseWindow = new Stage();
		onePurchaseWindow.initModality(Modality.APPLICATION_MODAL);

		Text text = new Text(Globals.city.getCity() + "'s package is " + Globals.city.getOneTimeCost() + "$");

		area = new TextField("No file selected");
		area.setDisable(true);

		Button buttonSelect = new Button("Select File");
		Button buttonSave = new Button("Save");
		Button buttonCancel = new Button("Cancel");
		buttonCancel.setOnAction(e -> onePurchaseWindow.close());
		buttonSelect.setOnAction(e -> {
			selectFolder();
		});
		buttonSave.setOnAction(e -> {
			try {
				// saveToFile();
				saveAllMaps();
				@SuppressWarnings("resource")
				Socket socket = new Socket("localhost", 5555);
				Object[] array = new Object[3];
				array[0] = "OneTimePurchase";
				// get[1] = "" + ImagePlaces[i].getPlace().getCityName();
				array[1] = Globals.user;
				array[2] = Globals.city.getCity();

				ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
				objectOutput.writeObject(array);

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			onePurchaseWindow.close();
		});

		HBox layout = new HBox(20);

		layout.getChildren().add(buttonCancel);
		layout.getChildren().add(buttonSave);
		layout.setAlignment(Pos.CENTER);

		HBox hbox = new HBox(area, buttonSelect);
		hbox.setAlignment(Pos.CENTER);

		VBox layoutV = new VBox(20);
		layoutV.getChildren().add(text);
		layoutV.getChildren().add(hbox);
		layoutV.getChildren().add(layout);
		layoutV.setAlignment(Pos.CENTER);
		Scene dialogScene = new Scene(layoutV, 300, 200);
		onePurchaseWindow.setScene(dialogScene);
		onePurchaseWindow.show();

	}

	@FXML
	void FixedPurchase(ActionEvent event) throws UnknownHostException, IOException {
		addDataBasetoMember();
		Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		URL url = getClass().getResource("payementforpurchase.fxml");
		AnchorPane pane = FXMLLoader.load(url);

		Globals.backLink = "DefaultPage.fxml";

		Scene scene = new Scene(pane);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	@SuppressWarnings("unchecked")
	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() throws IOException, Exception {
		Globals.backLink = "MainPage.fxml";

		searchCity.getStyleClass().removeAll("addBobOk, focus");
		searchCity.getStyleClass().add("addBobOk");

		buildData("city");
		buildData("place");

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

		mapCol1.setStyle("-fx-alignment: CENTER;");
		mapCol1.setMinWidth(50);
		mapCol1.setCellValueFactory(new PropertyValueFactory<Place, String>("numOfmaps"));

		flPlace = new FilteredList<Place>(dataPlace, p -> true);// Pass the dataCity to a filtered list
		searchTable1.setItems(flPlace);// Set the table's items using the filtered list
		searchTable1.getColumns().addAll(PlaceCol1, CityCol1, DescriptionCol1, mapCol1);

		searchTable1.setVisible(false);
		searchTable1.setDisable(true);

		searchTable.getColumns().clear();
		searchTable.setEditable(true);

		searchTable.setRowFactory(tv -> {
			TableRow<City> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 1 && (!row.isEmpty())) {
					City cityRow = searchTable.getSelectionModel().getSelectedItem();
					Globals.city = (City) cityRow;
				}
			});
			return row;
		});

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

		flCity = new FilteredList<City>(dataCity, p -> true);// Pass the data to a filtered list
		searchTable.setItems(flCity);// Set the table's items using the filtered list
		searchTable.getColumns().addAll(CityCol, DescriptionCol, mapCol, placeCol, pathCol);

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

		String[] get = new String[1];
		get[0] = "getCatalog";
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
					if (type.equals("city")) {
						for (int i = 1; i <= (int) object[0]; i++) {
							dataCity.add((City) object[i]);
						}
					} else {
						for (int i = 1; i <= (int) object[0]; i++) {
							dataPlace.add((Place) object[i]);
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
		URL url = getClass().getResource("MainPage.fxml");
		AnchorPane pane = FXMLLoader.load(url);
		Scene scene = new Scene(pane);
		scene.getStylesheets().add(getClass().getResource("styleMain.css").toExternalForm());
		primaryStage.setScene(scene);
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
		primaryStage.show();
	}

	@FXML
	void messageFunc(ActionEvent event) throws IOException {

		Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		URL url = getClass().getResource("Messages.fxml");
		AnchorPane pane = FXMLLoader.load(url);
		Globals.backLink = "DefaultPage.fxml";
		Scene scene = new Scene(pane);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	public static void getFromCity(String str) throws UnknownHostException, IOException {
		@SuppressWarnings("resource")
		Socket socket = new Socket("localhost", 5555);

		String[] get = new String[2];
		get[0] = "getMyRoutes";
		if (str.equals("Maps")) {
			get[0] = "getMyMaps";
		}
		get[1] = Globals.city.getCity();
		try {
			ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
			objectOutput.writeObject(get);
			try {
				ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());
				try {
					Object[] object = (Object[]) objectInput.readObject();
					if (str.equals("Maps")) {
						for (int i = 1; i <= (int) object[0]; i++) {
							dataMaps.add((Map) object[i]);
						}
					} else {
						for (int i = 1; i <= (int) object[0]; i++) {
							dataRoutes.add((Route) object[i]);
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

	public static void selectFolder() {
		directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle("Select Folder");
		directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		dir = directoryChooser.showDialog(null);
		if (dir != null)
			area.setText(dir.getAbsolutePath());
	}

	public static void saveToFile() throws IOException {
		if (dir != null) {
			getFromCity("Maps");
			getFromCity("Routes");

			dataMaps.forEach((Map) -> {
				URL url;
				try {
					url = new URL(Map.getLinkCustomer());
					BufferedImage image23 = ImageIO.read(url);

					try {
						File file = new File(dir + "/" + Map.getCity() + Map.getId() + "Map.png");
						ImageIO.write(image23, "png", file);
					} catch (IOException ex) {
						ex.printStackTrace();
					}

				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});

			dataRoutes.forEach((Route) -> {
				URL url;
				try {
					url = new URL(Route.getLink());
					BufferedImage image23 = ImageIO.read(url);

					try {
						File file = new File(dir + "/" + Route.getCity() + Route.getId() + "Route.png");
						ImageIO.write(image23, "png", file);
					} catch (IOException ex) {
						ex.printStackTrace();
					}

				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
		}
	}

	public void saveAllMaps() throws ClassNotFoundException, IOException {

		@SuppressWarnings("resource")
		Socket socket = new Socket("localhost", 5555);
		String[] array = new String[2];
		array[0] = "getMaps";
		array[1] = Globals.city.getCity();

		ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
		objectOutput.writeObject(array);

		ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());
		Object data = objectInput.readObject();
		selectFolder();
		for (int i = 1; i < ((Object[]) data).length; i++) {

			Map map = (Map) (((Object[]) data)[i]);
			Globals.map = map;
			mainPane = new AnchorPane();
			final Stage DownloadPage = new Stage();
			DownloadPage.initModality(Modality.APPLICATION_MODAL);

			Text text = new Text(Globals.city.getCity() + "'s package is " + Globals.city.getOneTimeCost() + "$");

			area = new TextField("No file selected");
			area.setDisable(true);

			HBox layout = new HBox(20);

			layout.setAlignment(Pos.CENTER);
			layout.getChildren().add(mainPane);

			HBox hbox = new HBox(area, mainPane);
			hbox.setAlignment(Pos.CENTER);

			VBox layoutV = new VBox(20);
			layoutV.getChildren().add(text);
			layoutV.getChildren().add(hbox);
			layoutV.getChildren().add(layout);
			layoutV.setAlignment(Pos.CENTER);
			Scene DownlaodDialog = new Scene(layoutV, 1, 1);
			DownloadPage.setScene(DownlaodDialog);
			DownloadPage.show();

			Image LinkedImage = new Image(map.getLinkCustomer());
			MainImage = new ImageView(LinkedImage);
			MainImage.relocate(162, 50);
			MainImage.setFitWidth(LinkedImage.getWidth());
			MainImage.setFitHeight(LinkedImage.getHeight());

			mainPane.getChildren().add(MainImage);
			// Globals.map = map;

			DrawPlaces();

			FileChooser fileChooser = new FileChooser();

			// Set extension filter
			fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("png files (*.png)", "*.png"));

			// Prompt user to select a file
			// File file = fileChooser.showSaveDialog(null);

			// dir=directoryChooser.showDialog(null);
			File file = new File(dir + "/" + map.getCity() + "Map" + map.getId() + ".png");
			if (file != null) {
				try {
					// Pad the capture area
					WritableImage writableImage = new WritableImage((int) MainImage.getFitWidth(),
							(int) MainImage.getFitHeight());
					// WritableImage writableImage = new WritableImage((int)2000 , (int)2000);
					SnapshotParameters params = new SnapshotParameters();

					params.setViewport(new Rectangle2D(MainImage.getLayoutX(), MainImage.getLayoutY(),
							MainImage.getFitWidth(), MainImage.getFitHeight()));
					((Node) mainPane).snapshot(params, writableImage);
					// writableImage= pixelScaleAwareCanvasSnapshot(mainPane,params,2);
					// ((Node)mainPane).snapshot()
					// ((Node)event.getSource()).snapshot(null, writableImage);
					RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
					// Write the snapshot to the chosen file
					ImageIO.write(renderedImage, "png", file);
					DownloadPage.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}

		}

	}

	@SuppressWarnings("deprecation")
	void DrawPlaces() throws IOException, ClassNotFoundException {

		// ImagePlaces = null;
		Counter = 0;
		for (int i = 0; i < ImagePlaces.length; i++) {

			if (ImagePlaces[i] != null) {

				mainPane.getChildren().removeAll(ImagePlaces[i].getImageview());
				mainPane.getChildren().removeAll(ImagePlaces[i].getLabel());

			}

		}
		ImagePlaces = new ImagePlace[10];

		@SuppressWarnings("resource")
		Socket socket = new Socket("localhost", 5555);
		String[] array = new String[2];
		String[] array2 = new String[2];
		array[0] = "getPlaces";
		// get[1] = "" + ImagePlaces[i].getPlace().getCityName();
		array[1] = "" + Globals.map.getId();

		ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
		objectOutput.writeObject(array);

		Object data;
		ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());
		data = objectInput.readObject();
		// System.out.println(((Object[]) data)[0]);
		Place[] list = (Place[]) ((Object[]) data)[1];

		array2[0] = "getUPlaces";
		// get[1] = "" + ImagePlaces[i].getPlace().getCityName();
		array2[1] = "" + Globals.map.getId();
		Socket socket2 = new Socket("localhost", 5555);
		objectOutput = new ObjectOutputStream(socket2.getOutputStream());
		objectOutput.writeObject(array2);

		Object data2;
		ObjectInputStream objectInput2 = new ObjectInputStream(socket2.getInputStream());
		data2 = objectInput2.readObject();
		// System.out.println(((Object[]) data2)[0]);
		UPlace[] list2 = (UPlace[]) ((Object[]) data2)[1];

		int counter = 0;
		int flag = 0;
		for (int i = 0; i < list.length; i++) {
			for (int c = 0; c < list2.length; c++) {
				// if (Globals.map.getId() == list2[c].getSerialID()) {
				if (list[i].getSerialID() == list2[c].getPlaceId()) {
					Image im = new Image("File:loc.png");
					ImageView newLoc = new ImageView(im);
					// newLoc.relocate((double) list2[c].getLocX(), (double) list2[c].getLocY());
					Label label = new Label();
					// label.relocate((double) list2[c].getLocX(), (double) list2[c].getLocY());

					newLoc.relocate(list2[c].getLocX() - (im.getWidth() / 2) * aspect,
							list2[c].getLocY() - (im.getHeight() / 2) * aspect);

					label.setFont(new Font("Quicksand", 20));
					UPlace p = new UPlace("" + Globals.map.getId(), list2[c].getCityName(), list2[c].getPlaceName(),
							list2[c].getDescription(), list2[c].getClassification(), list2[c].getAccessibility(),
							list2[c].getSerialID(), list2[c].getLocX(), list2[c].getLocY(), list2[c].getType(),
							list2[c].getNumOfmaps(), list2[c].getPlaceId());
					label.setText(p.getPlaceName());
					Text t = new Text();
					t.setText(label.getText());
					t.setFont(label.getFont());
					double width = t.getBoundsInLocal().getWidth();
					double height = t.getBoundsInLocal().getHeight();

					label.relocate(list2[c].getLocX() - (width / 2),
							list2[c].getLocY() - (height + (im.getHeight() / 2) * aspect));

					newLoc.setFitHeight(im.getHeight() * aspect);
					newLoc.setFitWidth(im.getWidth() * aspect);
					newLoc.setId("imv" + Counter);

					ImagePlaces[counter] = new ImagePlace(counter, newLoc, label, p, list2[c].getLocX(),
							list2[c].getLocY(), false);
					counter++;
					Counter++;
					flag = 1;
					break;
				}

				// }
			}
			if (flag == 0) {
				Image im = new Image("File:loc.png");
				ImageView newLoc = new ImageView(im);
				double X = list[i].getLocX(), Y = list[i].getLocY();
				// newLoc1.relocate(X, Y);
				Label label = new Label();
				// label.relocate(X, Y);

				newLoc.relocate(list[i].getLocX() - (im.getWidth() / 2) * aspect,
						list[i].getLocY() - (im.getHeight() / 2) * aspect);

				label.setFont(new Font("Quicksand", 20));
				Place p = new Place("" + Globals.map.getId(), list[i].getCityName(), list[i].getPlaceName(),
						list[i].getDescription(), list[i].getClassification(), list[i].getAccessibility(),
						list[i].getSerialID(), list[i].getLocX(), list[i].getLocY(), list[i].getType(),
						list[i].getNumOfmaps());
				label.setText(p.getPlaceName());

				Text t = new Text();
				t.setText(label.getText());
				t.setFont(label.getFont());
				double width = t.getBoundsInLocal().getWidth();
				double height = t.getBoundsInLocal().getHeight();

				label.relocate(list[i].getLocX() - (width / 2),
						list[i].getLocY() - (height + (im.getHeight() / 2) * aspect));

				newLoc.setFitHeight(im.getHeight() * aspect);
				newLoc.setFitWidth(im.getWidth() * aspect);
				newLoc.setId("imv" + Counter);

				ImagePlaces[counter] = new ImagePlace(counter, newLoc, label, p, list[i].getLocX(), list[i].getLocY(),
						false);
				System.out.println(ImagePlaces[counter].getLabel().getText());
				counter++;
				Counter++;

			}
			flag = 0;
		}

		for (int c = 0; c < list2.length; c++) {
			if (list2[c].getType().equals("NEW") || list2[c].getType().equals("UNEW")) {
				Image im = new Image("File:loc.png");
				ImageView newLoc = new ImageView(im);
				// newLoc.relocate((double) list2[c].getLocX(), (double) list2[c].getLocY());
				Label label = new Label();
				// label.relocate((double) list2[c].getLocX(), (double) list2[c].getLocY());

				newLoc.relocate(list2[c].getLocX() - (im.getWidth() / 2) * aspect,
						list2[c].getLocY() - (im.getHeight() / 2) * aspect);

				label.setFont(new Font("Quicksand", 20));
				UPlace p = new UPlace("" + Globals.map.getId(), list2[c].getCityName(), list2[c].getPlaceName(),
						list2[c].getDescription(), list2[c].getClassification(), list2[c].getAccessibility(),
						list2[c].getSerialID(), list2[c].getLocX(), list2[c].getLocY(), list2[c].getType(),
						list2[c].getNumOfmaps(), list2[c].getPlaceId());
				label.setText(p.getPlaceName());

				Text t = new Text();
				t.setText(label.getText());
				t.setFont(label.getFont());
				double width = t.getBoundsInLocal().getWidth();
				double height = t.getBoundsInLocal().getHeight();

				label.relocate(list2[c].getLocX() - (width / 2),
						list2[c].getLocY() - (height + (im.getHeight() / 2) * aspect));

				newLoc.setFitHeight(im.getHeight() * aspect);
				newLoc.setFitWidth(im.getWidth() * aspect);
				newLoc.setId("imv" + Counter);
				ImagePlaces[counter] = new ImagePlace(counter, newLoc, label, p, list2[c].getLocX(), list2[c].getLocY(),
						false);
				counter++;
				Counter++;
			}
		}

		for (int i = 0; i < ImagePlaces.length; i++) {

			if (ImagePlaces[i] != null) {

				mainPane.getChildren().addAll(ImagePlaces[i].getImageview());
				mainPane.getChildren().addAll(ImagePlaces[i].getLabel());

			}

		}

	}

}
