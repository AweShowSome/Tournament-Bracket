package application;

import java.util.ArrayList;
import java.util.function.UnaryOperator;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.util.converter.IntegerStringConverter;

///////////////////////////////////////////////////////////////////////////////
//
//Title:            X4- Tournament Bracket
//Files:            Bracket.java, Game.java, Main.java, Player.java, application.css, teams.txt
//
//Semester:         Spring 2018
//
//Lecturer's Name:  Debra Deppeler CS400
//
////////////////////////////////////////////////////////////////////////////////

/**
 * An individual game in the bracket
 * Each game has information about the teams that are in that game, player 1 and 2
 * 
 *
 */
public class Game {
    
    // Round names for single elimination
    private static String[] roundsSingleElim = { "Finals", "Semi-Finals", "Top 8", "Top 16", "Top 32", "Top 64", "Top 128" };
    private Player p1, p2; // players 1 and 2 respectively
    private final int round, gameNum; // Round Game is part of (Correlates with array index its in)
    public static int roundCount = 0;
    public GameUI gameBox;
    
    public Player winner; // Winner of the match
    private Game topGame, bottomGame, parentGame; // Gets relative location
    private static final boolean WINNER_NOTE = false; // Tells us whether to say what match a player is coming from
    
    /**
     * Constructor for a game object
     * 
     * @param round
     *            Round number the game's on
     * @param gameNum
     *            Identifier for the game in that round
     * @param parentGame
     *            A previously constructed game that this game will "feed" into
     */
    public Game(int round, int gameNum, Game parentGame) {
        p1 = null;
        p2 = null;
        this.round = round;
        this.gameNum = gameNum;
        if (round > roundCount)
            roundCount = round + 1;
        if (parentGame != null) { // Don't want null pointer exceptions
            this.parentGame = parentGame;
            parentGame.setChild(this);
        }
    }
    
    /**
     * Constructor for a game object
     * 
     * @param p1
     *            The first team
     * @param p2
     *            The second team
     * @param round
     *            Round number the game's on
     * @param gameNum
     *            Identifier for the game in that round
     * @param parentGame
     *            A previously constructed game that this game will "feed" into
     */
    public Game(Player p1, Player p2, int round, int gameNum, Game parentGame) {
        this.p1 = p1;
        this.p2 = p2;
        this.round = round;
        this.gameNum = gameNum;
        if (round >= roundCount)
            roundCount = round + 1;
        if (parentGame != null) { // Don't want null pointer exceptions
            this.parentGame = parentGame;
            parentGame.setChild(this);
        }
    }
    
    /**
     * Recursive method that
     * Creates the UI for each game
     * Together, it creates the entire bracket
     * 
     * @param rounds
     *            The arrayList to store each game
     */
    public void createUI(ArrayList<VBox> rounds) {
        // Recursively work bottom up (left right) to create the games
        if (topGame != null)
            topGame.createUI(rounds);
        if (bottomGame != null)
            bottomGame.createUI(rounds);
        
        if (bottomGame == null && topGame == null) { // Last possible round; Only round for byes
            if (p2 == null) { // Only possible option. p1 will always be filled
                setWinner(p1);
                // return; // Doesn't create the game box
            }
        }
        
        gameBox = new GameUI(); // Create UI for that game
        // Add game to the rounds
        rounds.get(round).getChildren().add(gameBox.options);
    }
    
    /**
     * Class that holds the information of each game
     * 
     * @author andreweng
     *
     */
    private class GameUI {
        HBox options; // Holds the match name, the match, and the button to submit scores
        VBox match; // Holds the players' data
        Label matchName; // The Label for the match
        Button setMatch; // Finalizes match scores and automatically selects winner
        HBox play1, play2; // Player 1 and 2's boxes for their name and scores
        Label p1Name, p2Name; // p1 and p2's names respectively
        TextField score1, score2; // p1 and p2's scores respectively
        
