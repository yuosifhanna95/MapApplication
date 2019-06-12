package application;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ViewRouteController {

	private ImagePlace[] ImagePlaces = new ImagePlace[10];
	private RoutePlace[] ORoutePlaceList;
	private int Counter = 0;
	private double aspect = 0.75f;

	@FXML
	private ImageView routeImage;

	@FXML
	private AnchorPane mainPane;

	@FXML
	private Button back;

	@FXML
	private TableView<RoutePlace> routeInfo;

	@FXML
	private TableColumn<RoutePlace, String> placeCol;

	@FXML
	private TableColumn<RoutePlace, String> timeCol;

	private ObservableList<RoutePlace> data = null;

	@FXML
	private Button btn_save;

	@SuppressWarnings("unchecked")
	@FXML
	public void initialize() throws IOException, ClassNotFoundException {
		Image LinkedImage = new Image(Globals.route.getLink());
		routeImage = new ImageView(LinkedImage);
		routeImage.relocate(162, 50);
		routeImage.setFitWidth(LinkedImage.getWidth());
		routeImage.setFitHeight(LinkedImage.getHeight());

		mainPane.getChildren().add(routeImage);

		buildData();

		routeInfo.getColumns().clear();
		routeInfo.setEditable(true);

		placeCol.setStyle("-fx-alignment: CENTER;");
		placeCol.setMinWidth(100);
		placeCol.setCellValueFactory(new PropertyValueFactory<RoutePlace, String>("place"));

		timeCol.setStyle("-fx-alignment: CENTER;");
		timeCol.setMinWidth(150);
		timeCol.setCellValueFactory(new PropertyValueFactory<RoutePlace, String>("time"));

		FilteredList<RoutePlace> flCity = new FilteredList<RoutePlace>(data, p -> true);// Pass the data to a filtered
																						// list
		routeInfo.setItems(flCity);// Set the table's items using the filtered list
		routeInfo.getColumns().addAll(placeCol, timeCol);
		DrawPlaces();
	}

	@SuppressWarnings("deprecation")
	void DrawPlaces() throws IOException, ClassNotFoundException {

		Counter = 0;
		for (int i = 0; i < ImagePlaces.length; i++) {

			if (ImagePlaces[i] != null) {

				mainPane.getChildren().removeAll(ImagePlaces[i].getImageview());
				mainPane.getChildren().removeAll(ImagePlaces[i].getLabel());

			}

		}
		ImagePlaces = new ImagePlace[10];

		@SuppressWarnings("resource")
		Socket socket = new Socket(Globals.IpAddress, 5555);
		String[] array = new String[2];
		String[] array2 = new String[2];
		array[0] = "getRoutePlaces";
		// get[1] = "" + ImagePlaces[i].getPlace().getCityName();
		array[1] = "" + Globals.route.getId();

		ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
		objectOutput.writeObject(array);

		Object data;
		ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());
		data = objectInput.readObject();
		// System.out.println(((Object[]) data)[0]);
		ORoutePlaceList = (RoutePlace[]) ((Object[]) data)[1];

		int counter = 0, counter2 = 0;
		int flag = 0;
		for (int i = 0; i < ORoutePlaceList.length; i++) {
			if (flag == 0) {
				Image im = new Image("File:oldloc.png");
				ImageView newLoc = new ImageView(im);
				double X = ORoutePlaceList[i].getLocX(), Y = ORoutePlaceList[i].getLocY();
				// newLoc1.relocate(X, Y);
				Label label = new Label();
				// label.relocate(X, Y);
				RoutePlace rp = new RoutePlace(ORoutePlaceList[i].getRouteId(), ORoutePlaceList[i].getPlace(),
						ORoutePlaceList[i].getTime(), ORoutePlaceList[i].getLocX(), ORoutePlaceList[i].getLocY(),
						ORoutePlaceList[i].getSerialID());
				newLoc.relocate(ORoutePlaceList[i].getLocX() + routeImage.getLayoutX() - (im.getWidth() / 2) * aspect,
						ORoutePlaceList[i].getLocY() + routeImage.getLayoutY() - (im.getHeight() / 2) * aspect);

				label.setFont(new Font("Quicksand", 20));
				label.setText(rp.getPlace());

				Text t = new Text();
				t.setText(label.getText());
				t.setFont(label.getFont());
				double width = t.getBoundsInLocal().getWidth();
				double height = t.getBoundsInLocal().getHeight();

				label.relocate(ORoutePlaceList[i].getLocX() + routeImage.getLayoutX() - (width / 2),
						ORoutePlaceList[i].getLocY() + routeImage.getLayoutY()
								- (height + (im.getHeight() / 2) * aspect));

				newLoc.setFitHeight(im.getHeight() * aspect);
				newLoc.setFitWidth(im.getWidth() * aspect);
				newLoc.setId("imv" + Counter);

				ImagePlaces[counter] = new ImagePlace(counter, newLoc, label, rp, ORoutePlaceList[i].getLocX(),
						ORoutePlaceList[i].getLocY(), false);
				counter++;
				counter2++;
				Counter++;
			}
			flag = 0;
		}

		for (int i = 0; i < ImagePlaces.length; i++) {

			if (ImagePlaces[i] != null) {

				mainPane.getChildren().addAll(ImagePlaces[i].getImageview());
				mainPane.getChildren().addAll(ImagePlaces[i].getLabel());

			}

		}

	}

	@FXML
	void backFunc(ActionEvent event) throws IOException {
		Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		URL url = getClass().getResource(Globals.backLink);
		Globals.backLink = "MyMapsScene.fxml";
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

	public void buildData() throws UnknownHostException, IOException {
		@SuppressWarnings("resource")
		Socket socket = new Socket(Globals.IpAddress, 5555);
		data = FXCollections.observableArrayList();

		String[] get = new String[2];
		get[0] = "getRoutePlaces";
		get[1] = Integer.toString(Globals.route.getId());
		try {
			ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
			objectOutput.writeObject(get);
			try {
				ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());
				try {
					RoutePlace[] object = (RoutePlace[]) ((Object[]) objectInput.readObject())[1];
					for (int i = 0; i < object.length; i++) {
						data.add((RoutePlace) object[i]);
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

	@FXML
	void saveImage(ActionEvent event) {
		// updateLocations();
		FileChooser fileChooser = new FileChooser();

		// Set extension filter
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("png files (*.png)", "*.png"));

		// Prompt user to select a file
		File file = fileChooser.showSaveDialog(null);

		if (file != null) {
			try {
				// Pad the capture area
				WritableImage writableImage = new WritableImage((int) routeImage.getFitWidth(),
						(int) routeImage.getFitHeight());
				// WritableImage writableImage = new WritableImage((int)2000 , (int)2000);
				SnapshotParameters params = new SnapshotParameters();

				params.setViewport(new Rectangle2D(routeImage.getLayoutX(), routeImage.getLayoutY(),
						routeImage.getFitWidth(), routeImage.getFitHeight()));
				((Node) mainPane).snapshot(params, writableImage);
				// writableImage= pixelScaleAwareCanvasSnapshot(mainPane,params,2);
				// ((Node)mainPane).snapshot()
				// ((Node)event.getSource()).snapshot(null, writableImage);
				RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
				// Write the snapshot to the chosen file
				ImageIO.write(renderedImage, "png", file);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

}
