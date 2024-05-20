package de.hitec.nhplus.controller.caregiver;

import de.hitec.nhplus.Main;
import de.hitec.nhplus.datastorage.CaregiverDao;
import de.hitec.nhplus.datastorage.DaoFactory;
import de.hitec.nhplus.model.Caregiver;
import de.hitec.nhplus.model.Patient;
import de.hitec.nhplus.utils.DateConverter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * The <code>AllPatientController</code> contains the entire logic of the
 * patient view. It determines which data is displayed and how to react to
 * events.
 */
public class AllCaregiverController {

    @FXML
    private TableView<Caregiver> tableView;

    @FXML
    private TableColumn<Caregiver, Integer> columnId;

    @FXML
    private TableColumn<Caregiver, String> columnFirstName;

    @FXML
    private TableColumn<Caregiver, String> columnSurname;

    @FXML
    private TableColumn<Caregiver, String> columnDateOfBirth;

    @FXML
    private TableColumn<Caregiver, String> columnTelephone;

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
    private TextField textFieldCID;

    @FXML
    private TextField textFieldTelephone;

    @FXML
    private CheckBox checboxIsBlocked;

    private final ObservableList<Caregiver> caregivers = FXCollections.observableArrayList();
    private CaregiverDao dao;

    /**
     * When <code>initialize()</code> gets called, all fields are already
     * initialized. For example from the FXMLLoader
     * after loading an FXML-File. At this point of the lifecycle of the Controller,
     * the fields can be accessed and
     * configured.
     */
    public void initialize() {
        this.readAllAndShowInTableView();

        this.columnId.setCellValueFactory(new PropertyValueFactory<>("cid"));

        // CellValueFactory to show property values in TableView
        this.columnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        // CellFactory to write property values from with in the TableView
        this.columnFirstName.setCellFactory(TextFieldTableCell.forTableColumn());

        this.columnSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        this.columnSurname.setCellFactory(TextFieldTableCell.forTableColumn());

        this.columnTelephone.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        this.columnTelephone.setCellFactory(TextFieldTableCell.forTableColumn());

        this.columnIsBlocked.setCellValueFactory(new PropertyValueFactory<>("isBlocked"));
        this.columnIsBlocked.setCellFactory(CheckBoxTableCell.forTableColumn(this.columnIsBlocked));

        // Anzeigen der Daten
        this.tableView.setItems(this.caregivers);

        this.buttonDelete.setDisable(true);
        this.tableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Caregiver>() {
            @Override
            public void changed(ObservableValue<? extends Caregiver> observableValue, Caregiver oldCaregiver,
                    Caregiver newCaregiver) {
                ;
                AllCaregiverController.this.buttonDelete.setDisable(newCaregiver == null);
            }
        });

        this.buttonBlock.setDisable(true);
        this.tableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Caregiver>() {
            @Override
            public void changed(ObservableValue<? extends Caregiver> observableValue, Caregiver oldCaregiver,
                    Caregiver newCaregiver) {
                ;
                AllCaregiverController.this.buttonBlock.setDisable(newCaregiver == null);
            }
        });
    }

    /**
     * When a cell of the column with first names was changed, this method will be
     * called, to persist the change.
     *
     * @param event Event including the changed object and the change.
     */
    @FXML
    public void handleOnEditFirstname(TableColumn.CellEditEvent<Caregiver, String> event) {
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
    public void handleOnEditSurname(TableColumn.CellEditEvent<Caregiver, String> event) {
        event.getRowValue().setSurname(event.getNewValue());
        this.doUpdate(event);
    }

    /**
     * When a cell of the column with care levels was changed, this method will be
     * called, to persist the change.
     *
     * @param event Event including the changed object and the change.
     */
    @FXML
    public void handleOnEditTelephone(TableColumn.CellEditEvent<Caregiver, String> event) {
        event.getRowValue().setTelephone(event.getNewValue());
        this.doUpdate(event);
    }

    /**
     * Updates a Caregiver by calling the method <code>update()</code> of
     * {@link CaregiverDao}.
     *
     * @param event Event including the changed object and the change.
     */
    private void doUpdate(TableColumn.CellEditEvent<Caregiver, String> event) {
        try {
            this.dao.update(event.getRowValue());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Reloads all Caregivers to the table by clearing the list of all Caregivers
     * and filling it again by all persisted
     * Caregivers, delivered by {@link CaregiverDao}.
     */
    private void readAllAndShowInTableView() {
        this.caregivers.clear();
        this.dao = DaoFactory.getDaoFactory().createCaregiverDao();
        try {
            this.caregivers.addAll(this.dao.readAll());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * This method handles events fired by the button to delete patients. It calls
     * {@link CaregiverDao} to delete the
     * patient from the database and removes the object from the list, which is the
     * data source of the
     * <code>TableView</code>.
     */
    @FXML
    public void handleDelete() {
        Caregiver selectedItem = this.tableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            try {
                DaoFactory.getDaoFactory().createCaregiverDao().deleteById(selectedItem.getCid());
                this.tableView.getItems().remove(selectedItem);
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
    }

    /**
     * This method handles events fired by the button to delete patients. It calls
     * {@link CaregiverDao} to delete the
     * patient from the database and removes the object from the list, which is the
     * data source of the
     * <code>TableView</code>.
     */
    @FXML
    public void handleBlock() {
        Caregiver selectedItem = this.tableView.getSelectionModel().getSelectedItem();
        selectedItem.setIsBlocked(!selectedItem.isBlocked());
        if (selectedItem != null) {
            try {
                this.dao.update(selectedItem);
                readAllAndShowInTableView();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
    }

    /**
     * This method handles the events fired by the button to add a patient. It
     * collects the data from the
     * <code>TextField</code>s, creates an object of class <code>Patient</code> of
     * it and passes the object to
     * {@link CaregiverDao} to persist the data.
     */
    @FXML
    public void handleAddNewCaregiver() {
        String surname = this.textFieldSurname.getText();
        String firstName = this.textFieldFirstName.getText();
        String birthday = this.textFieldDateOfBirth.getText();
        LocalDate date = DateConverter.convertStringToLocalDate(birthday);
        String telephone = this.textFieldTelephone.getText();
        // String username = this.textFieldUsername.getText();
        // String passswort = this.textFieldPasswort.getText();

        try {
            this.dao.create(new Caregiver(firstName, surname, date, telephone, "user" + firstName,
                    "password" + firstName, false));
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        readAllAndShowInTableView();
        clearTextfields();
    }

    /**
     * Clears all contents from all <code>TextField</code>s.
     */
    private void clearTextfields() {
        this.textFieldFirstName.clear();
        this.textFieldSurname.clear();
        this.textFieldDateOfBirth.clear();
        this.textFieldTelephone.clear();
    }

    @FXML
    public void handleAddNewCaregiverButton() {
        newCaregiverWindow();
    }

    public void newCaregiverWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    Main.class.getResource("/de/hitec/nhplus/views/caregiver/NewCaregiverView.fxml"));
            AnchorPane pane = loader.load();
            Scene scene = new Scene(pane);

            // the primary stage should stay in the background
            Stage stage = new Stage();

            NewCaregiverController controller = loader.getController();
            controller.initialize(stage);

            stage.setScene(scene);
            stage.centerOnScreen();
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
