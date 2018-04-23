package application;

import java.util.ArrayList;
import java.util.List;

/**
 * Bracket that holds the entire tournament
 * 
 * @author andreweng
 *
 */
public class Bracket {
    
    ArrayList<Player> players; // List of players in the tournament
    ArrayList<ArrayList<Game>> rounds; // List of games broken down by rounds they're in
    
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
        // Don't want any input we can't work with
        if (teams == null || teams.size() < 1)
            throw new IllegalArgumentException();
        // TODO i changed the condition of the above if statement because it is okay if we have a single player. we just declare it as the winner.
        
        // Assuming no nonsense lines, create a new player for each line
        // Players are seeded by their order
        for (int i = 0; i < teams.size(); i++) {
            players.add(new Player(teams.get(i), i));
        }
    }
    
    /**
     * Creates a bracket based on the number of players in the tournament
     * Accommodates for any number of players > 1
     * Links all games together to the games they should be linked to
     */
    public void generateBracket() {

        
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
                // System.out.println(parentGame);
                
                if (i != rounds.size() - 1) { // If not last round
                    if (i == 0) // Grand Finals; No parent game
                        rounds.get(i).add(new Game(i, null));
                    else
                        // Add an empty game to be filled in for later
                        // Game remembers what game it will lead to
                        rounds.get(i).add(new Game(i, rounds.get(i - 1).get(parentGame)));
                }
                else { // Else, add a game with relevant players
                    if (i != 0)
                        rounds.get(i).add(new Game(players.get(j), players.get(players.size() - j - 1), i, rounds.get(i - 1).get(parentGame)));
                    else
                        rounds.get(i).add(new Game(players.get(j), players.get(players.size() - j - 1), i, null));
                }
            }
            // System.out.println("Round " + (i + 1) + ": " + rounds.get(i).size() + " game(s)");
        }
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
}
