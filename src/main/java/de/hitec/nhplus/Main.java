package de.hitec.nhplus;

import de.hitec.nhplus.controller.SceneController;

import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {


    @Override
    public void start(Stage primaryStage) {
        SceneController sceneController = new SceneController(primaryStage);
        sceneController.init();
    }

    public static void main(String[] args) {
        launch(args);
    }
}