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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

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
 * Runner of the program
 * 
 */
public class Main extends Application {
    
    public static Bracket br = new Bracket();
    
    @Override
    public void start(Stage primaryStage) {
    	
        primaryStage.setTitle("Tournement Generator");
        
        // Set Scene
        Scene scene = new Scene(br.bracket.scroll, 1080, 720);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        List<String> players;
        // reading the files to get all teams
        try {
            players = Files.readAllLines(Paths.get(args[0]));
            List<String> act = new ArrayList<String>();
            for (String team : players) { // Remove all unnecessary lines
                if (!team.trim().isEmpty())
                    act.add(team);
            }
            br.parse(act);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
       //launching the applications
        launch(args);
    }
    
}
