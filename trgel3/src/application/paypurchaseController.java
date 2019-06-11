/**
 * Sample Skeleton for "payementforpurchase.fxml" Controller Class
 * You can copy and paste this code into your favorite IDE
 **/

package application;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class paypurchaseController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="back"
    private Button back; // Value injected by FXMLLoader

    @FXML // fx:id="city"
    private TextField city; // Value injected by FXMLLoader

    @FXML // fx:id="infopay"
    private ComboBox<String> infopay; // Value injected by FXMLLoader

    @FXML // fx:id="payinfo"
    private TextField payinfo= new TextField(); // Value injected by FXMLLoader

    @FXML // fx:id="paypurchase"
    private Button paypurchase; // Value injected by FXMLLoader

    @FXML // fx:id="period"
    private TextField period; // Value injected by FXMLLoader

    @FXML // fx:id="price"
    private TextField price; // Value injected by FXMLLoader

    @FXML // fx:id="usname"
    private TextField usname; // Value injected by FXMLLoader

   
    // Handler for Button[fx:id="back"] onAction
    @FXML
    void doback(ActionEvent event)  throws IOException{
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

    // Handler for ComboBox[fx:id="infopay"] onAction
    

    // Handler for Button[fx:id="paypurchase"] onAction
    @FXML
    void dopurchasement(ActionEvent event) throws UnknownHostException, IOException {
    	int timeperiod=0;
    	
    	if(!(period.getText().equals("")))
    	 timeperiod=Integer.parseInt(period.getText());
        String user=usname.getText();
        Date sdate=new Date();
        SimpleDateFormat simpledate=new SimpleDateFormat("dd/MM/YYYY");
         
        String startdate=simpledate.format(sdate);
        try {
			sdate = simpledate.parse(startdate);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        Date enddate=sdate;
        double price1=Double.parseDouble(price.getText());
        String city1=Globals.city.getCity();
        FixedPurchase fp=new FixedPurchase(user,timeperiod,city1,sdate,enddate,price1);
        if(!(user.equals(Globals.user.getUserName()))){
        	JOptionPane.showMessageDialog(null, "the username isn't correct");
        }
        else if(!(city1.equals(city.getText()))){
        	JOptionPane.showMessageDialog(null, "the cityname isn't correct");
        }
        else {
        @SuppressWarnings("resource")
    	Socket	 socket = new Socket(Globals.IpAddress,5555);
        Object[] set = new Object[4];
        set[0] = "dofixedpurchase";
        set[1]= fp;
        set[2]=payinfo.getText();
        set[3]=infopay.getValue();
        try {
            ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
            objectOutput.writeObject(set); 
            ObjectInputStream  objectInput = new ObjectInputStream(socket.getInputStream());
            try {
 				Object data=objectInput.readObject();
 				if((data).equals("the period should be bigger than 3")){
 					JOptionPane.showMessageDialog(null, "the period should be bigger than 3");
 				}
 				if((data).equals("the period is very big,you can purchase until 180 day")){
 					JOptionPane.showMessageDialog(null, "the period is very big,you can purchase until 180 day");
 				}
 				
 				if((data).equals("we need the payinfo to continue")){
 					JOptionPane.showMessageDialog(null, "we need the payinfo to continue");
 				}
 				if((data).equals("thanks for purchace,you will enjoy")){
 					JOptionPane.showMessageDialog(null, "thanks for purchace,you will enjoy");
 					Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
 					URL url = getClass().getResource("MyMapsScene.fxml");
 					AnchorPane pane = FXMLLoader.load(url);
 					Globals.backLink = "DefaultPage.fxml";
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

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() throws  UnknownHostException, IOException {
    	
    	
    	@SuppressWarnings("resource")
    	Socket	 socket = new Socket(Globals.IpAddress,5555);
		 
			
			
		 
   	 
        Object[] get = new Object[3];
        get[0] = "getfixedcostandpayinfo";
        get[1]= Globals.city.getCity();
        get[2]=Globals.user.getUserName();
        try {
            ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
            objectOutput.writeObject(get); 
            
            ObjectInputStream  objectInput = new ObjectInputStream(socket.getInputStream());
           
        try {
        	
        	Object data=objectInput.readObject();
        	double x=((double)((Object[])(data))[0]);
        	String p=((String)((Object[])(data))[1]);
        	price.setText(String.valueOf(x));
        	payinfo.setText(p);
        	infopay.getItems().addAll("No", "Yes");
				 infopay.setPromptText("No");
				 socket.close();
				// payinfo.setVisible(false);
				// payinfo.setDisable(true);
				 infopay.setOnAction(event -> {
					 
							switch (infopay.getValue()) {
							case "No":
								payinfo.setText(p);
								 
								 break;
							case "Yes":
								payinfo.setText("");
								 break;
							}
				 
				 });
				
				
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
