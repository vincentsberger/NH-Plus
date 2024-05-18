package de.hitec.nhplus.controller;

import java.net.URL;

import de.hitec.nhplus.datastorage.ConnectionBuilder;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public final class SceneController {

    private static SceneController INSTANCE;

    private Stage stage;

    private SceneController() {
    }

    public static SceneController getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SceneController();
        }

        return INSTANCE;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setScene(Scene scene) {
        this.stage.setScene(scene);
    }

    /**
     * Returns a Scene from a given FXML filename, e.g. "LoginScene.fxml".
     * @param fxmlFile
     * @return Scene|null 
     */
    public Scene getSceneFromResource(String fxmlFile) {
        try {
            URL pathToFxmlFile = this.getClass().getResource("/de/hitec/nhplus/scenes/" + fxmlFile);
            BorderPane borderPane = FXMLLoader.load(pathToFxmlFile);
            Scene scene = new Scene(borderPane);
            return scene;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Initialises the scene
     * @return void
     */
    public void init() {
        this.stage.setTitle("NH-Plus - Willkommen");
        Scene initScene = getSceneFromResource("LoginScene.fxml");
        this.stage.setScene(initScene);
        this.stage.setResizable(false);
        this.stage.show();
        this.stage.setOnCloseRequest(event -> {
            ConnectionBuilder.closeConnection();
            Platform.exit();
            System.exit(0);
        });
    }
    public Scene getScene() {
        return this.stage.getScene();
    }

    public Stage getStage() {
        return this.stage;
    }
}
