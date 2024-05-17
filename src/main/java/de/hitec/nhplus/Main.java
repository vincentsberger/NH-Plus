package de.hitec.nhplus;

import de.hitec.nhplus.controller.SceneController;

import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {


    @Override
    public void start(Stage primaryStage) {
        SceneController controller = SceneController.getInstance();
        controller.setStage(primaryStage);
        controller.init();
    }

    public static void main(String[] args) {
        launch(args);
    }
}