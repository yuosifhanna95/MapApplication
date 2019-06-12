package application;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.JOptionPane;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ShowReportsController {
	static private java.sql.Date sdate1;
	static private java.sql.Date edate;
	private ObservableList<Reports> dataReport = FXCollections.observableArrayList();
	@FXML
	private ComboBox<String> comboBox;
	@FXML
	private TextField cityname = new TextField();
	@FXML
	private Button back;
	@FXML
	private TableView<Reports> reportTable;
	@FXML
	private TableColumn<Reports, String> CityName;
	@FXML
	private TableColumn<Reports, String> numOfMaps;
	@FXML
	private TableColumn<Reports, String> numofonetimepurchase;
	@FXML
	private TableColumn<Reports, String> numOfSubscribers;
	@FXML
	private TableColumn<Reports, String> numofRenews;
	@FXML
	private TableColumn<Reports, String> numOfViews;
	@FXML
	private TableColumn<Reports, String> numOfDownloads;
	@FXML
	private Button Show;
	@FXML
	private DatePicker startdate;
	@FXML
	private DatePicker enddate;
	@FXML
	private Label sdate = new Label("Start date here");

	@SuppressWarnings("unchecked")
	@FXML
	void ShowBtn(ActionEvent event) throws IOException {
		LocalDate sd = startdate.getValue();
		LocalDate ed = enddate.getValue();
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-uuuu");
		try {
			String stringdate = sd.format(dateFormatter);
			String stringdate1 = ed.format(dateFormatter);
			SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
			java.util.Date date = sdf1.parse(stringdate);
			java.util.Date date1 = sdf1.parse(stringdate1);
			sdate1 = new java.sql.Date(date.getTime());
			edate = new java.sql.Date(date1.getTime());

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Please fill in the period.");
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return;
		}

		String type = "All cities";
		if (comboBox.getValue().equals("One city")) {
			type = "One city";
		}
		if (comboBox.getValue().equals("One city") && cityname.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "Please write the name of the city.");
		} else {
			// reportTable.getItems().clear();
			reportTable.getColumns().clear();

			// reportTable = new TableView<>();
			buildData(type);

			// reportTable.getStyleClass().removeAll("addBobOk, focus");
			// reportTable.getStyleClass().add("addBobOk");

			CityName.setStyle("-fx-alignment: CENTER;");
			CityName.setMinWidth(100);
			CityName.setCellValueFactory(new PropertyValueFactory<Reports, String>("City"));
			numOfMaps.setStyle("-fx-alignment: CENTER;");
			numOfMaps.setMinWidth(100);
			numOfMaps.setCellValueFactory(new PropertyValueFactory<Reports, String>("NumMaps"));
			numofonetimepurchase.setStyle("-fx-alignment: CENTER;");
			numofonetimepurchase.setMinWidth(100);
			numofonetimepurchase.setCellValueFactory(new PropertyValueFactory<Reports, String>("NumOtPurchase"));
			numOfSubscribers.setStyle("-fx-alignment: CENTER;");
			numOfSubscribers.setMinWidth(100);
			numOfSubscribers.setCellValueFactory(new PropertyValueFactory<Reports, String>("NumSubscribers"));
			numofRenews.setStyle("-fx-alignment: CENTER;");
			numofRenews.setMinWidth(100);
			numofRenews.setCellValueFactory(new PropertyValueFactory<Reports, String>("NumRenews"));
			numOfViews.setStyle("-fx-alignment: CENTER;");
			numOfViews.setMinWidth(100);
			numOfViews.setCellValueFactory(new PropertyValueFactory<Reports, String>("NumViews"));
			numOfDownloads.setStyle("-fx-alignment: CENTER;");
			numOfDownloads.setMinWidth(100);
			numOfDownloads.setCellValueFactory(new PropertyValueFactory<Reports, String>("NumDownloads"));

			FilteredList<Reports> flReport = new FilteredList<Reports>(dataReport, p -> true);// Pass the dataCity to a
																								// filtered list
			reportTable.setItems(flReport);// Set the table's items using the filtered list
			reportTable.getColumns().addAll(CityName, numOfMaps, numofonetimepurchase, numOfSubscribers, numofRenews,
					numOfViews, numOfDownloads);

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
		primaryStage.show();
	}

	@SuppressWarnings("unchecked")
	@FXML
	public void initialize() throws UnknownHostException, IOException {

		comboBox.getItems().addAll("One city", "All cities");
		comboBox.getSelectionModel().selectFirst();
		reportTable.getColumns().clear();
		reportTable.setEditable(true);
		comboBox.setPromptText("One city");
		comboBox.setOnAction(event -> {

			switch (comboBox.getValue()) {
			case "One city":
				cityname.setText("");
				cityname.setVisible(true);
				cityname.setDisable(false);

				break;
			case "All cities":
				cityname.setVisible(false);
				cityname.setDisable(true);
				break;
			}

		});

	}

	public void buildData(String type) throws UnknownHostException, IOException {
		@SuppressWarnings("resource")
		Socket socket = new Socket("localhost", 5555);

		Object[] get = new Object[4];
		get[0] = "getreport";
		get[1] = "One city";
		get[3] = cityname.getText();
		if (type.equals("All cities")) {
			get[1] = "All cities";
			get[3] = "All cities";
		}
		Reports rep = new Reports(((String) ((Object[]) (get))[3]), 0, 0, 0, 0, 0, 0, sdate1, edate);
		get[2] = rep;
		try {
			ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
			objectOutput.writeObject(get);
			try {
				ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());
				try {
					dataReport = FXCollections.observableArrayList();
					Object[] object = (Object[]) objectInput.readObject();

					for (int i = 1; i <= (int) object[0]; i++) {
						dataReport.add((Reports) object[i]);
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
