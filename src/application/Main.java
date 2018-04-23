package application;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {

	public static Bracket br = new Bracket();

	@Override
	public void start(Stage primaryStage) {
		//		System.out.println(br.players.size());
		//Arraylist to store the panel references
		ArrayList<BorderPane> panels = new ArrayList<BorderPane>();
		int numberOfRounds = br.rounds.size(), numberOfTeams = br.players.size();

		//first border pane to avoid the null pointer exception and also that every tournament will have at least one panel.
		if(br.players.size()>1)
			panels.add(new BorderPane());
		else {
			//TODO deal with a single team situation
			
			System.out.println("singleplayer");
			return;
		}
		//setting background color
		panels.get(0).setStyle("-fx-background-color: linear-gradient(from 0% 15% to 0% 105%, #FFFFFF, #c0c0c0, #ffd700)");
		Label title = new Label("Tournement Generator");
		title.setFont(new Font("Comic Sans MS Bold", 72.0));
		panels.get(0).setTop(title);

		//adds inner panels to all panels equal to the number of rounds and adds the panels to the array list
		// the center part of the inner most panel will be reserved for the final game info
//		System.out.println("Marker");
		int i;
		for(i = 0; i < numberOfRounds - 1; i++) {
			BorderPane innerPane = new BorderPane();
			panels.add(innerPane);
			panels.get(i).setCenter(innerPane);

			GridPane leftBox = new GridPane(), rightBox = new GridPane();
			for (int j = 0; j < numberOfTeams/ Math.pow(2, i+1); j+=2) {
				leftBox.add(new Label("Team A - " + j), 0, j);
				leftBox.add(new TextField(), 1, j);
				leftBox.add(new Label("Team B - " + j), 0, j+1);
				leftBox.add(new TextField(), 1, j+1);
				rightBox.add(new Label("Team A - " + j), 0, j);
				rightBox.add(new TextField(), 1, j);
				rightBox.add(new Label("Team B - " + j), 0, j+1);
				rightBox.add(new TextField(), 1, j+1);

			}
			((BorderPane)panels.get(i).getCenter()).setLeft(leftBox);
			((BorderPane)panels.get(i).getCenter()).setRight(rightBox);
		}
		
		GridPane center = new GridPane();
		center.add(new Label("Team A - " + 0), 0, 0);
		center.add(new TextField(), 1, 0);
		center.add(new Label("Team B"), 0, 1);
		center.add(new TextField(), 1, 1);
		panels.get(panels.size()-1).setCenter(center);


		Scene scene = new Scene(panels.get(0), 1500, 1000);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	//making Vertical boxes for each left side and right side of each panel.
	//		for (int i = 0; i < numberOfRounds; i++) {
	//			
	//			//TODO add text boxes and labels to all vBoxes according to the number of games to be played in that round.
	//			panels.get(i).setLeft(leftVBox);
	//			panels.get(i).setRight(rightVBox);
	//}
	//		primaryStage.show();
	//		}
	//BorderPane root = new BorderPane();
	//		            root.setPadding(new Insets(20, 20, 20, 20));
	//	panels.get(0).setStyle("-fx-background-color: linear-gradient(from 0% 15% to 0% 105%, #FFFFFF, #c0c0c0, #ffd700)");
	//            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
	//            
	//            Label Title = new Label("Tourney Generator 0.1ÃŸ");
	//            Title.setFont(new Font("Comic Sans MS Bold", 72.0));

	//             HBox hbox = new HBox();
	//             hbox.setPrefSize(600, 400);
	//            
	//             TextArea entranceList = new TextArea();
	//             entranceList.setPrefSize(300, 400);
	//             entranceList.setPromptText("Add the participants here:");
	//            
	//             hbox.getChildren().add(entranceList);
	//             HBox.setMargin(entranceList, new Insets(10, 10, 10, 10));
	//            
	//             final Button fileButton = new Button("Import Text File");
	//            
	//             fileButton.setOnAction(new EventHandler<ActionEvent>() {
	//             @Override
	//             public void handle(ActionEvent e) {
	//             System.out.println("Hello World!");
	//             }
	//             });

	//             fileButton.setId("bevel-grey");

	// Set Window widgets
	//            root.setTop(Title);
	//            BorderPane.setAlignment(Title, Pos.TOP_CENTER);
	//             root.setBottom(fileButton);
	//             BorderPane.setAlignment(fileButton, Pos.TOP_CENTER);
	//             root.setCenter(hbox);
	//             BorderPane.setAlignment(hbox, Pos.CENTER);
	//             Sets initial window
	//  }
	//        catch (Exception e) {
	//            e.printStackTrace();
	//        }
	//    }

	public static void main(String[] args) {

		File dataFile = new File(args[0]);
		try
		{
			Scanner scnr = new Scanner(dataFile);
			//List<String> teams = Files.readAllLines(Paths.get(args[0]));
			List<String> act = new ArrayList<String>();

			while(scnr.hasNextLine()) {
				act.add(scnr.nextLine());
			}



			br.parse(act);
			br.generateBracket();
//			for( int i = 0; i < br.players.size(); i++)
//				System.out.println(br.players.get(i).name);
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		launch(args);
	}

}

