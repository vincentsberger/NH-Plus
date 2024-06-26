package de.hitec.nhplus.datastorage;

import de.hitec.nhplus.model.Caregiver;
import de.hitec.nhplus.utils.DateConverter;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;


/**
 * Implements the Interface <code>DaoImp</code>. Overrides methods to generate
 * specific <code>PreparedStatements</code>,
 * to execute the specific SQL Statements.
 */
public class CaregiverDao extends DaoImp<Caregiver> {

    /**
     * The constructor initiates an object of <code>PatientDao</code> and passes the
     * connection to its super class.
     *
     * @param connection Object of <code>Connection</code> to execute the
     *                   SQL-statements.
     */
    public CaregiverDao(Connection connection) {
        super(connection);
    }

    /**
     * Generates a <code>PreparedStatement</code> to persist the given object of
     * <code>Patient</code>.
     *
     * @param caregiver Object of <code>Patient</code> to persist.
     * @return <code>PreparedStatement</code> to insert the given patient.
     */
    @Override
    protected PreparedStatement getCreateStatement(Caregiver caregiver) {
        PreparedStatement preparedStatement = null;
        try {
            final String SQL = "INSERT INTO caregiver (firstname, surname, dateOfBirth, telephone, username, password, is_blocked) "
                    +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            preparedStatement = this.connection.prepareStatement(SQL);
            preparedStatement.setString(1, caregiver.getFirstName());
            preparedStatement.setString(2, caregiver.getSurname());
            preparedStatement.setString(3, caregiver.getDateOfBirth());
            preparedStatement.setString(4, caregiver.getTelephone());
            preparedStatement.setString(5, caregiver.getUsername());
            preparedStatement.setString(6, caregiver.getPassword());
            preparedStatement.setBoolean(7, caregiver.isBlocked());

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return preparedStatement;
    }

    /**
     * Generates a <code>PreparedStatement</code> to query a patient by a given
     * patient id (pid).
     *
     * @param cid Caregiver id to query.
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
     * Maps a <code>ResultSet</code> of one patient to an object of
     * <code>Patient</code>.
     * 
     * columnIndex => prop name
     * 1 => cid
     * 2 => firstName
     * 3 => surName
     * 4 => dateOfBirth
     * 5 => telephone
     * 6 => username
     * 7 => password
     * 8 => isBlocked
     *
     * @param result ResultSet with a single row. Columns will be mapped to an
     *               object of class <code>Patient</code>.
     * @return Object of class <code>Patient</code> with the data from the
     *         resultSet.
     */
    @Override
    protected Caregiver getInstanceFromResultSet(ResultSet result) throws SQLException {
        return new Caregiver(
                result.getInt(1), // cid
                result.getString(2), // firstName
                result.getString(3), // surName
                DateConverter.convertStringToLocalDate(result.getString(4)), // dateOfBirth
                result.getString(5), // telephone
                result.getString(6), // username
                result.getString(7), // password
                result.getBoolean(8) // isBlocked

        );
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
     * Maps a <code>ResultSet</code> of all patients to an <code>ArrayList</code> of
     * <code>Patient</code> objects.
     *
     * @param result ResultSet with all rows. The Columns will be mapped to objects
     *               of class <code>Patient</code>.
     * @return <code>ArrayList</code> with objects of class <code>Patient</code> of
     *         all rows in the
     *         <code>ResultSet</code>.
     * 
     *         columnIndex => prop name
     *         1 => cid
     *         2 => firstName
     *         3 => surName
     *         4 => dateOfBirth
     *         5 => telephone
     *         6 => username
     *         7 => password
     *         8 => isBlocked
     */
    @Override
    protected ArrayList<Caregiver> getListFromResultSet(ResultSet result) throws SQLException {
        ArrayList<Caregiver> list = new ArrayList<>();
        while (result.next()) {
            LocalDate date = DateConverter.convertStringToLocalDate(result.getString(4));
            Caregiver caregiver = new Caregiver(result.getInt(1), result.getString(2),
                    result.getString(3), date,
                    result.getString(5), result.getString(6), result.getString(7), result.getBoolean(8));
            list.add(caregiver);
        }
        return list;
    }

    /**
     * Generates a <code>PreparedStatement</code> to update the given patient,
     * identified
     * by the id of the Caregiver (cid).
     *
     * @param caregiver Caregiver object to update.
     * @return <code>PreparedStatement</code> to update the given patient.
     */
    @Override
    protected PreparedStatement getUpdateStatement(Caregiver caregiver) {
        PreparedStatement preparedStatement = null;
        try {
            final String SQL = "UPDATE caregiver SET " +
                    "firstname=?, " +
                    "surname=?, " +
                    "dateOfBirth=?, " +
                    "telephone=?, " +
                    "is_blocked=? " +
                    "WHERE cid=?";
            preparedStatement = this.connection.prepareStatement(SQL);
            preparedStatement.setString(1, caregiver.getFirstName());
            preparedStatement.setString(2, caregiver.getSurname());
            preparedStatement.setString(3, caregiver.getDateOfBirth());
            preparedStatement.setString(4, caregiver.getTelephone());
            preparedStatement.setBoolean(5, caregiver.isBlocked());
            preparedStatement.setLong(6, caregiver.getCid());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return preparedStatement;
    }

    /**
     * Generates a <code>PreparedStatement</code> to delete a caregiver with the
     * given id.
     *
     * @param cid Id of the caregiver to delete.
     * @return <code>PreparedStatement</code> to delete caregiver with the given id.
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

    @Override
    protected PreparedStatement getDescribedColumns() {
        PreparedStatement preparedStatement = null;
        try {
            final String SQL = "PRAGMA table_info(caregiver)";
            preparedStatement = this.connection.prepareStatement(SQL);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return preparedStatement;
    }

    @Override
    protected ArrayList<String> getDescribeResultSet(ResultSet result) throws SQLException {
        ArrayList<String> list = new ArrayList<>();
        while (result.next()) {
            String columnName = result.getString("name");
            list.add(columnName);
        }
        return list;
    }
}
