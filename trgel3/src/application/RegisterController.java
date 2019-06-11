/**
 * Sample Skeleton for "RegisterScene.fxml" Controller Class
 * You can copy and paste this code into your favorite IDE
 **/

package application;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

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

public class RegisterController {
	static private final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	static private final String DB = "eCp4XWJvNw";
	static private final String DB_URL = "jdbc:mysql://remotemysql.com/" + DB + "?useSSL=false";
	static private final String USER = "eCp4XWJvNw";
	static private final String PASS = "eSS7xZeTpg";
//	public static Connection connecttion() {
//		Connection conn=null;
//		try {
//			Class.forName(JDBC_DRIVER);
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			return null;
//		}
//		try {
//			conn = DriverManager.getConnection(DB_URL, USER, PASS);
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			return null;
//		}
//		return conn;
//	}
//	public static int checkuser(String user2,Connection	conn) {
//		Statement pr;
//		
//		ResultSet rs=null;
//		int a=0;
//    	String sql ="SELECT * FROM user WHERE userName ='" + user2 + "'";
//    	//Connection	conn=connecttion();
//    	if(conn!=null) {
//    		try {
//				pr=conn.createStatement();
//    			 
//    			rs=pr.executeQuery(sql);
//    			 
//				//pr.setString(1, user2);
//				
//				//rs=pr.executeQuery();
//				//rs.next();
//				//System.out.println(rs.getObject(1));
//				if(rs.next()) {
//					//System.out.println(user2);
//				//rs.next();
//				//System.out.println(rs.getString("first name"));
//				a=1;
//			/*	if(rs.next()) {
//					//System.out.println(user2);
//					a=1;
//				}*/
//				}
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//    		
//    	}
//    	
//    	return a;
//	}
	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="cancelbtn"
	private Button cancelbtn; // Value injected by FXMLLoader

	@FXML // fx:id="emaiil"
	private TextField emaiil; // Value injected by FXMLLoader

	@FXML // fx:id="fname"
	private TextField fname; // Value injected by FXMLLoader

	@FXML // fx:id="lname"
	private TextField lname; // Value injected by FXMLLoader

	@FXML // fx:id="logbtn"
	private Button logbtn; // Value injected by FXMLLoader

	@FXML // fx:id="pass"
	private PasswordField pass; // Value injected by FXMLLoader

	@FXML // fx:id="payinfo"
	private TextField payinfo; // Value injected by FXMLLoader

	@FXML // fx:id="phoneno"
	private TextField phoneno; // Value injected by FXMLLoader

	@FXML // fx:id="reg"
	private Button reg; // Value injected by FXMLLoader

	@FXML // fx:id="usernamme"
	private TextField usernamme; // Value injected by FXMLLoader

	// Handler for Button[fx:id="cancelbtn"] onAction
	@FXML
	void cancelation(ActionEvent event) {
		System.exit(0);// handle the event here
	}

	// Handler for Button[fx:id="logbtn"] onAction
	@FXML
	void login(ActionEvent event) throws IOException {
		Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		URL url = getClass().getResource("MainPage.fxml");
		AnchorPane pane = FXMLLoader.load(url);

		Scene scene = new Scene(pane);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
		// handle the event here
	}

	// Handler for Button[fx:id="reg"] onAction
	@FXML
	void performed(ActionEvent event) throws UnknownHostException, IOException {
		String fname1 = fname.getText();
		String lname1 = lname.getText();
		String phone = phoneno.getText();
		String email = emaiil.getText();
		String pay = payinfo.getText();
		String user = usernamme.getText();
		String password = pass.getText();
		String type = "member";// we have to fix this
		User Client = new User(0,fname1, lname1, email, user, password, phone, pay, type, "");

		@SuppressWarnings("resource")
		Socket socket = new Socket(Globals.IpAddress, 5555);

		Object[] get = new Object[2];
		get[0] = "Register";
		get[1] = Client;
		try {
			ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
			objectOutput.writeObject(get);
			ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());
			try {
				Object data = objectInput.readObject();

				if ((data).equals("fill all the fields")) {
					JOptionPane.showMessageDialog(null, "Please fill all the fields");
				}
				if ((data).equals("thanks for registeration")) {
					JOptionPane.showMessageDialog(null, "thanks for registeration");
				}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		assert cancelbtn != null : "fx:id=\"cancelbtn\" was not injected: check your FXML file 'RegisterScene.fxml'.";
		assert emaiil != null : "fx:id=\"emaiil\" was not injected: check your FXML file 'RegisterScene.fxml'.";
		assert fname != null : "fx:id=\"fname\" was not injected: check your FXML file 'RegisterScene.fxml'.";
		assert lname != null : "fx:id=\"lname\" was not injected: check your FXML file 'RegisterScene.fxml'.";
		assert logbtn != null : "fx:id=\"logbtn\" was not injected: check your FXML file 'RegisterScene.fxml'.";
		assert pass != null : "fx:id=\"pass\" was not injected: check your FXML file 'RegisterScene.fxml'.";
		assert payinfo != null : "fx:id=\"payinfo\" was not injected: check your FXML file 'RegisterScene.fxml'.";
		assert phoneno != null : "fx:id=\"phoneno\" was not injected: check your FXML file 'RegisterScene.fxml'.";
		assert reg != null : "fx:id=\"reg\" was not injected: check your FXML file 'RegisterScene.fxml'.";
		assert usernamme != null : "fx:id=\"usernamme\" was not injected: check your FXML file 'RegisterScene.fxml'.";

		// Initialize your logic here: all @FXML variables will have been injected

	}

}
