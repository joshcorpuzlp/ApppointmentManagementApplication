package Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Appointment {
    private int appointmentId;
    private String location;
    private String type;

    //you might be able to replace this later with inner join tables combining appointments with users and customers
    private int userId;
    private int customerId;
    private int consultantId;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private LocalDate date;
    private LocalTime startTimeOnly;
    private LocalTime endTimeOnly;


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

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
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

    public int getConsultantId() {
        return consultantId;
    }

    public void setConsultantId(int consultantId) {
        this.consultantId = consultantId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDateTime localDateTime) {
        date = localDateTime.toLocalDate();
    }

    public LocalTime getStartTimeOnly() {
        return startTimeOnly;
    }

    public void setStartTimeOnly(LocalDateTime localDateTime) {
        startTimeOnly = localDateTime.toLocalTime();
    }

    public LocalTime getEndTimeOnly() {
        return endTimeOnly;
    }

    public void setEndTimeOnly(LocalDateTime localDateTime) {
        this.endTimeOnly = localDateTime.toLocalTime();
    }

    //constructor for the Appointment class.
    public Appointment(int appointmentId, String location, String type, int userId, int customerId, int consultantId,LocalDateTime startTime, LocalDateTime endTime) {
        this.appointmentId = appointmentId;
        this.location = location;
        this.type = type;
        this.userId = userId;
        this.customerId = customerId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.consultantId = consultantId;
        setDate(startTime);
        setStartTimeOnly(startTime);
        setEndTimeOnly(endTime);
    }
}
