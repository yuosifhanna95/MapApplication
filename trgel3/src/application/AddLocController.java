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

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Transform;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class AddLocController {
	private int Mode;
	private Label[] labels = new Label[10];
	private ImageView[] locations = new ImageView[10];
	private Place[] Places = new Place[10];
	private ImagePlace[] ImagePlaces = new ImagePlace[10];
	private ImagePlace[] ImagePlacesOld = new ImagePlace[10];
	private ImagePlace CurImagePlace;
	private int Counter = 0;
	private int current = 0;
	private double aspect = 0.75f;
	private Place[] OPlacelist;
	private UPlace[] UPlacelist;

	@FXML
	private Button btn_DeletePlace;

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
	private Button ConfirmUpdate;

	@FXML
	private ImageView raninImage;

	@FXML
	private TextArea TF_Description;

	@FXML
	private TextField TF_Classification;

	@FXML
	private TextField TF_Accessibility;

	@FXML
	private Button btn_SaveDet;

	@FXML
	private ComboBox<String> comboBox;

	@FXML
	private Button btn_UpdateVer;

	@FXML
	void DeletePlace(ActionEvent event) throws Exception, IOException {
		if (CurImagePlace != null) {
			Place p = ImagePlaces[CurImagePlace.getId()].getPlace();
			CurImagePlace.getPlace().setType("DELETE");
			ImagePlaces[CurImagePlace.getId()].getPlace().setType("DELETE");
			mainPane.getChildren().removeAll(ImagePlaces[CurImagePlace.getId()].getImageview());
			mainPane.getChildren().removeAll(ImagePlaces[CurImagePlace.getId()].getLabel());
			mainPane.getChildren().removeAll(ImagePlacesOld[CurImagePlace.getId()].getImageview());
			mainPane.getChildren().removeAll(ImagePlacesOld[CurImagePlace.getId()].getLabel());
			ImagePlaces[CurImagePlace.getId()].setChanged(true);
			ImagePlaces[CurImagePlace.getId()].setPlace(new UPlace(p.getMapId(), p.getCityName(), p.getPlaceName(),
					p.getDescription(), p.getClassification(), p.getAccessibility(), p.getLocX(), p.getLocY(),
					p.getType(), (int) p.getSerialID()));

			// new UPlace(p.getMapId(), p.getCityName(), p.getPlaceName(),
			// p.getDescription(), p.getClassification(), p.getAccessibility(),
			// p.getSerialID(), p.getLocX(),
			// p.getLocY(), p.getType(), p.getNumOfmaps(),p.getSerialID()));

			// ImagePlaces[CurImagePlace.getId()] =
			// null;
		}
		// BuildAllPlaces();
		// BuildOldMap();
		// DrawUpdatedPlaces();
		// BuildAllPlaces();
		// BuildOldMap();
	}

	@FXML
	void UpdateVersion(ActionEvent event) throws Exception, IOException {

		@SuppressWarnings("resource")
		Socket socket = new Socket("localhost", 5555);
		String[] array = new String[2];
		ObjectOutputStream objectOutput;
		array[0] = "VersionUpdate";
		array[1] = "" + Globals.map.getCity();
		objectOutput = new ObjectOutputStream(socket.getOutputStream());
		objectOutput.writeObject(array);

	}

	@FXML
	void SaveDetailsFunc(ActionEvent event) {
		if (CurImagePlace != null) {
			try {

				String ID = CurImagePlace.getImageview().getId();
				ID = ID.replace("imv", "");
				int ind = Integer.parseInt(ID);
				String PlaceDes = TF_Description.getText();
				int Accessibilty = Integer.parseInt(TF_Accessibility.getText());
				String Classification = TF_Classification.getText();
				ImagePlaces[ind].getPlace().setDescription(PlaceDes);
				ImagePlaces[ind].getPlace().setAccessibility(Accessibilty);
				ImagePlaces[ind].getPlace().setClassification(Classification);

			} catch (Exception e) {
				// TODO: handle exception
			}

		}
	}

	@FXML
	void UpdateMap(ActionEvent event) throws Exception {

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
		Boolean TherewasUpdate = false;
		// System.out.println(((Object[]) data)[0]);
		UPlace[] list = (UPlace[]) ((Object[]) data)[1];
		for (int i = 0; i < ImagePlaces.length; i++) {
			if (ImagePlaces[i] != null)
				if (ImagePlaces[i].getChanged()) {
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
						int mapsNum = ImagePlaces[i].getPlace().getNumOfmaps();
						String type = ImagePlaces[i].getPlace().getType();
						array2[0] = "UpdatePlace";
						array2[1] = new UPlace(MapId, CityName, PlaceName, description, classification, accessibility,
								serialid, LocX, LocY, type, mapsNum, PlaceId);
						// objectOutput = new ObjectOutputStream(socket.getOutputStream());
						array2[2] = "" + serialid;
						@SuppressWarnings("resource")
						Socket socket2 = new Socket("localhost", 5555);
						objectOutput = new ObjectOutputStream(socket2.getOutputStream());
						objectOutput.writeObject(array2);
						flagnew = 0;
						TherewasUpdate = true;

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
								int mapsNum = ImagePlaces[i].getPlace().getNumOfmaps();
								String type = "UPDATE";
								array2[0] = "UpdatePlace";
								array2[1] = new Place(MapId, CityName, PlaceName, description, classification,
										accessibility, serialid, LocX, LocY, type, mapsNum);
								// objectOutput = new ObjectOutputStream(socket.getOutputStream());
								array2[2] = "" + serialid;
								@SuppressWarnings("resource")
								Socket socket2 = new Socket("localhost", 5555);
								objectOutput = new ObjectOutputStream(socket2.getOutputStream());
								objectOutput.writeObject(array2);
								flagnew = 0;
								TherewasUpdate = true;
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
							String type;
							if (ImagePlaces[i].getPlace() instanceof UPlace)
								if (((UPlace) ImagePlaces[i].getPlace()).getPlaceId() == -1)
									type = "NEW";
								else if ((ImagePlaces[i].getPlace()).getType().equals("DELETE"))
									type = "DELETE";
								else
									type = "UPDATE";
							else
								type = "UPDATE";

							array2[0] = "UpdatePlace";
							Place pp = new Place(MapId, CityName, PlaceName, description, classification, accessibility,
									LocX, LocY, type);
							array2[1] = pp;
							array2[2] = "" + serialid;
							@SuppressWarnings("resource")
							Socket socket2 = new Socket("localhost", 5555);
							objectOutput = new ObjectOutputStream(socket2.getOutputStream());
							objectOutput.writeObject(array2);
							TherewasUpdate = true;
						}
					}
				}
		}
		if (TherewasUpdate)
			Globals.ThereIsMapUpdate = true;
		if (Globals.MODE >= 4 && Globals.ThereIsMapUpdate)
			ConfirmUpdate.setVisible(true);
		if (TherewasUpdate) {
			BuildAllPlaces();
			BuildOldMap();

		}
	}

	@SuppressWarnings("deprecation")
	void BuildAllPlaces() throws IOException, ClassNotFoundException {

		// ImagePlaces = null;ty
		Counter = 0;
		for (int i = 0; i < ImagePlaces.length; i++) {

			if (ImagePlaces[i] != null) {

				mainPane.getChildren().removeAll(ImagePlaces[i].getImageview());
				mainPane.getChildren().removeAll(ImagePlaces[i].getLabel());

			}

		}
		for (int i = 0; i < ImagePlacesOld.length; i++) {

			if (ImagePlacesOld[i] != null) {

				mainPane.getChildren().removeAll(ImagePlacesOld[i].getImageview());
				mainPane.getChildren().removeAll(ImagePlacesOld[i].getLabel());

			}

		}
		ImagePlacesOld = new ImagePlace[10];
		ImagePlaces = new ImagePlace[10];

		@SuppressWarnings("resource")
		Socket socket = new Socket("localhost", 5555);
		String[] array = new String[3];
		String[] array2 = new String[2];
		array[0] = "getPlaces";
		// get[1] = "" + ImagePlaces[i].getPlace().getCityName();
		array[1] = "" + Globals.map.getId();
		array[2] = "" + Globals.map.getCity();

		ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
		objectOutput.writeObject(array);

		Object data;
		ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());
		data = objectInput.readObject();
		// System.out.println(((Object[]) data)[0]);
		OPlacelist = (Place[]) ((Object[]) data)[1];

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
		UPlacelist = (UPlace[]) ((Object[]) data2)[1];

		int counter = 0, counter2 = 0;
		int flag = 0;
		for (int i = 0; i < OPlacelist.length; i++) {
			for (int c = 0; c < UPlacelist.length; c++) {
				// if (Globals.map.getId() == list2[c].getSerialID()) {
				if (OPlacelist[i].getSerialID() == UPlacelist[c].getPlaceId()) {
					Image im = new Image("File:editedloc.png");
					ImageView newLoc = new ImageView(im);
					// newLoc.relocate((double) list2[c].getLocX(), (double) list2[c].getLocY());
					Label label = new Label();
					// label.relocate((double) list2[c].getLocX(), (double) list2[c].getLocY());

					newLoc.relocate(UPlacelist[c].getLocX() - (im.getWidth() / 2) * aspect,
							UPlacelist[c].getLocY() - (im.getHeight() / 2) * aspect);

					label.setFont(new Font("Quicksand", 20));
					UPlace p = new UPlace("" + Globals.map.getId(), UPlacelist[c].getCityName(),
							UPlacelist[c].getPlaceName(), UPlacelist[c].getDescription(),
							UPlacelist[c].getClassification(), UPlacelist[c].getAccessibility(),
							UPlacelist[c].getSerialID(), UPlacelist[c].getLocX(), UPlacelist[c].getLocY(),
							UPlacelist[c].getType(), UPlacelist[c].getNumOfmaps(), UPlacelist[c].getPlaceId());
					label.setText(p.getPlaceName());
					Text t = new Text();
					t.setText(label.getText());
					t.setFont(label.getFont());
					double width = t.getBoundsInLocal().getWidth();
					double height = t.getBoundsInLocal().getHeight();

					label.relocate(UPlacelist[c].getLocX() - (width / 2),
							UPlacelist[c].getLocY() - (height + (im.getHeight() / 2) * aspect));

					newLoc.setFitHeight(im.getHeight() * aspect);
					newLoc.setFitWidth(im.getWidth() * aspect);
					newLoc.setId("imv" + Counter);

					ImagePlaces[counter] = new ImagePlace(counter, newLoc, label, p, UPlacelist[c].getLocX(),
							UPlacelist[c].getLocY(), false);
					counter++;
					Counter++;
					flag = 1;
					break;
				}

				// }
			}
			if (flag == 0) {
				Image im = new Image("File:oldloc.png");
				ImageView newLoc = new ImageView(im);
				double X = OPlacelist[i].getLocX(), Y = OPlacelist[i].getLocY();
				// newLoc1.relocate(X, Y);
				Label label = new Label();
				// label.relocate(X, Y);

				newLoc.relocate(OPlacelist[i].getLocX() - (im.getWidth() / 2) * aspect,
						OPlacelist[i].getLocY() - (im.getHeight() / 2) * aspect);

				label.setFont(new Font("Quicksand", 20));
				Place p = new Place("" + Globals.map.getId(), OPlacelist[i].getCityName(), OPlacelist[i].getPlaceName(),
						OPlacelist[i].getDescription(), OPlacelist[i].getClassification(),
						OPlacelist[i].getAccessibility(), OPlacelist[i].getSerialID(), OPlacelist[i].getLocX(),
						OPlacelist[i].getLocY(), OPlacelist[i].getType(), OPlacelist[i].getNumOfmaps());
				label.setText(p.getPlaceName());

				Text t = new Text();
				t.setText(label.getText());
				t.setFont(label.getFont());
				double width = t.getBoundsInLocal().getWidth();
				double height = t.getBoundsInLocal().getHeight();

				label.relocate(OPlacelist[i].getLocX() - (width / 2),
						OPlacelist[i].getLocY() - (height + (im.getHeight() / 2) * aspect));

				newLoc.setFitHeight(im.getHeight() * aspect);
				newLoc.setFitWidth(im.getWidth() * aspect);
				newLoc.setId("imv" + Counter);

				ImagePlaces[counter] = new ImagePlace(counter, newLoc, label, p, OPlacelist[i].getLocX(),
						OPlacelist[i].getLocY(), false);
				counter++;
				counter2++;
				Counter++;
			}
			flag = 0;
		}

		for (int c = 0; c < UPlacelist.length; c++) {
			if (UPlacelist[c].getType().equals("NEW") || UPlacelist[c].getType().equals("UNEW")) {
				Image im = new Image("File:newloc.png");
				ImageView newLoc = new ImageView(im);
				// newLoc.relocate((double) list2[c].getLocX(), (double) list2[c].getLocY());
				Label label = new Label();
				// label.relocate((double) list2[c].getLocX(), (double) list2[c].getLocY());

				newLoc.relocate(UPlacelist[c].getLocX() - (im.getWidth() / 2) * aspect,
						UPlacelist[c].getLocY() - (im.getHeight() / 2) * aspect);

				label.setFont(new Font("Quicksand", 20));
				UPlace p = new UPlace("" + Globals.map.getId(), UPlacelist[c].getCityName(),
						UPlacelist[c].getPlaceName(), UPlacelist[c].getDescription(), UPlacelist[c].getClassification(),
						UPlacelist[c].getAccessibility(), UPlacelist[c].getSerialID(), UPlacelist[c].getLocX(),
						UPlacelist[c].getLocY(), UPlacelist[c].getType(), UPlacelist[c].getNumOfmaps(),
						UPlacelist[c].getPlaceId());
				label.setText(p.getPlaceName());

				Text t = new Text();
				t.setText(label.getText());
				t.setFont(label.getFont());
				double width = t.getBoundsInLocal().getWidth();
				double height = t.getBoundsInLocal().getHeight();

				label.relocate(UPlacelist[c].getLocX() - (width / 2),
						UPlacelist[c].getLocY() - (height + (im.getHeight() / 2) * aspect));

				newLoc.setFitHeight(im.getHeight() * aspect);
				newLoc.setFitWidth(im.getWidth() * aspect);
				newLoc.setId("imv" + Counter);
				ImagePlaces[counter] = new ImagePlace(counter, newLoc, label, p, UPlacelist[c].getLocX(),
						UPlacelist[c].getLocY(), false);
				counter++;
				Counter++;
			}
		}

		// for (int i = 0; i < ImagePlaces.length; i++) {

		// if (ImagePlaces[i] != null) {

		// mainPane.getChildren().addAll(ImagePlaces[i].getImageview());
		// mainPane.getChildren().addAll(ImagePlaces[i].getLabel());

		// }

		// }
		DrawUpdatedPlaces();

		AddMethodsForIP();

	}

	@SuppressWarnings("deprecation")
	void DrawOldPlaces() throws IOException, ClassNotFoundException {

		for (int i = 0; i < ImagePlaces.length; i++) {

			if (ImagePlaces[i] != null) {
				if (ImagePlaces[i].getPlace() instanceof UPlace) {
					if (!ImagePlaces[i].getPlace().getType().equals("DELETE")) {

						mainPane.getChildren().removeAll(ImagePlaces[i].getImageview());
						mainPane.getChildren().removeAll(ImagePlaces[i].getLabel());
					}
				} else {
					mainPane.getChildren().removeAll(ImagePlaces[i].getImageview());
					mainPane.getChildren().removeAll(ImagePlaces[i].getLabel());
				}
			}
		}
		for (int i = 0; i < ImagePlacesOld.length; i++) {

			if (ImagePlacesOld[i] != null) {
				if (ImagePlacesOld[i].getPlace() instanceof UPlace) {
					if (!ImagePlacesOld[i].getPlace().getType().equals("DELETE")) {
						mainPane.getChildren().addAll(ImagePlacesOld[i].getImageview());
						mainPane.getChildren().addAll(ImagePlacesOld[i].getLabel());
					}
				} else {
					mainPane.getChildren().addAll(ImagePlacesOld[i].getImageview());
					mainPane.getChildren().addAll(ImagePlacesOld[i].getLabel());
				}

			}
		}

		// AddMethodsForIP();

	}

	void DrawUpdatedPlaces() throws IOException, ClassNotFoundException {

		for (int i = 0; i < ImagePlaces.length; i++) {

			if (ImagePlaces[i] != null)
				if (ImagePlaces[i].getPlace() instanceof UPlace) {
					if (!ImagePlaces[i].getPlace().getType().equals("DELETE")) {

						mainPane.getChildren().addAll(ImagePlaces[i].getImageview());
						mainPane.getChildren().addAll(ImagePlaces[i].getLabel());

					}
				} else if (ImagePlaces[i].getPlace() instanceof Place) {
					mainPane.getChildren().addAll(ImagePlaces[i].getImageview());
					mainPane.getChildren().addAll(ImagePlaces[i].getLabel());
				}
		}
		for (int i = 0; i < ImagePlacesOld.length; i++) {

			if (ImagePlacesOld[i] != null)
				if (ImagePlacesOld[i].getPlace() instanceof UPlace) {
					if (!ImagePlacesOld[i].getPlace().getType().equals("DELETE")) {

						mainPane.getChildren().removeAll(ImagePlacesOld[i].getImageview());
						mainPane.getChildren().removeAll(ImagePlacesOld[i].getLabel());

					}
				} else {
					mainPane.getChildren().removeAll(ImagePlacesOld[i].getImageview());
					mainPane.getChildren().removeAll(ImagePlacesOld[i].getLabel());
				}

		}

		// AddMethodsForIP();

	}

	void BuildOldMap() throws IOException, Exception {

		int counter = 0;
		int flag = 0;
		for (int i = 0; i < OPlacelist.length; i++) {

			if (flag == 0) {
				Image im = new Image("File:oldloc.png");
				ImageView newLoc = new ImageView(im);
				double X = OPlacelist[i].getLocX(), Y = OPlacelist[i].getLocY();
				// newLoc1.relocate(X, Y);
				Label label = new Label();
				// label.relocate(X, Y);

				newLoc.relocate(OPlacelist[i].getLocX() - (im.getWidth() / 2) * aspect,
						OPlacelist[i].getLocY() - (im.getHeight() / 2) * aspect);

				label.setFont(new Font("Quicksand", 20));
				Place p = new Place("" + Globals.map.getId(), OPlacelist[i].getCityName(), OPlacelist[i].getPlaceName(),
						OPlacelist[i].getDescription(), OPlacelist[i].getClassification(),
						OPlacelist[i].getAccessibility(), OPlacelist[i].getSerialID(), OPlacelist[i].getLocX(),
						OPlacelist[i].getLocY(), OPlacelist[i].getType(), OPlacelist[i].getNumOfmaps());
				label.setText(p.getPlaceName());

				Text t = new Text();
				t.setText(label.getText());
				t.setFont(label.getFont());
				double width = t.getBoundsInLocal().getWidth();
				double height = t.getBoundsInLocal().getHeight();

				label.relocate(OPlacelist[i].getLocX() - (width / 2),
						OPlacelist[i].getLocY() - (height + (im.getHeight() / 2) * aspect));

				newLoc.setFitHeight(im.getHeight() * aspect);
				newLoc.setFitWidth(im.getWidth() * aspect);
				newLoc.setId("imv" + Counter);

				ImagePlacesOld[counter] = new ImagePlace(counter, newLoc, label, p, OPlacelist[i].getLocX(),
						OPlacelist[i].getLocY(), false);

				counter++;
				Counter++;
			}
			flag = 0;

		}

	}

	void updateLocations() {
		Image im = new Image("File:oldloc.png");
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
		Image im = new Image("File:oldloc.png");
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
							ImagePlaces[Integer.parseInt(ID)].setChanged(true);

							TF_Accessibility
									.setText("" + ImagePlaces[Integer.parseInt(ID)].getPlace().getAccessibility());
							TF_Description.setText(ImagePlaces[Integer.parseInt(ID)].getPlace().getDescription());
							TF_Classification.setText(ImagePlaces[Integer.parseInt(ID)].getPlace().getClassification());

						}
					} else if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
						CurImagePlace = new ImagePlace();
						CurImagePlace.setImageview((ImageView) event.getSource());
						String ID = CurImagePlace.getImageview().getId();
						ID = ID.replace("imv", "");
						CurImagePlace.setId(Integer.parseInt(ID));
						CurImagePlace.setLabel(ImagePlaces[Integer.parseInt(ID)].getLabel());
						CurImagePlace.setPlace(ImagePlaces[Integer.parseInt(ID)].getPlace());

						TF_Accessibility.setText("" + ImagePlaces[Integer.parseInt(ID)].getPlace().getAccessibility());
						TF_Description.setText(ImagePlaces[Integer.parseInt(ID)].getPlace().getDescription());
						TF_Classification.setText(ImagePlaces[Integer.parseInt(ID)].getPlace().getClassification());

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
		if (!NewLocation.getText().equals("") && Counter < 10 && ThereIsNoLocation(NewLocation.getText())) {
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
			Image im = new Image("File:newloc.png");
			ImageView newLoc = new ImageView(im);
			newLoc.relocate(MapImage.getLayoutX(), MapImage.getLayoutY());
			label.relocate(MapImage.getLayoutX(), MapImage.getLayoutY());

			newLoc.setFitHeight(im.getHeight() * aspect);
			newLoc.setFitWidth(im.getWidth() * aspect);
			newLoc.setId("imv" + Counter);
			labels[Counter] = label;
			locations[Counter] = newLoc;
			Places[Counter] = new UPlace("" + Globals.map.getId(), Globals.map.getCity(), label.getText(), "", "", 1,
					(int) newLoc.getX(), (int) newLoc.getY(), "NEW", -1);

			ImagePlaces[Counter] = new ImagePlace(Counter, locations[Counter], labels[Counter], Places[Counter],
					(int) MapImage.getLayoutX(), (int) MapImage.getLayoutY(), true);

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
								TF_Accessibility
										.setText("" + ImagePlaces[Integer.parseInt(ID)].getPlace().getAccessibility());
								TF_Description.setText(ImagePlaces[Integer.parseInt(ID)].getPlace().getDescription());
								TF_Classification
										.setText(ImagePlaces[Integer.parseInt(ID)].getPlace().getClassification());

							}
						} else if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
							CurImagePlace = new ImagePlace();
							CurImagePlace.setImageview((ImageView) event.getSource());
							String ID = CurImagePlace.getImageview().getId();
							ID = ID.replace("imv", "");
							CurImagePlace.setId(Integer.parseInt(ID));
							CurImagePlace.setLabel(labels[CurImagePlace.getId()]);
							CurImagePlace.setPlace(Places[CurImagePlace.getId()]);
							TF_Accessibility
									.setText("" + ImagePlaces[Integer.parseInt(ID)].getPlace().getAccessibility());
							TF_Description.setText(ImagePlaces[Integer.parseInt(ID)].getPlace().getDescription());
							TF_Classification.setText(ImagePlaces[Integer.parseInt(ID)].getPlace().getClassification());

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
	void ConfirmUpdate(ActionEvent event) throws Exception {

		@SuppressWarnings("resource")
		Socket socket = new Socket("localhost", 5555);
		String[] array = new String[2];
		ObjectOutputStream objectOutput;

		array[0] = "ConfirmUpdate";
		array[1] = "" + Globals.map.getId();
		objectOutput = new ObjectOutputStream(socket.getOutputStream());
		objectOutput.writeObject(array);
		ConfirmUpdate.setVisible(false);
		BuildAllPlaces();
		BuildOldMap();
		/*
		 * for (int i = 0; i < ImagePlaces.length; i++) { if (ImagePlaces[i] != null) {
		 * if (ImagePlaces[i].getPlace() instanceof UPlace) { String MapId =
		 * ImagePlaces[i].getPlace().getMapId(); array[0] = "ConfirmUpdate"; array[1] =
		 * "" + MapId; objectOutput = new ObjectOutputStream(socket.getOutputStream());
		 * objectOutput.writeObject(array); }
		 * 
		 * }
		 * 
		 * }
		 */
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

		comboBox.getItems().addAll("Original", "Updated");

		comboBox.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(@SuppressWarnings("rawtypes") ObservableValue value, String old, String newv) {
				if (newv.equals("Original")) {
					try {
						DrawOldPlaces();
					} catch (ClassNotFoundException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if (old != null) {
					try {
						DrawUpdatedPlaces();
					} catch (ClassNotFoundException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				// Show your scene here
			}
		});
		TF_Description.setWrapText(true);
		if (Globals.MODE < 4) {
			ConfirmUpdate.setVisible(false);
		}
		if (!Globals.ThereIsMapUpdate)
			ConfirmUpdate.setVisible(false);

		Image LinkedImage = new Image(Globals.map.getLinkCustomer());
		raninImage = new ImageView(LinkedImage);
		raninImage.relocate(291, 50);
		raninImage.setFitWidth(LinkedImage.getWidth());
		raninImage.setFitHeight(LinkedImage.getHeight());

		mainPane.getChildren().add(raninImage);
		Label label = new Label();
		label.setText("stam");
		label.setFont(new Font("Arial", 20));
		Image im = new Image("File:oldloc.png");
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
		BuildAllPlaces();
		BuildOldMap();

		// updateLocations();

	}

	@FXML
	void mouseClicked(ActionEvent event) throws IOException {

		Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		URL url = getClass().getResource("DefaultPage.fxml");
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

	public Boolean ThereIsNoLocation(String Loc) {
		for (int i = 0; i < ImagePlaces.length; i++)
			if (ImagePlaces[i] != null)
				if (ImagePlaces[i].getPlace().getPlaceName().toLowerCase().equals(Loc.toLowerCase()))
					return false;
		return true;
	}

}