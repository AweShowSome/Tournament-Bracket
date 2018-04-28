package application;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 * Bracket that holds the entire tournament
 * 
 * @author andreweng
 *
 */
public class Bracket {
    
    ArrayList<Player> players; // List of players in the tournament
    ArrayList<ArrayList<Game>> rounds; // List of games broken down by rounds they're in
    BracketUI bracket;
    public static Label results;
    public static final int SCORE_SIZE = 35;
    
    public Bracket() {
        players = new ArrayList<Player>();
        rounds = new ArrayList<ArrayList<Game>>();
    }
    
    /**
     * Parse's the data fed in either by hand or by file
     * Players are seeded by order of appearance in list
     * 
     * @param teams
     */
    public void parse(List<String> teams) {
        // Assuming no nonsense lines, create a new player for each line
        // Players are seeded by their order
        for (int i = 0; i < teams.size(); i++)
            players.add(new Player(teams.get(i), i));
        generateBracket(); // Go on to generate the bracket
    }
    
    /**
     * Creates a bracket based on the number of players in the tournament
     * Accommodates for any number of players > 1
     * Links all games together to the games they should be linked to
     */
    public void generateBracket() {
        if (players.isEmpty() || players.size() == 1)
            return; // Don't do anything if num of players == 0, 1
        // Makes players.size() a power of two
        // Handles Byes
        while ((Math.log(players.size()) / Math.log(2)) % 1 > 0)
            players.add(null);
        // Adds the number of rounds required
        for (int i = 0; i < Math.log(players.size()) / Math.log(2); i++)
            rounds.add(new ArrayList<Game>());
            
        // Adds games for each round
        // Ex: i = 0, 1 game is added,
        // i = 1, 2 games are added,
        // i = 2, 4 games are added, etc.
        int parentGame = 0;
        for (int i = 0; i < rounds.size(); i++) {
            int games = (int) Math.pow(2, i); // Number of games to exist in this round
            for (int j = 0; j < games; j++) {
                if (i != 0) // If not GF, we can set a game's "parent game"
                    parentGame = ((games - Math.abs(games - 1 - (2 * j))) / 2);
                
                if (i != rounds.size() - 1) { // If not last round
                    if (i == 0) // Grand Finals; No parent game
                        rounds.get(i).add(new Game(i, j, null));
                    else
                        // Add an empty game to be filled in for later
                        // Game remembers what game it will lead to
                        rounds.get(i).add(new Game(i, j, rounds.get(i - 1).get(parentGame)));
                }
                else { // Else, add a game with relevant players
                    if (i != 0)
                        rounds.get(i).add(new Game(players.get(j), players.get(players.size() - j - 1), i, j, rounds.get(i - 1).get(parentGame)));
                    else
                        rounds.get(i).add(new Game(players.get(j), players.get(players.size() - j - 1), i, j, null));
                }
            }
        }
        bracket = new BracketUI();
    }
    
    /**
     * Basic toString for debugging purposes. Gets the status of each game
     * Works if generateBracket has been ran
     */
    public void getGameStatuses() { // Doesn't work with only 1 team
        System.out.println(gameTraversal(rounds.get(0).get(0)));
    }
    
    /**
     * Private recursive method for toString method to work
     * 
     * @param g
     *            The current game
     * @return
     *         Each game's toString in LRO Order
     */
    private String gameTraversal(Game g) {
        if (g == null)
            return "";
        String s = "";
        s += gameTraversal(g.getTop());
        s += gameTraversal(g.getBottom());
        s += g.toString() + "\n";
        
        return s;
    }
    
    public static void setResults(Player p1, Player p2, Player p3) {
        String res = "First: " + p1.name + "\nSecond: " + p2.name;
        if (p3 != null)
            res += "\nThird: " + p3.name;
        results.setText(res);
        results.setVisible(true);
    }
    
    class BracketUI {
        ScrollPane scroll;
        BorderPane root;
        Label title;
        HBox bracketBox;
        VBox resultsBox;
        ArrayList<VBox> matchRounds;
        
        public BracketUI() {
            scroll = new ScrollPane();
            
            resultsBox = new VBox();
            
            root = new BorderPane();
            root.setPadding(new Insets(20, 20, 20, 20));
            // setting background color
            root.setStyle("-fx-background-color: linear-gradient(from 0% 15% to 0% 105%, #FFFFFF, #c0c0c0, #ffd700)");
            
            // Set Title
            Label title = new Label("Tournement Generator");
            title.setFont(new Font("Helvetica Neue", 72.0));
            BorderPane.setAlignment(title, Pos.TOP_CENTER);
            root.setTop(title);
            
            // Box to hold the bracket
            bracketBox = new HBox();
            bracketBox.setPrefSize(1080, 720);
            
            matchRounds = new ArrayList<VBox>();
            for (int i = 0; i < rounds.size(); i++) { // Create a VBox for each round
                matchRounds.add(new VBox(10)); // Number inside gives padding between each game inside each round
                matchRounds.get(i).setPadding(new Insets(0, 0, 0, 50)); // Padding between rounds
                matchRounds.get(i).setAlignment(Pos.CENTER); // All content will be center-aligned
            }
            
            // Puts Rounds in order from left to right (like a normal bracket)
            for (int i = matchRounds.size() - 1; i >= 0; i--)
                bracketBox.getChildren().add(matchRounds.get(i));
            
            results = new Label("Ex Results Here");
            results.setAlignment(Pos.CENTER);
            results.setVisible(false);
            
            resultsBox.getChildren().addAll(results, bracketBox);
            root.setCenter(resultsBox);
            BorderPane.setAlignment(resultsBox, Pos.CENTER);
            
            // If there's only one player
            if (players.size() == 1) {
                Label winner = new Label("A winner is YOU!\n" + players.get(0).name);
                winner.setFont(new Font("Calibri", 72.0));
                winner.setTextAlignment(TextAlignment.CENTER);
                bracketBox.getChildren().add(winner);
                bracketBox.setAlignment(Pos.CENTER);
            } // If more than one player, actually create the tournament
            else if (!rounds.isEmpty())
                rounds.get(0).get(0).createUI(matchRounds);
            
            // Set Screen Size
            scroll.setPrefSize(1080, 720);
            scroll.setContent(root);
        }
        
    }
}
