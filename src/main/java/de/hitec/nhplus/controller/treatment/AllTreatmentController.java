package de.hitec.nhplus.controller.treatment;

import de.hitec.nhplus.Main;
import de.hitec.nhplus.datastorage.CaregiverDao;
import de.hitec.nhplus.datastorage.DaoFactory;
import de.hitec.nhplus.datastorage.PatientDao;
import de.hitec.nhplus.datastorage.TreatmentDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import de.hitec.nhplus.model.Caregiver;
import de.hitec.nhplus.model.Patient;
import de.hitec.nhplus.model.Treatment;
import javafx.collections.transformation.SortedList;
import javafx.collections.transformation.FilteredList;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;
import java.time.LocalTime;


public class AllTreatmentController {

    @FXML
    private TableView<Treatment> tableView;

    @FXML
    private TableColumn<Treatment, Integer> columnId;

    @FXML
    private TableColumn<Treatment, Integer> columnPid;

    @FXML
    private TableColumn<Treatment, Integer> columnCid;

    @FXML
    private TableColumn<Treatment, String> columnDate;

    @FXML
    private TableColumn<Treatment, String> columnBegin;

    @FXML
    private TableColumn<Treatment, String> columnEnd;

    @FXML
    private TableColumn<Treatment, String> columnDescription;

    @FXML
    private ComboBox<String> comboBoxPatientSelection;
    
    @FXML
    private ComboBox<String> comboBoxCaregiverSelection;

    @FXML
    private ComboBox<String> comboBoxTreatmentSelection;

    @FXML
    private Button buttonDelete;

    @FXML
    private TextField searchField;

    private final ObservableList<Treatment> treatments = FXCollections.observableArrayList();
    private TreatmentDao dao;
    private final ObservableList<String> patientSelection = FXCollections.observableArrayList();
    private final ObservableList<String> caregiverSelection = FXCollections.observableArrayList();
    private ArrayList<Patient> patientList;
    private ArrayList<Caregiver> caregiverList;
    private FilteredList<Treatment> filteredData;
    private final ObservableList<String> treatmentSelection = FXCollections.observableArrayList();
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");



