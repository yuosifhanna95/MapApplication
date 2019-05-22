package application;

import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;




public class CatalogController {

    @FXML
    private ComboBox<String> comboBox;
    @FXML
    private TextField searchText = new TextField();	
    @FXML
    private TableView<City> searchTable ;
    @FXML
    private TableColumn<City, String>  CityCol;

    @FXML
    private TableColumn<City, String>  DescriptionCol;
    
    @FXML
    private TableColumn<City, String>  mapCol;
    
    @FXML
    private TableColumn<City, String>  placeCol;
    
    @FXML
    private TableColumn<City, String>  pathCol;
    
    private ObservableList<City> data = null;
       
    @FXML
    private Button back;
    
    
    
    @FXML
    void backFunc(ActionEvent event) throws IOException {
    	Stage primaryStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
		URL url = getClass().getResource("MainPage.fxml");
		AnchorPane pane =FXMLLoader.load(url);

		Scene scene = new Scene(pane);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
    }
    
    @SuppressWarnings("unchecked")
	@FXML
    public void initialize() throws UnknownHostException, IOException {
    	buildData();
    	comboBox.getItems().addAll("City", "Place", "Description");
    	searchText.setPromptText("Write here");
    	
    	
    	searchTable.getColumns().clear();
    	searchTable.setEditable(true);
    	
    	
    	CityCol.setStyle( "-fx-alignment: CENTER;");
    	CityCol.setMinWidth(100);
        CityCol.setCellValueFactory( new PropertyValueFactory<City, String>("city"));

        DescriptionCol.setCellFactory(tc -> {
            TableCell<City, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(DescriptionCol.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell ;
        });
        DescriptionCol.setStyle( "-fx-alignment: CENTER;");
        DescriptionCol.setMinWidth(150);
        DescriptionCol.setCellValueFactory(
                new PropertyValueFactory<City, String>("Description"));

        mapCol.setStyle( "-fx-alignment: CENTER;");
        mapCol.setMinWidth(50);
        mapCol.setCellValueFactory(
              new PropertyValueFactory<City, String>("numOfMaps"));
        
        placeCol.setStyle( "-fx-alignment: CENTER;");
        placeCol.setMinWidth(50);
        placeCol.setCellValueFactory(
                new PropertyValueFactory<City, String>("numOfPlaces"));
        
        pathCol.setStyle( "-fx-alignment: CENTER;");
        pathCol.setMinWidth(50);
        pathCol.setCellValueFactory(
                new PropertyValueFactory<City, String>("numOfPaths"));
        
        FilteredList<City> flCity = new FilteredList<City>(data, p -> true);//Pass the data to a filtered list
        searchTable.setItems(flCity);//Set the table's items using the filtered list
        searchTable.getColumns().addAll(CityCol, DescriptionCol, mapCol, placeCol, pathCol);
        
    	searchText.setOnKeyReleased(new EventHandler<KeyEvent>() {
          public void handle(KeyEvent ke) {
        	  switch (comboBox.getValue()) {
              case "City":
                  flCity.setPredicate(p -> p.getCity().toLowerCase().contains(searchText.getText().toLowerCase().trim()));
                  break;
              case "Place":
                  //flCity.setPredicate(p -> p.getPlace().toLowerCase().contains(searchText.getText().toLowerCase().trim()));
                  break;
              case "Description":
                  flCity.setPredicate(p -> p.getDescription().toLowerCase().contains(searchText.getText().toLowerCase().trim()));
                  break;
          }
          }
        });
    	
    }
    
    
	public void buildData() throws UnknownHostException, IOException {
       @SuppressWarnings("resource")
       Socket socket = new Socket("localhost",5555);
       data = FXCollections.observableArrayList();
       
       String[] get = new String[1];
       get[0] = "getCatalog";
       try {
           ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
           objectOutput.writeObject(get); 
           try {
               ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());
               try {
                   Object[] object = (Object[]) objectInput.readObject();
                   for(int i=1 ; i <= (int) object[0] ; i++) {
                	  data.add((City) object[i]);
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
    
}


