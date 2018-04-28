package application;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.UnaryOperator;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.util.converter.IntegerStringConverter;

/**
 * An individual game in the bracket
 * 
 * @author andreweng
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
    
    public void createUI(ArrayList<VBox> rounds) {
        // Recursively work bottom up to create the games
        if (topGame != null)
            topGame.createUI(rounds);
        if (bottomGame != null)
            bottomGame.createUI(rounds);
        
        if (bottomGame == null && topGame == null) { // Last possible round; Only round for byes
            if (p1 == null) {
                setWinner(p2);
                // return; // Doesn't create the game box
            }
            else if (p2 == null) {
                setWinner(p1);
                // return; // Doesn't create the game box
            }
        }
        
        gameBox = new GameUI();
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
        MenuButton but; // The button selecting for options
        MenuItem preWin, postWin; // Options the button has
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
            // Settings
            
            // Create Player Boxes
            play1 = new HBox();
            play2 = new HBox();
            
            // Set display for p1's Name box
            if (p1 != null) // If player1 exists
                p1Name = new Label(p1.name); // Display name
            else { // Else
                if (topGame != null) { // If it has a child game
                    // Prepared for winner of that game
                    p1Name = new Label("Winner of " + (topGame.round + 1) + "." + (topGame.gameNum + 1));
                    // p1Name = new Label("");
                }
                else // Else it's a bye
                    p1Name = new Label("Bye");
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
                    p2Name = new Label("Winner of " + (bottomGame.round + 1) + "." + (bottomGame.gameNum + 1));
                    // p2Name = new Label("");
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
                if (newText.matches("-?(([1-9][0-9]*)|0)?")) {
                    return change;
                }
                else if ("-".equals(change.getText())) {
                    
                    // if user types or pastes a "-" in middle of current text,
                    // toggle sign of value:
                    
                    if (change.getControlText().startsWith("-")) {
                        // if we currently start with a "-", remove first character:
                        change.setText("");
                        change.setRange(0, 1);
                        // since we're deleting a character instead of adding one,
                        // the caret position needs to move back one, instead of
                        // moving forward one, so we modify the proposed change to
                        // move the caret two places earlier than the proposed change:
                        change.setCaretPosition(change.getCaretPosition() - 2);
                        change.setAnchor(change.getAnchor() - 2);
                    }
                    else {
                        // otherwise just insert at the beginning of the text:
                        change.setRange(0, 0);
                    }
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
            
            // P2s score
            score2 = new TextField();
            // Set p2's score settings
            score2.maxWidth(Bracket.SCORE_SIZE);
            score2.setPrefWidth(Bracket.SCORE_SIZE);
            score2.setMinWidth(Bracket.SCORE_SIZE);
            score2.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, integerFilter));
            score2.setText("");
            
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
            
            // Finally create the button
            // Button for selecting winner of that match
            // but = new Button("S\nE\nT");
            but = new MenuButton("•\n•\n•");
            // Settings
            but.setStyle("-fx-font: 12 arial");
            but.setPrefSize(10, 60);
            but.setAlignment(Pos.CENTER);
            but.setPopupSide(Side.RIGHT);
            
            // Set menu items/options to be done
            preWin = new MenuItem("Select Winner");
            postWin = new MenuItem("Modify Game Results");
            
            // Set action on mouse click/enter
            preWin.setOnAction(new EventHandler<ActionEvent>() {
                // Creates an alert allowing the user to select a winner
                // Bracket will update to reflect this
                @Override
                public void handle(ActionEvent e) {
                    Alert winWindow = new Alert(AlertType.NONE);
                    winWindow.initStyle(StageStyle.UTILITY);
                    winWindow.initModality(Modality.APPLICATION_MODAL);
                    winWindow.setContentText("Choose a Winner:");
                    
                    ButtonType selectP1 = new ButtonType(p1.name);
                    ButtonType selectP2 = new ButtonType(p2.name);
                    ButtonType cancel = new ButtonType("Cancel");
                    winWindow.getButtonTypes().setAll(selectP1, selectP2, cancel);
                    
                    Optional<ButtonType> result = winWindow.showAndWait();
                    
                    int s1 = 0;
                    int s2 = 0;
                    
                    if (!score1.getText().equals(""))
                        s1 = Integer.parseInt(score1.getText());
                    if (!score2.getText().equals(""))
                        s2 = Integer.parseInt(score2.getText());
                    if (result.get() == selectP1) {
                        if (s1 <= s2) {
                            Alert warnWin = new Alert(AlertType.NONE);
                            warnWin.initStyle(StageStyle.UTILITY);
                            warnWin.initModality(Modality.APPLICATION_MODAL);
                            warnWin.setContentText("Player 1's score isn't greater than Player 2's score.\nAre you sure about this?");
                            
                            ButtonType yes = new ButtonType("Yes");
                            ButtonType no = new ButtonType("No");
                            
                            warnWin.getButtonTypes().setAll(yes, no);
                            
                            Optional<ButtonType> warnRes = warnWin.showAndWait();
                            if (warnRes.get() != yes) {
                                warnWin.close();
                                return;
                            }
                            warnWin.close();
                        }
                        setWinner(p1);
                    }
                    else if (result.get() == selectP2) {
                        if (s1 >= s2) {
                            Alert warnWin = new Alert(AlertType.NONE);
                            warnWin.initStyle(StageStyle.UTILITY);
                            warnWin.initModality(Modality.APPLICATION_MODAL);
                            warnWin.setContentText("Player 2's score isn't greater than Player 1's score.\nAre you sure about this?");
                            
                            ButtonType yes = new ButtonType("Yes");
                            ButtonType no = new ButtonType("No");
                            
                            warnWin.getButtonTypes().setAll(yes, no);
                            
                            Optional<ButtonType> warnRes = warnWin.showAndWait();
                            if (warnRes.get() != ButtonType.OK) {
                                warnWin.close();
                                return;
                            }
                            warnWin.close();
                        }
                        setWinner(p2);
                    }
                    else {
                        winWindow.close();
                        return;
                    }
                    preWin.setVisible(false);
                    postWin.setVisible(true);
                    score1.setEditable(false);
                    score2.setEditable(false);
                    winWindow.close();
                }
            });
            
            // Set action on mouse click/enter
            postWin.setOnAction(new EventHandler<ActionEvent>() {
                
                // Allows for modification of prior games if necessary
                @Override
                public void handle(ActionEvent e) {
                    Alert modWin = new Alert(AlertType.NONE);
                    modWin.initStyle(StageStyle.UTILITY);
                    modWin.initModality(Modality.APPLICATION_MODAL);
                    modWin.setContentText("Are you sure?");
                    ButtonType yes = new ButtonType("Yes");
                    ButtonType no = new ButtonType("No");
                    modWin.getButtonTypes().setAll(yes, no);
                    Optional<ButtonType> result = modWin.showAndWait();
                    if (result.get() == yes) {
                        preWin.setVisible(true);
                        postWin.setVisible(false);
                        score1.setEditable(true);
                        score2.setEditable(true);
                    }
                    else {
                        modWin.close();
                        return;
                    }
                    modWin.close();
                }
            });
            
            if (winner == null) { // For non byes
                postWin.setVisible(false);
                if (p1 == null || p2 == null)
                    // Buttons will be reenabled when two players exist in the game
                    but.setDisable(true);
            }
            else // For byes
                but.setDisable(true);
            
            but.getItems().addAll(preWin, postWin);
            
            options.getChildren().add(but);
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
            gameBox.but.setDisable(false);
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
            gameBox.preWin.setVisible(true);
            gameBox.postWin.setVisible(false);
            gameBox.but.setDisable(true);
        }
        this.p1 = p1;
        if (p1 != null && gameBox != null) // Label new playerin appropriate box
            gameBox.p1Name.setText(p1.name);
        else if (p1 == null) {
            // gameBox.p1Name.setText("Winner of " + (topGame.round + 1) + "." + (topGame.gameNum + 1));
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
            gameBox.preWin.setVisible(true);
            gameBox.postWin.setVisible(false);
            gameBox.but.setDisable(true);
        }
        this.p2 = p2;
        if (p2 != null && gameBox != null) // Label new player in appropriate box
            gameBox.p2Name.setText(p2.name);
        else if (p2 == null) {
            gameBox.p2Name.setText("Winner of " + (bottomGame.round + 1) + "." + (bottomGame.gameNum + 1));
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
        else {
            Player first = p;
            Player second, third;
            second = (p == p1) ? p2 : p1;
            if (topGame != null && bottomGame != null) {
                int s11, s12, s21, s22;
                s11 = Integer.parseInt(topGame.gameBox.score1.getText());
                s12 = Integer.parseInt(topGame.gameBox.score2.getText());
                s21 = Integer.parseInt(bottomGame.gameBox.score1.getText());
                s22 = Integer.parseInt(bottomGame.gameBox.score2.getText());
                Player pot1, pot2;
                if (s11 >= s12) {
                    pot1 = topGame.p2;
                    s11 = s12;
                }
                else
                    pot1 = topGame.p1;
                if (s21 >= s22) {
                    pot2 = bottomGame.p2;
                    s21 = s22;
                }
                else
                    pot2 = bottomGame.p1;
                third = (s11 >= s22) ? pot1 : pot2;
            }
            else
                third = null;
            Bracket.setResults(first, second, third);
            
        }
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
