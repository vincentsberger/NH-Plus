package de.hitec.nhplus.utils;

import de.hitec.nhplus.datastorage.CaregiverDao;
import de.hitec.nhplus.datastorage.ConnectionBuilder;
import de.hitec.nhplus.datastorage.DaoFactory;
import de.hitec.nhplus.datastorage.PatientDao;
import de.hitec.nhplus.datastorage.TreatmentDao;
import de.hitec.nhplus.model.Patient;
import de.hitec.nhplus.model.Treatment;
import de.hitec.nhplus.model.Caregiver;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static de.hitec.nhplus.utils.DateConverter.convertStringToLocalDate;
import static de.hitec.nhplus.utils.DateConverter.convertStringToLocalTime;

/**
 * Call static class provides to static methods to set up and wipe the database.
 * It uses the class ConnectionBuilder
 * and its path to build up the connection to the database. The class is
 * executable. Executing the class will build
 * up a connection to the database and calls setUpDb() to wipe the database,
 * build up a clean database and fill the
 * database with some test data.
 */
public class SetUpDB {

        /**
         * This method wipes the database by dropping the tables. Then the method calls
         * DDL statements to build it up from
         * scratch and DML statements to fill the database with hard coded test data.
         */
        public static void setUpDb() {
                Connection connection = ConnectionBuilder.getConnection();
                SetUpDB.wipeDb(connection);
                SetUpDB.setUpTableCaregiver(connection);
                SetUpDB.setUpCaregiver();
                SetUpDB.setUpTablePatient(connection);
                SetUpDB.setUpPatients();
                SetUpDB.setUpTableTreatment(connection);
                SetUpDB.setUpTreatments();
        }

        /**
         * This method wipes the database by dropping the tables.
         */
        public static void wipeDb(Connection connection) {
                try (Statement statement = connection.createStatement()) {
                        statement.execute("DROP TABLE patient");
                        statement.execute("DROP TABLE treatment");
                        statement.execute("DROP TABLE caregiver");
                } catch (SQLException exception) {
                        System.out.println(exception.getMessage());
                }
        }

        private static void setUpTablePatient(Connection connection) {
                final String SQL = "CREATE TABLE IF NOT EXISTS patient (" +
                                "   pid INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                "   firstname TEXT NOT NULL, " +
                                "   surname TEXT NOT NULL, " +
                                "   dateOfBirth TEXT NOT NULL, " +
                                "   carelevel TEXT NOT NULL, " +
                                "   roomnumber TEXT NOT NULL " +
                                ");";
                try (Statement statement = connection.createStatement()) {
                        statement.execute(SQL);
                } catch (SQLException exception) {
                        System.out.println(exception.getMessage());
                }
        }

        private static void setUpTableCaregiver(Connection connection) {
                final String SQL = "CREATE TABLE IF NOT EXISTS caregiver (" +
                                "   cid INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                "   firstname TEXT NOT NULL, " +
                                "   surname TEXT NOT NULL, " +
                                "   dateOfBirth TEXT NOT NULL, " +
                                "   telephone TEXT NOT NULL " +
                                ");";

                try (Statement statement = connection.createStatement()) {

                        statement.execute(SQL);
                } catch (SQLException exception) {
                        System.out.println(exception.getMessage());
                }
        }

        private static void setUpTableTreatment(Connection connection) {
                final String SQL = "CREATE TABLE IF NOT EXISTS treatment (" +
                                "   tid INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                "   pid INTEGER NOT NULL, " +
                                "   cid INTEGER NOT NULL, " +
                                "   treatment_date TEXT NOT NULL, " +
                                "   begin TEXT NOT NULL, " +
                                "   end TEXT NOT NULL, " +
                                "   description TEXT NOT NULL, " +
                                "   remarks TEXT NOT NULL," +
                                "   FOREIGN KEY(pid) REFERENCES patient (pid)," +
                                "   FOREIGN KEY(cid) REFERENCES caregiver (cid)" +
                                ");";

                try (Statement statement = connection.createStatement()) {
                        statement.execute(SQL);
                } catch (SQLException exception) {
                        System.out.println(exception.getMessage());
                }
        }

        private static void setUpPatients() {
                try {
                        PatientDao dao = DaoFactory.getDaoFactory().createPatientDAO();
                        dao.create(
                                        new Patient("Seppl", "Herberger", convertStringToLocalDate("1945-12-01"), "4",
                                                        "202"));
                        dao.create(new Patient("Martina", "Gerdsen", convertStringToLocalDate("1954-08-12"), "5",
                                        "010"));
                        dao.create(new Patient("Gertrud", "Franzen", convertStringToLocalDate("1949-04-16"), "3",
                                        "002"));
                        dao.create(new Patient("Ahmet", "Yilmaz", convertStringToLocalDate("1941-02-22"), "3", "013"));
                        dao.create(new Patient("Hans", "Neumann", convertStringToLocalDate("1955-12-12"), "2", "001"));
                        dao.create(new Patient("Elisabeth", "Müller", convertStringToLocalDate("1958-03-07"), "5",
                                        "110"));
                } catch (SQLException exception) {
                        exception.printStackTrace();
                }
        }

