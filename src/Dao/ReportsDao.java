package Dao;

import Utility.dbConnection;

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

}
