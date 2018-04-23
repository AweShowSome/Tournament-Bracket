package application;

/**
 * An individual game in the bracket
 * 
 * @author andreweng
 *
 */
public class Game {
    
    // Round names for single elimination
    private static String[] roundsSingleElim = { "Finals", "Semi-Finals", "Top 8", "Top 16", "Top 32", "Top 64", "Top 128" };
    // Round names for double elimination (In case we really want to)
    
    // TODO: ^ i don't think we need to. Debra absolutely didnt want extra stuff coz she cant test it and it messes with her tests
    private static String[] roundsDoubleElim = {
            "Grand Finals", "Loser's Finals", "Loser's Semis",
            "Winner's Finals", "Loser's Quarters", "Loser's Eigths",
            "Winner's Semi's", "Loser's Top 12", "Loser's Top 16",
            "Winner's Quarters", "Loser's Top 24", "Loser's Top 32",
            "Top 32", "Loser's Top 48", "Loser's Top 64",
            "Top 64", "Loser's Top 96", "Loser's Top 128",
            "Top 128" };
    private Player p1, p2; // players 1 and 2 respectively
    public int s1, s2; // Scores for player1 & player2 respectively
    private final int round; // Round Game is part of (Correlates with array index its in)
    public static int roundCount = 0;
    
    public Player winner; // Winner of the match
    // TODO we dont need this either
    private Game topGame, bottomGame, parentGame; // Gets relative location
    
    public Game(int round, Game parentGame) {
        p1 = null;
        p2 = null;
        s1 = 0;
        s2 = 0;
        this.round = round;
        if (round > roundCount)
            roundCount = round + 1;
        if (parentGame != null) { // Don't want null pointer exceptions
            this.parentGame = parentGame;
            parentGame.setChild(this);
        }
    }
    
    public Game(Player p1, Player p2, int round, Game parentGame) {
        this.p1 = p1;
        this.p2 = p2;
        s1 = 0;
        s2 = 0;
        this.round = round;
        if (round > roundCount)
            roundCount = round + 1;
        if (parentGame != null) { // Don't want null pointer exceptions
            this.parentGame = parentGame;
            parentGame.setChild(this);
        }
    }
    
    // - - - - - Helper Methods - - - - - \\
    
    /**
     * toString for debugging purposes
     */
    @Override
    public String toString() {
        String s = roundsSingleElim[round] + ":\n";
        if (p1 != null && p2 != null) {
            s += p1.name + " vs " + p2.name;
            s += "\nScore: " + s1 + " : " + s2;
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
    
    public Game getTop() {
        return topGame;
    }
    
    public Game getBottom() {
        return bottomGame;
    }
    
    public Game getParent() {
        return parentGame;
    }
    
    public Player getP1() {
        return p1;
    }
    
    public Player getP2() {
        return p2;
    }
    
    public int getRound() {
        return round;
    }
    
    public void setS1(int s1) {
        this.s1 = s1;
    }
    
    public void setS2(int s2) {
        this.s2 = s2;
    }
    
    public void setP1(Player p1) {
        this.p1 = p1;
    }
    
    public void setP2(Player p2) {
        this.p2 = p2;
    }
    
    public void setWinner(Player p) {
        winner = p;
    }
    
    public void setChild(Game child) {
        if (topGame != null)
            bottomGame = child;
        else
            topGame = child;
    }
}
