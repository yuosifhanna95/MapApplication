package application;



import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;


public class DefPageController {

	@FXML
	private Button btn_message;

	@FXML
	private Button myMaps;

	@FXML
	private Button Back;

	@FXML
	private Button OneTimePurchase;
	
	@FXML
    private Button FixedTimePurchase;

	@FXML
	private ComboBox<String> comboBox;

	@FXML
	private TextField searchText = new TextField();

	@FXML
	private TableView<City> searchTable;

	@FXML
	private TableColumn<City, String> CityCol;

	@FXML
	private TableColumn<City, String> DescriptionCol;

	@FXML
	private TableColumn<City, String> mapCol;

	@FXML
	private TableColumn<City, String> placeCol;

	@FXML
	private TableColumn<City, String> pathCol;
	
	@FXML
    private TableView<Place> searchTable1 ;
    
    @FXML
    private TableColumn<Place, String>  PlaceCol1;
    
    @FXML
    private TableColumn<Place, String>  CityCol1;

    @FXML
    private TableColumn<Place, String>  DescriptionCol1;
    
    @FXML
    private TableColumn<Place, String>  mapCol1;

    private ObservableList<City> dataCity = FXCollections.observableArrayList();

    private ObservableList<Place> dataPlace = FXCollections.observableArrayList();
    
    private static ObservableList<Map> dataMaps = FXCollections.observableArrayList();
    
    private static ObservableList<Route> dataRoutes = FXCollections.observableArrayList();
    
    FilteredList<City> flCity = null;
    
    FilteredList<Place> flPlace = null;
    
    @FXML
    private Button searchCity;

    @FXML
    private Button searchPlace;

	@FXML
	private Button btn_AddLoc;
	
	private static DirectoryChooser directoryChooser;

	static File dir;
	
	static TextField area;
	
	private DatePicker DatePicker;
	
	private String costVal;
	
	private int period;
	
    
	
