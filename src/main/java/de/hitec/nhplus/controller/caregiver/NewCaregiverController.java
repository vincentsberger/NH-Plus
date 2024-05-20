package de.hitec.nhplus.controller.caregiver;

import java.sql.SQLException;
import java.time.LocalDate;

import de.hitec.nhplus.datastorage.CaregiverDao;
import de.hitec.nhplus.datastorage.DaoFactory;
import de.hitec.nhplus.model.Caregiver;
import de.hitec.nhplus.utils.DateConverter;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class NewCaregiverController {

    @FXML
    private Label labelFirstName;

    @FXML
    private Label labelSurname;

    @FXML
    private Label labelPflegerFullName;

    @FXML
    private Label labelPflegerTelephone;

    @FXML
    private Label labelPflegerId;

    @FXML
    private Label labelPatientId;

    @FXML
    private TextField textFieldFirstName;

    @FXML
    private TextField textFieldSurname;

    @FXML
    private TextField textFieldTelephone;
    
    @FXML
    private TextField textFieldUsername;

    @FXML
    private TextField textFieldPassword;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Button buttonAdd;

    private Stage stage;

    public void initialize(Stage stage) {
        this.stage = stage;

        this.buttonAdd.setDisable(true);
        ChangeListener<String> inputNewCaregiverListener = (observableValue, oldText,
                newText) -> NewCaregiverController.this.buttonAdd
                        .setDisable(!NewCaregiverController.this.areInputDataValid());
        this.textFieldFirstName.textProperty().addListener(inputNewCaregiverListener);
        this.textFieldSurname.textProperty().addListener(inputNewCaregiverListener);
        this.textFieldUsername.textProperty().addListener(inputNewCaregiverListener);
        this.textFieldPassword.textProperty().addListener(inputNewCaregiverListener);
        this.datePicker.valueProperty()
                .addListener((observableValue, localDate, t1) -> NewCaregiverController.this.buttonAdd
                        .setDisable(!NewCaregiverController.this.areInputDataValid()));
        this.datePicker.setConverter(new StringConverter<>() {
            @Override
            public String toString(LocalDate localDate) {
                return (localDate == null) ? "" : DateConverter.convertLocalDateToString(localDate);
            }

            @Override
            public LocalDate fromString(String localDate) {
                return DateConverter.convertStringToLocalDate(localDate);
            }
        });
    }

    @FXML
    public void handleAdd() {
        
        String surname = this.textFieldSurname.getText();
        String firstName = this.textFieldFirstName.getText();
        LocalDate dateOfBirth = this.datePicker.getValue();
        String telephone = this.textFieldTelephone.getText();
        String username = this.textFieldUsername.getText();
        String password = this.textFieldPassword.getText();
        Caregiver newCaregiver = new Caregiver(firstName, surname, dateOfBirth, telephone, username, password, false);
        createCaregiver(newCaregiver);
        clearTextfields();
        stage.close();
    }

    private void createCaregiver(Caregiver caregiver) {
        CaregiverDao dao = DaoFactory.getDaoFactory().createCaregiverDao();
        try {
            dao.create(caregiver);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

        /**
     * Clears all contents from all <code>TextField</code>s.
     */
    private void clearTextfields() {
        this.textFieldFirstName.clear();
        this.textFieldSurname.clear();
        this.datePicker.setValue(null);
        this.textFieldTelephone.clear();
    }

    @FXML
    public void handleCancel() {
        stage.close();
    }

    private boolean areInputDataValid() {
        if (this.datePicker.toString().isEmpty()) {
            try {
                DateConverter.convertStringToLocalDate(this.datePicker.toString());
            } catch (Exception exception) {
                return false;
            }
        }

        return !this.textFieldFirstName.getText().isBlank() && !this.textFieldSurname.getText().isBlank() &&
                !this.datePicker.toString().isEmpty() && !this.textFieldTelephone.getText().isBlank();
    }
}