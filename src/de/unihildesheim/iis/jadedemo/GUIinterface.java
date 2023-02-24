package de.unihildesheim.iis.jadedemo;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import jade.core.AID;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class GUIinterface extends Application {
    private TextField searchField = new TextField();
    private TextField pathField = new TextField();
    private String sentQuery;
    private static Label messageLabel;
    // creating agent variable
    Runtime runtime;
    Profile profile;
    AgentContainer mainContainer ;
    AgentController agentTwo;
    //
	@Override
	public void start(Stage stage) {
		// Load the search icon image
        Image searchIcon = new Image("C:\\Users\\HP\\Downloads\\search.png");

        // Create an ImageView for the search icon
        ImageView iconView = new ImageView(searchIcon);
        iconView.setFitHeight(20);
        iconView.setFitWidth(20);
		// Create a label and a text field for the search bar
        searchField = new TextField();
        searchField.setPromptText("Search");
        pathField = new TextField();
        Button searchButton = new Button("Search");
        searchButton.setGraphic(iconView);
     // Set the width and height of the ImageView
        iconView.setFitWidth(10);
        iconView.setFitHeight(10);
     // Add an event handler to the search button to perform the search
        searchButton.setOnAction(event -> 
        {
			try {
				sendQuery();
			} catch (StaleProxyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
        Arrow leftArrow = new Arrow(ArrowType.LEFT);
        Arrow rightArrow = new Arrow(ArrowType.RIGHT);

        Button leftButton = new Button();
        leftButton.setGraphic(new StackPane(leftArrow));
        leftButton.setOnAction(e -> System.out.println("Left button clicked"));

        Button rightButton = new Button();
        rightButton.setGraphic(new StackPane(rightArrow));
        rightButton.setOnAction(e -> System.out.println("Right button clicked"));

        // Create a layout for the search bar and add the label, text field, and button to it
        HBox searchBar = new HBox(10);
        searchBar.setPadding(new Insets(10));
        searchBar.getChildren().addAll(leftButton,rightButton,pathField, searchField, searchButton);

        // Create a layout for the scene and add the search bar to it
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().add(searchBar);

        // Create a scene and add the layout to it
        Scene scene = new Scene(layout);

        // Set the stage title and scene, then show the stage
        stage.setTitle("Search Bar Example");
        stage.setScene(scene);
        stage.show();
			

	}
    
	    public void sendQuery() throws StaleProxyException{
	        String query = searchField.getText();
	        runtime = jade.core.Runtime.instance();
	        runtime.setCloseVM(true);

	        profile = new ProfileImpl("127.0.0.1", 1099, null);
	        profile.setParameter(Profile.GUI, "true");

	        mainContainer = runtime.createMainContainer(profile);

	        agentTwo = mainContainer
	            .createNewAgent("AgentTwo", AgentTwo.class.getName(), new Object[0]);
	        agentTwo.start();
	        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
	        message.setContent(query);
	        message.addReceiver(new AID("AgentOne", AID.ISLOCALNAME));
	        mainContainer.acceptNewAgent("AgentOne", new AgentOne(message)).start();
	       
	    }
	    public static void updateUI(String message) {
	        System.out.println(message);
	       // messageLabel.setText(message);
	    }

	    private static class Arrow extends Polygon {
	        public Arrow(ArrowType type) {
	            if (type == ArrowType.LEFT) {
	                getPoints().addAll(new Double[]{
	                    15.0, 0.0,
	                    0.0, 10.0,
	                    15.0, 20.0,
	                    15.0, 15.0,
	                    5.0, 10.0,
	                    15.0, 5.0});
	            } else if (type == ArrowType.RIGHT) {
	                getPoints().addAll(new Double[]{
	                    0.0, 0.0,
	                    15.0, 10.0,
	                    0.0, 20.0,
	                    0.0, 15.0,
	                    10.0, 10.0,
	                    0.0, 5.0});
	            }
	        }
	    }

	    private enum ArrowType {
	        LEFT, RIGHT
	    }


}
