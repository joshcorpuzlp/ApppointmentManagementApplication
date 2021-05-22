package Dao;

import Model.Contact;
import Utility.DbConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class ReportsDao {

    /**
     * Method that returns a String report containing.
     * the number of appointments per type in the current month.
     * Data is pulled from the connected Database.
     *
     * @return A String object
     */
    public String report1() {
        String query = "SELECT Type, COUNT(Type) AS Number_Per_Type\n" +
                "FROM appointments\n" +
                "WHERE month(Start) = month(current_date())\n" +
                "GROUP BY Type;";
        //StringBuilder is utilized as it is better to be used in a for loop.
        StringBuilder reportMessage = new StringBuilder();

        try {
            ResultSet rs = DbConnection.getStatement().executeQuery(query);

            reportMessage.append("These are the number of appointments per type this month:\n");

            while (rs.next()) {
                String type = rs.getString("Type");
                int numType = rs.getInt("Number_Per_Type");

                //Utilized StringBuilder class instead of a String with a += operator.
                reportMessage.append(type).append(": ").append(numType).append("\n");

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return reportMessage.toString();

    }


    /**
     * Method that returns the appointments for a selected Contact passed on as the method parameter
     * Data is pulled from the connected Database.
     * @param contact Passes a contact object
     * @return Returns a String object
     * @throws SQLException
     */
    public String report2(Contact contact) throws SQLException {
        StringBuilder reportMessage = new StringBuilder();

        String query = "SELECT summaryTable.appointmentID, summaryTable.contactName, summaryTable.startTime, summaryTable.endTime, summaryTable.title, summaryTable.description, cu.Customer_Name as customerName\n" +
                "FROM (SELECT a.Appointment_ID as appointmentID, a.Contact_ID as contactID, \n" +
                "\tc.Contact_Name as contactName, \n" +
                "\ta.Start as startTime, a.End as endTime, \n" +
                "\ta.Title as title, a.Type as type, \n" +
                "\ta.Description as description, a.Customer_ID as customerId\n" +
                "\tFROM appointments as a\n" +
                "\tLEFT OUTER JOIN contacts as c\n" +
                "\ton a.Contact_ID = c.Contact_ID) as summaryTable\n" +
                "LEFT OUTER JOIN customers cu\n" +
                "ON summaryTable.customerId = cu.Customer_ID\n" +
                "WHERE contactID = ?;";
        PreparedStatement reportQuery = DbConnection.getConnection().prepareStatement(query);
        reportQuery.setInt(1, contact.getContactId());

        ResultSet rs = reportQuery.executeQuery();

        reportMessage.append("Contact " + contact.getContactName() + " has the following appointments:\n");

        while (rs.next()) {

            //intake data from query to variable holders
            int appointmentId = rs.getInt("appointmentID");
            String contactName = rs.getString("contactName");
            Timestamp startTime = rs.getTimestamp("startTime");
            Timestamp endTime = rs.getTimestamp("endTime");
            String title = rs.getString("title");
            String description = rs.getString("description");
            String customerName = rs.getString("customerName");

            //declare proper variables needed to format the times and date properly
            String correctStartTime;
            String correctEndTime;
            LocalDate date;
            DateTimeFormatter myFormat = DateTimeFormatter.ofPattern("hh:mm a");

            //set the time data to UTC
            ZonedDateTime tempTime = startTime.toLocalDateTime().atZone(ZoneId.of("UTC"));
            ZonedDateTime tempTime2 = endTime.toLocalDateTime().atZone(ZoneId.of("UTC"));

            //convert UTC to localTime
            ZoneId localZone = ZoneId.systemDefault();
            ZonedDateTime defaultZoned = tempTime.withZoneSameInstant(localZone);
            ZonedDateTime defaultZoned2 = tempTime2.withZoneSameInstant(localZone);

            //convert the Start and End Times to the proper format
            correctStartTime = defaultZoned.format(myFormat);
            correctEndTime = defaultZoned2.format(myFormat);
            date = defaultZoned.toLocalDate();

            //add to reportMessage
            reportMessage.append(
                    String.format("AppointmentID: %d | Start: %s | End: %s | Date: %s | Title: %s | Description: %s | Customer: %s",
                            appointmentId, correctStartTime, correctEndTime, date, title, description, customerName));


        }
        //return reportMessage as a string
        return reportMessage.toString();

    }


    /**
     * Method that returns a String message that details the average appointment length for the passed User argument.
     * Data is pulled from the connected Database.
     * @param contact Passes a contact object
     * @return returns a String object
     * @throws SQLException
     */
    public String report3(Contact contact) throws SQLException {
        String reportMessage = "";
        String query = "SELECT a.Contact_ID, c.Contact_Name , avg(timestampdiff(SECOND, a.Start, a.End) / 60 / 60)  AS Avg_Appt_Length\n" +
                "FROM appointments a\n" +
                "LEFT OUTER JOIN contacts as c\n" +
                "ON a.Contact_ID = c.Contact_ID\n" +
                "WHERE a.Contact_ID = ?;";

        PreparedStatement reportQuery = DbConnection.getConnection().prepareStatement(query);
        reportQuery.setInt(1, contact.getContactId());

        ResultSet rs = reportQuery.executeQuery();

        while (rs.next()) {
            //retrieve the Average appointment length from the database
            double avgApptLenD = rs.getDouble("Avg_Appt_Length");

            //convert into a string
            String formatted = String.format("%.2f",avgApptLenD);

            //split the double where the "." is
            String[] convert = formatted.split("\\.");

            //convert each side of the double, one to hours and the other to minutes.
            int hours = Integer.parseInt(convert[0]);
            double minutes = Double.parseDouble(convert[1]) * 0.6;

            //print the message using String.format
            reportMessage = String.format("The contact: \"%s\" has an average appointment length of %d hour(s) and %.0f minute(s).",contact.getContactName(), hours, minutes);

        }
        return reportMessage;
    }

}
