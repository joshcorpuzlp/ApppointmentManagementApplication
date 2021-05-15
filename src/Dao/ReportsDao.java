package Dao;

import Model.User;
import Utility.dbConnection;

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

}
