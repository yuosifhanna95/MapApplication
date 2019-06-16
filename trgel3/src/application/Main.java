package application;

import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {

			final Stage onePurchaseWindow = new Stage();
			onePurchaseWindow.initModality(Modality.APPLICATION_MODAL);
			Button buttonConnect = new Button("Connect");
			GridPane gridPanePayment = new GridPane();
			gridPanePayment.setHgap(10);
			gridPanePayment.setVgap(10);

			Text textPayment = new Text("Enter IP Address");
			textPayment.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
			gridPanePayment.add(textPayment, 0, 0);

			TextField Address = new TextField();
			HBox h = new HBox(20);

			h.getChildren().add(Address);
			h.setAlignment(Pos.CENTER);
			gridPanePayment.add(h, 0, 2);
			Address.setVisible(true);

			buttonConnect.setOnAction(e -> {

				Address.setVisible(true);

				Globals.MODE = 0;
				if (!Address.equals("") && !Address.equals(null))
					Globals.IpAddress = Address.getText();
				URL url = getClass().getResource("MainPage.fxml");
				AnchorPane pane;
				try {
					pane = FXMLLoader.load(url);
					Scene scene = new Scene(pane);
					scene.getStylesheets().add(getClass().getResource("styleMain.css").toExternalForm());
					primaryStage.setScene(scene);
					primaryStage.show();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			});

			HBox layout = new HBox(20);
			layout.getChildren().add(buttonConnect);
			layout.setAlignment(Pos.CENTER);

			HBox hbox1 = new HBox(20);
			hbox1.getChildren().add(gridPanePayment);
			hbox1.setAlignment(Pos.CENTER);

			VBox layoutV = new VBox(20);
			layoutV.getChildren().add(hbox1);
			layoutV.getChildren().add(layout);
			layoutV.setAlignment(Pos.CENTER);
			Scene dialogScene = new Scene(layoutV, 500, 300);
			onePurchaseWindow.setScene(dialogScene);
			onePurchaseWindow.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Globals.IpAddress = "127.0.0.1";
		Globals.city = null;
		Globals.cityName = "";
		Globals.FixedPurchase = null;
		Globals.Fplaces = null;
		Globals.Fplaces1 = null;
		Globals.Fplaces2 = null;
		Globals.place = null;
		Globals.places = null;
		Globals.user = null;
		Globals.map = null;
		Globals.MODE = -1;
		Globals.route = null;
		Globals.userView = null;
		Globals.ThereIsRouteUpdate = false;
		Globals.ThereIsMapUpdate = false;
		Globals.ThereIsCityUpdate = false;
		Globals.SearchOp = "";
		Globals.Searchfilter = "";

		launch(args);
	}
}
