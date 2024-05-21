package de.hitec.nhplus.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

import de.hitec.nhplus.Main;
import de.hitec.nhplus.controller.scenes.MainSceneController;
import de.hitec.nhplus.datastorage.ConnectionBuilder;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public final class SceneController {

    private static SceneController INSTANCE;

    private Stage stage;
    private Scene startScene;
    private String user;

    private SceneController() {
    }

    public static SceneController getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SceneController();
        }

        return INSTANCE;
    }

    /**
     * Initialises the main window
     * 
     * @return void
     */
    public void init() {
        this.startScene = this.getViewFromResource("scenes/LoginScene.fxml");
        this.stage.setTitle("NH-Plus - Digitale Pflegeheim-Verwaltung - Login");
        this.stage.setScene(this.startScene);
        this.stage.setResizable(false);
        this.stage.centerOnScreen();
        this.stage.show();
        this.stage.setOnCloseRequest(event -> {
            ConnectionBuilder.closeConnection();
            Platform.exit();
            System.exit(0);
        });
    }

    public void switchToMainScene() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    Main.class.getResource("/de/hitec/nhplus/scenes/MainScene.fxml"));
            BorderPane mainSceneBorderPane = loader.load();
            Scene mainScene = new Scene(mainSceneBorderPane);
            MainSceneController controller = loader.getController();
            controller.initialize(this.getUser());

            this.stage.setScene(mainScene);
            this.stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns a View from a given FXML filename relative to resource path, e.g.
     * "views/treatment/AllTreatmentiew.fxml".
     * 
     * @param fxmlFile
     * @return Scene|null
     */
    public Scene getViewFromResource(String fxmlFile) {
        try {
            URL pathToFxmlFile = this.getClass().getResource("/de/hitec/nhplus/" + fxmlFile);
            BorderPane borderPane = FXMLLoader.load(pathToFxmlFile);
            Scene scene = new Scene(borderPane);
            return scene;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Sets stage for main window
     * 
     * @param stage Stage to be set for the main window (default: primaryStage)
     * @return void
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Sets scene for the current stage
     * 
     * @param scene Scene to be rendered
     * @return void
     */
    public void setScene(Scene scene) {
        this.stage.setScene(scene);
    }

    /**
     * Gets the current scene from the main window stage
     * 
     * @return Scene
     */
    public Scene getScene() {
        return this.stage.getScene();
    }

    /**
     * Gets the stage from the main window
     * 
     * @return Stage
     */
    public Stage getStage() {
        return this.stage;
    }

    /**
     * Gets the user of this session
     * 
     * @return String
     */
    public String getUser() {
        return this.user;
    }

    /**
     * Sets the user for this session
     * 
     * @param user Uer to be set for this session
     */
    public void setUser(String user) {
        this.user = user;
    }
}
