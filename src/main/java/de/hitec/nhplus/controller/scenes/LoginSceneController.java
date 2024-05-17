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

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Text errorMessageText;

    @FXML
    public void handleSubmitLoginButtonAction(ActionEvent event) {
        AuthentificationHelper authHelper = new AuthentificationHelper();
        String usernameInput = (String)usernameField.getText();
        String passwordInput = (String)passwordField.getText();
        if(!authHelper.isValidLogin(usernameInput, passwordInput)) {
            usernameField.setText("");
            passwordField.setText("");
            errorMessageText.setVisible(true);
            System.out.println("test");
            return;
        }
        SceneController controller = SceneController.getInstance();
        Scene nextScene = controller.getSceneFromResource("DashboardScene.fxml");
        controller.setScene(nextScene);
    }
}
