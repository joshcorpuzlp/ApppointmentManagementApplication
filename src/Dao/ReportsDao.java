package Dao;

import Model.User;
import Utility.dbConnection;
import jdk.jfr.Timespan;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportsDao {

    public String report1() {
        String query = "SELECT Type, COUNT(Type) AS Number_Per_Type\n" +
                "FROM appointments\n" +
                "WHERE month(Start) = month(current_date())\n" +
                "GROUP BY Type;";
        String reportMessage = "";

        try {
            ResultSet rs = dbConnection.getStatement().executeQuery(query);

            reportMessage += "These are the number of appointments per type this month:\n";

            while (rs.next()) {
                String type = rs.getString("Type");
                int numType = rs.getInt("Number_Per_Type");
                reportMessage += type + ": " + numType + "\n";

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return reportMessage;

    }

    public String report2(User user) throws SQLException {
        String reportMessage = "";
        String query = "SELECT summaryTable.userNames as User_Names, count(summaryTable.appointments) as Number_Of_Appointments\n" +
                "FROM (SELECT a.Appointment_ID as appointments, a.User_ID as userIDs, u.User_Name as userNames\n" +
                "\tFROM appointments as a\n" +
                "\tLEFT OUTER JOIN users as u\n" +
                "\ton a.User_ID = u.User_ID) AS summaryTable\n" +
                "WHERE userIDs = ?;";
        PreparedStatement reportQuery = dbConnection.getConnection().prepareStatement(query);
        reportQuery.setInt(1, user.getUserId());

        ResultSet rs = reportQuery.executeQuery();

        while (rs.next()) {
            int numAppointments = rs.getInt("Number_Of_Appointments");
            reportMessage += "User: " + user.getUserName() + " has " + numAppointments + " appointments";
        }
        return reportMessage;
    }

    public String report3(User user) throws SQLException {
        String reportMessage = "";
        String query = "SELECT a.User_ID, u.User_Name, avg(timestampdiff(SECOND, a.Start, a.End) / 60 / 60)  AS Avg_Appt_Length \n" +
                "FROM appointments a\n" +
                "LEFT OUTER JOIN users as u\n" +
                "ON a.User_ID = u.User_ID\n" +
                "WHERE a.User_ID = ?";

        PreparedStatement reportQuery = dbConnection.getConnection().prepareStatement(query);
        reportQuery.setInt(1, user.getUserId());

        ResultSet rs = reportQuery.executeQuery();

        while (rs.next()) {
            //retrieve the Average appoinment length from the database
            double avgApptLenD = rs.getDouble("Avg_Appt_Length");

            //convert into a string
            String formatted = String.format("%.2f",avgApptLenD);

            //split the double where the "." is
            String[] convert = formatted.split("\\.");

            //convert each side of the double, one to hours and the other to minutes.
            int hours = Integer.parseInt(convert[0]) * 60;
            double minutes = Double.parseDouble(convert[1]) * 0.6;

            //print the message using String.format
            reportMessage = String.format("The user: \"%s\" has an average appointment length of %d hours and %.0f minutes.",user.getUserName(), hours, minutes);

        }
        return reportMessage;
    }

}
