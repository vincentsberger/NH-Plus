package de.hitec.nhplus.model;

import de.hitec.nhplus.utils.DateConverter;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;

/**
 * Patients live in a NURSING home and are treated by nurses.
 */
public class Caregiver extends Person {

    private SimpleLongProperty cid;
    private final SimpleStringProperty dateOfBirth;
    private final SimpleStringProperty telephone;


    public Caregiver(String firstName, String surname, LocalDate dateOfBirth, String telephone) {
        super(firstName, surname);
        this.dateOfBirth = new SimpleStringProperty(DateConverter.convertLocalDateToString(dateOfBirth));
        this.telephone = new SimpleStringProperty(telephone);
    }

    public Caregiver(long cid, String firstName, String surname, LocalDate dateOfBirth, String telephone) {
        super(firstName, surname);
        this.cid = new SimpleLongProperty(cid);
        this.telephone = new SimpleStringProperty(telephone);
        this.dateOfBirth = new SimpleStringProperty(DateConverter.convertLocalDateToString(dateOfBirth));
    }

    public long getCid() {
        return cid.get();
    }

    public SimpleLongProperty cidProperty() {
        return this.cid;
    }


    public String getDateOfBirth() {
        return dateOfBirth.get();
    }

    public void setDateOfBirth(String date) {
        dateOfBirth.set(date);
    }

    public SimpleStringProperty dateOfBirthProperty() {
        return dateOfBirth;
    }

    public String getTelephone() { return telephone.get();}

    public void setTelephone(String tel) {
        telephone.set(tel);
    }


    public String toString() {
        return "Caregiver" + "\nCID: " + this.cid +
                "\nFirstname: " + this.getFirstName() +
                "\nSurname: " + this.getSurname() +
                "\nDate of Birth: " + this.getDateOfBirth() +
                "\nTelephone: " + this.getTelephone();


    }
}