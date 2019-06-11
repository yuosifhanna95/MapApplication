package application;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import java.time.Instant;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JOptionPane;

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
    
    private int oldPeriod;
    
    private int newPeriod;
    
    private String renewVal;
    
    private ObservableList<FixedPurchase> data = null;

    @FXML
    void RenewBtn(ActionEvent event) {
    	final Stage RenewPurchaseWindow = new Stage();
    	RenewPurchaseWindow.initModality(Modality.APPLICATION_MODAL);
		 
		Text text = new Text("The purchase is until " + Globals.FixedPurchase.getEndDate());
		Button buttonSet = new Button("Show Price");
		Button buttonCancel = new Button("Cancel");
		Button buttonSave = new Button("Save"); 
		Text cost = new Text();
		oldPeriod = Globals.FixedPurchase.getPeriod();
	
		buttonCancel.setOnAction(e -> RenewPurchaseWindow.close());
		buttonSet.setOnAction(e -> {
			calculate();
			String str = "The price is " + renewVal;
			if(oldPeriod != newPeriod) {
				try {
					str += "! You will get 10% discount if you select: " + calcNewDate(oldPeriod);
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			cost.setText(str);
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
        
        gridPane.add(buttonSet, 2, 1);
        
        GridPane gridPanePayment = new GridPane();
        gridPanePayment.setHgap(10);
        gridPanePayment.setVgap(10);
        
        Text textPayment =  new Text("Payment method");
        textPayment.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        gridPanePayment.add(textPayment, 0, 0);
        
        Button buttonChange= new Button("Change"); 
        gridPanePayment.add(buttonChange, 1, 0);
        
        ToggleGroup group = new ToggleGroup();
        RadioButton rb1 = new RadioButton();
        rb1.setText("   VISA ("+ Globals.user.getPayment() +")");
        gridPanePayment.add(rb1, 0, 1);
        rb1.setToggleGroup(group);
        rb1.setSelected(true);
        
        RadioButton rb2 = new RadioButton();	        
		TextField newPayment = new TextField();
		HBox h = new HBox(20);
        h.getChildren().add(rb2);
        h.getChildren().add(newPayment);
        h.setAlignment(Pos.CENTER);
        gridPanePayment.add(h, 0, 2);
		newPayment.setVisible(false);
		rb2.setToggleGroup(group);
		rb2.setVisible(false);
		
		buttonChange.setOnAction(e -> {
			newPayment.setVisible(true);
			rb2.setVisible(true);
		});
		
		buttonSave.setOnAction(e -> {
			if(rb2.isSelected() && newPayment.getText().contentEquals("")) {
				JOptionPane.showMessageDialog(null, "Please insert payment method!");
			}
			else {
				try {
					saveNewPurchaseToDB();
					RenewPurchaseWindow.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		 }});
        
        HBox layout = new HBox(20);
        layout.getChildren().add(buttonCancel);
        layout.getChildren().add(buttonSave);
        layout.setAlignment(Pos.CENTER);

        HBox hbox = new HBox(20);
        hbox.getChildren().add(cost);
        hbox.setAlignment(Pos.CENTER);
        
        HBox hbox2 = new HBox(20);
        hbox2.getChildren().add(gridPane);
        hbox2.setAlignment(Pos.CENTER);
        
        HBox hbox3 = new HBox(20);
        hbox3.getChildren().add(gridPanePayment);
        hbox3.setAlignment(Pos.CENTER);
        
        VBox layoutV = new VBox(20);
        layoutV.getChildren().add(text);
        layoutV.getChildren().add(hbox2);
        layoutV.getChildren().add(hbox);
        layoutV.getChildren().add(hbox3);
        layoutV.getChildren().add(layout);
        layoutV.setAlignment(Pos.CENTER);
        
        Scene dialogScene = new Scene(layoutV, 500, 300);
        RenewPurchaseWindow.setScene(dialogScene);
        RenewPurchaseWindow.show();
    }


	private String calcNewDate(int oldPeriod) throws ParseException {
		Date oldDate = Globals.FixedPurchase.getEndDate();
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		String oldDateAsString = df.format(oldDate);
		c.setTime(df.parse(oldDateAsString));
		c.add(Calendar.DATE, oldPeriod);
		oldDateAsString = df.format(c.getTime());
		
		return oldDateAsString;
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
		primaryStage.setOnCloseRequest(e-> {
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
    void ShowPathsBtn(ActionEvent event) throws IOException {
    	Stage primaryStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        URL url = getClass().getResource("ShowRoutesScene.fxml");
    	Globals.backLink = "MyMapsScene.fxml";		 	    	
		AnchorPane pane;
		pane = FXMLLoader.load(url);
		
		Scene scene = new Scene(pane);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.setOnCloseRequest(e-> {
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
    	Stage primaryStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
    	URL url = getClass().getResource(Globals.backLink);
		AnchorPane pane =FXMLLoader.load(url);

		Scene scene = new Scene(pane);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.setOnCloseRequest(e-> {
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
        Socket socket = new Socket(Globals.IpAddress,5555);
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
    	
    	Socket socket = new Socket(Globals.IpAddress,5555);
   
    	Object[] set = new Object[6];
        set[0] = "addNewDate";
        set[1] = newDate;	
        set[2] = Globals.FixedPurchase.getEndDate();
        set[3] = newPeriod;
        set[4] = Globals.FixedPurchase.getUser();
        set[5] = Globals.FixedPurchase.getCity();
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
        
        newPeriod = (int) diffInDays;
   
		double m = diffInDays/30;
		double months = Math.ceil(m);
		Double price = months * cost;
		if(newPeriod == oldPeriod) {
			price *= 0.9;
		}
        
        renewVal = Double.toString(price);
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

