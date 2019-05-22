package application;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MessagesController {


    @FXML
    private Label label_message;
    
    @FXML
    private Button back;

    @FXML
    void backFunc(ActionEvent event) throws IOException {
    	
      	Stage primaryStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
    	URL url = getClass().getResource("DefaultPage.fxml");
			AnchorPane pane =FXMLLoader.load(url);

			Scene scene = new Scene(pane);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();

    }
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() throws IOException, Exception {
    	
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
         label_message.setText((String)data);
   
    }
}
