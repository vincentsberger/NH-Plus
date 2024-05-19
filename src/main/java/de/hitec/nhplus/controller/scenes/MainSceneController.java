package de.hitec.nhplus.controller.scenes;

import de.hitec.nhplus.Main;
import de.hitec.nhplus.controller.SceneController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

import java.io.IOException;

public class MainSceneController {

    private SceneController sceneController;

    @FXML
    private BorderPane mainSceneBorderPane;
    @FXML
    private Text userInfoText;

    public void initialize(String user) {
        this.userInfoText.setText(user);
        this.sceneController = SceneController.getInstance();
    }

    @FXML
    private void handleShowAllPatient(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(
                Main.class.getResource("/de/hitec/nhplus/views/patient/AllPatientView.fxml"));
        try {
            mainSceneBorderPane.setCenter(loader.load());
            this.sceneController.getStage().setTitle("NH-Plus - Digitale Pflegeheim-Verwaltung - Patient/innen");
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @FXML
    private void handleShowAllTreatments(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(
                Main.class.getResource("/de/hitec/nhplus/views/treatment/AllTreatmentView.fxml"));
        try {
            mainSceneBorderPane.setCenter(loader.load());
            this.sceneController.getStage().setTitle("NH-Plus - Digitale Pflegeheim-Verwaltung - Behandlungen");
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @FXML
    private void handleShowAllCaregiver(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(
                Main.class.getResource("/de/hitec/nhplus/views/caregiver/AllCaregiverView.fxml"));
        try {
            mainSceneBorderPane.setCenter(loader.load());
            this.sceneController.getStage().setTitle("NH-Plus - Digitale Pflegeheim-Verwaltung - Pfleger/innen");
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
