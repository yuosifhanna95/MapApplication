package application;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;

import java.awt.image.RenderedImage;
import java.io.File;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.transform.Transform;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class AddLocController {
	private Label[] labels= new Label[10];
	private ImageView[] locations = new ImageView[10];
	private Place[] Places= new Place[10];
	private int Counter=0;
	private int current=0;

    @FXML
    private AnchorPane mainPane;
    
    @FXML
    private ImageView MapImage;

    @FXML
    private Label label_message;
    
    @FXML
    private Button back;

    @FXML
    private Button btn_save;

    @FXML
    private Button btn_EditMode;

    @FXML
    private Button btn_ViewMode;

    @FXML
    private Button btn_AddLoc;
    
    @FXML
    private TextField NewLocation;
    
    @FXML
	private Button OK;
    
    @FXML
    private ImageView raninImage;
    
    
    @FXML
    void AddLocation(ActionEvent event) {
    	if(!NewLocation.getText().equals("") && Counter<10)
    	{
    		EventHandler<MouseEvent> OkEventHandler = new EventHandler<MouseEvent>() {
    			
    			@Override
    			public void handle(MouseEvent event) {
    				if(event.getEventType()==MouseEvent.MOUSE_CLICKED)
    				{
    					mainPane.getChildren().remove(OK);
    					current=-1;
    				}
    				// TODO Auto-generated method stub

    		    	//mainPane.getChildren().addAll(newLoc);
    			}
    		};
    		current = Counter;
        	Label label= new Label();
        	OK = new Button();
        	OK.setText("set");
        	OK.addEventHandler(MouseEvent.MOUSE_CLICKED, OkEventHandler);
        	label.setText(NewLocation.getText());
        	label.setFont(new Font("Arial",20));
        	Image im= new Image("File:loc.png");
        	ImageView newLoc = new ImageView(im);
        	newLoc.relocate(MapImage.getLayoutX(), MapImage.getLayoutY());
        	label.relocate(MapImage.getLayoutX(), MapImage.getLayoutY());
        	double aspect=0.75f;
        	newLoc.setFitHeight(im.getHeight() * aspect);
        	newLoc.setFitWidth(im.getWidth() * aspect);
        	labels[Counter]=label;
        	locations[Counter]=newLoc;
        	Places[Counter]= new Place();
        	mainPane.getChildren().addAll(locations[Counter]);
        	mainPane.getChildren().addAll(labels[Counter]);
        	mainPane.getChildren().addAll(OK);
        	EventHandler<MouseEvent> mouseEvent = new EventHandler<MouseEvent>() {
    			
    			@Override
    			public void handle(MouseEvent event) {
    				if(current!=-1)
    				{
	    				if(event.getEventType()==MouseEvent.MOUSE_CLICKED || event.getEventType()==MouseEvent.MOUSE_DRAGGED)
	    				{
	    					locations[current].relocate(event.getSceneX()-(im.getWidth()/2)*aspect, event.getSceneY()-(im.getHeight()/2)*aspect);
	    					labels[current].relocate(event.getSceneX()-(labels[current].getWidth()/2), event.getSceneY()-(labels[current].getHeight() +(im.getHeight()/2)*aspect));
	    					OK.relocate(event.getSceneX()-(OK.getWidth()/2), event.getSceneY()+(OK.getHeight()));
	    				}
	    				// TODO Auto-generated method stub

    				}
    			}
    		};
        	mainPane.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent);
        	mainPane.addEventHandler(MouseEvent.MOUSE_DRAGGED, mouseEvent);
        	mainPane.addEventHandler(MouseEvent.MOUSE_RELEASED, mouseEvent);
        	Counter++;
    	}
    	else
    	{
    		
    	}
    }
    
    @FXML
    void changeMode(ActionEvent event) {

    }

    
    @FXML
    void initialize() throws IOException, Exception {

    	raninImage = new ImageView(new Image("https://i.ibb.co/JsJP3r1/Haifa1.png"));
    	raninImage.relocate(162 , 50);
    	mainPane.getChildren().add(raninImage);
    	Label label= new Label();
    	label.setText("مش دارنا");
    	label.setFont(new Font("Arial",20));
    	Image im= new Image("File:loc.png");
    	ImageView newLoc = new ImageView(im);
    	newLoc.relocate(50, 50);
    	double aspect=0.75f;
    	newLoc.setFitHeight(im.getHeight() * aspect);
    	newLoc.setFitWidth(im.getWidth() * aspect);
    	//Stage primaryStage =(Stage) mainPane.getScene().getWindow();
    	//mainPane.getChildren().addAll(newLoc);
    	//mainPane.getChildren().addAll(label);

       	EventHandler<MouseEvent> mouseEvent = new EventHandler<MouseEvent>() {
			
			@Override
			public void handle(MouseEvent event) {
				// TODO Auto-generated method stub
		    	//newLoc.relocate(event.getSceneX()-(im.getWidth()/2)*aspect, event.getSceneY()-(im.getHeight()/2)*aspect);
		    	//label.relocate(event.getSceneX()-(label.getWidth()/2), event.getSceneY()-(label.getHeight() +(im.getHeight()/2)*aspect));
		    	//mainPane.getChildren().addAll(newLoc);
			}
		};

 
    	
    }
    
    @FXML
    void mouseClicked(ActionEvent event) throws IOException {
    	
      	Stage primaryStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
    	URL url = getClass().getResource("DefaultPage.fxml");
			AnchorPane pane =FXMLLoader.load(url);

			Scene scene = new Scene(pane);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
    }
    
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
    

    @FXML
    void saveImage(ActionEvent event) {
    	  FileChooser fileChooser = new FileChooser();

          //Set extension filter
          fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("png files (*.png)", "*.png"));

          //Prompt user to select a file
          File file = fileChooser.showSaveDialog(null);

          if(file != null){
              try {
                  //Pad the capture area
            	  WritableImage writableImage = new WritableImage((int)MapImage.getFitWidth() , (int)MapImage.getFitHeight());
            	  //WritableImage writableImage = new WritableImage((int)2000 , (int)2000);
                  SnapshotParameters params = new SnapshotParameters();
                  
                  params.setViewport(new Rectangle2D(MapImage.getLayoutX(), MapImage.getLayoutY(), MapImage.getFitWidth() ,MapImage.getFitHeight()));
                  ((Node)mainPane).snapshot(params, writableImage);
                  //writableImage= pixelScaleAwareCanvasSnapshot(mainPane,params,2);
                  //((Node)mainPane).snapshot()
                  //((Node)event.getSource()).snapshot(null, writableImage);
                  RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                  //Write the snapshot to the chosen file
                  ImageIO.write(renderedImage, "png", file);
              }
              catch (IOException ex)
              { 
            	  ex.printStackTrace(); 
              }
          }
    }
    
    public WritableImage pixelScaleAwareCanvasSnapshot(AnchorPane node,SnapshotParameters params, double pixelScale) {
        WritableImage writableImage = new WritableImage((int)MapImage.getFitWidth(), (int)MapImage.getFitHeight());
        SnapshotParameters spa = params;
        spa.setTransform(Transform.scale(pixelScale, pixelScale));
        return node.snapshot(spa, writableImage);     
    }

}