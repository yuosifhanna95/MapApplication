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

public class AddRLocController {
	private int Mode;
	private Label[] labels = new Label[10];
	private ImageView[] locations = new ImageView[10];
	private Place[] Places = new Place[10];
	private RoutePlace[] RPlaces = new RoutePlace[10];
	private ImagePlace[] ImagePlaces = new ImagePlace[10];
	private ImagePlace[] ImagePlacesOld = new ImagePlace[10];
	private ImagePlace CurImagePlace;
	private int Counter = 0;
	private int current = 0;
	private double aspect = 0.75f;
	private Place[] OPlacelist;
	private UPlace[] UPlacelist;
	private RoutePlace[] ORoutePlaceList;
	private URoutePlace[] URoutePlaceList;

	@FXML
	private Button btn_DeletePlace;

	@FXML
	private Button btn_UpdateVer;

	@FXML
	private Button btn_UpdateRoute;

	@FXML
	private AnchorPane mainPane;

	@FXML
	private ImageView RouteImage;

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
	private ComboBox<String> comboboxLoc;

	@FXML
	void DeletePlace(ActionEvent event) throws Exception, IOException {
		if (CurImagePlace != null) {
			RoutePlace p = ImagePlaces[CurImagePlace.getId()].getRplace();
			CurImagePlace.getRplace().setType("DELETE");
			ImagePlaces[CurImagePlace.getId()].getRplace().setType("DELETE");
			mainPane.getChildren().removeAll(ImagePlaces[CurImagePlace.getId()].getImageview());
			mainPane.getChildren().removeAll(ImagePlaces[CurImagePlace.getId()].getLabel());
			mainPane.getChildren().removeAll(ImagePlacesOld[CurImagePlace.getId()].getImageview());
			mainPane.getChildren().removeAll(ImagePlacesOld[CurImagePlace.getId()].getLabel());
			ImagePlaces[CurImagePlace.getId()].setChanged(true);
			ImagePlaces[CurImagePlace.getId()].setRplace(new URoutePlace(p.getRouteId(), (int) p.getSerialID(),
					p.getPlace(), p.getTime(), p.getLocX(), p.getLocY(), -1, p.getType()));
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
		Socket socket = new Socket(Globals.IpAddress, 5555);
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
	void UpdateRoute(ActionEvent event) throws Exception {

		@SuppressWarnings("resource")
		Socket socket = new Socket(Globals.IpAddress, 5555);
		String[] array = new String[2];
		Object[] array2 = new Object[3];
		array[0] = "getURoutePlaces";
		// get[1] = "" + ImagePlaces[i].getPlace().getCityName();
		array[1] = "" + Globals.route.getId();

		ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
		objectOutput.writeObject(array);

		Object data;
		ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());
		data = objectInput.readObject();
		// System.out.println(((Object[]) data)[0]);
		URoutePlaceList = (URoutePlace[]) ((Object[]) data)[1];
		// UPlacelist = GetUPListFromURoutePlace(URoutePlaceList);
		UPlace[] list = GetUPListFromURoutePlace(URoutePlaceList);
		Boolean TherewasUpdate = false;
		for (int i = 0; i < ImagePlaces.length; i++) {
			if (ImagePlaces[i] != null)
				if (ImagePlaces[i].getChanged()) {
					int flagnew = 1;
					if (ImagePlaces[i].getRplace() instanceof URoutePlace) {
						long serialid = ImagePlaces[i].getRplace().getSerialID();
						int RouteId = ImagePlaces[i].getRplace().getRouteId();
						int RPlaceId = ((URoutePlace) (ImagePlaces[i].getRplace())).getRPlaceId();
						String CityName = Globals.route.getCity();// ImagePlaces[i].getRplace().getCity();
						String PlaceName = ImagePlaces[i].getRplace().getPlace();
						// String description = ImagePlaces[i].getRplace().getDescription();
						// String classification = ImagePlaces[i].getRplace().getClassification();
						// int accessibility = ImagePlaces[i].getRplace().getAccessibility();
						int LocX = ImagePlaces[i].getX();
						int LocY = ImagePlaces[i].getY();
						int time = ImagePlaces[i].getRplace().getTime();
						// int mapsNum = ImagePlaces[i].getRplace().getNumOfmaps();
						String Type = ((URoutePlace) ImagePlaces[i].getRplace()).getType();
						array2[0] = "UpdateRPlace";
						array2[1] = new URoutePlace(RouteId, RPlaceId, PlaceName, time, LocX, LocY, serialid, Type);
						// objectOutput = new ObjectOutputStream(socket.getOutputStream());
						array2[2] = "" + Globals.route.getCity();
						@SuppressWarnings("resource")
						Socket socket2 = new Socket(Globals.IpAddress, 5555);
						objectOutput = new ObjectOutputStream(socket2.getOutputStream());
						objectOutput.writeObject(array2);
						flagnew = 0;
						TherewasUpdate = true;

					} else {

						for (RoutePlace pl : URoutePlaceList) {

							if (ImagePlaces[i].getRplace().getSerialID() == pl.getSerialID()) {
								long serialid = ImagePlaces[i].getRplace().getSerialID();
								int RouteId = ImagePlaces[i].getRplace().getRouteId();
								String CityName = Globals.route.getCity();// ImagePlaces[i].getRplace().getCity();
								String PlaceName = ImagePlaces[i].getRplace().getPlace();
								// String description = ImagePlaces[i].getRplace().getDescription();
								// String classification = ImagePlaces[i].getRplace().getClassification();
								// int accessibility = ImagePlaces[i].getRplace().getAccessibility();
								int LocX = ImagePlaces[i].getX();
								int LocY = ImagePlaces[i].getY();
								int time = ImagePlaces[i].getRplace().getTime();
								String type = "UPDATE";
								array2[0] = "UpdateRPlace";
								array2[1] = new RoutePlace(RouteId, PlaceName, time, LocX, LocY, serialid);
								// objectOutput = new ObjectOutputStream(socket.getOutputStream());
								array2[2] = "" + Globals.route.getCity();
								@SuppressWarnings("resource")
								Socket socket2 = new Socket(Globals.IpAddress, 5555);
								objectOutput = new ObjectOutputStream(socket2.getOutputStream());
								objectOutput.writeObject(array2);
								flagnew = 0;
								TherewasUpdate = true;
								break;

							}

						}
						if (flagnew == 1) {
							long serialid = ImagePlaces[i].getRplace().getSerialID();
							int RouteId = ImagePlaces[i].getRplace().getRouteId();
							String CityName = Globals.route.getCity();// ImagePlaces[i].getRplace().getCity();
							String PlaceName = ImagePlaces[i].getRplace().getPlace();
							// String description = ImagePlaces[i].getRplace().getDescription();
							// String classification = ImagePlaces[i].getRplace().getClassification();
							// int accessibility = ImagePlaces[i].getRplace().getAccessibility();
							int LocX = ImagePlaces[i].getX();
							int LocY = ImagePlaces[i].getY();
							int time = ImagePlaces[i].getRplace().getTime();
							String type;
							if (ImagePlaces[i].getRplace() instanceof URoutePlace)
								if (((URoutePlace) ImagePlaces[i].getRplace()).getRPlaceId() == -1)
									type = "NEW";
								else if ((ImagePlaces[i].getPlace()).getType().equals("DELETE"))
									type = "DELETE";
								else
									type = "UPDATE";
							else
								type = "UPDATE";
							array2[0] = "UpdateRPlace";
							RoutePlace pp = new RoutePlace(RouteId, PlaceName, time, LocX, LocY, serialid, type);
							array2[1] = pp;
							array2[2] = "" + Globals.route.getCity();
							@SuppressWarnings("resource")
							Socket socket2 = new Socket(Globals.IpAddress, 5555);
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
		OPlacelist = GetPListFromRoutePlace(ORoutePlaceList);

		array2[0] = "getURoutePlaces";
		// get[1] = "" + ImagePlaces[i].getPlace().getCityName();
		array2[1] = "" + Globals.route.getId();
		Socket socket2 = new Socket(Globals.IpAddress, 5555);
		objectOutput = new ObjectOutputStream(socket2.getOutputStream());
		objectOutput.writeObject(array2);

		Object data2;
		ObjectInputStream objectInput2 = new ObjectInputStream(socket2.getInputStream());
		data2 = objectInput2.readObject();
		// System.out.println(((Object[]) data2)[0]);
		URoutePlaceList = (URoutePlace[]) ((Object[]) data2)[1];
		UPlacelist = GetUPListFromURoutePlace(URoutePlaceList);

		int counter = 0, counter2 = 0;
		int flag = 0;
		for (int i = 0; i < ORoutePlaceList.length; i++) {
			for (int c = 0; c < URoutePlaceList.length; c++) {
				// if (Globals.map.getId() == list2[c].getSerialID()) {
				if (ORoutePlaceList[i].getSerialID() == URoutePlaceList[c].getRPlaceId()) {
					Image im = new Image("File:editedloc.png");
					ImageView newLoc = new ImageView(im);
					// newLoc.relocate((double) list2[c].getLocX(), (double) list2[c].getLocY());
					Label label = new Label();
					// label.relocate((double) list2[c].getLocX(), (double) list2[c].getLocY());

					newLoc.relocate(URoutePlaceList[c].getLocX() - (im.getWidth() / 2) * aspect,
							URoutePlaceList[c].getLocY() - (im.getHeight() / 2) * aspect);

					label.setFont(new Font("Quicksand", 20));
					UPlace p = new UPlace("" + Globals.route.getId(), UPlacelist[c].getCityName(),
							UPlacelist[c].getPlaceName(), UPlacelist[c].getDescription(),
							UPlacelist[c].getClassification(), UPlacelist[c].getAccessibility(),
							UPlacelist[c].getSerialID(), URoutePlaceList[c].getLocX(), URoutePlaceList[c].getLocY(),
							UPlacelist[c].getType(), UPlacelist[c].getNumOfmaps(), UPlacelist[c].getPlaceId());
					label.setText(p.getPlaceName());
					Text t = new Text();
					t.setText(label.getText());
					t.setFont(label.getFont());
					double width = t.getBoundsInLocal().getWidth();
					double height = t.getBoundsInLocal().getHeight();

					label.relocate(URoutePlaceList[c].getLocX() - (width / 2),
							URoutePlaceList[c].getLocY() - (height + (im.getHeight() / 2) * aspect));

					newLoc.setFitHeight(im.getHeight() * aspect);
					newLoc.setFitWidth(im.getWidth() * aspect);
					newLoc.setId("imv" + Counter);

					URoutePlace URp = new URoutePlace(URoutePlaceList[c].getRouteId(), URoutePlaceList[c].getRPlaceId(),
							URoutePlaceList[c].getPlace(), URoutePlaceList[c].getTime(), URoutePlaceList[c].getLocX(),
							URoutePlaceList[c].getLocY(), URoutePlaceList[c].getSerialID(),
							URoutePlaceList[c].getType());

					ImagePlaces[counter] = new ImagePlace(counter, newLoc, label, URp, URoutePlaceList[c].getLocX(),
							URoutePlaceList[c].getLocY(), false);
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
				double X = ORoutePlaceList[i].getLocX(), Y = ORoutePlaceList[i].getLocY();
				// newLoc1.relocate(X, Y);
				Label label = new Label();
				// label.relocate(X, Y);

				newLoc.relocate(ORoutePlaceList[i].getLocX() - (im.getWidth() / 2) * aspect,
						ORoutePlaceList[i].getLocY() - (im.getHeight() / 2) * aspect);

				label.setFont(new Font("Quicksand", 20));
				Place p = new Place("" + Globals.route.getId(), OPlacelist[i].getCityName(),
						OPlacelist[i].getPlaceName(), OPlacelist[i].getDescription(), OPlacelist[i].getClassification(),
						OPlacelist[i].getAccessibility(), OPlacelist[i].getSerialID(), ORoutePlaceList[i].getLocX(),
						ORoutePlaceList[i].getLocY(), OPlacelist[i].getType(), OPlacelist[i].getNumOfmaps());
				label.setText(p.getPlaceName());

				Text t = new Text();
				t.setText(label.getText());
				t.setFont(label.getFont());
				double width = t.getBoundsInLocal().getWidth();
				double height = t.getBoundsInLocal().getHeight();

				label.relocate(ORoutePlaceList[i].getLocX() - (width / 2),
						ORoutePlaceList[i].getLocY() - (height + (im.getHeight() / 2) * aspect));

				newLoc.setFitHeight(im.getHeight() * aspect);
				newLoc.setFitWidth(im.getWidth() * aspect);
				newLoc.setId("imv" + Counter);

				RoutePlace rp = new RoutePlace(ORoutePlaceList[i].getRouteId(), ORoutePlaceList[i].getPlace(),
						ORoutePlaceList[i].getTime(), ORoutePlaceList[i].getLocX(), ORoutePlaceList[i].getLocY(),
						ORoutePlaceList[i].getSerialID());

				ImagePlaces[counter] = new ImagePlace(counter, newLoc, label, rp, ORoutePlaceList[i].getLocX(),
						ORoutePlaceList[i].getLocY(), false);
				counter++;
				counter2++;
				Counter++;
			}
			flag = 0;
		}

		for (int c = 0; c < URoutePlaceList.length; c++) {
			if (URoutePlaceList[c].getType().equals("NEW") || URoutePlaceList[c].getType().equals("UNEW")) {
				Image im = new Image("File:newloc.png");
				ImageView newLoc = new ImageView(im);
				// newLoc.relocate((double) list2[c].getLocX(), (double) list2[c].getLocY());
				Label label = new Label();
				// label.relocate((double) list2[c].getLocX(), (double) list2[c].getLocY());

				newLoc.relocate(UPlacelist[c].getLocX() - (im.getWidth() / 2) * aspect,
						UPlacelist[c].getLocY() - (im.getHeight() / 2) * aspect);

				label.setFont(new Font("Quicksand", 20));
				UPlace p = new UPlace("" + Globals.route.getId(), UPlacelist[c].getCityName(),
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

				URoutePlace URp = new URoutePlace(URoutePlaceList[c].getRouteId(), URoutePlaceList[c].getRPlaceId(),
						URoutePlaceList[c].getPlace(), URoutePlaceList[c].getTime(), URoutePlaceList[c].getLocX(),
						URoutePlaceList[c].getLocY(), URoutePlaceList[c].getSerialID(), URoutePlaceList[c].getType());
				ImagePlaces[counter] = new ImagePlace(counter, newLoc, label, URp, UPlacelist[c].getLocX(),
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
				if (ImagePlaces[i].getRplace() instanceof URoutePlace) {
					if (!ImagePlaces[i].getRplace().getType().equals("DELETE")) {

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
				if (ImagePlacesOld[i].getRplace() instanceof URoutePlace) {
					if (!ImagePlacesOld[i].getRplace().getType().equals("DELETE")) {
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
				if (ImagePlaces[i].getRplace() instanceof URoutePlace) {
					if (!ImagePlaces[i].getPlace().getType().equals("DELETE")) {

						mainPane.getChildren().addAll(ImagePlaces[i].getImageview());
						mainPane.getChildren().addAll(ImagePlaces[i].getLabel());

					}
				} else if (ImagePlaces[i].getRplace() instanceof RoutePlace) {
					mainPane.getChildren().addAll(ImagePlaces[i].getImageview());
					mainPane.getChildren().addAll(ImagePlaces[i].getLabel());
				}
		}
		for (int i = 0; i < ImagePlacesOld.length; i++) {

			if (ImagePlacesOld[i] != null)
				if (ImagePlacesOld[i].getRplace() instanceof URoutePlace) {
					if (!ImagePlacesOld[i].getRplace().getType().equals("DELETE")) {

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
				Place p = new Place("" + Globals.route.getId(), OPlacelist[i].getCityName(),
						OPlacelist[i].getPlaceName(), OPlacelist[i].getDescription(), OPlacelist[i].getClassification(),
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

							// TF_Accessibility
							// .setText("" +
							// ImagePlaces[Integer.parseInt(ID)].getPlace().getAccessibility());
							// TF_Description.setText(ImagePlaces[Integer.parseInt(ID)].getPlace().getDescription());
							// TF_Classification.setText(ImagePlaces[Integer.parseInt(ID)].getPlace().getClassification());

						}
					} else if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
						CurImagePlace = new ImagePlace();
						CurImagePlace.setImageview((ImageView) event.getSource());
						String ID = CurImagePlace.getImageview().getId();
						ID = ID.replace("imv", "");
						CurImagePlace.setId(Integer.parseInt(ID));
						CurImagePlace.setLabel(ImagePlaces[Integer.parseInt(ID)].getLabel());
						CurImagePlace.setPlace(ImagePlaces[Integer.parseInt(ID)].getPlace());
						CurImagePlace.setRplace(ImagePlaces[Integer.parseInt(ID)].getRplace());
						// TF_Accessibility.setText("" +
						// ImagePlaces[Integer.parseInt(ID)].getPlace().getAccessibility());
						// TF_Description.setText(ImagePlaces[Integer.parseInt(ID)].getPlace().getDescription());
						// TF_Classification.setText(ImagePlaces[Integer.parseInt(ID)].getPlace().getClassification());

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
		// if (!NewLocation.getText().equals("") && Counter < 10) {
		if (ThereIsNoLocation(comboboxLoc.getValue()) && Counter < 10) {
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
			// label.setText(NewLocation.getText());
			label.setText(comboboxLoc.getValue());
			label.setFont(new Font("Quicksand", 20));
			Image im = new Image("File:newloc.png");
			ImageView newLoc = new ImageView(im);
			newLoc.relocate(RouteImage.getLayoutX(), RouteImage.getLayoutY());
			label.relocate(RouteImage.getLayoutX(), RouteImage.getLayoutY());

			newLoc.setFitHeight(im.getHeight() * aspect);
			newLoc.setFitWidth(im.getWidth() * aspect);
			newLoc.setId("imv" + Counter);
			labels[Counter] = label;
			locations[Counter] = newLoc;
			Places[Counter] = new UPlace("" + Globals.route.getId(), Globals.route.getCity(), label.getText(), "", "",
					1, (int) newLoc.getX(), (int) newLoc.getY(), "NEW", -1);
			RPlaces[Counter] = new URoutePlace(Globals.route.getId(), -1, label.getText(), 10, (int) newLoc.getX(),
					(int) newLoc.getY(), -1, "NEW");

			ImagePlaces[Counter] = new ImagePlace(Counter, locations[Counter], labels[Counter], RPlaces[Counter],
					(int) RouteImage.getLayoutX(), (int) RouteImage.getLayoutY(), true);

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
							CurImagePlace.setRplace(RPlaces[CurImagePlace.getId()]);

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
							CurImagePlace.setRplace(RPlaces[CurImagePlace.getId()]);
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
		Socket socket = new Socket(Globals.IpAddress, 5555);
		String[] array = new String[3];
		ObjectOutputStream objectOutput;

		array[0] = "ConfirmRUpdate";
		array[1] = "" + Globals.route.getId();
		array[2] = "" + Globals.route.getCity();
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

	void InitializeComboBoxLocations() throws Exception, IOException {

		@SuppressWarnings("resource")
		Socket socket = new Socket(Globals.IpAddress, 5555);
		String[] array = new String[2];
		Object[] array2 = new Object[3];
		array[0] = "getPlacesForRoutes";
		// get[1] = "" + ImagePlaces[i].getPlace().getCityName();
		array[1] = "" + Globals.route.getCity();

		ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
		objectOutput.writeObject(array);

		Object data;
		ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());
		data = objectInput.readObject();
		Place[] list = (Place[]) (((Object[]) data)[1]);
		for (int i = 0; i < list.length; i++) {
			comboboxLoc.getItems().addAll(list[i].getPlaceName());
		}

	}

	@FXML
	void initialize() throws IOException, Exception {

		InitializeComboBoxLocations();
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
		if (!Globals.ThereIsRouteUpdate)
			ConfirmUpdate.setVisible(false);

		Image LinkedImage = new Image(Globals.route.getLink());
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
		// DrawOldPlaces();
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
		WritableImage writableImage = new WritableImage((int) RouteImage.getFitWidth(),
				(int) RouteImage.getFitHeight());
		SnapshotParameters spa = params;
		spa.setTransform(Transform.scale(pixelScale, pixelScale));
		return node.snapshot(spa, writableImage);
	}

	public Place[] GetPListFromRoutePlace(RoutePlace[] list) {

		Place[] list2 = new Place[list.length];
		for (int i = 0; i < list.length; i++) {
			list2[i] = new Place("0", Globals.city.getCity(), list[i].getPlace(), "", "", 0, 0, list[i].getLocX(),
					list[i].getLocY(), "UPDATE", 0);
		}
		return list2;
	}

	public UPlace[] GetUPListFromURoutePlace(URoutePlace[] list) {

		UPlace[] list2 = new UPlace[list.length];
		for (int i = 0; i < list.length; i++) {
			list2[i] = new UPlace("0", Globals.city.getCity(), list[i].getPlace(), "", "", 0, 0, list[i].getLocX(),
					list[i].getLocY(), list[i].getType(), 0, 0);
		}
		return list2;
	}

	public Boolean ThereIsNoLocation(String Loc) {
		for (int i = 0; i < ImagePlaces.length; i++)
			if (ImagePlaces[i] != null)
				if (ImagePlaces[i].getRplace().getPlace().toLowerCase().equals(Loc.toLowerCase()))
					return false;

		return true;
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