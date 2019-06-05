package application;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MyProfileController {

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private TextField phone;

    @FXML
    private TextField email;

    @FXML
    private TextField payment;

    @FXML
    private TextField user;
    
    @FXML
    private PasswordField password ;

    @FXML
    private Button saveBtn;

    @FXML
    private Button back;

    @FXML
    private Button resetBtn;
    
    User userInfo = Globals.user;

    @FXML
    void onBack(ActionEvent event) throws IOException {
    	Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		URL url = getClass().getResource("DefaultPage.fxml");
		AnchorPane pane = FXMLLoader.load(url);
		Scene scene = new Scene(pane);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
    }

    @FXML
    void reset(ActionEvent event) {
    	setInfo();	
    }

    @FXML
    void save(ActionEvent event) throws UnknownHostException, IOException {
    	String First = firstName.getText();
    	String Last = lastName.getText();
    	String emailN = email.getText();
    	String phoneN = phone.getText();
    	String userN = user.getText();
    	String paymentN = payment.getText();
    	String pass;
    	
    	
    	if(password.getText().equals("")) {
    		pass = userInfo.getPassword();
    	}
    	else
    		pass = password.getText();
    	
    	User newInfo = new User(userInfo.getId(), First, Last, emailN, userN, pass, phoneN, paymentN, "", "");
    	
	    @SuppressWarnings("resource")
    	Socket	 socket = new Socket("localhost",5555);
        Object[] set = new Object[2];
        set[0] = "updateUserInfo";
        set[1]= newInfo;
      
        try {
            ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
            objectOutput.writeObject(set); 
        }
        catch (IOException e) 
        {
            e.printStackTrace();
        } 
        JOptionPane.showMessageDialog(null, "Info is updated!");
    }
    
    @FXML 
	void initialize() throws IOException, Exception {
    	setInfo();	
    }

	private void setInfo() {
    	firstName.setText(userInfo.getFirstName());
    	lastName.setText(userInfo.getLastName());
    	email.setText(userInfo.getEmail());
    	phone.setText(userInfo.getPhoneNumber());
    	user.setText(userInfo.getUserName());
    	payment.setText(userInfo.getPayment());
	}

}