package Model;

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

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private LocalDate date;
    private ZonedDateTime startTimeOnly;
    private ZonedDateTime endTimeOnly;
    private String formattedStartTime;
    private String formattedEndTime;

    public void setFormattedStartTime(LocalDateTime startTime) {
        DateTimeFormatter myFormat = DateTimeFormatter.ofPattern("hh:mm");
        formattedStartTime = startTime.atZone(ZoneId.systemDefault()).format(myFormat);
    }
    public String getFormattedStartTime() {
        return formattedStartTime;
    }

    public void setFormattedEndTime(LocalDateTime endTime) {
        DateTimeFormatter myFormat = DateTimeFormatter.ofPattern("hh:mm");
        formattedEndTime = endTime.atZone(ZoneId.systemDefault()).format(myFormat);
    }
    public String getFormattedEndTime() {
        return formattedEndTime;
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

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
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

    public void setDate(LocalDateTime localDateTime) {
        date = localDateTime.toLocalDate();
    }

    public ZonedDateTime getStartTimeOnly() {
        return startTimeOnly;
    }

    public void setStartTimeOnly(LocalDateTime localDateTime) {
        startTimeOnly = localDateTime.atZone(ZoneId.systemDefault());
    }

    public ZonedDateTime getEndTimeOnly() {
        return endTimeOnly;
    }

    public void setEndTimeOnly(LocalDateTime localDateTime) {
        this.endTimeOnly = localDateTime.atZone(ZoneId.systemDefault());
    }

    //constructor for the Appointment class.
    public Appointment(int appointmentId, String location, String type, int userId, String customerName, String consultantName,LocalDateTime startTime, LocalDateTime endTime) {
        this.appointmentId = appointmentId;
        this.location = location;
        this.type = type;
        this.userId = userId;
        this.customerName = customerName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.consultantName = consultantName;
        setDate(startTime);
        setStartTimeOnly(startTime);
        setEndTimeOnly(endTime);
        setFormattedStartTime(startTime);
        setFormattedEndTime(endTime);
    }
}
