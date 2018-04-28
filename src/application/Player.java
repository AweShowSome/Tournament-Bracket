///////////////////////////////////////////////////////////////////////////////
//                   
// Title:            X4- Tournament Bracket
// Files:            Bracket.java, Game.java, Main.java, Player.java
//
// Semester:         Spring 2018
//
// Lecturer's Name:  Debra Deppeler CS400
//
////////////////////////////////////////////////////////////////////////////////
package application;

/**
 * A player in the tournament. the class holds all properties of a participating team in the tournament.
 * 
 */
public class Player implements Comparable<Player> {
    String name;  //name of the team
    int seed;     //the seed value which gives the team positions before the tournament starts to make games
    int rank;     //rank of the team in the tournament
    
    /*
     * constructor
     */
    public Player(String name, int seed) {
        this.name = name;
        this.seed = seed;
    }
    
    @Override
    public int compareTo(Player o) {
        return seed - o.seed;
    }
}
