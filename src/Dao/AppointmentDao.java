package Dao;

import Model.Appointment;
import Model.AppointmentManager;
import Utility.DbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;


public class AppointmentDao implements Dao<Appointment> {

    private ObservableList<Appointment> tempAppointmentHolder = FXCollections.observableArrayList();

    /**
     * Loads the ObservableList of Appointment objects with new objects using data from the database
     */
    @Override
    public void loadDbObjects() {
        String query = "SELECT a.Appointment_ID, a.Location, a.Type, a.User_ID, a.Customer_ID, c.Customer_Name, a.Contact_ID, co.Contact_Name, a.Start, a.End, a.Title, a.Description\n" +
                "from appointments a\n" +
                "LEFT JOIN customers c \n" +
                "ON a.Customer_ID = c.Customer_ID\n" +
                "LEFT JOIN contacts co\n" +
                "on a.Contact_ID = co.Contact_ID;\n";

        try {
            ResultSet rs = DbConnection.getStatement().executeQuery(query);
            while (rs.next()) {
                int appointmentId = rs.getInt("Appointment_ID");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                int userId = rs.getInt("User_ID");
                int customerId = rs.getInt("Customer_ID");
                String customerName = rs.getString("Customer_Name");
                String consultantName = rs.getString("Contact_Name");
                Timestamp startTime = rs.getTimestamp("Start");
                Timestamp endTime = rs.getTimestamp("End");
                String title = rs.getString("Title");
                String description = rs.getString("Description");

                tempAppointmentHolder.add(new Appointment(appointmentId, location, type, userId, customerId, customerName, consultantName, startTime, endTime, title, description));

            }

            AppointmentManager.setAppointments(tempAppointmentHolder);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    /**
     * Adds an Appointment object to the ObservableList of Appointments
     * @param appointment passes an Appointment object
     * @throws SQLException
     */
    @Override
    public void addObject(Appointment appointment) throws SQLException {
        String query = "INSERT INTO appointments (Title, Location, Type, Start, End, Customer_ID, Contact_ID, User_ID)\n" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement insertQuery = DbConnection.getConnection().prepareStatement(query);
        insertQuery.setString(1, "title");
        insertQuery.setString(2, appointment.getLocation());
        insertQuery.setString(3, appointment.getType());
        insertQuery.setTimestamp(4, appointment.getStartDateTimeSQL());
        insertQuery.setTimestamp(5, appointment.getEndDateTimeSQL());
        insertQuery.setInt(6, appointment.getCustomer().getCustomerId());
        insertQuery.setInt(7, appointment.getContact().getContactId());
        insertQuery.setInt(8, appointment.getUserId());

        insertQuery.execute();
    }

    /**
     * A method that modifies the Appointment object with the Appointment object passed
     * @param appointment Passes an Appointment object
     * @throws SQLException
     */
    @Override
    public void modifyObject(Appointment appointment) throws SQLException {
        String query = "UPDATE appointments\n" +
                "SET Location = ?, Type = ?, Start = ?, End = ?, Customer_ID = ?, Contact_ID = ?, User_ID = ?\n" +
                "WHERE Appointment_ID = ?;";
        PreparedStatement updateQuery = DbConnection.getConnection().prepareStatement(query);
        updateQuery.setString(1, appointment.getLocation());
        updateQuery.setString(2, appointment.getType());
        updateQuery.setTimestamp(3, appointment.getStartDateTimeSQL());
        updateQuery.setTimestamp(4, appointment.getEndDateTimeSQL());
        updateQuery.setInt(5, appointment.getCustomer().getCustomerId());
        updateQuery.setInt(6, appointment.getContact().getContactId());
        updateQuery.setInt(7, appointment.getUserId());
        updateQuery.setInt(8, appointment.getAppointmentId());

        updateQuery.execute();

    }

    /**
     * A method that removes the passed Appointment object from the Observablelist of Appointment objects
     * @param appointment Passes an Appointment object
     * @throws SQLException
     */
    @Override
    public void removeObject(Appointment appointment) throws SQLException {
        String query = "DELETE FROM appointments WHERE Appointment_ID = ?;";

        PreparedStatement removeQuery = DbConnection.getConnection().prepareStatement(query);
        removeQuery.setInt(1, appointment.getAppointmentId());

        removeQuery.execute();

    }
}
