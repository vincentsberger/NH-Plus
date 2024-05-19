package de.hitec.nhplus.controller.scenes;

import de.hitec.nhplus.controller.SceneController;
import de.hitec.nhplus.utils.AuthentificationHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;


public class LoginSceneController {

    /** count bad logins */
    private int badLoginCounter = 0;


    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Text badLoginMessageText;
    @FXML
    private Text badLoginCounterText;

    @FXML
    public void handleSubmitButtonAction(ActionEvent event) {
        AuthentificationHelper authHelper = new AuthentificationHelper();
        String usernameInput = (String)usernameField.getText();
        String passwordInput = (String)passwordField.getText();
        if(!authHelper.isValidLogin(usernameInput, passwordInput)) {
            badLoginCounter++;
            usernameField.setText("");
            passwordField.setText("");
            badLoginMessageText.setText("Fehler: Falscher Benutzername und / oder falsches Passwort!");
            badLoginCounterText.setText("Anzahl Fehlversuche: " + badLoginCounter);
            badLoginMessageText.setVisible(true);
            badLoginCounterText.setVisible(true);
            usernameField.requestFocus();
            return;
        }
        SceneController sceneController = SceneController.getInstance();
        sceneController.setUser(usernameInput);
        sceneController.getStage().setTitle("NH-Plus - Digitale Pflegeheim-Verwaltung - Übersicht");
        sceneController.switchToMainScene();
    }

}
