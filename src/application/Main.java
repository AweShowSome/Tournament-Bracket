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
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class Main extends Application {
    
    public static Bracket br = new Bracket();
    
    @Override
    public void start(Stage primaryStage) {
        ScrollPane scrollPane = new ScrollPane();
        
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20, 20, 20, 20));
        // setting background color
        root.setStyle("-fx-background-color: linear-gradient(from 0% 15% to 0% 105%, #FFFFFF, #c0c0c0, #ffd700)");
        
        // Set Title
        Label title = new Label("Tournement Generator");
        title.setFont(new Font("Helvetica Neue", 72.0));
        BorderPane.setAlignment(title, Pos.TOP_CENTER);
        root.setTop(title);
        
        primaryStage.setTitle("Tournement Generator");
        
        // Box to hold the bracket
        HBox bracketBox = new HBox();
        bracketBox.setPrefSize(1080, 720);
        
        ArrayList<VBox> rounds = new ArrayList<VBox>();
        for (int i = 0; i < br.rounds.size(); i++) { // Create a VBox for each round
            rounds.add(new VBox(10)); // Number inside gives padding between each game inside each round
            rounds.get(i).setPadding(new Insets(0, 0, 0, 50)); // Padding between rounds
            rounds.get(i).setAlignment(Pos.CENTER); // All content will be center-aligned
        }
        
        // Puts Rounds in order from left to right (like a normal bracket)
        for (int i = rounds.size() - 1; i >= 0; i--)
            bracketBox.getChildren().add(rounds.get(i));
        
        root.setCenter(bracketBox);
        
        // If there's only one player
        if (br.players.size() == 1) {
            Label winner = new Label("A winner is YOU!\n" + br.players.get(0).name);
            winner.setFont(new Font("Calibri", 72.0));
            winner.setTextAlignment(TextAlignment.CENTER);
            bracketBox.getChildren().add(winner);
            bracketBox.setAlignment(Pos.CENTER);
        } // If more than one player, actually create the tournament
        else if (!br.rounds.isEmpty())
            br.rounds.get(0).get(0).createUI(rounds);
        
        // Set Screen Size
        scrollPane.setPrefSize(1080, 720);
        scrollPane.setContent(root);
        
        // Set Scene
        Scene scene = new Scene(scrollPane, 1080, 720);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        
        // TextArea entranceList = new TextArea();
        // entranceList.setPrefSize(300, 400);
        // entranceList.setPromptText("Add the participants here:");
        //
        // hbox.getChildren().add(entranceList);
        // HBox.setMargin(entranceList, new Insets(10, 10, 10, 10));
        //
        // final Button fileButton = new Button("Import Text File");
        //
        // fileButton.setOnAction(new EventHandler<ActionEvent>() {
        // @Override
        // public void handle(ActionEvent e) {
        // System.out.println("Hello World!");
        // }
        // });
        
        // fileButton.setId("bevel-grey");
        
        // Set Window widgets
        // root.setTop(Title);
        // BorderPane.setAlignment(Title, Pos.TOP_CENTER);
        // root.setBottom(fileButton);
        // BorderPane.setAlignment(fileButton, Pos.TOP_CENTER);
        // root.setCenter(hbox);
        // BorderPane.setAlignment(hbox, Pos.CENTER);
        // Sets initial window
        // primaryStage.setTitle("Tourney Generator 0.1ß");
        // }
        // catch (Exception e) {
        // e.printStackTrace();
        // }
        // }
        
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        List<String> players;
        try {
            players = Files.readAllLines(Paths.get(args[0]));
            List<String> act = new ArrayList<String>();
            for (String team : players) {
                if (!team.trim().isEmpty()) {
                    act.add(team);
                }
            }
            br.parse(act);
            br.generateBracket();
            // br.getGameStatuses();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        launch(args);
    }
    
}