    public void initialize() {
        initializeTableView();
        initializeSearch();
        comboBoxPatientSelection.setItems(patientSelection);
        comboBoxPatientSelection.getSelectionModel().select(0);
        comboBoxCaregiverSelection.setItems(caregiverSelection);
        comboBoxCaregiverSelection.getSelectionModel().select(0);
        comboBoxTreatmentSelection.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            searchField.clear();
            updateFilter();
        });

        comboBoxTreatmentSelection.setItems(treatmentSelection);
        comboBoxTreatmentSelection.getSelectionModel().select(0);
    }

    public void initializeTableView() {
        readAllAndShowInTableView();

        this.columnId.setCellValueFactory(new PropertyValueFactory<>("tid"));
        this.columnPid.setCellValueFactory(new PropertyValueFactory<>("pid"));
        this.columnCid.setCellValueFactory(new PropertyValueFactory<>("cid"));
        this.columnDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        this.columnBegin.setCellValueFactory(new PropertyValueFactory<>("begin"));
        this.columnEnd.setCellValueFactory(new PropertyValueFactory<>("end"));
        this.columnDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        this.tableView.setItems(this.treatments);

        // Disabling the button to delete treatments as long, as no treatment was selected.
        this.buttonDelete.setDisable(true);
        this.tableView.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldTreatment, newTreatment) ->
                        AllTreatmentController.this.buttonDelete.setDisable(newTreatment == null));

        this.createPatientComboBoxData();
        this.createCaregiverComboBoxData();
        createTreatmentComboBoxData();
    }

    public void initializeSearch() {
        filteredData = new FilteredList<>(treatments, p -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            updateFilter();
        });

        SortedList<Treatment> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());

        tableView.setItems(sortedData);
    }
    private void updateFilter() {
        String newValue = searchField.getText();
        String selectedColumn = getSelectedSearch();
        filteredData.setPredicate(treatment -> {
            if (newValue == null || newValue.isEmpty()) {
                return true;
            }

            String lowerCaseFilter = newValue.toLowerCase();

            switch (selectedColumn) {
                case "ID":
                    return String.valueOf(treatment.getTid()).toLowerCase().contains(lowerCaseFilter);
                case "PatientID":
                    return String.valueOf(treatment.getPid()).toLowerCase().contains(lowerCaseFilter);
                case "PflegerID":
                    return String.valueOf(treatment.getCid()).toLowerCase().contains(lowerCaseFilter);
                case "Beginn":
                    String beginString = treatment.getBegin().format(String.valueOf(TIME_FORMATTER)).toLowerCase();
                    return beginString.contains(lowerCaseFilter);
                case "Ende":
                    String endString = treatment.getEnd().format(String.valueOf(TIME_FORMATTER)).toLowerCase();
                    return endString.contains(lowerCaseFilter);
                case "Datum":
                    String dateOfBirthString = treatment.getDate().format(String.valueOf(DATE_FORMATTER)).toLowerCase();
                    return dateOfBirthString.contains(lowerCaseFilter);
                case "Kurzbeschreibung":
                    return String.valueOf(treatment.getDescription()).toLowerCase().contains(lowerCaseFilter);
                default:
                    return false;
            }
        });
    }

    public void readAllAndShowInTableView() {
        this.treatments.clear();
        comboBoxPatientSelection.getSelectionModel().select(0);
        comboBoxCaregiverSelection.getSelectionModel().select(0);
        this.dao = DaoFactory.getDaoFactory().createTreatmentDao();
        try {
            this.treatments.addAll(dao.readAll());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    private void createPatientComboBoxData() {
        PatientDao dao = DaoFactory.getDaoFactory().createPatientDAO();
        try {
            patientList = (ArrayList<Patient>) dao.readAll();
            this.patientSelection.add("Patienten auswählen...");
            for (Patient patient: patientList) {
                this.patientSelection.add(patient.getSurname());
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    private void createCaregiverComboBoxData() {
        CaregiverDao cDao = DaoFactory.getDaoFactory().createCaregiverDao();
        try {
            caregiverList = (ArrayList<Caregiver>) cDao.readAll();
            this.caregiverSelection.add("Pfleger auswählen...");
            for (Caregiver caregiver: caregiverList) {
                this.caregiverSelection.add(caregiver.getSurname());
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @FXML
    public void handlePatientComboBox() {
        String selectedPatient = this.comboBoxPatientSelection.getSelectionModel().getSelectedItem();
        this.treatments.clear();
        this.dao = DaoFactory.getDaoFactory().createTreatmentDao();

        if (selectedPatient.equals("alle")) {
            try {
                this.treatments.addAll(this.dao.readAll());
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }

        Patient patient = searchPatientInList(selectedPatient);
        if (patient !=null) {
            try {
                this.treatments.addAll(this.dao.readTreatmentsByPid(patient.getPid()));
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
    }

    @FXML
    public void handleCaregiverComboBox() {
        // String selectedCaregiver = this.comboBoxCaregiverSelection.getSelectionModel().getSelectedItem();
        // this.treatments.clear();
        // this.dao = DaoFactory.getDaoFactory().createTreatmentDao();

        // if (selectedCaregiver.equals("alle")) {
        //     try {
        //         this.treatments.addAll(this.dao.readAll());
        //     } catch (SQLException exception) {
        //         exception.printStackTrace();
        //     }
        // }

        // Caregiver caregiver = searchCaregiverInList(selectedCaregiver);
        // if (caregiver !=null) {
        //     try {
        //         this.treatments.addAll(this.dao.readTreatmentsByPid(caregiver.getCid()));
        //     } catch (SQLException exception) {
        //         exception.printStackTrace();
        //     }
        // }
    }


    private Caregiver searchCaregiverInList(String surname) {
        for (Caregiver caregiver : this.caregiverList) {
            if (caregiver.getSurname().equals(surname)) {
                return caregiver;
            }
        }
        return null;
    }


    private Patient searchPatientInList(String surname) {
        for (Patient patient : this.patientList) {
            if (patient.getSurname().equals(surname)) {
                return patient;
            }
        }
        return null;
    }

    @FXML
    private void createTreatmentComboBoxData() {
        if (treatmentSelection.isEmpty()) {
            treatmentSelection.add("ID");
            treatmentSelection.add("PatientID");
            treatmentSelection.add("PflegerID");
            treatmentSelection.add("Datum");
            treatmentSelection.add("Beginn");
            treatmentSelection.add("Ende");
            treatmentSelection.add("Kurzbeschreibung");
            comboBoxPatientSelection.setItems(treatmentSelection);
        }
    }
    private String getSelectedSearch() {
        return this.comboBoxTreatmentSelection.getSelectionModel().getSelectedItem();
    }



    @FXML
    public void handleDelete() {
        int index = this.tableView.getSelectionModel().getSelectedIndex();
        Treatment t = this.treatments.remove(index);
        TreatmentDao dao = DaoFactory.getDaoFactory().createTreatmentDao();
        try {
            dao.deleteById(t.getTid());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @FXML
    public void handleNewTreatment() {
        try{
            String selectedPatient = this.comboBoxPatientSelection.getSelectionModel().getSelectedItem();
            Patient patient = searchPatientInList(selectedPatient);
            String selectedCaregiver = this.comboBoxCaregiverSelection.getSelectionModel().getSelectedItem();
            Caregiver caregiver = searchCaregiverInList(selectedCaregiver);
            newTreatmentWindow(patient, caregiver);
        } catch (NullPointerException exception){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("Patient für die Behandlung fehlt!");
            alert.setContentText("Wählen Sie über die Combobox einen Patienten aus!");
            alert.showAndWait();
        }
    }

    @FXML
    public void handleMouseClick() {
        tableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && (tableView.getSelectionModel().getSelectedItem() != null)) {
                int index = this.tableView.getSelectionModel().getSelectedIndex();
                Treatment treatment = this.treatments.get(index);
                treatmentWindow(treatment);
            }
        });
    }

    public void newTreatmentWindow(Patient patient, Caregiver caregiver) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/de/hitec/nhplus/views/treatment/NewTreatmentView.fxml"));
            AnchorPane pane = loader.load();
            Scene scene = new Scene(pane);

            // the primary stage should stay in the background
            Stage stage = new Stage();

            NewTreatmentController controller = loader.getController();
            controller.initialize(this, stage, patient, caregiver);

            stage.setScene(scene);
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @FXML
    public void handleSearch() {

    }

    public void treatmentWindow(Treatment treatment){
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/de/hitec/nhplus/views/treatment/TreatmentView.fxml"));
            AnchorPane pane = loader.load();
            Scene scene = new Scene(pane);

            // the primary stage should stay in the background
            Stage stage = new Stage();
            TreatmentController controller = loader.getController();
            controller.initializeController(this, stage, treatment);

            stage.setScene(scene);
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
