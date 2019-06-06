package application;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Date;

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
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MessagesController {

	 @FXML
	 private TableView<Message> messageTable;
	 
	    @FXML
	    private TableColumn<Message, String> MessageDescription;

	    @FXML
	    private TableColumn<Message, String> Typemessage;
	    @FXML
	    private TableColumn<Message, Date> Datesend;
	    
	    private ObservableList<Message> data = null;

	@FXML
	private Button Back;

	@FXML
	void backFunc(ActionEvent event) throws IOException {

		Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		URL url = getClass().getResource(Globals.backLink);
		AnchorPane pane = FXMLLoader.load(url);

		Scene scene = new Scene(pane);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	@SuppressWarnings("unchecked")
	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() throws IOException, Exception {
		
buildData();
    	
    	messageTable.getColumns().clear();
    	messageTable.setEditable(true);
    
    	
    	
    	
    	MessageDescription.setStyle( "-fx-alignment: CENTER;");
    	MessageDescription.setMinWidth(150);
    	MessageDescription.setCellValueFactory( new PropertyValueFactory<Message, String>("message"));
    	
    	MessageDescription.setCellFactory(tc -> {
            TableCell<Message, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind( MessageDescription.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell ;
        });
      
    	 Typemessage.setStyle( "-fx-alignment: CENTER;");
    	 Typemessage.setMinWidth(100);
    	 Typemessage.setCellValueFactory(
                new PropertyValueFactory<Message, String>("typemessage"));
    	 Datesend.setStyle( "-fx-alignment: CENTER;");
    	 Datesend.setMinWidth(100);
    	 Datesend.setCellValueFactory(
                new PropertyValueFactory<Message, Date>("Datesend"));
        
        FilteredList<Message> flCity = new FilteredList<Message>(data, p -> true);//Pass the data to a filtered list
        messageTable.setItems(flCity);//Set the table's items using the filtered list
        messageTable.getColumns().addAll(MessageDescription, Typemessage,Datesend);
        	
	}
	public void buildData() throws UnknownHostException, IOException{
		 @SuppressWarnings("resource")
		  Socket socket = new Socket("localhost",5555);
		 data = FXCollections.observableArrayList();
		 String[] array = new String[2];
			array[0] = "getMessages";
			array[1] = Globals.user.getUserName();
			 try {
			      ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
			      objectOutput.writeObject(array); 
			      try {
			          ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());
			          try {
			              Object[] object = (Object[]) objectInput.readObject();
			              for(int i=1 ; i <= (int) object[0] ; i++) {
			           	  data.add((Message) object[i]);
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
