package de.hitec.nhplus.model;

import de.hitec.nhplus.utils.DateConverter;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Patients live in a NURSING home and are treated by nurses.
 */
public class Caregiver extends Person {

    private SimpleLongProperty cid;
    private final SimpleStringProperty dateOfBirth;
    private final List<Treatment> allTreatments = new ArrayList<>();
    private final SimpleStringProperty telephone;


    public Caregiver(String firstName, String surname, String dateOfBirth) {
        super(firstName, surname);
        this.dateOfBirth = new SimpleStringProperty(dateOfBirth);
    }

    public long getCid() {
        return cid.get();
    }

    public SimpleLongProperty pidProperty() {
        return cid;
    }

    public String getDateOfBirth() {
        return dateOfBirth.get();
    }

    public SimpleStringProperty dateOfBirthProperty() {
        return dateOfBirth;
    }

    public String getTelephone() { return telephone.get();}


    public String toString() {
        return "Caregiver" + "\nCID: " + this.cid +
                "\nFirstname: " + this.getFirstName() +
                "\nSurname: " + this.getSurname() +
                "\nDate of Birth: " + this.getDateOfBirth();

    }
}