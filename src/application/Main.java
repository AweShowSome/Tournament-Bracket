package application;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

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
        List<String> players;	//list of all teams participating in the tournament.
        
        //reading the file to get all teams
        try {
            players = Files.readAllLines(Paths.get(args[0]));
            List<String> act = new ArrayList<String>();
            for (String team : players) {
                if (!team.trim().isEmpty())
                    act.add(team);
            }
            br.parse(act);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
        .args.launching the applications
        launch(args);
    }
    
}
