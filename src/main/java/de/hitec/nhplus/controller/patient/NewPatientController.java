package de.hitec.nhplus.controller.patient;

import java.sql.SQLException;
import java.time.LocalDate;

import de.hitec.nhplus.datastorage.DaoFactory;
import de.hitec.nhplus.datastorage.PatientDao;
import de.hitec.nhplus.model.Patient;
import de.hitec.nhplus.utils.DateConverter;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class NewPatientController {

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
    private DatePicker datePicker;

    @FXML
    private TextField textFieldCareLevel;

    @FXML
    private TextField textFieldRoomNumber;

    @FXML
    private SimpleBooleanProperty isBlocked;

    @FXML
    private Button buttonAdd;

    private Stage stage;

    public void initialize(Stage stage) {
        this.stage = stage;

        this.buttonAdd.setDisable(true);
        ChangeListener<String> inputNewPatientListener = (observableValue, oldText,
                newText) -> NewPatientController.this.buttonAdd
                        .setDisable(!NewPatientController.this.areInputDataValid());
        this.textFieldFirstName.textProperty().addListener(inputNewPatientListener);
        this.textFieldSurname.textProperty().addListener(inputNewPatientListener);
        this.textFieldCareLevel.textProperty().addListener(inputNewPatientListener);
        this.textFieldRoomNumber.textProperty().addListener(inputNewPatientListener);
        this.datePicker.valueProperty()
                .addListener((observableValue, localDate, t1) -> NewPatientController.this.buttonAdd
                        .setDisable(!NewPatientController.this.areInputDataValid()));
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
        String careLevel = this.textFieldCareLevel.getText();
        String roomnumber = this.textFieldRoomNumber.getText();
        Boolean isBlocked = false;
        Patient newPatient = new Patient(firstName, surname, dateOfBirth, careLevel, roomnumber, isBlocked);
        createPatient(newPatient);
        clearTextfields();
        stage.close();
    }

    private void createPatient(Patient patient) {
        PatientDao dao = DaoFactory.getDaoFactory().createPatientDAO();
        try {
            dao.create(patient);
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
        this.textFieldCareLevel.clear();
        this.textFieldRoomNumber.clear();
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
                !this.datePicker.toString().isEmpty() && !this.textFieldCareLevel.getText().isBlank() && !this.textFieldRoomNumber.getText().isBlank();
    }
}