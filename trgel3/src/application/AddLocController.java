package application;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.transform.Transform;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class AddLocController {
	private int Mode;
	private Label[] labels = new Label[10];
	private ImageView[] locations = new ImageView[10];
	private Place[] Places = new Place[10];
	private ImagePlace[] ImagePlaces = new ImagePlace[10];
	private ImagePlace CurImagePlace;
	private int Counter = 0;
	private int current = 0;
	private double aspect = 0.75f;

	@FXML
	private Button btn_UpdateMap;

	@FXML
	private AnchorPane mainPane;

	@FXML
	private ImageView MapImage;

	@FXML
	private Label label_message;

	@FXML
	private Button back;

	@FXML
	private Button btn_save;

	@FXML
	private Button btn_EditMode;

	@FXML
	private Button btn_ViewMode;

	@FXML
	private Button btn_AddLoc;

	@FXML
	private TextField NewLocation;

	@FXML
	private Button OK;

	@FXML
	private ImageView raninImage;

	@FXML
	void UpdateMap(ActionEvent event) throws IOException, ClassNotFoundException {

		@SuppressWarnings("resource")
		Socket socket = new Socket("localhost", 5555);
		String[] array = new String[2];
		Object[] array2 = new Object[3];
		array[0] = "getUPlaces";
		// get[1] = "" + ImagePlaces[i].getPlace().getCityName();
		array[1] = "" + Globals.map.getId();

		ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
		objectOutput.writeObject(array);

		Object data;
		ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());
		data = objectInput.readObject();
		System.out.println(((Object[]) data)[0]);
		UPlace[] list = (UPlace[]) ((Object[]) data)[1];
		for (int i = 0; i < ImagePlaces.length; i++) {
			if (ImagePlaces[i] != null) {
				int flagnew = 1;
				if (ImagePlaces[i].getPlace() instanceof UPlace) {
					long serialid = ImagePlaces[i].getPlace().getSerialID();
					String MapId = ImagePlaces[i].getPlace().getMapId();
					int PlaceId = ((UPlace) (ImagePlaces[i].getPlace())).getPlaceId();
					String CityName = ImagePlaces[i].getPlace().getCityName();
					String PlaceName = ImagePlaces[i].getPlace().getPlaceName();
					String description = ImagePlaces[i].getPlace().getDescription();
					String classification = ImagePlaces[i].getPlace().getClassification();
					int accessibility = ImagePlaces[i].getPlace().getAccessibility();
					int LocX = ImagePlaces[i].getX();
					int LocY = ImagePlaces[i].getY();
					String type = ImagePlaces[i].getPlace().getType();
					array2[0] = "UpdatePlace";
					array2[1] = new UPlace(MapId, CityName, PlaceName, description, classification, accessibility,
							serialid, LocX, LocY, type, PlaceId);
					// objectOutput = new ObjectOutputStream(socket.getOutputStream());
					array2[2] = "" + serialid;
					@SuppressWarnings("resource")
					Socket socket2 = new Socket("localhost", 5555);
					objectOutput = new ObjectOutputStream(socket2.getOutputStream());
					objectOutput.writeObject(array2);
					flagnew = 0;

				} else {

					for (Place pl : list) {

						if (ImagePlaces[i].getPlace().getSerialID() == pl.getSerialID()) {
							long serialid = ImagePlaces[i].getPlace().getSerialID();
							String MapId = ImagePlaces[i].getPlace().getMapId();
							String CityName = ImagePlaces[i].getPlace().getCityName();
							String PlaceName = ImagePlaces[i].getPlace().getPlaceName();
							String description = ImagePlaces[i].getPlace().getDescription();
							String classification = ImagePlaces[i].getPlace().getClassification();
							int accessibility = ImagePlaces[i].getPlace().getAccessibility();
							int LocX = ImagePlaces[i].getX();
							int LocY = ImagePlaces[i].getY();
							String type = "UPDATE";
							array2[0] = "UpdatePlace";
							array2[1] = new Place(MapId, CityName, PlaceName, description, classification,
									accessibility, serialid, LocX, LocY, type);
							// objectOutput = new ObjectOutputStream(socket.getOutputStream());
							array2[2] = "" + serialid;
							@SuppressWarnings("resource")
							Socket socket2 = new Socket("localhost", 5555);
							objectOutput = new ObjectOutputStream(socket2.getOutputStream());
							objectOutput.writeObject(array2);
							flagnew = 0;
							break;

						}

					}
					if (flagnew == 1) {
						long serialid = ImagePlaces[i].getPlace().getSerialID();
						String MapId = ImagePlaces[i].getPlace().getMapId();
						String CityName = ImagePlaces[i].getPlace().getCityName();
						String PlaceName = ImagePlaces[i].getPlace().getPlaceName();
						String description = ImagePlaces[i].getPlace().getDescription();
						String classification = ImagePlaces[i].getPlace().getClassification();
						int accessibility = ImagePlaces[i].getPlace().getAccessibility();
						int LocX = ImagePlaces[i].getX();
						int LocY = ImagePlaces[i].getY();
						String type = "NEW";
						array2[0] = "UpdatePlace";
						Place pp = new Place(MapId, CityName, PlaceName, description, classification, accessibility,
								LocX, LocY, type);
						array2[1] = pp;
						@SuppressWarnings("resource")
						Socket socket2 = new Socket("localhost", 5555);
						objectOutput = new ObjectOutputStream(socket2.getOutputStream());
						objectOutput.writeObject(array2);
					}
				}
			}
		}

	}

	void InitializeImagePlaces() {

	}

	void DrawPlaces() throws IOException, ClassNotFoundException {

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
		System.out.println(((Object[]) data)[0]);
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
		System.out.println(((Object[]) data2)[0]);
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
							list2[c].getPlaceId());
					label.setText(p.getPlaceName());

					label.relocate(list2[c].getLocX() - (label.getWidth() / 2),
							list2[c].getLocY() - (label.getHeight() + (im.getHeight() / 2) * aspect));

					newLoc.setFitHeight(im.getHeight() * aspect);
					newLoc.setFitWidth(im.getWidth() * aspect);
					newLoc.setId("imv" + Counter);

					ImagePlaces[counter] = new ImagePlace(counter, newLoc, label, p, list2[c].getLocX(),
							list2[c].getLocY());
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
						list[i].getSerialID(), list[i].getLocX(), list[i].getLocY(), list[i].getType());
				label.setText(p.getPlaceName());

				label.relocate(list[i].getLocX() - (label.getWidth() / 2),
						list[i].getLocY() - (label.getHeight() + (im.getHeight() / 2) * aspect));

				newLoc.setFitHeight(im.getHeight() * aspect);
				newLoc.setFitWidth(im.getWidth() * aspect);
				newLoc.setId("imv" + Counter);

				ImagePlaces[counter] = new ImagePlace(counter, newLoc, label, p, list[i].getLocX(), list[i].getLocY());
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
						list2[c].getPlaceId());
				label.setText(p.getPlaceName());
				label.relocate(list2[c].getLocX() - (label.getWidth() / 2),
						list2[c].getLocY() - (label.getHeight() + (im.getHeight() / 2) * aspect));

				newLoc.setFitHeight(im.getHeight() * aspect);
				newLoc.setFitWidth(im.getWidth() * aspect);
				newLoc.setId("imv" + Counter);
				ImagePlaces[counter] = new ImagePlace(counter, newLoc, label, p, list2[c].getLocX(),
						list2[c].getLocY());
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

		AddMethodsForIP();

	}

	void updateLocations() {
		Image im = new Image("File:loc.png");
		for (int i = 0; i < ImagePlaces.length; i++)
			if (ImagePlaces[i] != null) {
				ImagePlaces[i].getImageview().relocate(
						ImagePlaces[i].getPlace().getLocX() - (im.getWidth() / 2) * aspect,
						ImagePlaces[i].getPlace().getLocY() - (im.getHeight() / 2) * aspect);
				ImagePlaces[i].getLabel().relocate(
						ImagePlaces[i].getPlace().getLocX() - (ImagePlaces[i].getLabel().getWidth() / 2),
						ImagePlaces[i].getPlace().getLocY()
								- (ImagePlaces[i].getLabel().getHeight() + (im.getHeight() / 2) * aspect));
			}
	}

	void AddMethodsForIP() {
		Image im = new Image("File:loc.png");
		EventHandler<MouseEvent> EditTouchEvent = new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if (Mode == 1) {
					if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {

						CurImagePlace = new ImagePlace();
						CurImagePlace.setImageview((ImageView) event.getSource());
						String ID = CurImagePlace.getImageview().getId();
						ID = ID.replace("imv", "");
						CurImagePlace.setId(Integer.parseInt(ID));
						CurImagePlace.setLabel(ImagePlaces[Integer.parseInt(ID)].getLabel());

						if (event.getSource() instanceof ImageView) {
							ImageView imView = (ImageView) event.getSource();
							imView.relocate(event.getSceneX() - (im.getWidth() / 2) * aspect,
									event.getSceneY() - (im.getHeight() / 2) * aspect);
							CurImagePlace.getLabel().relocate(
									event.getSceneX() - (CurImagePlace.getLabel().getWidth() / 2), event.getSceneY()
											- (CurImagePlace.getLabel().getHeight() + (im.getHeight() / 2) * aspect));

							// CurImagePlace.setX((int) event.getSceneX());
							// CurImagePlace.setY((int) event.getSceneY());
							ImagePlaces[Integer.parseInt(ID)].setX((int) event.getSceneX());
							ImagePlaces[Integer.parseInt(ID)].setY((int) event.getSceneY());

						}
					} else if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
						CurImagePlace = new ImagePlace();
						CurImagePlace.setImageview((ImageView) event.getSource());
						String ID = CurImagePlace.getImageview().getId();
						ID = ID.replace("imv", "");
						CurImagePlace.setId(Integer.parseInt(ID));
						CurImagePlace.setLabel(ImagePlaces[Integer.parseInt(ID)].getLabel());
						CurImagePlace.setPlace(ImagePlaces[Integer.parseInt(ID)].getPlace());

					}
					// TODO Auto-generated method stub
				}

			}
		};

		for (int i = 0; i < ImagePlaces.length; i++)
			if (ImagePlaces[i] != null) {
				ImagePlaces[i].getImageview().addEventFilter(MouseEvent.MOUSE_CLICKED, EditTouchEvent);
				ImagePlaces[i].getImageview().addEventFilter(MouseEvent.MOUSE_DRAGGED, EditTouchEvent);
			}
	}

	@FXML
	void AddLocation(ActionEvent event) {
		Mode = 0;
		if (!NewLocation.getText().equals("") && Counter < 10) {
			EventHandler<MouseEvent> OkEventHandler = new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
						mainPane.getChildren().remove(OK);
						current = -1;
					}
					// TODO Auto-generated method stub

				}
			};
			current = Counter;
			Label label = new Label();
			OK = new Button();
			OK.setText("set");
			OK.addEventHandler(MouseEvent.MOUSE_CLICKED, OkEventHandler);
			label.setText(NewLocation.getText());
			label.setFont(new Font("Quicksand", 20));
			Image im = new Image("File:loc.png");
			ImageView newLoc = new ImageView(im);
			newLoc.relocate(MapImage.getLayoutX(), MapImage.getLayoutY());
			label.relocate(MapImage.getLayoutX(), MapImage.getLayoutY());

			newLoc.setFitHeight(im.getHeight() * aspect);
			newLoc.setFitWidth(im.getWidth() * aspect);
			newLoc.setId("imv" + Counter);
			labels[Counter] = label;
			locations[Counter] = newLoc;
			Places[Counter] = new Place("" + Globals.map.getId(), Globals.map.getCity(), label.getText(), "", "", 1,
					(int) newLoc.getX(), (int) newLoc.getY(), "NEW");

			ImagePlaces[Counter] = new ImagePlace(Counter, locations[Counter], labels[Counter], Places[Counter],
					(int) MapImage.getLayoutX(), (int) MapImage.getLayoutY());

			mainPane.getChildren().addAll(locations[Counter]);
			mainPane.getChildren().addAll(labels[Counter]);
			mainPane.getChildren().addAll(OK);

			EventHandler<MouseEvent> EditTouchEvent = new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					if (Mode == 1) {
						if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {

							CurImagePlace = new ImagePlace();
							CurImagePlace.setImageview((ImageView) event.getSource());
							String ID = CurImagePlace.getImageview().getId();
							ID = ID.replace("imv", "");
							CurImagePlace.setId(Integer.parseInt(ID));
							CurImagePlace.setLabel(labels[CurImagePlace.getId()]);
							CurImagePlace.setPlace(Places[CurImagePlace.getId()]);

							if (event.getSource() instanceof ImageView) {
								ImageView imView = (ImageView) event.getSource();
								imView.relocate(event.getSceneX() - (im.getWidth() / 2) * aspect,
										event.getSceneY() - (im.getHeight() / 2) * aspect);
								labels[CurImagePlace.getId()].relocate(
										event.getSceneX() - (labels[CurImagePlace.getId()].getWidth() / 2),
										event.getSceneY() - (labels[CurImagePlace.getId()].getHeight()
												+ (im.getHeight() / 2) * aspect));
								CurImagePlace.setX((int) event.getSceneX());
								CurImagePlace.setY((int) event.getSceneY());

							}
						} else if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
							CurImagePlace = new ImagePlace();
							CurImagePlace.setImageview((ImageView) event.getSource());
							String ID = CurImagePlace.getImageview().getId();
							ID = ID.replace("imv", "");
							CurImagePlace.setId(Integer.parseInt(ID));
							CurImagePlace.setLabel(labels[CurImagePlace.getId()]);
							CurImagePlace.setPlace(Places[CurImagePlace.getId()]);

						}
						// TODO Auto-generated method stub
					}

				}
			};

			EventHandler<MouseEvent> mouseEvent = new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					if (current != -1) {
						if (event.getEventType() == MouseEvent.MOUSE_CLICKED
								|| event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
							locations[current].relocate(event.getSceneX() - (im.getWidth() / 2) * aspect,
									event.getSceneY() - (im.getHeight() / 2) * aspect);
							labels[current].relocate(event.getSceneX() - (labels[current].getWidth() / 2),
									event.getSceneY() - (labels[current].getHeight() + (im.getHeight() / 2) * aspect));
							OK.relocate(event.getSceneX() - (OK.getWidth() / 2), event.getSceneY() + (OK.getHeight()));
							locations[current].addEventFilter(MouseEvent.MOUSE_CLICKED, EditTouchEvent);
							locations[current].addEventFilter(MouseEvent.MOUSE_DRAGGED, EditTouchEvent);
							ImagePlaces[current].setX((int) event.getSceneX());
							ImagePlaces[current].setY((int) event.getSceneY());
						}
						// TODO Auto-generated method stub

					}
				}
			};

			mainPane.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent);
			mainPane.addEventHandler(MouseEvent.MOUSE_DRAGGED, mouseEvent);
			mainPane.addEventHandler(MouseEvent.MOUSE_RELEASED, mouseEvent);
			Counter++;
		} else {

		}
	}

	@FXML
	void changeMode(ActionEvent event) {
		if (((Node) event.getSource()).getId().equals("btn_EditMode")) {
			Mode = 1;
		} else if (((Node) event.getSource()).getId().equals("btn_ViewMode")) {
			Mode = 0;
		}
	}

	@FXML
	void initialize() throws IOException, Exception {

		Image LinkedImage = new Image(Globals.map.getLinkCustomer());
		raninImage = new ImageView(LinkedImage);
		raninImage.relocate(162, 50);
		raninImage.setFitWidth(LinkedImage.getWidth());
		raninImage.setFitHeight(LinkedImage.getHeight());

		mainPane.getChildren().add(raninImage);
		Label label = new Label();
		label.setText("مش دارنا");
		label.setFont(new Font("Arial", 20));
		Image im = new Image("File:loc.png");
		ImageView newLoc = new ImageView(im);
		newLoc.relocate(50, 50);
		double aspect = 0.75f;
		newLoc.setFitHeight(im.getHeight() * aspect);
		newLoc.setFitWidth(im.getWidth() * aspect);
		// Stage primaryStage =(Stage) mainPane.getScene().getWindow();
		// mainPane.getChildren().addAll(newLoc);
		// mainPane.getChildren().addAll(label);

		EventHandler<MouseEvent> EditTouchEvent = new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if (Mode == 1) {
					if (event.getEventType() == MouseEvent.MOUSE_CLICKED
							|| event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
						ImageView imView = (ImageView) event.getSource();
						imView.relocate(event.getSceneX() - (im.getWidth() / 2) * aspect,
								event.getSceneY() - (im.getHeight() / 2) * aspect);
						// labels[current].relocate(event.getSceneX()-(labels[current].getWidth()/2),
						// event.getSceneY()-(labels[current].getHeight() +(im.getHeight()/2)*aspect));
						// OK.relocate(event.getSceneX()-(OK.getWidth()/2),
						// event.getSceneY()+(OK.getHeight()));
					}
					// TODO Auto-generated method stub
				}

			}
		};

		for (int i = 0; i < locations.length; i++) {
			if (locations[i] != null) {
				locations[i].addEventFilter(MouseEvent.MOUSE_CLICKED, EditTouchEvent);
			}
		}
		DrawPlaces();
		updateLocations();

	}

	@FXML
	void mouseClicked(ActionEvent event) throws IOException {

		Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		URL url = getClass().getResource("DefaultPage.fxml");
		AnchorPane pane = FXMLLoader.load(url);

		Scene scene = new Scene(pane);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@FXML
	void backFunc(ActionEvent event) throws IOException {

		Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		URL url = getClass().getResource("SingleEditMap.fxml");
		AnchorPane pane = FXMLLoader.load(url);

		Scene scene = new Scene(pane);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@FXML
	void saveImage(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();

		// Set extension filter
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("png files (*.png)", "*.png"));

		// Prompt user to select a file
		File file = fileChooser.showSaveDialog(null);

		if (file != null) {
			try {
				// Pad the capture area
				WritableImage writableImage = new WritableImage((int) raninImage.getFitWidth(),
						(int) raninImage.getFitHeight());
				// WritableImage writableImage = new WritableImage((int)2000 , (int)2000);
				SnapshotParameters params = new SnapshotParameters();

				params.setViewport(new Rectangle2D(raninImage.getLayoutX(), raninImage.getLayoutY(),
						raninImage.getFitWidth(), raninImage.getFitHeight()));
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

	public WritableImage pixelScaleAwareCanvasSnapshot(AnchorPane node, SnapshotParameters params, double pixelScale) {
		WritableImage writableImage = new WritableImage((int) MapImage.getFitWidth(), (int) MapImage.getFitHeight());
		SnapshotParameters spa = params;
		spa.setTransform(Transform.scale(pixelScale, pixelScale));
		return node.snapshot(spa, writableImage);
	}

}