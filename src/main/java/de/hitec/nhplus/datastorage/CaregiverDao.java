package de.hitec.nhplus.datastorage;

import de.hitec.nhplus.model.Caregiver;
import de.hitec.nhplus.utils.DateConverter;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Implements the Interface <code>DaoImp</code>. Overrides methods to generate specific <code>PreparedStatements</code>,
 * to execute the specific SQL Statements.
 */
public class CaregiverDao extends DaoImp<Caregiver> {

    /**
     * The constructor initiates an object of <code>PatientDao</code> and passes the connection to its super class.
     *
     * @param connection Object of <code>Connection</code> to execute the SQL-statements.
     */
    public CaregiverDao(Connection connection) {
        super(connection);
    }

    /**
     * Generates a <code>PreparedStatement</code> to persist the given object of <code>Patient</code>.
     *
     * @param caregiver Object of <code>Patient</code> to persist.
     * @return <code>PreparedStatement</code> to insert the given patient.
     */
    @Override
    protected PreparedStatement getCreateStatement(Caregiver caregiver) {
        PreparedStatement preparedStatement = null;
        try {
            final String SQL = "INSERT INTO caregiver (firstname, surname, dateOfBirth, telephone) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            preparedStatement = this.connection.prepareStatement(SQL);
            preparedStatement.setString(1, caregiver.getFirstName());
            preparedStatement.setString(2, caregiver.getSurname());
            preparedStatement.setString(3, caregiver.getDateOfBirth());
            preparedStatement.setString(4, caregiver.getTelephone());

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return preparedStatement;
    }

    /**
     * Generates a <code>PreparedStatement</code> to query a patient by a given patient id (pid).
     *
     * @param pid Patient id to query.
     * @return <code>PreparedStatement</code> to query the patient.
     */
    @Override
    protected PreparedStatement getReadByIDStatement(long cid) {
        PreparedStatement preparedStatement = null;
        try {
            final String SQL = "SELECT * FROM caregiver WHERE cid = ?";
            preparedStatement = this.connection.prepareStatement(SQL);
            preparedStatement.setLong(1, cid);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return preparedStatement;
    }

    /**
     * Maps a <code>ResultSet</code> of one patient to an object of <code>Patient</code>.
     *
     * @param result ResultSet with a single row. Columns will be mapped to an object of class <code>Patient</code>.
     * @return Object of class <code>Patient</code> with the data from the resultSet.
     */
    @Override
    protected Caregiver getInstanceFromResultSet(ResultSet result) throws SQLException {
        return new Caregiver(
                result.getInt(1),
                result.getString(2),
                result.getString(3),
                DateConverter.convertStringToLocalDate(result.getString(4)),
                result.getString(5),

    }

    /**
     * Generates a <code>PreparedStatement</code> to query all patients.
     *
     * @return <code>PreparedStatement</code> to query all patients.
     */
    @Override
    protected PreparedStatement getReadAllStatement() {
        PreparedStatement statement = null;
        try {
            final String SQL = "SELECT * FROM caregiver";
            statement = this.connection.prepareStatement(SQL);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return statement;
    }

    /**
     * Maps a <code>ResultSet</code> of all patients to an <code>ArrayList</code> of <code>Patient</code> objects.
     *
     * @param result ResultSet with all rows. The Columns will be mapped to objects of class <code>Patient</code>.
     * @return <code>ArrayList</code> with objects of class <code>Patient</code> of all rows in the
     * <code>ResultSet</code>.
     */
    @Override
    protected ArrayList<Caregiver> getListFromResultSet(ResultSet result) throws SQLException {
        ArrayList<Caregiver> list = new ArrayList<>();
        while (result.next()) {
            LocalDate date = DateConverter.convertStringToLocalDate(result.getString(4));
            Caregiver caregiver = new Caregiver(result.getInt(1), result.getString(2),
                    result.getString(3), date,
                    result.getString(5));
            list.add(caregiver);
        }
        return list;
    }

    /**
     * Generates a <code>PreparedStatement</code> to update the given patient, identified
     * by the id of the patient (pid).
     *
     * @param patient Patient object to update.
     * @return <code>PreparedStatement</code> to update the given patient.
     */
    @Override
    protected PreparedStatement getUpdateStatement(Caregiver caregiver) {
        PreparedStatement preparedStatement = null;
        try {
            final String SQL =
                    "UPDATE patient SET " +
                            "firstname = ?, " +
                            "surname = ?, " +
                            "dateOfBirth = ?, " +
                            "carelevel = ?, " +
                            "roomnumber = ?, " +
                            "assets = ? " +
                            "WHERE pid = ?";
            preparedStatement = this.connection.prepareStatement(SQL);
            preparedStatement.setString(1, caregiver.getFirstName());
            preparedStatement.setString(2, caregiver.getSurname());
            preparedStatement.setString(3, caregiver.getDateOfBirth());
            preparedStatement.setString(4, caregiver.getTelephone());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return preparedStatement;
    }

    /**
     * Generates a <code>PreparedStatement</code> to delete a patient with the given id.
     *
     * @param pid Id of the patient to delete.
     * @return <code>PreparedStatement</code> to delete patient with the given id.
     */
    @Override
    protected PreparedStatement getDeleteStatement(long cid) {
        PreparedStatement preparedStatement = null;
        try {
            final String SQL = "DELETE FROM caregiver WHERE cid = ?";
            preparedStatement = this.connection.prepareStatement(SQL);
            preparedStatement.setLong(1, cid);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return preparedStatement;
    }
}
