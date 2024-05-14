package de.hitec.nhplus;

import de.hitec.nhplus.datastorage.ConnectionBuilder;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private Stage mainStage;
    private Stage authStage;
    private Boolean isLoggedIn = false;

    @Override
    public void start(Stage primaryStage) {
        this.authStage = authStage;
        this.mainStage = mainStage;
        mainWindow();
    }

    public void mainWindow() {
        try {
            if (!isLoggedIn) {
                FXMLLoader authWindowloader = new FXMLLoader(Main.class.getResource("de/hitec/nhplus/UserAuthentificationView"));
                BorderPane authWindowPane = authWindowloader.load();

                Scene scene = new Scene(authWindowPane);
                this.authStage.setTitle("NH-Plus - Anmelden");
                this.authStage.setScene(scene);
                this.authStage.setResizable(true);
                this.authStage.show();

                this.authStage.setOnCloseRequest(event -> {
                    ConnectionBuilder.closeConnection();
                    Platform.exit();
                    System.exit(0);
                });
            } else {

                FXMLLoader loader = new FXMLLoader(Main.class.getResource("/de/hitec/nhplus/MainWindowView.fxml"));
                BorderPane pane = loader.load();

                Scene scene = new Scene(pane);
                this.primaryStage.setTitle("NH-Plus - Deine Praxis-Übersicht");
                this.primaryStage.setScene(scene);
                this.primaryStage.setResizable(true);
                this.primaryStage.show();

                this.primaryStage.setOnCloseRequest(event -> {
                    ConnectionBuilder.closeConnection();
                    Platform.exit();
                    System.exit(0);
                });
            }
        } catch (

        IOException exception) {
            exception.printStackTrace();
        }
    }

    private void showAuthWindow() {
        
    }

    private void showMainWindow() {
        
    }

    public static void main(String[] args) {
        launch(args);
    }
}