        public GameUI() {
            // Create Options Box
            options = new HBox();
            // Settings
            // Padding works well up to 32 Teams. 33+, "Later Rounds" don't space out far enough
            int roundDif = roundCount - 1 - round;
            double padding = Math.pow(roundDif * 10, 1.55);
            options.setPadding(new Insets(padding, 0, padding, 0)); // Initial padding for spacing between games
            
            // Create Match Name
            matchName = new Label((round + 1) + "." + (gameNum + 1));
            // Set game name's settings
            matchName.setTextAlignment(TextAlignment.RIGHT);
            matchName.setPadding(new Insets(0, 5, 0, 0));
            matchName.setMinWidth(40);
            matchName.setAlignment(Pos.CENTER_RIGHT);
            
            options.getChildren().add(matchName); // Push onto leftmost side of options
            
            // Create Match Box
            match = new VBox();
            
            // Create Player Boxes
            play1 = new HBox();
            play2 = new HBox();
            
            // Set display for p1's Name box
            if (p1 != null) // If player1 exists
                p1Name = new Label(p1.name); // Display name
            else { // Else prepare for winner of top child game
                if (WINNER_NOTE)
                    p1Name = new Label("Winner of " + (topGame.round + 1) + "." + (topGame.gameNum + 1));
                else
                    p1Name = new Label("");
            }
            // Settings for p1's name
            p1Name.setPadding(new Insets(0, 0, 0, 5));
            p1Name.setPrefWidth(120);
            p1Name.setMinWidth(120);
            p1Name.setPrefHeight(30);
            p1Name.setId("namebox");
            p1Name.setAlignment(Pos.CENTER_LEFT);
            
            // Set display for p2's Name box
            if (p2 != null) // If player2 exists
                p2Name = new Label(p2.name); // Display name
            else { // Else
                if (bottomGame != null) { // If it has a child game
                    // Prepared for winner of that game
                    if (WINNER_NOTE)
                        p2Name = new Label("Winner of " + (bottomGame.round + 1) + "." + (bottomGame.gameNum + 1));
                    else
                        p2Name = new Label("");
                }
                else // Else it's a bye
                    p2Name = new Label("Bye");
            }
            // Settings for p2's name
            p2Name.setPadding(new Insets(0, 0, 0, 5));
            p2Name.setPrefWidth(120);
            p2Name.setMinWidth(120);
            p2Name.setPrefHeight(30);
            p2Name.setId("namebox");
            p2Name.setAlignment(Pos.CENTER_LEFT);
            
            UnaryOperator<Change> integerFilter = change -> {
                String newText = change.getControlNewText();
                // if proposed change results in a valid value, return change as-is:
                if (newText.matches("(([1-9][0-9]*)|0)?")) {
                    return change;
                }

                // invalid change, veto it by returning null:
                return null;
            };
            
            // Scores for p1 and p2
            // P1's score
            score1 = new TextField();
            // Set p1's score settings
            score1.maxWidth(Bracket.SCORE_SIZE);
            score1.setPrefWidth(Bracket.SCORE_SIZE);
            score1.setMinWidth(Bracket.SCORE_SIZE);
            score1.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, integerFilter));
            score1.setText("");
            
