package application;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import java.time.Instant;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.time.temporal.ChronoUnit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;

import java.time.Month;
import java.time.ZoneId;

public class MyMapsController {

    @FXML
    private TableView<FixedPurchase> mapTable;

    @FXML
    private TableColumn<FixedPurchase, String> cityCol;


    @FXML
    private Button Back;

    @FXML
    private ImageView map;
    
    @FXML
    private Button Renew;

    @FXML
    private Button Maps;

    @FXML
    private Button Paths;
    
    private DatePicker DatePicker;
    
    private String renewVal;
    
    private ObservableList<FixedPurchase> data = null;

    @FXML
    void RenewBtn(ActionEvent event) {
    	final Stage RenewPurchaseWindow = new Stage();
    	RenewPurchaseWindow.initModality(Modality.APPLICATION_MODAL);
		 
		Text text = new Text("The purchase is until " + Globals.FixedPurchase.getEndDate());
		Button buttonSet = new Button("Calculate");
		Button buttonCancel = new Button("Cancel");
		Button buttonSave = new Button("Save"); 
		Text cost = new Text();
		
		buttonCancel.setOnAction(e -> RenewPurchaseWindow.close());
		buttonSet.setOnAction(e -> {
			calculate();
			cost.setText(renewVal);
		 });
		buttonSave.setOnAction(e -> {
			try {
				saveNewPurchaseToDB();
				RenewPurchaseWindow.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		 });
		
		
		
		DatePicker = new DatePicker();
		DatePicker checkInDatePicker = new DatePicker();
		Date dateToConvert = new Date();
		dateToConvert = Globals.FixedPurchase.getEndDate();
		LocalDate ld = new java.sql.Date( dateToConvert.getTime() ) .toLocalDate();
        checkInDatePicker.setValue(ld);
        final Callback<DatePicker, DateCell> dayCellFactory = 
            new Callback<DatePicker, DateCell>() {
                @Override
                public DateCell call(final DatePicker datePicker) {
                    return new DateCell() {
                        @Override
                        public void updateItem(LocalDate item, boolean empty) {
                            super.updateItem(item, empty);
                            if (item.isBefore(
                                    checkInDatePicker.getValue().plusDays(1))
                                ) {
                                    setDisable(true);
                                    setStyle("-fx-background-color: #ffc0cb;");
                            }
                            if (item.isAfter(
                                    checkInDatePicker.getValue().plusMonths(6))
                                ) {
                                    setDisable(true);
                                    setStyle("-fx-background-color: #ffc0cb;");
                            }
                            long p = ChronoUnit.DAYS.between(
                                    checkInDatePicker.getValue(), item
                            );
                            setTooltip(new Tooltip(
                                "You're about to stay for " + p + " days")
                            );
                    }
                };
            }
        };
        DatePicker.setDayCellFactory(dayCellFactory);
        DatePicker.setValue(checkInDatePicker.getValue().plusDays(1));
        
        GridPane gridPane = new GridPane();
	    gridPane.setHgap(10);
	    gridPane.setVgap(10);
	      
	    Label checkInlabel = new Label("Set Date:");
	    gridPane.add(checkInlabel, 0, 0);

        GridPane.setHalignment(checkInlabel, HPos.LEFT);
        gridPane.add(DatePicker, 0, 1);
        
        HBox layout = new HBox(20);
        layout.getChildren().add(buttonCancel);
        layout.getChildren().add(buttonSave);
        layout.setAlignment(Pos.CENTER);

        HBox hbox = new HBox(20);
        hbox.getChildren().add(buttonSet);
        hbox.getChildren().add(cost);
        hbox.setAlignment(Pos.CENTER);
        
        HBox hbox2 = new HBox(20);
        hbox2.getChildren().add(gridPane);
        hbox2.setAlignment(Pos.CENTER);
        
        
        VBox layoutV = new VBox(20);
        layoutV.getChildren().add(text);
        layoutV.getChildren().add(hbox2);
        layoutV.getChildren().add(hbox);
        layoutV.getChildren().add(layout);
        layoutV.setAlignment(Pos.CENTER);
        
        Scene dialogScene = new Scene(layoutV, 300, 200);
        RenewPurchaseWindow.setScene(dialogScene);
        RenewPurchaseWindow.show();
    }


	@FXML
    void ShowMapsBtn(ActionEvent event) throws IOException {
		Stage primaryStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        URL url = getClass().getResource("ShowMapsScene.fxml");
    	Globals.backLink = "MyMapsScene.fxml";		 	    	
		AnchorPane pane;
		pane = FXMLLoader.load(url);
		
		Scene scene = new Scene(pane);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
    }

    @FXML
    void ShowPathsBtn(ActionEvent event) throws IOException {
    	Stage primaryStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        URL url = getClass().getResource("ShowRoutesScene.fxml");
    	Globals.backLink = "MyMapsScene.fxml";		 	    	
		AnchorPane pane;
		pane = FXMLLoader.load(url);
		
		Scene scene = new Scene(pane);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
    }
    
    @FXML
    void backFunc(ActionEvent event) throws IOException {
    	Stage primaryStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
    	URL url = getClass().getResource(Globals.backLink);
		AnchorPane pane =FXMLLoader.load(url);

		Scene scene = new Scene(pane);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
    }
    
    @SuppressWarnings("unchecked")
	@FXML
    public void initialize() throws UnknownHostException, IOException {
    	if(Globals.MODE<3)
    		Globals.backLink="DefaultPage.fxml";
    	else
    		Globals.backLink="EmployeePage.fxml";
    	
    	Maps.setDisable(true);
    	Paths.setDisable(true);
    	Renew.setDisable(true);
    		
    	buildData();
    	
    	mapTable.getColumns().clear();
    	mapTable.setEditable(true);
    
    	
    	mapTable.setRowFactory( tv -> {
    	    TableRow<FixedPurchase> row = new TableRow<>();
    	    row.setOnMouseClicked(event -> {
   	        if (event.getClickCount() == 1 && (! row.isEmpty()) ) {
	        	FixedPurchase FixedPurchaseRow = mapTable.getSelectionModel().getSelectedItem();
 	            Globals.FixedPurchase = FixedPurchaseRow;
 	            Maps.setDisable(false);
 	           	Paths.setDisable(false);
 	      	    Renew.setDisable(false);
   	        	}
    	    });
    	    return row ;
     	});
    	
    	cityCol.setStyle( "-fx-alignment: CENTER;");
    	cityCol.setMinWidth(100);
    	cityCol.setCellValueFactory( new PropertyValueFactory<FixedPurchase, String>("city"));

        
        FilteredList<FixedPurchase> flCity = new FilteredList<FixedPurchase>(data, p -> true);//Pass the data to a filtered list
        mapTable.setItems(flCity);//Set the table's items using the filtered list
        mapTable.getColumns().addAll(cityCol);
        	
    }
    
    public void buildData() throws UnknownHostException, IOException { 
        @SuppressWarnings("resource")
        Socket socket = new Socket("localhost",5555);
        data = FXCollections.observableArrayList();
        
        String[] get = new String[2];
        get[0] = "getFixedPurchase";
        get[1] = Globals.user.getUserName();
        try {
            ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
            objectOutput.writeObject(get); 
            try {
                ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());
                try {
                    Object[] object = (Object[]) objectInput.readObject();
                    for(int i=1 ; i <= (int) object[0] ; i++) {
                 	  data.add((FixedPurchase) object[i]);
                    }
               
                } catch (ClassNotFoundException e) {
                    System.out.println("The title list has not come from the server");
                    e.printStackTrace();
                }
            } catch (IOException e) {
                System.out.println("The socket for reading the object has problem");
                e.printStackTrace();
            }   
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        } 

     }
    
    private void saveNewPurchaseToDB() throws UnknownHostException, IOException {
    	LocalDate date = DatePicker.getValue();
    	if(date != null) {
    	Date newDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
    	
    	Socket socket = new Socket("localhost",5555);
   
    	Object[] set = new Object[4];
        set[0] = "addNewDate";
        set[1] = newDate;	
        set[2] = Globals.FixedPurchase.getUser();
        set[3] = Globals.FixedPurchase.getCity();
		try {
		    ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
		    objectOutput.writeObject(set); 
		  } 
		  catch (IOException e) 
		  {
		      e.printStackTrace();
		  } 
		
    	}
	}

	private void calculate() {
    	LocalDate date = DatePicker.getValue();
    	if(date != null) {
    	Date newDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
		double cost = Globals.FixedPurchase.getPrice();
        Date oldDate = Globals.FixedPurchase.getEndDate();
        
        double diffInDays = (double)( (newDate.getTime() - oldDate.getTime())/(1000 * 60 * 60 * 24) );
        diffInDays++;
   
		double m = diffInDays/30;
		double months = Math.ceil(m);
        
        renewVal = Double.toString(months * cost);
    	}
	}


}

