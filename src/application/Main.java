package application;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            BorderPane root = new BorderPane();
            root.setPadding(new Insets(20, 20, 20, 20));
            Scene scene = new Scene(root, 1080, 720);
            root.setStyle("-fx-background-color: linear-gradient(from 0% 15% to 0% 105%, #FFFFFF, #c0c0c0, #ffd700)");
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            
            Label Title = new Label("Tourney Generator 0.1ß");
            Title.setFont(new Font("Comic Sans MS Bold", 72.0));
            
            VBox vbox = new VBox();
            
            final Button fileButton = new Button("Import Text File");
            
            fileButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    System.out.println("Hello World!");
                }
            });
            
            fileButton.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    fileButton.setEffect(new DropShadow());
                }
            });
            
            fileButton.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    fileButton.setEffect(null);
                }
            });
            
            // Set Window widgets
            root.setTop(Title);
            BorderPane.setAlignment(Title, Pos.TOP_CENTER);
            root.setBottom(fileButton);
            BorderPane.setAlignment(fileButton, Pos.TOP_CENTER);
            
            // Sets initial window
            primaryStage.setScene(scene);
            primaryStage.setTitle("Tourney Generator 0.1ß");
            primaryStage.show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
