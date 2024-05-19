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
    private String username;
    private String password;
    private Boolean isBlocked;


    public Caregiver(String firstName, String surname, LocalDate dateOfBirth, String telephone, String username, String password, Boolean isBlocked) {
        super(firstName, surname);
        this.dateOfBirth = new SimpleStringProperty(DateConverter.convertLocalDateToString(dateOfBirth));
        this.telephone = new SimpleStringProperty(telephone);
        this.username = username;
        this.password = password;
        this.isBlocked = isBlocked;
    }

    public Caregiver(long cid, String firstName, String surname, LocalDate dateOfBirth, String telephone, String username, String password, Boolean isBlocked) {
        super(firstName, surname);
        this.cid = new SimpleLongProperty(cid);
        this.telephone = new SimpleStringProperty(telephone);
        this.dateOfBirth = new SimpleStringProperty(DateConverter.convertLocalDateToString(dateOfBirth));
        this.username = username;
        this.password = password;
        this.isBlocked = isBlocked;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public Boolean isBlocked() {
        return this.isBlocked;
    }

    public long getCid() {
        return this.cid.get();
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