package de.hitec.nhplus.controller.scenes;

import de.hitec.nhplus.controller.SceneController;
import de.hitec.nhplus.utils.AuthentificationHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
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
    private Text errorMessageText;

    @FXML
    public void handleSubmitButtonAction(ActionEvent event) {
        AuthentificationHelper authHelper = new AuthentificationHelper();
        String usernameInput = (String)usernameField.getText();
        String passwordInput = (String)passwordField.getText();
        if(!authHelper.isValidLogin(usernameInput, passwordInput)) {
            badLoginCounter++;
            usernameField.setText("");
            passwordField.setText("");
            errorMessageText.setText("Fehler: Falscher Benutzername und/oder falsches Passwort! Anzahl Fehlversuche: " + badLoginCounter);
            errorMessageText.setVisible(true);
            usernameField.requestFocus();
            return;
        }
        SceneController controller = SceneController.getInstance();
        Scene nextScene = controller.getSceneFromResource("DashboardScene.fxml");
        controller.setScene(nextScene);
    }

}
