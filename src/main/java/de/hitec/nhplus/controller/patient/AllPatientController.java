package de.hitec.nhplus.controller.patient;

import de.hitec.nhplus.Main;
import de.hitec.nhplus.datastorage.DaoFactory;
import de.hitec.nhplus.datastorage.PatientDao;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import de.hitec.nhplus.model.Patient;
import de.hitec.nhplus.utils.DateConverter;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

/**
 * The <code>AllPatientController</code> contains the entire logic of the
 * patient view. It determines which data is displayed and how to react to
 * events.
 */
public class AllPatientController {

    @FXML
    private TableView<Patient> tableView;

    @FXML
    private TableColumn<Patient, Integer> columnId;

    @FXML
    private TableColumn<Patient, String> columnFirstName;

    @FXML
    private TableColumn<Patient, String> columnSurname;

    @FXML
    private TableColumn<Patient, String> columnDateOfBirth;

    @FXML
    private TableColumn<Patient, String> columnCareLevel;

    @FXML
    private TableColumn<Patient, String> columnRoomNumber;

    @FXML
    private TableColumn<Patient, Boolean> columnIsBlocked;

    @FXML
    private Button buttonDelete;

    @FXML
    private Button buttonAdd;

    @FXML
    private Button buttonBlock;

    @FXML
    private TextField textFieldSurname;

    @FXML
    private TextField textFieldFirstName;

    @FXML
    private TextField textFieldDateOfBirth;

    @FXML
    private TextField textFieldCareLevel;

    @FXML
    private TextField textFieldRoomNumber;

    @FXML
    private CheckBox checkboxIsBlocked;

    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<String> comboBoxPatientSelection;

    @FXML
    private CheckBox checkboxShowBlockedOnly;

    private final ObservableList<Patient> patients = FXCollections.observableArrayList();
    private PatientDao dao;
    private FilteredList<Patient> filteredData;
    private final ObservableList<String> patientSelection = FXCollections.observableArrayList();


