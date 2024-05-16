package de.hitec.nhplus.controller;

import java.io.IOException;

import de.hitec.nhplus.datastorage.ConnectionBuilder;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class SceneController {

    private Stage stage;
    private Scene startScene;
    private String fxmlFile;

    public SceneController(Stage primaryStage) {
        this.stage = primaryStage;
        this.fxmlFile = "LoginScene.fxml";
        this.startScene = getSceneFromResource(this.fxmlFile);
    }

    public Scene getSceneFromResource(String fxmlFile) {
        try {
            BorderPane borderPane = new FXMLLoader(getClass().getResource("/de/hitec/nhplus/scenes/" + fxmlFile)).load();
            Scene startScene = new Scene(borderPane);
            return startScene;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void init() {
            // Scene scene = new Scene(pane);
            this.stage.setTitle("NH-Plus - Verwaltungssoftware");
            this.stage.setScene(this.startScene);
            this.stage.setResizable(false);
            this.stage.show();
    
            this.stage.setOnCloseRequest(event -> {
                ConnectionBuilder.closeConnection();
                Platform.exit();
                System.exit(0);
            });
    }

    public void switchToMainScene() throws IOException {
    }

    public void switchToLoginScene() {
        try {
            FXMLLoader paneLoader = new FXMLLoader(
                    getClass().getResource("/de/hitec/nhplus/AuthWindowBorderPane.fxml"));
            BorderPane pane = paneLoader.load();

            Scene scene = new Scene(pane);
            this.stage.setTitle("NH-Plus - Willkommen!");
            this.stage.setScene(scene);
            this.stage.setResizable(false);
            this.stage.show();

            this.stage.setOnCloseRequest(event -> {
                ConnectionBuilder.closeConnection();
                Platform.exit();
                System.exit(0);
            });
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

}
