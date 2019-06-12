package application;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ChangePricesController {
	@FXML
	private TableView<City> cityTable;

	@FXML
	private TableColumn<City, String> CityName;

	@FXML
	private TableColumn<City, String> FixedCost;
	@FXML
	private TableColumn<City, String> OneTimeCost;

	private ObservableList<City> data = null;

	@FXML
	private Button Back;

	@FXML
	private Button Changeprices;

	@FXML
	void backFunc(ActionEvent event) throws IOException {

		Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		URL url = getClass().getResource("ManagerPage.fxml");
		AnchorPane pane = FXMLLoader.load(url);
		Scene scene = new Scene(pane);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@FXML
	void dochange(ActionEvent event) throws UnknownHostException, IOException {
		final Stage Confirmation = new Stage();
		Confirmation.initModality(Modality.APPLICATION_MODAL);
		Text text = new Text("new prices for  " + Globals.city.getCity());
		text.setFont(Font.font("Verdana", FontWeight.BOLD, 25));
		GridPane gridPane = new GridPane();
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		Text textPayment = new Text(" fixedcost of " + Globals.city.getCity() + " city ");
		textPayment.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
		gridPane.add(textPayment, 0, 0);
		Button buttonChange = new Button("Change");
		gridPane.add(buttonChange, 2, 0);
		gridPane.setAlignment(Pos.CENTER);
		ToggleGroup group = new ToggleGroup();
		ToggleGroup group1 = new ToggleGroup();
		RadioButton rb1 = new RadioButton();
		RadioButton rb2 = new RadioButton();
		RadioButton rb3 = new RadioButton();
		rb1.setText(" the old fixedcost is " + Globals.city.getFixedCost());
		rb2.setText(" the old onetimecost is " + Globals.city.getOneTimeCost());
		gridPane.add(rb1, 0, 1);
		rb1.setToggleGroup(group);
		rb2.setToggleGroup(group1);
		rb1.setSelected(true);
		rb2.setSelected(true);
		RadioButton rb = new RadioButton();
		TextField newfixedcost = new TextField();
		TextField newonetimecost = new TextField();
		HBox h = new HBox(20);
		HBox h1 = new HBox(20);
		h.getChildren().add(rb);
		h.getChildren().add(newfixedcost);
		h.setAlignment(Pos.CENTER);
		h1.getChildren().add(rb3);
		h1.getChildren().add(newonetimecost);
		h1.setAlignment(Pos.CENTER);
		gridPane.add(h, 0, 2);
		newfixedcost.setVisible(false);
		rb.setToggleGroup(group);
		rb.setVisible(false);
		buttonChange.setOnAction(e -> {
			newfixedcost.setVisible(true);
			rb.setVisible(true);
		});
		GridPane gridPane1 = new GridPane();
		gridPane1.setHgap(10);
		gridPane1.setVgap(10);
		Text textPayment1 = new Text(" onetimecost of " + Globals.city.getCity() + " city ");
		textPayment1.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
		gridPane1.add(textPayment1, 0, 0);
		Button buttonChange1 = new Button("Change");
		gridPane1.add(buttonChange1, 2, 0);

		gridPane1.add(rb2, 0, 1);
		gridPane1.add(h1, 0, 2);
		newonetimecost.setVisible(false);
		rb3.setToggleGroup(group1);
		rb3.setVisible(false);
		buttonChange1.setOnAction(e -> {
			newonetimecost.setVisible(true);
			rb3.setVisible(true);
		});
		gridPane1.setAlignment(Pos.CENTER);
		GridPane gridPane3 = new GridPane();
		Button buttonUpdate = new Button("UpdatePrice");
		Button buttonCancel = new Button("Cancel");
		gridPane3.add(buttonUpdate, 0, 0);
		gridPane3.add(buttonCancel, 1, 0);
		gridPane3.setAlignment(Pos.CENTER);

		buttonUpdate.setOnAction(e -> {
			if (rb1.isSelected() && rb2.isSelected()) {
				JOptionPane.showMessageDialog(null,
						"you don't update the price of the city,so we will not send a message to the directory maneger to confirm.");
			} else {
				@SuppressWarnings("resource")
				Socket socket = null;
				try {
					socket = new Socket("localhost", 5555);
				} catch (UnknownHostException e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
				} catch (IOException e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
				}
				Object[] get = new Object[2];

				if (!newonetimecost.isVisible())
					newonetimecost.setText("" + Globals.city.getOneTimeCost());
				if (!newfixedcost.isVisible())
					newfixedcost.setText("" + Globals.city.getFixedCost());
				NewPrices np = new NewPrices(Globals.city.getCity(), Integer.parseInt(newfixedcost.getText()),
						Integer.parseInt(newonetimecost.getText()));
				get[0] = "confirm the prices and save it";
				get[1] = np;
				try {
					ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
					objectOutput.writeObject(get);
					ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());
					try {
						Object data = objectInput.readObject();

						JOptionPane.showMessageDialog(null, (String) data);

					} catch (ClassNotFoundException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}

		});
		VBox layoutV = new VBox(20);
		layoutV.getChildren().add(text);

		layoutV.getChildren().add(gridPane);
		layoutV.getChildren().add(gridPane1);
		layoutV.getChildren().add(gridPane3);
		layoutV.setAlignment(Pos.CENTER);
		Scene dialogScene = new Scene(layoutV, 500, 400);
		Confirmation.setScene(dialogScene);
		buttonCancel.setOnAction(e -> {
			Confirmation.close();
		});
		Confirmation.show();

	}

	@SuppressWarnings("unchecked")
	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() throws IOException, Exception {
		buildData();
		cityTable.getColumns().clear();
		cityTable.setEditable(true);
		Changeprices.setDisable(true);
		cityTable.setRowFactory(tv -> {
			TableRow<City> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() > 0 && (!row.isEmpty())) {
					Changeprices.setDisable(false);
					Globals.city = cityTable.getSelectionModel().getSelectedItem();

				}

			});
			return row;
		});
		CityName.setStyle("-fx-alignment: CENTER;");
		CityName.setMinWidth(150);
		CityName.setCellValueFactory(new PropertyValueFactory<City, String>("City"));
		FixedCost.setStyle("-fx-alignment: CENTER;");
		FixedCost.setMinWidth(100);
		FixedCost.setCellValueFactory(new PropertyValueFactory<City, String>("FixedCost"));
		OneTimeCost.setStyle("-fx-alignment: CENTER;");
		OneTimeCost.setMinWidth(100);
		OneTimeCost.setCellValueFactory(new PropertyValueFactory<City, String>("OneTimeCost"));
		FilteredList<City> flCity = new FilteredList<City>(data, p -> true);// Pass the data to a filtered list
		cityTable.setItems(flCity);// Set the table's items using the filtered list
		cityTable.getColumns().addAll(CityName, FixedCost, OneTimeCost);

	}

	public void buildData() throws UnknownHostException, IOException {
		@SuppressWarnings("resource")
		Socket socket = new Socket("localhost", 5555);
		data = FXCollections.observableArrayList();
		String[] get = new String[2];
		get[0] = "getCatalog";
		get[1] = "-1";
		try {
			ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
			objectOutput.writeObject(get);
			try {
				ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());
				try {
					Object[] object = (Object[]) objectInput.readObject();

					for (int i = 1; i <= (int) object[0]; i++) {
						data.add((City) object[i]);
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
}