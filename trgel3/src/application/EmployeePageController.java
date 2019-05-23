package application;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class EmployeePageController  {

    @FXML
    private Button btn_message;
    
    @FXML
    private Button Back;


    @FXML
    private Button btn_AddLoc;
    
	    
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() throws IOException, Exception {
    	
		if(Globals.MODE<3)
		{
			btn_AddLoc.setVisible(false);
		}

    	
		/*
		 ObjectInputStream objectInput;
		 @SuppressWarnings("resource")
		 Object data;
	     String[] array = new String[2];
	     array[0]="getMessages";
	     array[1]=Globals.user.getUserName();
	     
	     Socket socket = new Socket("localhost",5555);
	     ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
	     out.writeObject(array);
	     
	     objectInput = new ObjectInputStream(socket.getInputStream());
	     data=objectInput.readObject();
	     System.out.println(data);
	     */

    }
    
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


    @FXML
    void editmaps(ActionEvent event) throws IOException {

      	Stage primaryStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
    	URL url = getClass().getResource("EditMaps.fxml");
			AnchorPane pane =FXMLLoader.load(url);

			Scene scene = new Scene(pane);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
    }
    
    
    
    @FXML
    void messageFunc(ActionEvent event) throws IOException {
    	
      	Stage primaryStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
    	URL url = getClass().getResource("Messages.fxml");
			AnchorPane pane =FXMLLoader.load(url);

			Scene scene = new Scene(pane);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
    	
    }

}

