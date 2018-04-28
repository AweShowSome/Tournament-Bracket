///////////////////////////////////////////////////////////////////////////////
//                   
// Title:            X4- Tournament Bracket
// Files:            Bracket.java, Game.java, Main.java, Player.java, application.css, teams.txt
//
// Semester:         Spring 2018
//
// Lecturer's Name:  Debra Deppeler CS400
//
////////////////////////////////////////////////////////////////////////////////
package application;

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
 * A player in the tournament.
 * The class holds all of the properties of a participating team
 * Basically a key-value pair.
 * 
 *
 */
public class Player {
    String name; // Player's name
    int seed; // Player's seed
    
    /*
     * constructor
     */
    public Player(String name, int seed) {
        this.name = name;
        this.seed = seed;
    }
}