        private static void setUpTreatments() {
                try {
                        TreatmentDao dao = DaoFactory.getDaoFactory().createTreatmentDao();
                        dao.create(new Treatment(1, 1, convertStringToLocalDate("2023-06-03"),
                                        convertStringToLocalTime("11:00"),
                                        convertStringToLocalTime("15:00"), "Gespräch",
                                        "Der Patient hat enorme Angstgefühle und glaubt, er sei überfallen worden. Ihm seien alle Wertsachen gestohlen worden.\nPatient beruhigt sich erst, als alle Wertsachen im Zimmer gefunden worden sind."));
                        dao.create(new Treatment(2, 1, convertStringToLocalDate("2023-06-05"),
                                        convertStringToLocalTime("11:00"),
                                        convertStringToLocalTime("12:30"), "Gespräch",
                                        "Patient irrt auf der Suche nach gestohlenen Wertsachen durch die Etage und bezichtigt andere Bewohner des Diebstahls.\nPatient wird in seinen Raum zurückbegleitet und erhält Beruhigungsmittel."));
                        dao.create(new Treatment(3, 2, convertStringToLocalDate("2023-06-04"),
                                        convertStringToLocalTime("07:30"),
                                        convertStringToLocalTime("08:00"), "Waschen",
                                        "Patient mit Waschlappen gewaschen und frisch angezogen. Patient gewendet."));
                        dao.create(new Treatment(4, 1, convertStringToLocalDate("2023-06-06"),
                                        convertStringToLocalTime("15:10"),
                                        convertStringToLocalTime("16:00"), "Spaziergang",
                                        "Spaziergang im Park, Patient döst  im Rollstuhl ein"));
                        dao.create(new Treatment(8, 1, convertStringToLocalDate("2023-06-08"),
                                        convertStringToLocalTime("15:00"),
                                        convertStringToLocalTime("16:00"), "Spaziergang",
                                        "Parkspaziergang; Patient ist heute lebhafter und hat klare Momente; erzählt von seiner Tochter"));
                        dao.create(new Treatment(9, 2, convertStringToLocalDate("2023-06-07"),
                                        convertStringToLocalTime("11:00"),
                                        convertStringToLocalTime("11:30"), "Waschen",
                                        "Waschen per Dusche auf einem Stuhl; Patientin gewendet;"));
                        dao.create(new Treatment(12, 5, convertStringToLocalDate("2023-06-08"),
                                        convertStringToLocalTime("15:00"),
                                        convertStringToLocalTime("15:30"), "Physiotherapie",
                                        "Übungen zur Stabilisation und Mobilisierung der Rückenmuskulatur"));
                        dao.create(new Treatment(12, 5, convertStringToLocalDate("2023-08-24"),
                                        convertStringToLocalTime("09:30"),
                                        convertStringToLocalTime("10:15"), "KG", "Lympfdrainage"));
                        dao.create(new Treatment(12, 6, convertStringToLocalDate("2023-08-31"),
                                        convertStringToLocalTime("13:30"),
                                        convertStringToLocalTime("13:45"), "Toilettengang",
                                        "Hilfe beim Toilettengang; Patientin klagt über Schmerzen beim Stuhlgang. Gabe von Iberogast"));
                        dao.create(new Treatment(12, 6, convertStringToLocalDate("2023-09-01"),
                                        convertStringToLocalTime("16:00"),
                                        convertStringToLocalTime("17:00"), "KG",
                                        "Massage der Extremitäten zur Verbesserung der Durchblutung"));
                } catch (SQLException exception) {
                        exception.printStackTrace();
                }
        }

        private static void setUpCaregiver() {
                try {
                        CaregiverDao dao = DaoFactory.getDaoFactory().createCaregiverDao();
                        dao.create(new Caregiver("Seppl", "Herberger", convertStringToLocalDate("1945-12-01"),
                                        "0123456789"));
                        dao.create(new Caregiver("Martina", "Gerdsen", convertStringToLocalDate("1954-08-12"),
                                        "0123456789"));
                        dao.create(new Caregiver("Gertrud", "Franzen", convertStringToLocalDate("1949-04-16"),
                                        "0123456789"));
                        dao.create(new Caregiver("Ahmet", "Yilmaz", convertStringToLocalDate("1941-02-22"),
                                        "0123456789"));
                        dao.create(new Caregiver("Hans", "Neumann", convertStringToLocalDate("1955-12-12"),
                                        "0123456789"));
                        dao.create(new Caregiver("Elisabeth", "Müller", convertStringToLocalDate("1958-03-07"),
                                        "0123456789"));
                } catch (SQLException exception) {
                        exception.printStackTrace();
                }
        }

        public static void main(String[] args) {
                SetUpDB.setUpDb();
        }
}
