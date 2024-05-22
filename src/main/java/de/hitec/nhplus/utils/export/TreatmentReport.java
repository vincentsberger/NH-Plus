package de.hitec.nhplus.utils.export;

import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.spire.doc.Document;
import com.spire.doc.FileFormat;

import de.hitec.nhplus.datastorage.CaregiverDao;
import de.hitec.nhplus.datastorage.DaoFactory;
import de.hitec.nhplus.datastorage.PatientDao;
import de.hitec.nhplus.model.Caregiver;
import de.hitec.nhplus.model.Patient;
import de.hitec.nhplus.model.Treatment;

// import de.hitec.nhplus.datastorage.TreatmentDao;
// import de.hitec.nhplus.model.Treatment;

public class TreatmentReport {

    private final String templateFile = "src/main/java/de/hitec/nhplus/utils/export/Vorlage_05-TreatmentReport.docx";
    private Document document;
    private final Treatment treatment;
    private Patient patient;
    private Caregiver caregiver;

    public TreatmentReport(Treatment treatment) {
        // Load word document as template file
        this.treatment = treatment;
    }

    public void generate() {
        prepare();

        this.document = new Document(templateFile);
        // Replace a specific text

        this.document.replace("[currentDate]", this.getFormattedDate(new Date()), false, true);
        this.document.replace("[patient.pid]", String.valueOf(this.patient.getPid()), false, true);
        this.document.replace("[patient.surname]", this.patient.getSurname(), false, true);
        this.document.replace("[patient.firstname]", this.patient.getFirstName(), false, true);
        this.document.replace("[patient.dateOfBirth]", this.convertToLocaleDateString(this.patient.getDateOfBirth()), false, true);
        this.document.replace("[patient.careLevel]", this.patient.getCareLevel(), false, true);
        this.document.replace("[caregiver.cid]", String.valueOf(this.caregiver.getCid()), false, true);
        this.document.replace("[caregiver.surname]", this.caregiver.getSurname(), false, true);
        this.document.replace("[caregiver.firstname]", this.caregiver.getFirstName(), false, true);
        this.document.replace("[caregiver.dateOfBirth]", this.caregiver.getDateOfBirth(), false, true);
        this.document.replace("[caregiver.telephone]", this.caregiver.getTelephone(), false, true);
        this.document.replace("[treatment.id]", String.valueOf(this.treatment.getTid()), false, true);
        this.document.replace("[treatment.title]", this.treatment.getDescription(), false, true);
        this.document.replace("[treatment.date]", this.treatment.getDate(), false, true);
        this.document.replace("[treatment.timeStart]", this.treatment.getBegin(), false, true);
        this.document.replace("[treatment.timeEnd]", this.treatment.getEnd(), false, true);
        this.document.replace("[treatment.remarks]", this.treatment.getRemarks(), false, true);
    }

    public void saveToFile(String filename) {
        document.saveToFile(filename, FileFormat.PDF);
    }

    private void prepare() {
        PatientDao pDao = DaoFactory.getDaoFactory().createPatientDAO();
        CaregiverDao cDao = DaoFactory.getDaoFactory().createCaregiverDao();
        try {
            this.patient = pDao.read((int) treatment.getPid());
            this.caregiver = cDao.read((int) treatment.getCid());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String getFormattedDate(Date date) {
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.GERMANY);
        return dateFormat.format(date);
    }

    private String convertToLocaleDateString(String dateString) {
        // change date format for the report pdf export from 2011-10-21 to 21.10.2013
        String[] dateAsArray = dateString.split("-");
        List<String> stringList = Arrays.asList(dateAsArray);
        Collections.reverse(stringList);
        String localeDateString = stringList.get(0)
                .concat(".")
                .concat(stringList.get(1))
                .concat(".")
                .concat(stringList.get(2));
        return localeDateString;
    }
}