	@FXML
    void searchCityBtn(ActionEvent event) {
		searchPlace.getStyleClass().remove("addBobOk");
		searchCity.getStyleClass().removeAll("addBobOk, focus"); 
		searchCity.getStyleClass().add("addBobOk");
	    
    
		searchTable1.setVisible(false);
    	searchTable1.setDisable(true);
    	
    	searchTable.setVisible(true);
    	searchTable.setDisable(false);
    	
    	searchText.setOnKeyReleased(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent ke) {
          	  switch (comboBox.getValue()) {
                case "City":
                    flCity.setPredicate(p -> p.getCity().toLowerCase().contains(searchText.getText().toLowerCase().trim()));
                    break;
                case "Place":
                    flCity.setPredicate(p -> p.getPlaces().toLowerCase().contains(searchText.getText().toLowerCase().trim()));
                    break;
                case "Description":
                    flCity.setPredicate(p -> p.getDescription().toLowerCase().contains(searchText.getText().toLowerCase().trim()));
                    break;
            } 	  
            }
          });
      
    }

	@FXML
    void searchPlaceBtn(ActionEvent event) throws UnknownHostException, IOException {
		searchCity.getStyleClass().remove("addBobOk");
		searchPlace.getStyleClass().removeAll("addBobOk, focus"); 
    	searchPlace.getStyleClass().add("addBobOk");
    	
    	searchTable.setVisible(false);
    	searchTable.setDisable(true);
    	
    	searchTable1.setVisible(true);
    	searchTable1.setDisable(false);
    	
        searchText.setOnKeyReleased(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent ke) {
          	  switch (comboBox.getValue()) {
                case "City":
                	flPlace.setPredicate(p -> p.getCityName().toLowerCase().contains(searchText.getText().toLowerCase().trim()));
                    break;
                case "Place":
                	flPlace.setPredicate(p -> p.getPlaceName().toLowerCase().contains(searchText.getText().toLowerCase().trim()));
                    break;
                case "Description":
                	flPlace.setPredicate(p -> p.getDescription().toLowerCase().contains(searchText.getText().toLowerCase().trim()));
                    break;
            } 	  
            }
          });
    	

    }

	@FXML
	void showMymaps(ActionEvent event) throws IOException {

		Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		URL url = getClass().getResource("MyMapsScene.fxml");
		AnchorPane pane = FXMLLoader.load(url);
		Globals.backLink = "DefaultPage.fxml";
		Scene scene = new Scene(pane);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	@FXML
	void OnePurchase(ActionEvent event) {
		 final Stage onePurchaseWindow = new Stage();
		 onePurchaseWindow.initModality(Modality.APPLICATION_MODAL);
		 
		 Text text = new Text(Globals.city.getCity() + "'s package is " + Globals.city.getOneTimeCost() + "$");

		 area = new TextField("No file selected");
		 area.setDisable(true);
		 
		 Button buttonSelect= new Button("Select File");
		 Button buttonSave= new Button("Save");
		 Button buttonCancel= new Button("Cancel");
		 buttonCancel.setOnAction(e -> onePurchaseWindow.close());
		 buttonSelect.setOnAction(e -> {
			selectFolder();
		 });
		 buttonSave.setOnAction(e -> {
				try {
					saveToFile();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				onePurchaseWindow.close();
		 });
		 
         HBox layout = new HBox(20);
         
         layout.getChildren().add(buttonCancel);
         layout.getChildren().add(buttonSave);
         layout.setAlignment(Pos.CENTER);

         HBox hbox = new HBox(area, buttonSelect);
         hbox.setAlignment(Pos.CENTER);
         
         VBox layoutV = new VBox(20);
         layoutV.getChildren().add(text);
         layoutV.getChildren().add(hbox);
         layoutV.getChildren().add(layout);
         layoutV.setAlignment(Pos.CENTER);
         Scene dialogScene = new Scene(layoutV, 300, 200);
         onePurchaseWindow.setScene(dialogScene);
         onePurchaseWindow.show();

	}
	
	
	@FXML
    void FixedPurchase(ActionEvent event) throws UnknownHostException, IOException {
//		addDataBasetoMember();
//		Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();			
//		URL url = getClass().getResource("payementforpurchase.fxml");
//		AnchorPane pane = FXMLLoader.load(url);
//				
//		Globals.backLink = "DefaultPage.fxml";
//				
//		Scene scene = new Scene(pane);
//		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());			
//		primaryStage.setScene(scene);			
//		primaryStage.show();
		
		if(checkIfCityExist().equals("Yes")) {
			JOptionPane.showMessageDialog(null, "You already purchased this city!");
		}
		else {
			final Stage FixedPurchaseWindow = new Stage();
			FixedPurchaseWindow.initModality(Modality.APPLICATION_MODAL);
			 
			Text text = new Text("The purchase costs " + Globals.city.getFixedCost());
			Button buttonSet = new Button("Calculate");
			Button buttonCancel = new Button("Cancel");
			Button buttonPurchase= new Button("Purchase"); 
			Text cost = new Text();
			
			buttonPurchase.setDisable(true);
			
			buttonCancel.setOnAction(e -> FixedPurchaseWindow.close());
			buttonSet.setOnAction(e -> {
				calculate();
				cost.setText("The period is " + period + " days and it costs " + costVal);
				buttonPurchase.setDisable(false);
			 });
			buttonPurchase.setOnAction(e -> {
				try {
					savePurchase();
					FixedPurchaseWindow.close();
					JOptionPane.showMessageDialog(null, "thanks for purchace,you will enjoy");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			 });
			
			DatePicker = new DatePicker();
			
			DatePicker checkInDatePicker = new DatePicker();
	       
	        checkInDatePicker.setValue(LocalDate.now());
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
	       
	     
	
	        GridPane gridPane = new GridPane();
		    gridPane.setHgap(10);
		    gridPane.setVgap(10);
		      
		    Label checkInlabel = new Label("Select last date for the purchase:");
		    gridPane.add(checkInlabel, 0, 0);
	
	        GridPane.setHalignment(checkInlabel, HPos.LEFT);
	        gridPane.add(DatePicker, 0, 1);
	        
	        HBox layout = new HBox(20);
	        layout.getChildren().add(buttonCancel);
	        layout.getChildren().add(buttonPurchase);
	        layout.setAlignment(Pos.CENTER);
	
	        HBox hbox2 = new HBox(20);
	        hbox2.getChildren().add(gridPane);
	        hbox2.setAlignment(Pos.CENTER);
	        
	        
	        VBox layoutV = new VBox(20);
	        layoutV.getChildren().add(text);
	        layoutV.getChildren().add(hbox2);
	        layoutV.getChildren().add(buttonSet);
	        layoutV.getChildren().add(cost);
	        //layoutV.getChildren().add(hbox);
	        layoutV.getChildren().add(layout);
	        layoutV.setAlignment(Pos.CENTER);
	        
	        Scene dialogScene = new Scene(layoutV, 400, 300);
	        FixedPurchaseWindow.setScene(dialogScene);
	        FixedPurchaseWindow.show();
		}
    
	}
	
	@SuppressWarnings("unchecked")
	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() throws IOException, Exception {
		Globals.backLink = "MainPage.fxml";
		
		searchCity.getStyleClass().removeAll("addBobOk, focus"); 
		searchCity.getStyleClass().add("addBobOk");

		buildData("city");
    	buildData("place");
    	
		comboBox.getItems().addAll("City", "Place", "Description");
		searchText.setPromptText("Write here");

		searchTable1.getColumns().clear();
    	searchTable1.setEditable(true);
    	
      	PlaceCol1.setStyle( "-fx-alignment: CENTER;");
      	PlaceCol1.setMinWidth(100);
      	PlaceCol1.setCellValueFactory( new PropertyValueFactory<Place, String>("PlaceName"));
    	
    	CityCol1.setStyle( "-fx-alignment: CENTER;");
    	CityCol1.setMinWidth(100);
        CityCol1.setCellValueFactory( new PropertyValueFactory<Place, String>("CityName"));

        DescriptionCol1.setCellFactory(tc -> {
            TableCell<Place, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(DescriptionCol.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell ;
        });
      
        DescriptionCol1.setStyle( "-fx-alignment: CENTER;");
        DescriptionCol1.setMinWidth(150);
        DescriptionCol1.setCellValueFactory(
                new PropertyValueFactory<Place, String>("Description"));

        mapCol1.setStyle( "-fx-alignment: CENTER;");
        mapCol1.setMinWidth(50);
        mapCol1.setCellValueFactory(
              new PropertyValueFactory<Place, String>("numOfmaps"));
        
        flPlace = new FilteredList<Place>(dataPlace, p -> true);//Pass the dataCity to a filtered list
        searchTable1.setItems(flPlace);//Set the table's items using the filtered list
        searchTable1.getColumns().addAll(PlaceCol1, CityCol1, DescriptionCol1, mapCol1 );
		
    	searchTable1.setVisible(false);
    	searchTable1.setDisable(true);
    	
		searchTable.getColumns().clear();
		searchTable.setEditable(true);

		searchTable.setRowFactory(tv -> {
			TableRow<City> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 1 && (!row.isEmpty())) {
					City cityRow = searchTable.getSelectionModel().getSelectedItem();
					Globals.city = (City) cityRow;
				}
			});
			return row;
		});

		CityCol.setStyle("-fx-alignment: CENTER;");
		CityCol.setMinWidth(100);
		CityCol.setCellValueFactory(new PropertyValueFactory<City, String>("city"));

		DescriptionCol.setCellFactory(tc -> {
			TableCell<City, String> cell = new TableCell<>();
			Text text = new Text();
			cell.setGraphic(text);
			cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
			text.wrappingWidthProperty().bind(DescriptionCol.widthProperty());
			text.textProperty().bind(cell.itemProperty());
			return cell;
		});
		DescriptionCol.setStyle("-fx-alignment: CENTER;");
		DescriptionCol.setMinWidth(150);
		DescriptionCol.setCellValueFactory(new PropertyValueFactory<City, String>("Description"));

		mapCol.setStyle("-fx-alignment: CENTER;");
		mapCol.setMinWidth(50);
		mapCol.setCellValueFactory(new PropertyValueFactory<City, String>("numOfMaps"));

		placeCol.setStyle("-fx-alignment: CENTER;");
		placeCol.setMinWidth(50);
		placeCol.setCellValueFactory(new PropertyValueFactory<City, String>("numOfPlaces"));

		pathCol.setStyle("-fx-alignment: CENTER;");
		pathCol.setMinWidth(50);
		pathCol.setCellValueFactory(new PropertyValueFactory<City, String>("numOfPaths"));

		flCity = new FilteredList<City>(dataCity, p -> true);// Pass the data to a filtered list
		searchTable.setItems(flCity);// Set the table's items using the filtered list
		searchTable.getColumns().addAll(CityCol, DescriptionCol, mapCol, placeCol, pathCol);

		searchText.setOnKeyReleased(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent ke) {
				switch (comboBox.getValue()) {
				case "City":
					flCity.setPredicate(
							p -> p.getCity().toLowerCase().contains(searchText.getText().toLowerCase().trim()));
					break;
				case "Place":
					 flCity.setPredicate(p ->p.getPlaces().toLowerCase().contains(searchText.getText().toLowerCase().trim()));
					break;
				case "Description":
					flCity.setPredicate(
							p -> p.getDescription().toLowerCase().contains(searchText.getText().toLowerCase().trim()));
					break;
				}
			}
		});
		
		comboBox.getSelectionModel().selectFirst();
	
	}

	public String checkIfCityExist() throws UnknownHostException, IOException {
		@SuppressWarnings("resource")
		Socket socket = new Socket("localhost", 5555);
		dataCity = FXCollections.observableArrayList();

		String[] set = new String[3];
		set[0] = "checkCityExist";
		set[1] = Globals.user.getUserName();
		set[2] = Globals.city.getCity();
		try {
			ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
			objectOutput.writeObject(set);
			ObjectInputStream  objectInput = new ObjectInputStream(socket.getInputStream());
			try {
	 				Object data=objectInput.readObject();
	 				
	 				if((data).equals("Yes")){ System.out.println("here");
	 					return "Yes";
	 				}
			} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			} 
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "No";
	}

	public void buildData(String type) throws UnknownHostException, IOException {
	       @SuppressWarnings("resource")
	       Socket socket = new Socket("localhost",5555);

	       String[] get = new String[1];
	       get[0] = "getCatalog";
	       if(type.equals("place")){
	    	   get[0] = "getPlaceCatalog"; 
	       }
	       try {
	           ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
	           objectOutput.writeObject(get); 
	           try {
	               ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());
	               try {
	                   Object[] object = (Object[]) objectInput.readObject();
	                   if(type.equals("city")) {
		                   for(int i = 1 ; i <= (int) object[0] ; i++) {
		                	  dataCity.add((City) object[i]);
		                   }
	                   }
	                   else {
	                	   for(int i = 1 ; i <= (int) object[0] ; i++) {	 
	 	                	  dataPlace.add((Place) object[i]);
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
	       } 
	       catch (IOException e) 
	       {
	           e.printStackTrace();
	       } 

	    }

	@FXML
	void backFunc(ActionEvent event) throws IOException {

		Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		URL url = getClass().getResource("MainPage.fxml");
		AnchorPane pane = FXMLLoader.load(url);
		Scene scene = new Scene(pane);
		scene.getStylesheets().add(getClass().getResource("styleMain.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@FXML
	void addLoc(ActionEvent event) throws IOException {

		Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		URL url = getClass().getResource("AddLocations.fxml");
		AnchorPane pane = FXMLLoader.load(url);
		Globals.backLink = "DefaultPage.fxml";
		Scene scene = new Scene(pane);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@FXML
	void messageFunc(ActionEvent event) throws IOException {

		Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		URL url = getClass().getResource("Messages.fxml");
		AnchorPane pane = FXMLLoader.load(url);
		Globals.backLink = "DefaultPage.fxml";
		Scene scene = new Scene(pane);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();

	}
	
	
	
	public static void getFromCity(String str) throws UnknownHostException, IOException { 
		  @SuppressWarnings("resource")
		  Socket socket = new Socket("localhost",5555);
	
		  
		  String[] get = new String[2];
		  get[0] = "getMyRoutes";
		  if(str.equals("Maps")) {
			  get[0] = "getMyMaps";
		  }
		  get[1] = Globals.city.getCity();
		  try {
		      ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
		      objectOutput.writeObject(get); 
		      try {
		          ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());
		          try {
		              Object[] object = (Object[]) objectInput.readObject();
		              if(str.equals("Maps")) {
		            	  for(int i=1 ; i <= (int) object[0] ; i++) {
				           	  dataMaps.add((Map) object[i]);
				          }
		              }
		              else {
		            	  for(int i=1 ; i <= (int) object[0] ; i++) {
		            		  dataRoutes.add((Route) object[i]);
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
		  } 
		  catch (IOException e) 
		  {
		      e.printStackTrace();
		  } 
		
		}
	
	public static void selectFolder() {
		directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle("Select Folder");
		directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        dir = directoryChooser.showDialog(null);
        if(dir != null)
        	area.setText(dir.getAbsolutePath());
	}
	
	public static void saveToFile() throws IOException {
        if (dir != null) {
        getFromCity("Maps");
        getFromCity("Routes");
       
        dataMaps.forEach((Map) -> { 
        	URL url;
			try {
				url = new URL(Map.getLinkCustomer());
				BufferedImage image23 = ImageIO.read(url);
				
    			try {
    				File file = new File(dir + "/" + Map.getCity() +  Map.getId() +"Map.png");
    				ImageIO.write(image23, "png",  file);
    			} catch (IOException ex) {
    				ex.printStackTrace();
    			}
	      
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        });
        
        dataRoutes.forEach((Route) -> { 
        	URL url;
			try {
				url = new URL(Route.getLink());
				BufferedImage image23 = ImageIO.read(url);
				
    			try {
    				File file = new File(dir + "/" + Route.getCity() +Route.getId() +"Route.png");
    				ImageIO.write(image23, "png",  file);
    			} catch (IOException ex) {
    				ex.printStackTrace();
    			}
	    		
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        });
 	}
	}
	
	private void calculate() {
    	LocalDate date = DatePicker.getValue();
    	if(date != null) {
    	Date endDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
		double cost = Globals.city.getFixedCost();    
		Date today = Calendar.getInstance().getTime();
		
        double diffInDays = (double)( (endDate.getTime() - today.getTime())/(1000 * 60 * 60 * 24) );
        diffInDays++;
		period = (int) diffInDays;

        double m = diffInDays/30;
		double months = Math.ceil(m);
        
        costVal = Double.toString(months * cost);
    	}
	}
	
	 void savePurchase() throws UnknownHostException, IOException {
		    LocalDate date = DatePicker.getValue();
	    	if(date != null) {
	    	
	    	Date enddate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
	        String user = Globals.user.getUserName();;
	        Date sdate = Calendar.getInstance().getTime();
	        double price = (int)Globals.city.getFixedCost();
	        String city=Globals.city.getCity();
	        FixedPurchase fp=new FixedPurchase(user,period,city,sdate,enddate,price);

	        
	        @SuppressWarnings("resource")
	    	Socket	 socket = new Socket("localhost",5555);
	        Object[] set = new Object[4];
	        set[0] = "dofixedpurchase";
	        set[1]= fp;
	       // set[2]=payinfo.getText();
	        //set[3]=infopay.getValue();
	        try {
	            ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
	            objectOutput.writeObject(set); 
	            ObjectInputStream  objectInput = new ObjectInputStream(socket.getInputStream());
	            try {
	 				Object data=objectInput.readObject();
	 				
//	 				if((data).equals("we need the payinfo to continue")){
//	 					JOptionPane.showMessageDialog(null, "we need the payinfo to continue");
//	 				}
	 				if((data).equals("thanks for purchace,you will enjoy")){
//	 					JOptionPane.showMessageDialog(null, "thanks for purchace,you will enjoy");
//	 					Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//	 					URL url = getClass().getResource("MyMapsScene.fxml");
//	 					AnchorPane pane = FXMLLoader.load(url);
//	 					Globals.backLink = "DefaultPage.fxml";
//	 					Scene scene = new Scene(pane);
//	 					scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
//	 					primaryStage.setScene(scene);
//	 					primaryStage.show();
	 				}
	            } catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
	        }
	        catch (IOException e) 
	        {
	            e.printStackTrace();
	        } 
	        }
	    	
	    }

}
