package Model;

import com.sun.scenario.effect.Offset;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

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

    private Timestamp startDateTimeSQL;
    private Timestamp endDateTimeSQL;

    private String formattedStartTime;
    private String formattedEndTime;



    public LocalDateTime getStartTime() {
        return startTime;
    }

    //method that uses Timestamp Objects from the MySQL database as parameters. it will first assign it a UTC zone and then convert it to SystemZone.
    public void setStartTime(Timestamp startTime) {
        ZonedDateTime tempTime = startTime.toLocalDateTime().atZone(ZoneId.of("UTC"));
        ZoneId localZone = ZoneId.systemDefault();
        ZonedDateTime defaultZoned = tempTime.withZoneSameInstant(localZone);
        this.startTime = defaultZoned.toLocalDateTime();

        System.out.println(tempTime);
        System.out.println(defaultZoned);
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }
    //method that uses Timestamp Objects from the MySQL database as parameters. it will first assign it a UTC zone and then convert it to SystemZone.
    public void setEndTime(Timestamp endTime) {
        ZonedDateTime tempTime = endTime.toLocalDateTime().atZone(ZoneId.of("UTC"));
        ZoneId localZone = ZoneId.systemDefault();
        ZonedDateTime defaultZoned = tempTime.withZoneSameInstant(localZone);
        this.endTime = defaultZoned.toLocalDateTime();

    }

    //method does a similar function as the setStart/EndTime but creates a formatted string instead.
    public void setFormattedStartTime(Timestamp startTime) {
        DateTimeFormatter myFormat = DateTimeFormatter.ofPattern("hh:mm a");

        ZonedDateTime tempTime = startTime.toLocalDateTime().atZone(ZoneId.of("UTC"));
        ZoneId localZone = ZoneId.systemDefault();
        ZonedDateTime defaultZoned = tempTime.withZoneSameInstant(localZone);

        formattedStartTime = defaultZoned.format(myFormat);
    }
    public String getFormattedStartTime() {
        return formattedStartTime;
    }

    //method does a similar function as the setStart/EndTime but creates a formatted string instead.
    public void setFormattedEndTime(Timestamp endTime) {
        DateTimeFormatter myFormat = DateTimeFormatter.ofPattern("hh:mm a");

        ZonedDateTime tempTime = endTime.toLocalDateTime().atZone(ZoneId.of("UTC"));
        ZoneId localZone = ZoneId.systemDefault();
        ZonedDateTime defaultZoned = tempTime.withZoneSameInstant(localZone);

        formattedEndTime = defaultZoned.format(myFormat);

    }
    public String getFormattedEndTime() {
        return formattedEndTime;
    }


    //setters and getters for converting LocalDateTime to TimeStamp, used with User created appointments
    public void setStartDateTimeSQL(LocalDateTime startTime) {
        Timestamp timestamp = Timestamp.valueOf(startTime);
        this.startDateTimeSQL = timestamp;
    }
    public Timestamp getStartDateTimeSQL() {
        return startDateTimeSQL;
    }

    public void setEndDateTimeSQL(LocalDateTime endTime) {
        Timestamp timestamp = Timestamp.valueOf(endTime);
        this.endDateTimeSQL = timestamp;
    }
    public Timestamp getEndDateTimeSQL() {
        return endDateTimeSQL;
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



    //constructor for the Appointment class. Used when creating from Database data.
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

    //overloaded constructor for the Appointment class. Used when creating from User Input
    public Appointment(int appointmentId, String location, String type, int userId, String customerName, String consultantName,LocalTime startTime, LocalTime endTime, LocalDate date) {
        this.appointmentId = appointmentId;
        this.location = location;
        this.type = type;
        this.userId = userId;
        this.customerName = customerName;

        //converts startTime, endTime and date inputs to create LocalDateTime objects.
        this.date = date;
        this.startTime = LocalDateTime.of(date, startTime);
        this.endTime = LocalDateTime.of(date, endTime);

        //converts the startTime and endTime, and date inputs to create a localDateTime to be converted into TimeStamp
        setStartDateTimeSQL(LocalDateTime.of(date, startTime));
        setEndDateTimeSQL(LocalDateTime.of(date, endTime));
        this.consultantName = consultantName;

    }
}
