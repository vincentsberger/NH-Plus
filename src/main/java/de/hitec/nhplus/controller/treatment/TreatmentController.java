package de.hitec.nhplus.controller.treatment;

import java.io.File;
import java.sql.SQLException;
import java.time.LocalDate;

import de.hitec.nhplus.datastorage.CaregiverDao;
import de.hitec.nhplus.datastorage.DaoFactory;
import de.hitec.nhplus.datastorage.PatientDao;
import de.hitec.nhplus.datastorage.TreatmentDao;
import de.hitec.nhplus.model.Caregiver;
import de.hitec.nhplus.model.Patient;
import de.hitec.nhplus.model.Treatment;
import de.hitec.nhplus.utils.DateConverter;
import de.hitec.nhplus.utils.export.TreatmentReport;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class TreatmentController {

    @FXML
    private Label labelPatientId;
    @FXML
    private Label labelPatientName;

    @FXML
    private Label labelCareLevel;

    @FXML
    private Label labelCaregiverId;

    @FXML
    private Label labelCaregiverName;

    @FXML
    Label labelCaregiverTelephone;

    @FXML
    private TextField textFieldBegin;

    @FXML
    private TextField textFieldEnd;

    @FXML
    private TextField textFieldDescription;

    @FXML
    private TextArea textAreaRemarks;

    @FXML
    private Button pdfExportButton;

    @FXML
    private DatePicker datePicker;

    private AllTreatmentController controller;
    private Stage stage;
    private Patient patient;
    private Caregiver caregiver;
    private Treatment treatment;

    public void initializeController(AllTreatmentController controller, Stage stage, Treatment treatment) {
        this.stage = stage;
        this.controller = controller;
        PatientDao pDao = DaoFactory.getDaoFactory().createPatientDAO();
        CaregiverDao cDao = DaoFactory.getDaoFactory().createCaregiverDao();
        try {
            this.patient = pDao.read((int) treatment.getPid());
            this.caregiver = cDao.read((int) treatment.getCid());
            this.treatment = treatment;
            showData();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    private void showData() {
        this.labelPatientId.setText("P-000" + Long.toString(this.patient.getPid()));
        this.labelPatientName.setText(patient.getSurname() + ", " + patient.getFirstName());
        this.labelCareLevel.setText(patient.getCareLevel());
        this.labelCaregiverId.setText("C-000" + Long.toString(this.caregiver.getCid()));
        this.labelCaregiverName.setText(this.caregiver.getSurname() + ", " + this.caregiver.getFirstName());
        this.labelCaregiverTelephone.setText(this.caregiver.getTelephone());
        LocalDate date = DateConverter.convertStringToLocalDate(treatment.getDate());
        this.datePicker.setValue(date);
        this.textFieldBegin.setText(this.treatment.getBegin());
        this.textFieldEnd.setText(this.treatment.getEnd());
        this.textFieldDescription.setText(this.treatment.getDescription());
        this.textAreaRemarks.setText(this.treatment.getRemarks());
    }

    @FXML
    public void handleChange() {
        this.treatment.setDate(this.datePicker.getValue().toString());
        this.treatment.setBegin(textFieldBegin.getText());
        this.treatment.setEnd(textFieldEnd.getText());
        this.treatment.setDescription(textFieldDescription.getText());
        this.treatment.setRemarks(textAreaRemarks.getText());
        doUpdate();
        controller.readAllAndShowInTableView();
        stage.close();
    }

    @FXML
    public void handlePdfExportButton() {

        TreatmentReport treatmentReport = new TreatmentReport(this.treatment);
        String initialFilename = "Behandlungsbericht-"+ this.treatment.getTid() +".pdf";
        treatmentReport.generate();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Bericht speichern unter...");
        fileChooser.setInitialFileName(initialFilename);
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            String absolutePathToFile = file.getAbsolutePath();
            treatmentReport.saveToFile(absolutePathToFile);
        }
    }


    private void doUpdate() {
        TreatmentDao dao = DaoFactory.getDaoFactory().createTreatmentDao();
        try {
            dao.update(treatment);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @FXML
    public void handleCancel() {
        stage.close();
    }
}