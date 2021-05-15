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
    private String contactName;

    //need to fix Appointment class to instead take in Customer and Contact objects instead of just their names.
    private Customer customer;
    private Contact contact;

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


    /*
    *setters and getters for converting LocalDateTime to TimeStamp, used with User created appointments
     */

    //Method used to convert the user inputs of LocalDateTime class to TimeStamp objects. Called in the constructor.
    public void setStartDateTimeSQL(LocalDateTime startTime) {
        ZoneId utcZone = ZoneId.of("UTC");
        ZonedDateTime timeConvToUTC = startTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(utcZone);

        Timestamp timestamp = Timestamp.valueOf(timeConvToUTC.toLocalDateTime());
        this.startDateTimeSQL = timestamp;

    }
    //method returns the TimeStamp objects of an Appointment Object.
    public Timestamp getStartDateTimeSQL() {
        return startDateTimeSQL;
    }

    //Method used to convert the user inputs of LocalDateTime class to TimeStamp objects. Called in the constructor.
    public void setEndDateTimeSQL(LocalDateTime endTime) {
        ZoneId utcZone = ZoneId.of("UTC");
        ZonedDateTime timeConvToUTC = endTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(utcZone);

        Timestamp timestamp = Timestamp.valueOf(timeConvToUTC.toLocalDateTime());
        this.endDateTimeSQL = timestamp;
    }

    //method returns the TimeStamp objects of an Appointment Object.
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

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(Timestamp timestamp) {
        date = timestamp.toLocalDateTime().toLocalDate();
    }


    public void setCustomer(String customerName) {
        customer = AppointmentManager.getCustomerFromName(customerName);
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setContact(String contactName) {
        contact = AppointmentManager.getContactFromName(contactName);
    }
    public Contact getContact() {
        return contact;
    }



    //constructor for the Appointment class. Used when creating from Database data.
    public Appointment(int appointmentId, String location, String type, int userId, String customerName, String contactName, Timestamp startTime, Timestamp endTime) {
        this.appointmentId = appointmentId;
        this.location = location;
        this.type = type;
        this.userId = userId;
        this.customerName = customerName;
        setStartTime(startTime);
        setEndTime(endTime);
        this.contactName = contactName;
        setDate(startTime);
        setFormattedStartTime(startTime);
        setFormattedEndTime(endTime);

        //method that relates the Customer objects and Contact objects to an appointment via String input of names
        setCustomer(customerName);
        setContact(contactName);

    }

    //overloaded constructor for the Appointment class. Used when creating from User Input
    public Appointment(int appointmentId, String location, String type, String customerName, String contactName, LocalTime startTime, LocalTime endTime, LocalDate date, int userId) {
        this.appointmentId = appointmentId;
        this.location = location;
        this.type = type;
        this.customerName = customerName;

        //converts startTime, endTime and date inputs to create LocalDateTime objects.
        this.date = date;
        this.startTime = LocalDateTime.of(date, startTime);
        this.endTime = LocalDateTime.of(date, endTime);

        //converts the startTime and endTime, and date inputs to create a localDateTime to be converted into TimeStamp
        setStartDateTimeSQL(LocalDateTime.of(date, startTime));
        setEndDateTimeSQL(LocalDateTime.of(date, endTime));
        this.contactName = contactName;

        //method that relates the Customer objects and Contact objects to an appointment via String input of names
        setCustomer(customerName);
        setContact(contactName);

        this.userId = userId;

    }
}
