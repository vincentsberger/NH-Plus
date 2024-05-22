package de.hitec.nhplus.controller.caregiver;

import de.hitec.nhplus.Main;
import de.hitec.nhplus.datastorage.CaregiverDao;
import de.hitec.nhplus.datastorage.DaoFactory;
import de.hitec.nhplus.model.Caregiver;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
import java.util.Optional;

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
    private TableColumn<Caregiver, Boolean> columnIsBlocked;

    @FXML
    private Button buttonDelete;

    @FXML
    private Button buttonAdd;

    @FXML
    private Button buttonBlock;

    @FXML
    private CheckBox checkboxShowBlockedOnly;

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

    @FXML
    public void handleToggleShowBlockedOnly(ActionEvent event) {
        if(event.getSource() instanceof CheckBox) {
            CheckBox cb = (CheckBox) event.getSource();
            if(cb.isSelected()) {
                FilteredList<Caregiver> filteredCaregivers = caregivers.filtered(caregiver -> (caregiver.isBlocked() == true));
                this.tableView.setItems(filteredCaregivers);
            } else {
                this.tableView.setItems(caregivers);
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
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Warnung!");
        alert.setHeaderText("Sind Sie sicher? (Pfleger löschen)");
        alert.setContentText("Dieser Vorgang lässt sich NICHT mehr rückgängig machen.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
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
        return;
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

    @FXML
    public void handleAddNewCaregiverButton() {
        newPatientWindow();
    }

    public void newPatientWindow() {
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
            readAllAndShowInTableView();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
