package application;

import java.util.ArrayList;

/**
 * Test class for debugging purposes
 * 
 * @author andreweng
 *
 */
public class TestMain {
    
    public static void main(String[] args) {
        Bracket b = new Bracket();
        
        String[] rounds = { "Grand Finals", "Winner's Semi's", "Winner's Quarters", "Winner's Eighths", "Top 32", "Top 64", "Top 128" };
        ArrayList<String> teams = new ArrayList<String>();
        
        // 33 Teams
        
        teams.add("Team 1");
        teams.add("Team 2");
        teams.add("Team 3");
        teams.add("Team 4");
        teams.add("Team 5");
        teams.add("Team 6");
        teams.add("Team 7");
        teams.add("Team 8");
        teams.add("Team 9");
        teams.add("Team 10");
        teams.add("Team 11");
        teams.add("Team 12");
        teams.add("Team 13");
        teams.add("Team 14");
        teams.add("Team 15");
        teams.add("Team 16");
        teams.add("Team 17");
        teams.add("Team 18");
        teams.add("Team 19");
        teams.add("Team 20");
        teams.add("Team 21");
        teams.add("Team 22");
        teams.add("Team 23");
        teams.add("Team 24");
        teams.add("Team 25");
        teams.add("Team 26");
        teams.add("Team 27");
        teams.add("Team 28");
        teams.add("Team 29");
        teams.add("Team 30");
        teams.add("Team 31");
        teams.add("Team 32");
        // teams.add("team 33");
        
        b.parse(teams);
        b.generateBracket();
        
        b.getGameStatuses();
        
    }
    
}
