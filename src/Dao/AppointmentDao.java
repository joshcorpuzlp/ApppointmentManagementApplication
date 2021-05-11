package Dao;

import Model.Appointment;
import Model.AppointmentManager;
import Utility.dbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class AppointmentDao implements Dao<Appointment> {

    private ObservableList<Appointment> tempAppointmentHolder = FXCollections.observableArrayList();

    @Override
    public void loadDbObjects() {
        String query = "SELECT * FROM appointments;";

        try {
            ResultSet rs = dbConnection.getStatement().executeQuery(query);
            while (rs.next()) {
                int appointmentId = rs.getInt("Appointment_ID");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                int userId = rs.getInt("User_ID");
                int customerId = rs.getInt("Customer_ID");
                int consultantId = rs.getInt("Contact_ID");
                Timestamp startTime = rs.getTimestamp("Start");
                Timestamp endTime = rs.getTimestamp("End");

                tempAppointmentHolder.add(new Appointment(appointmentId, location, type, userId, customerId, consultantId,startTime.toLocalDateTime(), endTime.toLocalDateTime()));
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

    }

    @Override
    public void modifyObject(Appointment appointment) throws SQLException {

    }

    @Override
    public void removeObject(Appointment appointment) throws SQLException {

    }
}
