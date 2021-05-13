package Dao;

import Model.Appointment;
import Model.AppointmentManager;
import Utility.dbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;

public class AppointmentDao implements Dao<Appointment> {

    private ObservableList<Appointment> tempAppointmentHolder = FXCollections.observableArrayList();

    @Override
    public void loadDbObjects() {
        String query = "SELECT a.Appointment_ID,  a.Location, a.Type, a.User_ID, a.Customer_ID, c.Customer_Name, a.Contact_ID, co.Contact_Name, a.Start, a.End\n" +
                "from appointments a\n" +
                "LEFT JOIN customers c \n" +
                "ON a.Customer_ID = c.Customer_ID\n" +
                "LEFT JOIN contacts co\n" +
                "on a.Contact_ID = co.Contact_ID;\n";

        try {
            ResultSet rs = dbConnection.getStatement().executeQuery(query);
            while (rs.next()) {
                int appointmentId = rs.getInt("Appointment_ID");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                int userId = rs.getInt("User_ID");
                String customerName = rs.getString("Customer_Name");
                String consultantName = rs.getString("Contact_Name");
                Timestamp startTime = rs.getTimestamp("Start");
                Timestamp endTime = rs.getTimestamp("End");

                tempAppointmentHolder.add(new Appointment(appointmentId, location, type, userId, customerName, consultantName, startTime, endTime));
            }

            AppointmentManager.setAppointments(tempAppointmentHolder);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        for (Appointment a : AppointmentManager.getAllAppointments()) {
            System.out.println(a.getAppointmentId());
        }
    }

    @Override
    public void addObject(Appointment appointment) throws SQLException {
        String query = "INSERT INTO appointments (Title, Location, Type, Start, End, Customer_ID, Contact_ID)\n" +
                "VALUES (?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement insertQuery = dbConnection.getConnection().prepareStatement(query);
        insertQuery.setString(1, "title");
        insertQuery.setString(2, appointment.getLocation());
        insertQuery.setString(3, appointment.getType());
        insertQuery.setTimestamp(4, appointment.getStartDateTimeSQL());
        insertQuery.setTimestamp(5, appointment.getEndDateTimeSQL());
        //ISSUE WITH CUSTOMER ID, NAME and CONTACT
        insertQuery.setInt(6, appointment.getCustomer().getCustomerId());
        insertQuery.setInt(7, appointment.getContact().getContactId());

        insertQuery.execute();
    }

    @Override
    public void modifyObject(Appointment appointment) throws SQLException {

    }

    @Override
    public void removeObject(Appointment appointment) throws SQLException {

    }
}
