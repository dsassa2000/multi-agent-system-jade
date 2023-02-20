package de.unihildesheim.iis.jadedemo;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
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
    private TextField queryTextField = new TextField();
    private String sentQuery;
    private static Label messageLabel;
    // creating agent variable
    Runtime runtime;
    Profile profile;
    AgentContainer mainContainer ;
    AgentController agentTwo;
    //
	@Override
	public void start(Stage arg0) {
			// TODO Auto-generated method stub
			arg0.setTitle("JavaFX Welcome");
			GridPane grid = new GridPane();
			grid.setAlignment(Pos.CENTER);
			grid.setHgap(10);
			grid.setVgap(10);
			grid.setPadding(new Insets(25, 25, 25, 25));

			Scene scene = new Scene(grid, 300, 275);
			arg0.setScene(scene);
			Text scenetitle = new Text("Welcome");
			scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
			grid.add(scenetitle, 0, 0, 2, 1);

			Label userName = new Label("Input a query:");
			grid.add(userName, 0, 1);

			grid.add(queryTextField, 1, 1);
			// Create a button
	        Button button = new Button("Search");
	    	HBox hbBtn = new HBox(10);
			hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
			hbBtn.getChildren().add(button);
			grid.add(hbBtn, 1, 4);
	        button.setOnAction(e -> {
	        	try {
					sendQuery();
				} catch (StaleProxyException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});
	        messageLabel = new Label("No message yet");
			
			arg0.show();

	}
    
	    public void sendQuery() throws StaleProxyException{
	        String query = queryTextField.getText();
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
	        messageLabel.setText(message);
	    }

}
