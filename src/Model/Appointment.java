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
    private User user;
    private int customerId;
    private String customerName;
    private String contactName;

    //need to fix Appointment class to instead take in Customer and Contact objects instead of just their names.
    private Customer customer;
    private Contact contact;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDate startDate;
    private LocalDate endDate;

    private Timestamp startDateTimeSQL;
    private Timestamp endDateTimeSQL;

    private String formattedStartTime;
    private String formattedEndTime;
    private String title;
    private String description;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the StarTime of the Appointment object as a LocalDateTime
     * @return LocalDateTime object
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * method that uses Timestamp Objects from the MySQL database as parameters. it will first assign it a UTC zone and then convert it to SystemZone.
     * @param startTime Passes a TimeStamp object
     */
    public void setStartTime(Timestamp startTime) {
        ZonedDateTime tempTime = startTime.toLocalDateTime().atZone(ZoneId.of("UTC"));
        ZoneId localZone = ZoneId.systemDefault();
        ZonedDateTime defaultZoned = tempTime.withZoneSameInstant(localZone);
        this.startTime = defaultZoned.toLocalDateTime();

    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    /**
     * Method that uses Timestamp Objects from the MySQL database as parameters. it will first assign it a UTC zone and then convert it to SystemZone.
     * @param endTime Passes a TimeStamp object.
     */
    public void setEndTime(Timestamp endTime) {
        ZonedDateTime tempTime = endTime.toLocalDateTime().atZone(ZoneId.of("UTC"));
        ZoneId localZone = ZoneId.systemDefault();
        ZonedDateTime defaultZoned = tempTime.withZoneSameInstant(localZone);
        this.endTime = defaultZoned.toLocalDateTime();

    }

    /**
     * Method does a similar function as the setStart/EndTime but creates a formatted string instead.
     * @param startTime Passes a TimeStamp object.
     */
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

    /**
     * Method does a similar function as the setStart/EndTime but creates a formatted string instead.
     * @param endTime Passes a TimeStamp object.
     */
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

    /**
     * Method used to convert the user inputs of LocalDateTime class to TimeStamp objects. Called in the constructor.
     * @param startTime Passes a LocalDateTime object
     */
    public void setStartDateTimeSQL(LocalDateTime startTime) {
        ZoneId utcZone = ZoneId.of("UTC");
        ZonedDateTime timeConvToUTC = startTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(utcZone);

        Timestamp timestamp = Timestamp.valueOf(timeConvToUTC.toLocalDateTime());
        this.startDateTimeSQL = timestamp;

    }

    /**
     * Method returns the TimeStamp objects of an Appointment Object.
     * @return LocalDateTime object
     */
    public Timestamp getStartDateTimeSQL() {
        return startDateTimeSQL;
    }


    /**
     * Method used to convert the user inputs of LocalDateTime class to TimeStamp objects. Called in the constructor.
     * @param endTime Passes a LocalDateTime object
     */
    public void setEndDateTimeSQL(LocalDateTime endTime) {
        ZoneId utcZone = ZoneId.of("UTC");
        ZonedDateTime timeConvToUTC = endTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(utcZone);

        Timestamp timestamp = Timestamp.valueOf(timeConvToUTC.toLocalDateTime());
        this.endDateTimeSQL = timestamp;
    }

    /**
     * Method returns the TimeStamp objects of an Appointment Object. Utilized with the AppointmentDAO
     * @return returns a TimeStamp object.
     */
    public Timestamp getEndDateTimeSQL() {
        return endDateTimeSQL;
    }

    /**
     * Method that retrieves the AppoinmentId
     * @return An integer value containing the AppointmentId
     */
    public int getAppointmentId() {
        return appointmentId;
    }

    /**
     * Method that sets the AppointmentId
     * @param appointmentId passes an integer value
     */
    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    /**
     * Method that retrieves the location of the Appointment
     * @return A String object containing the location for the appointment
     */
    public String getLocation() {
        return location;
    }

    /**
     * Method that sets the location of the Appointment
     * @param location Passes a String object.
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Method that retrieves the type of the Appointment
     * @return A String object containing of the type of the appointment
     */
    public String getType() {
        return type;
    }

    /**
     * Method that sets the type of Appointment
     * @param type Passes a String object
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Method that retrieves the related User's userId
     * @return An Integer value containing the integer
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Method that sets the related User's userId
     * @param userId passes an integer.
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Method that returns the related Contact's name34
     * @return String object containing the Customer name
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * Method sets the Customer name for the appointment
     * @param customerName
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * Method that returns the related Contact's name
     * @return String object containing the contact name
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * Method sets the contact name for the appointment
     * @param contactName
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**
     * Method that returns the date of the Appoointment
     * @return
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Sets the date for Appointment by converting the passed TimeStamp object
     * @param timestamp Utilizes a TimeStamp object as a parameter
     */
    public void setStartDate(Timestamp timestamp) {
        startDate = timestamp.toLocalDateTime().toLocalDate();
    }

    /**
     * Method that returns the date of the Appoointment
     * @return
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Sets the date for Appointment by converting the passed TimeStamp object
     * @param timestamp Utilizes a TimeStamp object as a parameter
     */
    public void setEndDate(Timestamp timestamp) {
        endDate = timestamp.toLocalDateTime().toLocalDate();
    }

    /**
     * Method sets the Customer name for the appointment
     * @param customerName
     */
    public void setCustomer(String customerName) {
        customer = AppointmentManager.getCustomerFromName(customerName);
    }

    /**
     * Method that retrieves the Customer object within the appointment object
     * @return Returns the Customer object related the appointment object.
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * Method sets the contact for the appointment
     * @param contactName Passes the contact name as a String object as an argument
     */
    public void setContact(String contactName) {
        contact = AppointmentManager.getContactFromName(contactName);
    }

    /**
     * Method that retrieves the contact object within the Appointment object
     * @return Returns the contact object related to the appointment object.
     */
    public Contact getContact() {
        return contact;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }



    //

    /**
     * Constructor for the Appointment class. Used when creating from Database data.
     * @param appointmentId Passes an integer
     * @param location Passes a String object
     * @param type Passes a String object
     * @param userId Passes an integer
     * @param customerName Passes a String object
     * @param contactName Passes a String object
     * @param startTime Passes a LocalTime object
     * @param endTime Passes a LocalTime object
     */
    public Appointment(int appointmentId, String location, String type, int userId, int customerId, String customerName, String contactName, Timestamp startTime, Timestamp endTime, String title, String description) {
        this.appointmentId = appointmentId;
        this.location = location;
        this.type = type;
        this.userId = userId;
        this.customerName = customerName;
        this.customerId = customerId;



        setStartTime(startTime);
        setEndTime(endTime);
        this.contactName = contactName;
        setStartDate(startTime);
        setEndDate(endTime);
        setEndDate(endTime);
        setFormattedStartTime(startTime);
        setFormattedEndTime(endTime);


        //method that relates the Customer objects and Contact objects to an appointment via String input of names
        setCustomer(customerName);
        setContact(contactName);


        this.title = title;
        this.description = description;

    }


//    /**
//     * Overloaded constructor for the Appointment class. Used when creating from User Input
//     * @param appointmentId Passes an integer
//     * @param location Passes a String object
//     * @param type Passes a String object
//     * @param customerName Passes a String object
//     * @param contactName Passes a String object
//     * @param startTime Passes a LocalTime object
//     * @param endTime Passes a LocalTime object
//     * @param startDate Passes a LocalDate object
//     * @param userId Passes an integer
//     */
//    public Appointment(int appointmentId, String location, String type, String customerName, String contactName, LocalTime startTime, LocalTime endTime, LocalDate startDate, int userId) {
//        this.appointmentId = appointmentId;
//        this.location = location;
//        this.type = type;
//        this.customerName = customerName;
//
//        //converts startTime, endTime and date inputs to create LocalDateTime objects.
//        this.startDate = startDate;
//        this.startTime = LocalDateTime.of(startDate, startTime);
//        this.endTime = LocalDateTime.of(startDate, endTime);
//
//        //converts the startTime and endTime, and date inputs to create a localDateTime to be converted into TimeStamp
//        setStartDateTimeSQL(LocalDateTime.of(startDate, startTime));
//        setEndDateTimeSQL(LocalDateTime.of(startDate, endTime));
//        this.contactName = contactName;
//
//        //method that relates the Customer objects and Contact objects to an appointment via String input of names
//        setCustomer(customerName);
//        setContact(contactName);
//
//        this.userId = userId;
//
//    }

    /**
     * Overloaded constructor for the Appointment class. Used when creating from User Input
     * @param appointmentId Passes an integer
     * @param location Passes a String object
     * @param type Passes a String object
     * @param customerName Passes a String object
     * @param contactName Passes a String object
     * @param startTime Passes a LocalTime object
     * @param endTime Passes a LocalTime object
     * @param startDate Passes a LocalDate object
     * @param userId Passes an integer
     */
    public Appointment(int appointmentId, String location, String type, String customerName, String contactName, LocalTime startTime, LocalTime endTime, LocalDate startDate, LocalDate endDate, int userId) {
        this.appointmentId = appointmentId;
        this.location = location;
        this.type = type;
        this.customerName = customerName;


        //converts startTime, endTime and date inputs to create LocalDateTime objects.
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = LocalDateTime.of(startDate, startTime);
        this.endTime = LocalDateTime.of(endDate, endTime);

        //converts the startTime and endTime, and date inputs to create a localDateTime to be converted into TimeStamp
        setStartDateTimeSQL(LocalDateTime.of(startDate, startTime));
        setEndDateTimeSQL(LocalDateTime.of(endDate, endTime));
        this.contactName = contactName;

        //method that relates the Customer objects and Contact objects to an appointment via String input of names
        setCustomer(customerName);
        setCustomer(customerName);
        setContact(contactName);

        this.userId = userId;

    }
}
