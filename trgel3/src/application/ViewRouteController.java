package application;

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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ViewRouteController {
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
	    
	    @SuppressWarnings("unchecked")
		@FXML
	   	public void initialize() throws UnknownHostException, IOException {
		    routeImage = new ImageView(new Image(Globals.route.getLink()));
		    routeImage.relocate(162 , 50);
	    	mainPane.getChildren().add(routeImage);
	    	
	    	buildData();
	    	
	    	routeInfo.getColumns().clear();
	    	routeInfo.setEditable(true);
	    	
	    	placeCol.setStyle( "-fx-alignment: CENTER;");
	    	placeCol.setMinWidth(100);
	    	placeCol.setCellValueFactory( new PropertyValueFactory<RoutePlace, String>("place"));
	    
	      
	    	timeCol.setStyle( "-fx-alignment: CENTER;");
	    	timeCol.setMinWidth(150);
	    	timeCol.setCellValueFactory( new PropertyValueFactory<RoutePlace, String>("time"));
	        
	        FilteredList<RoutePlace> flCity = new FilteredList<RoutePlace>(data, p -> true);//Pass the data to a filtered list
	        routeInfo.setItems(flCity);//Set the table's items using the filtered list
	        routeInfo.getColumns().addAll(placeCol, timeCol);
	    }
		
	   @FXML
	   void backFunc(ActionEvent event) throws IOException {
	     	Stage primaryStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
	     	URL url = getClass().getResource(Globals.backLink);
	     	Globals.backLink = "MyMapsScene.fxml";
			AnchorPane pane =FXMLLoader.load(url);

			Scene scene = new Scene(pane);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
	   }
	   
	   public void buildData() throws UnknownHostException, IOException { 
			  @SuppressWarnings("resource")
			  Socket socket = new Socket("localhost",5555);
			  data = FXCollections.observableArrayList();
			  
			  String[] get = new String[2];
			  get[0] = "getRoutePlaces";
			  get[1] =  Integer.toString(Globals.route.getId());
			  try {
			      ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
			      objectOutput.writeObject(get); 
			      try {
			          ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());
			          try {
			              Object[] object = (Object[]) objectInput.readObject();
			              for(int i=1 ; i <= (int) object[0] ; i++) {
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
			  } 
			  catch (IOException e) 
			  {
			      e.printStackTrace();
			  } 
			
			}
}
