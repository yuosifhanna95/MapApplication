package application;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Date;

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
import javafx.scene.control.Control;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
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

public class MessagesController {

	@FXML
	private TableView<Message> messageTable;

	@FXML
	private TableColumn<Message, String> MessageDescription;

	@FXML
	private TableColumn<Message, String> Typemessage;
	@FXML
	private TableColumn<Message, Date> Datesend;

	private ObservableList<Message> data = null;
	private ObservableList<NewPrices> data1 = null;
	private String ncity = "";

	@FXML
	private Button Back;

	@FXML
	private Button Cont;

	@SuppressWarnings("unchecked")
	@FXML
	void docontinue(ActionEvent event) throws UnknownHostException, IOException {
		if (Cont.getText().equals("make a decision")) {
			final Stage Confirmation = new Stage();

			Confirmation.initModality(Modality.APPLICATION_MODAL);
			Text text = new Text("Confirm the new prices of " + ncity + " city");
			text.setFont(Font.font("Verdana", FontWeight.BOLD, 25));
			GridPane gridPane = new GridPane();
			gridPane.setHgap(10);
			gridPane.setVgap(10);
			TableView<NewPrices> NewPricesTable = new TableView<NewPrices>();
			NewPricesTable.getColumns().clear();
			NewPricesTable.setEditable(true);
			TableColumn<NewPrices, String> CityName = new TableColumn<NewPrices, String>();
			TableColumn<NewPrices, String> FixedCost = new TableColumn<NewPrices, String>();
			TableColumn<NewPrices, String> OneTimeCost = new TableColumn<NewPrices, String>();
			CityName.setText("CityName");
			FixedCost.setText("FixedCost");
			OneTimeCost.setText("OneTimeCost");
			CityName.setStyle("-fx-alignment: CENTER;");
			CityName.setMinWidth(100);
			CityName.setCellValueFactory(new PropertyValueFactory<NewPrices, String>("City"));
			FixedCost.setStyle("-fx-alignment: CENTER;");
			FixedCost.setMinWidth(100);
			FixedCost.setCellValueFactory(new PropertyValueFactory<NewPrices, String>("FixedCost"));
			OneTimeCost.setStyle("-fx-alignment: CENTER;");
			OneTimeCost.setMinWidth(100);
			OneTimeCost.setCellValueFactory(new PropertyValueFactory<NewPrices, String>("OneTimeCost"));
			buildData("NewPrices");

			FilteredList<NewPrices> flprice = new FilteredList<NewPrices>(data1, p -> true);// Pass the data to a
																							// filtered list
			NewPricesTable.setItems(flprice);// Set the table's items using the filtered list
			NewPricesTable.getColumns().addAll(CityName, FixedCost, OneTimeCost);
			gridPane.add(NewPricesTable, 0, 0);
			gridPane.setAlignment(Pos.CENTER);
			GridPane gridPane1 = new GridPane();
			gridPane1.setHgap(10);
			gridPane1.setVgap(10);
			Text text1 = new Text("you can confirm the changes?");
			text1.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
			gridPane1.add(text1, 0, 0);
			gridPane1.setAlignment(Pos.CENTER);
			ToggleGroup group = new ToggleGroup();
			RadioButton rb1 = new RadioButton();
			RadioButton rb2 = new RadioButton();
			rb1.setText("Yes");
			rb2.setText("no reason:");
			gridPane1.add(rb1, 0, 1);
			gridPane1.setAlignment(Pos.CENTER);
			rb1.setToggleGroup(group);
			rb2.setToggleGroup(group);
			rb1.setSelected(true);
			TextField refused = new TextField();
			HBox h = new HBox(20);
			h.getChildren().add(rb2);
			h.getChildren().add(refused);
			gridPane1.add(h, 0, 2);
			gridPane1.setAlignment(Pos.CENTER);
			GridPane gridPane3 = new GridPane();
			gridPane3.setHgap(10);
			gridPane3.setVgap(10);
			Button buttonSend = new Button("send");
			Button buttonClose = new Button("Close");
			gridPane3.add(buttonSend, 0, 0);
			gridPane3.add(buttonClose, 1, 0);
			gridPane3.setAlignment(Pos.CENTER);

			buttonSend.setOnAction(e -> {
				if (!(refused.getText().equals("")) && rb1.isSelected()) {
					JOptionPane.showMessageDialog(null, "you can't choose yes and no in the same time");
				} else {
					@SuppressWarnings("resource")
					Socket socket = null;
					try {
						socket = new Socket(Globals.IpAddress, 5555);
					} catch (UnknownHostException e3) {
						// TODO Auto-generated catch block
						e3.printStackTrace();
					} catch (IOException e3) {
						// TODO Auto-generated catch block
						e3.printStackTrace();
					}

					String[] array1 = new String[5];

					array1[0] = "send message to contentmaneger";
					array1[1] = "malki";
					array1[2] = "no";
					array1[3] = "Dear contentManeger,i can't confirm the new prices because i don't think that this changes will improve our site.";
					array1[4] = ncity;
					if (rb1.isSelected()) {
						array1[2] = "yes";
						array1[3] = "Dear contentManeger,i confirm the new prices ,thank you for your work,continue in improve our site.";
					}

					try {
						ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
						objectOutput.writeObject(array1);
						try {
							ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());
							try {
								Object data = objectInput.readObject();

								if ((data).equals("done")) {
									JOptionPane.showMessageDialog(null, "done");
									Confirmation.close();
								}

							} catch (ClassNotFoundException e1) {
								System.out.println("The title list has not come from the server");
								e1.printStackTrace();
							}
						} catch (IOException e1) {
							System.out.println("The socket for reading the object has problem");
							e1.printStackTrace();
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
			buttonClose.setOnAction(e -> {
				Confirmation.close();
			});
			Confirmation.setScene(dialogScene);
			Confirmation.show();
			Confirmation.show();
		}

	}

	@FXML
	void backFunc(ActionEvent event) throws IOException {

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
	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() throws IOException, Exception {

		buildData("Messages");

		Cont.setText("Click to one message to continue");
		Cont.setDisable(true);
		messageTable.getColumns().clear();
		messageTable.setEditable(true);

		messageTable.setRowFactory(tv -> {
			TableRow<Message> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() > 0 && (!row.isEmpty())) {
					Cont.setDisable(false);
					Message mss = messageTable.getSelectionModel().getSelectedItem();
					if (mss.getTypemessage().equals("warning")) {
						Cont.setText("go to my map to renew");
					}
					if (mss.getTypemessage().startsWith("Confirmation of change of prices")) {
						ncity = mss.getTypemessage().substring(41);
						Cont.setText("make a decision");
					}
				}

			});
			return row;
		});

		MessageDescription.setStyle("-fx-alignment: CENTER;");
		MessageDescription.setMinWidth(150);
		MessageDescription.setCellValueFactory(new PropertyValueFactory<Message, String>("message"));

		MessageDescription.setCellFactory(tc -> {
			TableCell<Message, String> cell = new TableCell<>();
			Text text = new Text();
			cell.setGraphic(text);
			cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
			text.wrappingWidthProperty().bind(MessageDescription.widthProperty());
			text.textProperty().bind(cell.itemProperty());
			return cell;
		});

		Typemessage.setStyle("-fx-alignment: CENTER;");
		Typemessage.setMinWidth(100);
		Typemessage.setCellValueFactory(new PropertyValueFactory<Message, String>("typemessage"));
		Datesend.setStyle("-fx-alignment: CENTER;");
		Datesend.setMinWidth(100);
		Datesend.setCellValueFactory(new PropertyValueFactory<Message, Date>("Datesend"));

		FilteredList<Message> flCity = new FilteredList<Message>(data, p -> true);// Pass the data to a filtered list
		messageTable.setItems(flCity);// Set the table's items using the filtered list
		messageTable.getColumns().addAll(MessageDescription, Typemessage, Datesend);

	}

	public void buildData(String type) throws UnknownHostException, IOException {
		@SuppressWarnings("resource")
		Socket socket = new Socket(Globals.IpAddress, 5555);
		data = FXCollections.observableArrayList();
		data1 = FXCollections.observableArrayList();
		String[] array = new String[2];
		if (type.equals("Messages")) {
			array[0] = "getMessages";
			array[1] = Globals.user.getUserName();
		} else if (type.equals("NewPrices")) {
			array[0] = "getNewPrices";
			array[1] = ncity;
		}
		try {
			ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
			objectOutput.writeObject(array);
			try {
				ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());
				try {
					Object[] object = (Object[]) objectInput.readObject();
					if (type.equals("Messages")) {
						for (int i = 1; i <= (int) object[0]; i++) {
							data.add((Message) object[i]);
						}
					} else if (type.equals("NewPrices")) {
						for (int i = 1; i <= (int) object[0]; i++) {
							data1.add((NewPrices) object[i]);
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
