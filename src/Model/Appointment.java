package Model;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class Appointment {
    private int appointmentId;
    private String location;
    private String type;

    //you might be able to replace this later with inner join tables combining appointments with users and customers
    private int userId;
    private String customerName;
    private String consultantName;

    private ZonedDateTime startTime;
    private ZonedDateTime endTime;
    private LocalDate date;

    private String formattedStartTime;
    private String formattedEndTime;



    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime.toLocalDateTime().atZone(ZoneId.systemDefault());
    }

    public ZonedDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime.toLocalDateTime().atZone(ZoneId.systemDefault());
    }




    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }



    public String getConsultantName() {
        return consultantName;
    }

    public void setConsultantName(String consultantName) {
        this.consultantName = consultantName;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(Timestamp timestamp) {
        date = timestamp.toLocalDateTime().toLocalDate();
    }



    public void setFormattedStartTime(Timestamp startTime) {
        DateTimeFormatter myFormat = DateTimeFormatter.ofPattern("hh:mm a");
        formattedStartTime = startTime.toLocalDateTime().atZone(ZoneId.systemDefault()).format(myFormat);
    }
    public String getFormattedStartTime() {
        return formattedStartTime;
    }

    public void setFormattedEndTime(Timestamp endTime) {
        DateTimeFormatter myFormat = DateTimeFormatter.ofPattern("hh:mm a");
        formattedEndTime = endTime.toLocalDateTime().atZone(ZoneId.systemDefault()).format(myFormat);
    }
    public String getFormattedEndTime() {
        return formattedEndTime;
    }



    //constructor for the Appointment class.
    public Appointment(int appointmentId, String location, String type, int userId, String customerName, String consultantName,Timestamp startTime, Timestamp endTime) {
        this.appointmentId = appointmentId;
        this.location = location;
        this.type = type;
        this.userId = userId;
        this.customerName = customerName;
        setStartTime(startTime);
        setEndTime(endTime);
        this.consultantName = consultantName;
        setDate(startTime);
        setFormattedStartTime(startTime);
        setFormattedEndTime(endTime);
    }
}
