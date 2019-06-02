package application;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
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

    private ObservableList<FixedPurchase> data = null;

    @FXML
    void RenewBtn(ActionEvent event) {

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
        Socket socket = new Socket("localhost",5555);
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
    


}

