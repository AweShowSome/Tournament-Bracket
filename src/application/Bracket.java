package application;

import java.util.ArrayList;
import java.util.List;

public class Bracket {
    
    ArrayList<Player> players;
    ArrayList<ArrayList<Game>> rounds;
    
    public Bracket() {
        players = new ArrayList<Player>();
        rounds = new ArrayList<ArrayList<Game>>();
    }
    
    public void parse(List<String> teams) {
        for (int i = 0; i < teams.size(); i++) {
            players.add(new Player(teams.get(i), i));
        }
    }
    
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
        for (int i = 0; i < rounds.size(); i++) {
            int games = (int) Math.pow(2, i);
            for (int j = 0; j < games; j++) {
                int parentGame = 0;
                if (i != 0)
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
    
    public void getGameStatuses() {
        System.out.println(gameTraversal(rounds.get(0).get(0)));
    }
    
    private String gameTraversal(Game g) {
        if (g == null)
            return "";
        String s = "";
        s += gameTraversal(g.topGame);
        s += gameTraversal(g.bottomGame);
        s += g.toString() + "\n";
        
        return s;
    }
}
