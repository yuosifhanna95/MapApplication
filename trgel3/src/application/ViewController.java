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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ViewController {

	private ImagePlace[] ImagePlaces = new ImagePlace[10];
	private int Counter = 0;
	private double aspect = 0.75f;

	@FXML
	private ImageView mapImage;

	@FXML
	private AnchorPane mainPane;

	@FXML
	private Button back;

	@FXML
	private Button btn_save;

	@FXML
	public void initialize() throws UnknownHostException, IOException, ClassNotFoundException {
		Image LinkedImage = new Image(Globals.map.getLinkCustomer());
		mapImage = new ImageView(LinkedImage);
		mapImage.relocate(162, 50);
		mapImage.setFitWidth(LinkedImage.getWidth());
		mapImage.setFitHeight(LinkedImage.getHeight());
		mainPane.getChildren().add(mapImage);

		DrawPlaces();
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

	@SuppressWarnings("deprecation")
	void DrawPlaces() throws IOException, ClassNotFoundException {

		@SuppressWarnings("resource")
		Socket socket = new Socket(Globals.IpAddress, 5555);
		String[] array = new String[5];
		String[] array2 = new String[2];
		array[0] = "getPlaces";
		// get[1] = "" + ImagePlaces[i].getPlace().getCityName();
		array[1] = "" + Globals.map.getId();
		array[2] = "" + Globals.map.getCity();
		array[3] = Globals.user.getType();
		// array[3] = "viewmap";

		ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
		objectOutput.writeObject(array);

		Object data;
		ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());
		data = objectInput.readObject();
		System.out.println(((Object[]) data)[0]);
		Place[] list = (Place[]) ((Object[]) data)[1];

		int counter = 0;
		int flag = 0;
		for (int i = 0; i < list.length; i++) {

			if (flag == 0) {
				Image im = new Image("File:oldloc.png");
				ImageView newLoc = new ImageView(im);
				double X = list[i].getLocX(), Y = list[i].getLocY();
				// newLoc1.relocate(X, Y);
				Label label = new Label();
				// label.relocate(X, Y);

				newLoc.relocate(list[i].getLocX() + mapImage.getLayoutX() - (im.getWidth() / 2) * aspect,
						list[i].getLocY() + mapImage.getLayoutY() - (im.getHeight() / 2) * aspect);

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

				label.relocate(list[i].getLocX() - (width / 2) + mapImage.getLayoutX(),
						list[i].getLocY() + mapImage.getLayoutY() - (height + (im.getHeight() / 2) * aspect));

				newLoc.setFitHeight(im.getHeight() * aspect);
				newLoc.setFitWidth(im.getWidth() * aspect);
				newLoc.setId("imv" + Counter);

				ImagePlaces[counter] = new ImagePlace(counter, newLoc, label, p, list[i].getLocX(), list[i].getLocY(),
						false);
				counter++;
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

		// AddMethodsForIP();

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
				WritableImage writableImage = new WritableImage((int) mapImage.getFitWidth(),
						(int) mapImage.getFitHeight());
				// WritableImage writableImage = new WritableImage((int)2000 , (int)2000);
				SnapshotParameters params = new SnapshotParameters();

				params.setViewport(new Rectangle2D(mapImage.getLayoutX(), mapImage.getLayoutY(), mapImage.getFitWidth(),
						mapImage.getFitHeight()));
				((Node) mainPane).snapshot(params, writableImage);
				// writableImage= pixelScaleAwareCanvasSnapshot(mainPane,params,2);
				// ((Node)mainPane).snapshot()
				// ((Node)event.getSource()).snapshot(null, writableImage);
				RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
				// Write the snapshot to the chosen file
				ImageIO.write(renderedImage, "png", file);

				@SuppressWarnings("resource")
				Socket socket = new Socket(Globals.IpAddress, 5555);
				String[] array = new String[2];
				array[0] = "staticDownload";
				array[1] = "" + Globals.map.getCity();
				ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
				objectOutput.writeObject(array);

			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

}