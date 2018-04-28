package application;

/**
 * A player in the tourney
 * 
 * @author andreweng
 *
 */
public class Player implements Comparable<Player> {
    String name;
    int seed;
    int rank;
    
    public Player(String name, int seed) {
        this.name = name;
        this.seed = seed;
    }
    
    @Override
    public int compareTo(Player o) {
        return seed - o.seed;
    }
}
