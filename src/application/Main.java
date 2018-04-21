package application;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {

	public static Bracket br = new Bracket();

	@Override
	public void start(Stage primaryStage) {
		//Arraylist to store the panel references
		ArrayList<BorderPane> panels = new ArrayList<BorderPane>();
		int numberOfRounds = 0, numberOfTeams = br.players.size();

		//calculating the number of rounds
		while(numberOfTeams > 0) {
			numberOfTeams /= 2;
			numberOfRounds++;
		}
		//first inner most pane
		panels.add(new BorderPane());
		panels.get(0).setStyle("-fx-background-color: linear-gradient(from 0% 15% to 0% 105%, #FFFFFF, #c0c0c0, #ffd700)");
		
		//adds inner panels to all panels equal to the number of rounds and adds the panels to the array list
		for( int i = 0; i < numberOfRounds - 1; i++) {

			BorderPane innerPane = new BorderPane();
			panels.add(innerPane);
			panels.get(i).setCenter(innerPane);
			

		}
		//making Vertical boxes for each left side and right side of each panel.
		for (int i = 0; i < numberOfRounds; i++) {
			VBox leftVBox = new VBox(), rightVBox = new VBox();
			//TODO add text boxes and labels to all vBoxes according to the number of games to be played in that round.
			panels.get(i).setLeft(leftVBox);
			panels.get(i).setRight(rightVBox);
		}
		//		primaryStage.show();
		//}
		//BorderPane root = new BorderPane();
		//		            root.setPadding(new Insets(20, 20, 20, 20));
		Scene scene = new Scene(panels.get(0), 1080, 720);
		//panels.get(0).setStyle("-fx-background-color: linear-gradient(from 0% 15% to 0% 105%, #FFFFFF, #c0c0c0, #ffd700)");
		//            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		//            
		//            Label Title = new Label("Tourney Generator 0.1ß");
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
		primaryStage.setScene(scene);
		//            primaryStage.setTitle("Tourney Generator 0.1ß");
		            primaryStage.show();
		//        }
		//        catch (Exception e) {
		//            e.printStackTrace();
		//        }
		//    }
	}
	public static void main(String[] args) {
		//		try {
		//			List<String> teams = Files.readAllLines(Paths.get(args[0]));
		//			List<String> act = new ArrayList<String>();
		//			for (String team : teams) {
		//				if (!team.trim().isEmpty())
		//					act.add(team);
		//			}
		//			br.parse(act);
		//			br.generateBracket();
		//		}
		//		catch (IOException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}
		launch(args);
	}
}