            // P2's score
            score2 = new TextField();
            // Set p2's score settings
            score2.maxWidth(Bracket.SCORE_SIZE);
            score2.setPrefWidth(Bracket.SCORE_SIZE);
            score2.setMinWidth(Bracket.SCORE_SIZE);
            score2.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, integerFilter));
            score2.setText("");
            
            // If game isn't active, we don't want to be able to edit that text
            if (p1 == null || p2 == null) {
                score1.setEditable(false);
                score2.setEditable(false);
            }
            
            // Add to each player's info box
            play1.getChildren().addAll(p1Name, score1);
            play2.getChildren().addAll(p2Name, score2);
            
            // Add each player to the match box
            match.getChildren().add(play1);
            match.getChildren().add(play2);
            
            // Add match to options
            options.getChildren().add(match);
            
            setMatch = new Button("•\n•\n•");
            setMatch.setStyle("-fx-font: 12 arial");
            setMatch.setPrefSize(10, 60);
            setMatch.setAlignment(Pos.CENTER);
            
            setMatch.setOnAction(new EventHandler<ActionEvent>() {
                
                @Override
                public void handle(ActionEvent event) {
                    int s1 = 0;
                    int s2 = 0;
                    if (!score1.getText().equals(""))
                        s1 = Integer.parseInt(score1.getText());
                    if (!score2.getText().equals(""))
                        s2 = Integer.parseInt(score2.getText());
                    if (s1 > s2)
                        setWinner(p1);
                    else if (s1 < s2)
                        setWinner(p2);
                    else {
                        Alert warning = new Alert(AlertType.WARNING);
                        warning.setContentText("The game is tied. You cannot select a winner");
                        warning.initModality(Modality.APPLICATION_MODAL);
                        warning.showAndWait();
                    }
                }
                
            });
            if (winner == null) { // For non byes
                // postWin.setVisible(false);
                if (p1 == null || p2 == null)
                    // Buttons will be reenabled when two players exist in the game
                    // but.setDisable(true);
                    setMatch.setDisable(true);
            }
            else { // For byes
                   // but.setDisable(true);
                setMatch.setDisable(true);
            }
            // but.getItems().addAll(preWin, postWin);
            
            options.getChildren().add(setMatch);
        }
        
    }
    
    // - - - - - Helper Methods - - - - - \\
    
    /**
     * Private helper method that checks if a game is eligible to be played
     */
    private void checkNewGame() {
        // If graphics have been loaded and neither player is null
        if (p1 != null && p2 != null && gameBox != null) {
            gameBox.score1.setEditable(true);
            gameBox.score2.setEditable(true);
            // gameBox.but.setDisable(false);
            gameBox.setMatch.setDisable(false);
        }
    }
    
    /**
     * toString for debugging purposes
     */
    @Override
    public String toString() {
        String s = roundsSingleElim[round] + ":\n";
        if (p1 != null && p2 != null) {
            s += p1.name + " vs " + p2.name;
            s += "\nScore: " + gameBox.score1.getText() + " : " + gameBox.score2.getText();
            if (winner != null)
                s += "Winner: " + winner.name;
        }
        else if (p1 == null && p2 != null) {
            if (topGame == null)
                s += p2.name + " Bye";
            else
                s += " Undecided vs " + p2.name;
        }
        else if (p2 == null && p1 != null) {
            if (bottomGame == null)
                s += p1.name + " Bye";
            else
                s += p1.name + " vs Undecided";
        }
        else
            s += "Game not created";
        s += "\n";
        return s;
    }
    
    // - - - - - Getters & Setters - - - - - \\
    
    /**
     * Getter method to get the top child game
     * 
     * @return
     *         The top (left) game
     */
    public Game getTop() {
        return topGame;
    }
    
    /**
     * Getter method to get the bottom child game
     * 
     * @return
     *         The bottom (right) game
     */
    public Game getBottom() {
        return bottomGame;
    }
    
    /**
     * Sets player 1 and performs any updates for the newly set player
     * 
     * @param p1
     *            The new player
     */
    public void setP1(Player p1) {
        if (this.p1 != null) { // If replacing another player
            if (this.p1 == winner) { // If match played and this player won
                setWinner(null);
            }
            // Reset match
            gameBox.score1.setText("");
            gameBox.score2.setText("");
            gameBox.score1.setEditable(false);
            gameBox.score2.setEditable(false);
            gameBox.setMatch.setDisable(true);
        }
        this.p1 = p1;
        if (p1 != null && gameBox != null) // Label new playerin appropriate box
            gameBox.p1Name.setText(p1.name);
        else if (p1 == null) {
            if (WINNER_NOTE)
                gameBox.p1Name.setText("Winner of " + (topGame.round + 1) + "." + (topGame.gameNum + 1));
            else
                gameBox.p1Name.setText("");
        }
        checkNewGame();
    }
    
    /**
     * Sets player 2 and performs any updates for the newly set player
     * 
     * @param p2
     *            The new player
     */
    public void setP2(Player p2) {
        if (this.p2 != null) { // If replacing another player
            if (this.p2 == winner) { // If match played and this player won
                setWinner(null);
            }
            // Reset match
            gameBox.score1.setText("");
            gameBox.score2.setText("");
            gameBox.score1.setEditable(false);
            gameBox.score2.setEditable(false);
            gameBox.setMatch.setDisable(true);
        }
        this.p2 = p2;
        if (p2 != null && gameBox != null) // Label new player in appropriate box
            gameBox.p2Name.setText(p2.name);
        else if (p2 == null) {
            if (WINNER_NOTE)
                gameBox.p2Name.setText("Winner of " + (bottomGame.round + 1) + "." + (bottomGame.gameNum + 1));
            else
                gameBox.p2Name.setText("");
        }
        checkNewGame();
    }
    
    /**
     * Sets Winner of this game and updates any games after
     * 
     * @param p
     *            The winner of this game
     */
    public void setWinner(Player p) {
        winner = p;
        if (parentGame != null) {
            if (parentGame.topGame == this)
                parentGame.setP1(winner);
            else
                parentGame.setP2(winner);
        }
        else { // If grand finals
            Player first = p;
            Player second, third;
            second = (p == p1) ? p2 : p1;
            if (topGame != null && bottomGame != null) { // Get the two or four relevant scores
                int s11, s12, s21, s22;
                s11 = (!topGame.gameBox.score1.getText().equals("")) ? Integer.parseInt(topGame.gameBox.score1.getText()) : 0;
                s12 = (!topGame.gameBox.score2.getText().equals("")) ? Integer.parseInt(topGame.gameBox.score2.getText()) : 0;
                s21 = (!bottomGame.gameBox.score1.getText().equals("")) ? Integer.parseInt(bottomGame.gameBox.score1.getText()) : 0;
                s22 = (!bottomGame.gameBox.score2.getText().equals("")) ? Integer.parseInt(bottomGame.gameBox.score2.getText()) : 0;
                Player pot1, pot2;
                if (s11 >= s12) {
                    pot1 = topGame.p2;
                    s11 = s12; // So we only need to compare a single variable
                }
                else
                    pot1 = topGame.p1;

                if (s21 >= s22) {
                    pot2 = bottomGame.p2;
                    s21 = s22; // So we only need to compare a single variable
                }
                else
                    pot2 = bottomGame.p1;
                System.out.println(pot1.name +pot2.name);
                third = (s11 >= s21) ? pot1 : pot2;
            }
            else
                third = null;
            System.out.println(first.name + second.name + third.name);
            Bracket.setResults(first, second, third); // Update results
        }
        gameBox.setMatch.setDisable(true); // Can only select once
    }
    
    /**
     * Sets the child of this game during setup
     * 
     * @param child
     */
    public void setChild(Game child) {
        if (topGame == null)
            topGame = child;
        else
            bottomGame = child;
    }
}
