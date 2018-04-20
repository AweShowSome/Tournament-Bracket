package application;

public class Game {
    
    // Round names for single elimination
    private static String[] rounds = { "Grand Finals", "Winner's Semi's", "Winner's Quarters", "Winner's Eighths", "Top 32", "Top 64", "Top 128" };
    private Player p1, p2; // players 1 and 2 respectively
    int s1, s2; // Scores for player1 & player2 respectively
    private final int round; // Round Game is part of (Corralates with array index its in)
    Player winner; // Winner of the match
    Game topGame, bottomGame, parentGame; // Gets relative location
    
    public Game(int round, Game parentGame) {
        p1 = null;
        p2 = null;
        s1 = 0;
        s2 = 0;
        this.round = round;
        if (parentGame != null) {
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
        this.parentGame = parentGame;
        parentGame.setChild(this);
    }
    
    public void setScore(int s1, int s2) {
        this.s1 = s1;
        this.s2 = s2;
    }
    
    public void setChild(Game child) {
        if (topGame != null)
            bottomGame = child;
        else
            topGame = child;
    }
    
    @Override
    public String toString() {
        String s = rounds[round] + ":\n";
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
}