    public void initialize() {
        initializeTableView();
        initializeSearch();

        comboBoxPatientSelection.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            searchField.clear();
            updateFilter();
        });

        comboBoxPatientSelection.setItems(patientSelection);
        comboBoxPatientSelection.getSelectionModel().select(0);
    }

    /**
     * When <code>initialize()</code> gets called, all fields are already
     * initialized. For example from the FXMLLoader
     * after loading an FXML-File. At this point of the lifecycle of the Controller,
     * the fields can be accessed and
     * configured.
     */
    public void initializeTableView() {
        this.readAllAndShowInTableView();

        this.columnId.setCellValueFactory(new PropertyValueFactory<>("pid"));

        // CellValueFactory to show property values in TableView
        this.columnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        // CellFactory to write property values from with in the TableView
        this.columnFirstName.setCellFactory(TextFieldTableCell.forTableColumn());

        this.columnSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        this.columnSurname.setCellFactory(TextFieldTableCell.forTableColumn());

        this.columnDateOfBirth.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        this.columnDateOfBirth.setCellFactory(TextFieldTableCell.forTableColumn());

        this.columnCareLevel.setCellValueFactory(new PropertyValueFactory<>("careLevel"));
        this.columnCareLevel.setCellFactory(TextFieldTableCell.forTableColumn());

        this.columnRoomNumber.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        this.columnRoomNumber.setCellFactory(TextFieldTableCell.forTableColumn());

        this.columnIsBlocked.setCellValueFactory(new PropertyValueFactory<>("isBlocked"));
        this.columnIsBlocked.setCellFactory(CheckBoxTableCell.forTableColumn(this.columnIsBlocked));

        // Anzeigen der Daten
        this.tableView.setItems(this.patients);

        this.buttonDelete.setDisable(true);
        this.buttonBlock.setDisable(true);
        this.tableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Patient>() {
            @Override
            public void changed(ObservableValue<? extends Patient> observableValue, Patient oldPatient,
                    Patient newPatient) {
                AllPatientController.this.buttonDelete.setDisable(newPatient == null);
                AllPatientController.this.buttonBlock.setDisable(newPatient == null);
            }
        });
        createPatientComboBoxData();
    }
    public void initializeSearch() {
        filteredData = new FilteredList<>(patients, p -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            updateFilter();
        });

        SortedList<Patient> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());

        tableView.setItems(sortedData);
    }
    private void updateFilter() {
        String newValue = searchField.getText();
        String selectedColumn = getSelectedSearch();
        filteredData.setPredicate(patient -> {
            if (newValue == null || newValue.isEmpty()) {
                return true;
            }

            String lowerCaseFilter = newValue.toLowerCase();

            switch (selectedColumn) {
                case "ID":
                    return String.valueOf(patient.getPid()).toLowerCase().contains(lowerCaseFilter);
                case "Vorname":
                    return patient.getFirstName().toLowerCase().contains(lowerCaseFilter);
                case "Nachname":
                    return patient.getSurname().toLowerCase().contains(lowerCaseFilter);
                case "Pflegegrad":
                    return patient.getCareLevel().toLowerCase().contains(lowerCaseFilter);
                case "Raum":
                    return patient.getRoomNumber().toLowerCase().contains(lowerCaseFilter);
                case "Geburtstag":
                    return patient.getDateOfBirth().toLowerCase().contains(lowerCaseFilter);
                default:
                    return false;
            }
        });
    }

    @FXML
    public void handleToggleShowBlockedOnly(ActionEvent event) {
        if (event.getSource() instanceof CheckBox) {
            CheckBox cb = (CheckBox) event.getSource();
            if (cb.isSelected()) {
                FilteredList<Patient> filteredPatients = patients.filtered(patient -> (patient.isBlocked() == true));
                this.tableView.setItems(filteredPatients);
            } else {
                this.tableView.setItems(patients);
            }
        }
    }

    /**
     * When a cell of the column with first names was changed, this method will be
     * called, to persist the change.
     *
     * @param event Event including the changed object and the change.
     */
    @FXML
    public void handleOnEditFirstname(TableColumn.CellEditEvent<Patient, String> event) {
        event.getRowValue().setFirstName(event.getNewValue());
        this.doUpdate(event);
    }

    /**
     * When a cell of the column with surnames was changed, this method will be
     * called, to persist the change.
     *
     * @param event Event including the changed object and the change.
     */
    @FXML
    public void handleOnEditSurname(TableColumn.CellEditEvent<Patient, String> event) {
        event.getRowValue().setSurname(event.getNewValue());
        this.doUpdate(event);
    }

    /**
     * When a cell of the column with dates of birth was changed, this method will
     * be called, to persist the change.
     *
     * @param event Event including the changed object and the change.
     */
    @FXML
    public void handleOnEditDateOfBirth(TableColumn.CellEditEvent<Patient, String> event) {
        event.getRowValue().setDateOfBirth(event.getNewValue());
        this.doUpdate(event);
    }

    /**
     * When a cell of the column with care levels was changed, this method will be
     * called, to persist the change.
     *
     * @param event Event including the changed object and the change.
     */
    @FXML
    public void handleOnEditCareLevel(TableColumn.CellEditEvent<Patient, String> event) {
        event.getRowValue().setCareLevel(event.getNewValue());
        this.doUpdate(event);
    }

    /**
     * When a cell of the column with room numbers was changed, this method will be
     * called, to persist the change.
     *
     * @param event Event including the changed object and the change.
     */
    @FXML
    public void handleOnEditRoomNumber(TableColumn.CellEditEvent<Patient, String> event) {
        event.getRowValue().setRoomNumber(event.getNewValue());
        this.doUpdate(event);
    }

    /**
     * This method handles events fired by the button to delete patients. It calls
     * {@link PatientDao} to delete the
     * patient from the database and removes the object from the list, which is the
     * data source of the
     * <code>TableView</code>.
     */
    @FXML
    public void handleBlock() {
        Patient selectedItem = this.tableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            try {
                selectedItem.setIsBlocked(!selectedItem.isBlocked());
                this.dao.update(selectedItem);
                readAllAndShowInTableView();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
    }

    /**
     * Updates a patient by calling the method <code>update()</code> of
     * {@link PatientDao}.
     *
     * @param event Event including the changed object and the change.
     */
    private void doUpdate(TableColumn.CellEditEvent<Patient, String> event) {
        try {
            this.dao.update(event.getRowValue());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Reloads all patients to the table by clearing the list of all patients and
     * filling it again by all persisted
     * patients, delivered by {@link PatientDao}.
     */
    private void readAllAndShowInTableView() {
        this.patients.clear();
        this.dao = DaoFactory.getDaoFactory().createPatientDAO();
        try {
            this.patients.addAll(this.dao.readAll());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @FXML
    private void createPatientComboBoxData() {
        if (patientSelection.isEmpty()) {
            patientSelection.add("ID");
            patientSelection.add("Nachname");
            patientSelection.add("Vorname");
            patientSelection.add("Geburtstag");
            patientSelection.add("Pflegegrad");
            patientSelection.add("Raum");
            comboBoxPatientSelection.setItems(patientSelection);
        }
    }
    private String getSelectedSearch() {
        return this.comboBoxPatientSelection.getSelectionModel().getSelectedItem();
    }


    /**
     * This method handles events fired by the button to delete patients. It calls
     * {@link PatientDao} to delete the
     * patient from the database and removes the object from the list, which is the
     * data source of the
     * <code>TableView</code>.
     */
    @FXML
    public void handleDelete() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Warnung!");
        alert.setHeaderText("Sind Sie sicher? (Patient löschen)");
        alert.setContentText("Dieser Vorgang lässt sich NICHT mehr rückgängig machen.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Patient selectedItem = this.tableView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                try {
                    DaoFactory.getDaoFactory().createPatientDAO().deleteById(selectedItem.getPid());
                    this.tableView.getItems().remove(selectedItem);
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            }
        }
    }

    @FXML
    public void handleAddNewPatientButton() {
        newPatientWindow();
    }

    public void newPatientWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    Main.class.getResource("/de/hitec/nhplus/views/patient/NewPatientView.fxml"));
            AnchorPane pane = loader.load();
            Scene scene = new Scene(pane);

            // the primary stage should stay in the background
            Stage stage = new Stage();

            NewPatientController controller = loader.getController();
            controller.initialize(stage);

            stage.setScene(scene);
            stage.centerOnScreen();
            stage.setResizable(false);
            stage.showAndWait();
            readAllAndShowInTableView();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * This method handles the events fired by the button to add a patient. It
     * collects the data from the
     * <code>TextField</code>s, creates an object of class <code>Patient</code> of
     * it and passes the object to
     * {@link PatientDao} to persist the data.
     */
    @FXML
    public void handleAdd() {
        String surname = this.textFieldSurname.getText();
        String firstName = this.textFieldFirstName.getText();
        String birthday = this.textFieldDateOfBirth.getText();
        LocalDate date = DateConverter.convertStringToLocalDate(birthday);
        String careLevel = this.textFieldCareLevel.getText();
        String roomNumber = this.textFieldRoomNumber.getText();
        Patient patient = new Patient(firstName, surname, date, careLevel, roomNumber, false);
        try {
            this.dao.create(patient);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        readAllAndShowInTableView();
        clearTextfields();
    }
    @FXML
    public void handleSearch() {

    }

    /**
     * Clears all contents from all <code>TextField</code>s.
     */
    private void clearTextfields() {
        this.textFieldFirstName.clear();
        this.textFieldSurname.clear();
        this.textFieldDateOfBirth.clear();
        this.textFieldCareLevel.clear();
        this.textFieldRoomNumber.clear();
    }
}
