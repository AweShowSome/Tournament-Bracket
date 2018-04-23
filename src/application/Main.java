package application;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
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
        Label title = new Label("Tournement Generator");
        title.setFont(new Font("Comic Sans MS Bold", 72.0));
        BorderPane.setAlignment(title, Pos.TOP_CENTER);
        root.setTop(title);
        
        HBox bracketBox = new HBox();
        bracketBox.setPrefSize(1080, 720);
        
        ArrayList<VBox> rounds = new ArrayList<VBox>();
        for (int i = 0; i < br.rounds.size(); i++) { // Create a VBox for each round
            rounds.add(new VBox(20));
            rounds.get(i).setPadding(new Insets(0, 0, 0, 50));
            rounds.get(i).setAlignment(Pos.CENTER);
        }
        
        // Puts Rounds in order from left to right (like a normal bracket
        for (int i = rounds.size() - 1; i >= 0; i--) {
            bracketBox.getChildren().add(rounds.get(i));
        }
        
        root.setCenter(bracketBox);
        if (!br.rounds.isEmpty())
            createGame(rounds, br.rounds.get(0).get(0));
        
        scrollPane.setPrefSize(1080, 720);
        scrollPane.setContent(root);
        
        Scene scene = new Scene(scrollPane, 1080, 720);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        
        //
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
        primaryStage.setScene(scene);
        // primaryStage.setTitle("Tourney Generator 0.1ß");
        primaryStage.show();
        // }
        // catch (Exception e) {
        // e.printStackTrace();
        // }
        // }
    }
    
    public static void main(String[] args) {
        ArrayList<String> teams = new ArrayList<String>();
        
        // 33 Teams
        
        teams.add("Team 1");
        teams.add("Team 2");
        teams.add("Team 3");
        teams.add("Team 4");
        teams.add("Team 5");
        teams.add("Team 6");
        teams.add("Team 7");
        teams.add("Team 8");
        teams.add("Team 9");
        // teams.add("Team 10");
        // teams.add("Team 11");
        // teams.add("Team 12");
        // teams.add("Team 13");
        // teams.add("Team 14");
        // teams.add("Team 15");
        // teams.add("Team 16");
        // teams.add("Team 17");
        // teams.add("Team 18");
        // teams.add("Team 19");
        // teams.add("Team 20");
        // teams.add("Team 21");
        // teams.add("Team 22");
        // teams.add("Team 23");
        // teams.add("Team 24");
        // teams.add("Team 25");
        // teams.add("Team 26");
        // teams.add("Team 27");
        // teams.add("Team 28");
        // teams.add("Team 29");
        // teams.add("Team 30");
        // teams.add("Team 31");
        // teams.add("Team 32");
        // teams.add("Team 33");
        // teams.add("ajsdfkjalk jl;aaosjdfk;a j");
        
        br.parse(teams);
        br.generateBracket();
        
        launch(args);
    }
    
    private void createGame(ArrayList<VBox> rounds, Game game) {
        // Recursively work bottom up to create the games
        if (game.getTop() != null)
            createGame(rounds, game.getTop());
        if (game.getBottom() != null)
            createGame(rounds, game.getBottom());
        
        VBox match = new VBox();
        HBox p1 = new HBox(); // Player 1's box
        HBox p2 = new HBox(); // Player 2's box
        
        // Names
        Label p1Name, p2Name;
        
        // P1's score
        TextField s1 = new TextField();
        s1.maxWidth(Bracket.SCORE_SIZE);
        s1.setPrefWidth(Bracket.SCORE_SIZE);
        
        // P2s score
        TextField s2 = new TextField();
        s2.maxWidth(Bracket.SCORE_SIZE);
        s2.setPrefWidth(Bracket.SCORE_SIZE);
        
        Player player1 = game.getP1();
        Player player2 = game.getP2();
        
        if (player1 != null) {
            String disName = "";
            disName = player1.name;
            p1Name = new Label(disName);
        }
        else {
            p1Name = new Label(" ");
        }
        p1Name.setPrefWidth(100);
        p1Name.setPrefHeight(30);
        p1Name.setAlignment(Pos.CENTER_LEFT);
        
        if (player2 != null) {
            String disName = "";
            disName = player2.name;
            p2Name = new Label(disName);
        }
        else {
            p2Name = new Label(" ");
        }
        p2Name.setPrefWidth(100);
        p2Name.setPrefHeight(30);
        p2Name.setAlignment(Pos.CENTER_LEFT);
        
        p1.getChildren().addAll(p1Name, s1);
        p2.getChildren().addAll(p2Name, s2);
        
        match.getChildren().add(p1);
        match.getChildren().add(p2);
        
        Button matchBut = new Button("Select Winner");
        matchBut.setPrefSize(100, 10);
        matchBut.setId("bevel-grey");
        
        match.getChildren().add(matchBut);
        
        rounds.get(game.getRound()).getChildren().add(match);
    }
    
}
