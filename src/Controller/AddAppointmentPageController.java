package Controller;

import Dao.AppointmentDao;
import Dao.ContactDao;
import Dao.CustomerDao;
import Dao.DivisionDao;
import Model.Appointment;
import Model.AppointmentManager;
import Utility.MainMenuWindow;
import Utility.ProgramAlerts;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.util.ResourceBundle;

public class AddAppointmentPageController implements Initializable {
    DivisionDao divisionDao = new DivisionDao();
    CustomerDao customerDao = new CustomerDao();
    AppointmentDao appointmentDao = new AppointmentDao();
    ContactDao contactDao = new ContactDao();


    @FXML private TextField locationField;
    @FXML private TextField typeField;
    @FXML private ComboBox<String> customerComboBox;
    @FXML private ComboBox<LocalTime> startTimeComboBox;
    @FXML private ComboBox<LocalTime> endTimeComboBox;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private ComboBox<String> contactComboBox;
    @FXML private ComboBox<String> userComboBox;


    //configure boolean flag variables
    private boolean confirmChanges = false;
    private boolean isTimeInvalid = false;
    private boolean isInputInvalid = false;
    private boolean isOutsideBusinessHours = false;


    /**
     * Method called when cancel button is pressed.
     * @param actionEvent triggered by button press
     * @throws IOException
     */
    public void cancelButtonPressed(ActionEvent actionEvent) throws IOException {

        confirmChanges = ProgramAlerts.cancelAlert();

        if (confirmChanges) {
            //Return to the MainPage.fxml
            MainMenuWindow.returnToMainMenu(actionEvent);
        }

    }

    /**
     * Method called when saveButton is Pressed. This will take in all the fields and selections, create an appointment, and update the database through AppointmentsDao.
     *
     * @param actionEvent triggered by button press
     * @throws IOException
     * @throws SQLException
     */
    public void saveButtonPressed(ActionEvent actionEvent) throws IOException, SQLException {

        //stores the errorMessage in a variable
        String errorMessage = inputValidation();

        if (isInputInvalid) {
            //the inputValidation method will make isInputValid = true and return the error message
            ProgramAlerts.inputValidationAlert(errorMessage);
            //exits the method to prevent it from saving.
            return;
        }

        //call the timeValidation method to check if time is valid for the selected user.
        //if invalid, it will trigger ProgramAlerts.overlappingTimes() and have it return false which will be in isTimeInvalid.
        timeValidation();

        //if isTimeInvalid == true, then clear startTime and endTimes and exit the saveButtonPressed method.
        //statement is skipped if isTimeInvalid == false
        if (isTimeInvalid) {
            return;
        }

        //check if time is within EST business hours
        insideBusinessHours();

        if (isOutsideBusinessHours) {
            ProgramAlerts.outsideBusinessHoursError();
            return;
        }

        //Calls the ProgramAlerts.saveChangesAlert and changes the flag boolean variable depending on the user response.
        confirmChanges = ProgramAlerts.saveChangesAlert();

        if (confirmChanges) {

            int appointmentId = AppointmentManager.getAllAppointments().size() + 1;
            String location = locationField.getText();
            String type = typeField.getText();
            String customerName = customerComboBox.getSelectionModel().getSelectedItem();
            String contactName = contactComboBox.getSelectionModel().getSelectedItem();
            LocalTime startTime = startTimeComboBox.getSelectionModel().getSelectedItem();
            LocalTime endTime = endTimeComboBox.getSelectionModel().getSelectedItem();
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();



            //utilizes AppointmentManager.getAllUserHashMaps to get the userId from a list of userNames that are also keys to the hashMap
            int userId =  AppointmentManager.getAllUserHashMaps().get(userComboBox.getSelectionModel().getSelectedItem()) ;


            Appointment appointmentToAdd = new Appointment(appointmentId, location, type, customerName, contactName, startTime, endTime, startDate, endDate, userId);
            AppointmentManager.addAppointment(appointmentToAdd);
            appointmentDao.addObject(appointmentToAdd);

            //Returns to the MainPage.fxml
            MainMenuWindow.returnToMainMenu(actionEvent);
        }

    }

    /**
     * Method validates the start and end time inputs for the selected contact and customer
     * if time is invalid, it will set the flag variable to true, used in the saveButtonPressed as a validation method.
     */
    public void timeValidation() {
        ObservableList<Appointment> contactAppointments = FXCollections.observableArrayList();

        for (int i = 0; i < AppointmentManager.getAllAppointments().size(); ++i) {
            if (contactComboBox.getSelectionModel().getSelectedItem()
                    .matches(AppointmentManager.getAllAppointments().get(i).getContactName())) {
                contactAppointments.add(AppointmentManager.getAllAppointments().get(i));
            }
        }

        LocalDateTime startTimeInput = LocalDateTime.of(startDatePicker.getValue(), startTimeComboBox.getSelectionModel().getSelectedItem());
        LocalDateTime endTimeInput = LocalDateTime.of(endDatePicker.getValue(), endTimeComboBox.getSelectionModel().getSelectedItem());

        for (Appointment contactAppointment : contactAppointments) {
            //check for whether the inputs clash with the selected contact's existing appointments.
            if (
                    (startTimeInput.isAfter(LocalDateTime.of(contactAppointment.getStartDate(), contactAppointment.getStartTime().toLocalTime())) &&
                            startTimeInput.isBefore(LocalDateTime.of(contactAppointment.getEndDate(), contactAppointment.getEndTime().toLocalTime()))) ||
                            (endTimeInput.isAfter(LocalDateTime.of(contactAppointment.getStartDate(), contactAppointment.getStartTime().toLocalTime())) &&
                                    endTimeInput.isBefore(LocalDateTime.of(contactAppointment.getEndDate(), contactAppointment.getEndTime().toLocalTime()))) ||
                            (startTimeInput.isEqual(LocalDateTime.of(contactAppointment.getStartDate(), contactAppointment.getStartTime().toLocalTime())) &&
                                    endTimeInput.isEqual(LocalDateTime.of(contactAppointment.getEndDate(), contactAppointment.getEndTime().toLocalTime()))) ||

                            //checks if start and end times over lap over another's start and endtimes
                            (startTimeInput.isBefore(LocalDateTime.of(contactAppointment.getStartDate(), contactAppointment.getStartTime().toLocalTime())) &&
                                    endTimeInput.isAfter(LocalDateTime.of(contactAppointment.getEndDate(), contactAppointment.getEndTime().toLocalTime())))


            ) {

                isTimeInvalid = true;
                ProgramAlerts.overlappingTimes("contact");
                //need to return to exit the for loop when conditional statement goes through
                return;

            }
            //necessary to set the isTimeInvalid to false next time that the saveButtonPressed is called..
            else {
                isTimeInvalid = false;
            }

        }


        ObservableList<Appointment> customerAppointments = FXCollections.observableArrayList();

        for (int i = 0; i < AppointmentManager.getAllAppointments().size(); ++i) {
            if (customerComboBox.getSelectionModel().getSelectedItem()
                    .matches(AppointmentManager.getAllAppointments().get(i).getCustomerName())) {
                customerAppointments.add(AppointmentManager.getAllAppointments().get(i));
            }
        }

        for (Appointment customerAppointment : customerAppointments) {
            if (
                    (startTimeInput.isAfter(LocalDateTime.of(customerAppointment.getStartDate(), customerAppointment.getStartTime().toLocalTime())) &&
                            startTimeInput.isBefore(LocalDateTime.of(customerAppointment.getEndDate(), customerAppointment.getEndTime().toLocalTime()))) ||
                            (endTimeInput.isAfter(LocalDateTime.of(customerAppointment.getStartDate(), customerAppointment.getStartTime().toLocalTime())) &&
                                    endTimeInput.isBefore(LocalDateTime.of(customerAppointment.getEndDate(), customerAppointment.getEndTime().toLocalTime()))) ||

                            //checks if start and end times over lap over another's start and endtimes
                            (startTimeInput.isBefore(LocalDateTime.of(customerAppointment.getStartDate(), customerAppointment.getStartTime().toLocalTime())) &&
                                    endTimeInput.isAfter(LocalDateTime.of(customerAppointment.getEndDate(), customerAppointment.getEndTime().toLocalTime())))


            ) {
                isTimeInvalid = true;
                ProgramAlerts.overlappingTimes("customer");
                //need to return to exit the for loop when conditional statement goes through
                return;
            }
            else {
                isTimeInvalid = false;
            }
        }


    }

    //

    /**
     * A method for input validation to check if each field is not blank
     * @return Returns a string object
     */
    public String inputValidation() {
        StringBuilder errorMessage = new StringBuilder();

        isInputInvalid = false;
        //validate each input
        try {

            if (locationField.getText().isBlank()) {
                throw new myExceptions("Location field is empty.\n");
            }
        }
        catch (myExceptions ex) {
            errorMessage.append(ex.getMessage());
            isInputInvalid = true;
        }

        try {
            if (typeField.getText().isBlank()) {
                throw new myExceptions("Type field is empty.\n");
            }
        }
        catch (myExceptions ex) {
            errorMessage.append(ex.getMessage());
            isInputInvalid = true;
        }

        try {
            if (customerComboBox.getSelectionModel().isEmpty()) {
                throw new myExceptions("No customer was selected.\n");
            }

        }
        catch (myExceptions ex) {
            errorMessage.append(ex.getMessage());
            isInputInvalid = true;
        }

        try {
            if (startTimeComboBox.getSelectionModel().isEmpty()) {
                throw new myExceptions("No start time selected.\n");
            }
            if (startTimeComboBox.getSelectionModel().getSelectedItem().isAfter(endTimeComboBox.getSelectionModel().getSelectedItem())) {
                throw new myExceptions("The start time can not be after the end time.\n");
            }
        }
        catch (myExceptions ex) {
            errorMessage.append((ex.getMessage()));
            isInputInvalid = true;
        }

        try {
            if (endTimeComboBox.getSelectionModel().isEmpty()) {
                throw new myExceptions("No end time selected.\n");
            }
        }
        catch (myExceptions ex) {
            errorMessage.append((ex.getMessage()));
            isInputInvalid = true;
        }

        try {
            if (startDatePicker.getValue() == null) {
                throw new myExceptions("No start date selected.\n");
            }
            if (startDatePicker.getValue().isAfter(endDatePicker.getValue())) {
                throw new myExceptions("Start date can not be after the end date.\n");
            }
        }
        catch (myExceptions ex) {
            errorMessage.append((ex.getMessage()));
            isInputInvalid = true;
        }

        try {
            if (endDatePicker.getValue() == null) {
                throw new myExceptions("No end date selected.\n");
            }
        }
        catch (myExceptions ex) {
            errorMessage.append((ex.getMessage()));
            isInputInvalid = true;
        }

        try {
            if (userComboBox.getSelectionModel().isEmpty()) {
                throw new myExceptions("No user selected.\n");
            }
        }
        catch (myExceptions ex) {
            errorMessage.append((ex.getMessage()));
            isInputInvalid = true;
        }

        try {
            if (contactComboBox.getSelectionModel().isEmpty()) {
                throw new myExceptions("No contact selected.\n");
            }
        }
        catch (myExceptions ex) {
            errorMessage.append((ex.getMessage()));
            isInputInvalid = true;
        }

        return errorMessage.toString();
    }

    //

    /**
     * Method that checks to make sure the start and end times the user selected are within business hours (8:00-22:00 EST)
     */
    public void insideBusinessHours() {
        ZonedDateTime startTime = LocalDateTime.of(startDatePicker.getValue(), startTimeComboBox.getSelectionModel().getSelectedItem()).atZone(ZoneId.systemDefault());
        ZonedDateTime endTime = LocalDateTime.of(startDatePicker.getValue(), endTimeComboBox.getSelectionModel().getSelectedItem()).atZone(ZoneId.systemDefault());



        ZonedDateTime estStart = startTime.toInstant().atZone(ZoneId.of("EST", ZoneId.SHORT_IDS));
        ZonedDateTime estEnd = endTime.toInstant().atZone(ZoneId.of("EST", ZoneId.SHORT_IDS));


        //Compared by using LocalTime datatypes of the project requirement of 8:00 am to 10:00 pm EST
        LocalTime openingHour = LocalTime.parse("07:59");
        LocalTime closingHour = LocalTime.parse("22:01");

        Boolean startTimeAllowed = estStart.toLocalTime().isAfter(openingHour);
        Boolean endTimeAllowed = estEnd.toLocalTime().isBefore(closingHour);

        System.out.println(estStart);
        System.out.println(estEnd);
        System.out.println(estStart.toLocalTime());
        System.out.println(estEnd.toLocalTime());
        System.out.println(openingHour);
        System.out.println(closingHour);

        //if else is necessary to allow code to validate both ways.
        if (startTimeAllowed && endTimeAllowed)
            isOutsideBusinessHours = false;
        else
            isOutsideBusinessHours = true;

    }


    /**
     * Method that initializes the AddAppointmentPage.
     * loads the ObservableList of Customer objects within AppointmentManager with contents of the DB.
     * loads the ComboBox with User userNames.
     * loads the ObservableList of Customer objects within the AppointmentManager to the customerComboBox.
     * loads the ComboBox with the LocalTimes.
     * loads the ComboBox with the LocalTimes.
     * load ContactComboBox with Contacts.
     * Lambda used to disable past dates and weekends from being selected.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //loads the ObservableList of Customer objects within AppointmentManager with contents of the DB.
        divisionDao.loadDbObjects();
        customerDao.loadDbObjects();
        appointmentDao.loadDbObjects();
        contactDao.loadDbObjects();

        //loads the ComboBox with User userNames
        for (int i = 0; i < AppointmentManager.getAllUsers().size(); ++i) {
            userComboBox.getItems().addAll(AppointmentManager.getAllUsers().get(i).getUserName());
        }

        //loads the ObservableList of Customer objects within the AppointmentManager to the customerComboBox
        for (int i = 0; i < AppointmentManager.getAllCustomers().size(); ++i) {
            customerComboBox.getItems().add(AppointmentManager.getCustomer(i).getCustomerName());
        }

        //loads the ComboBox with the LocalTimes
        for (int hour = 5; hour <= 21; ++hour ) {
            for (int minutes = 0; minutes <= 45; minutes += 15) {
//                DateTimeFormatter myFormat = DateTimeFormatter.ofPattern("hh:mm a");

                LocalTime startTime = LocalTime.of(hour, minutes);
//                startTimeComboBox.getItems().add(startTime.format(myFormat));
                startTimeComboBox.getItems().add(startTime);
            }

        }

        //loads the ComboBox with the LocalTimes
        for (int hour = 5; hour <= 21; ++hour ) {
            for (int minutes = 0; minutes <= 45; minutes += 15) {
//                DateTimeFormatter myFormat = DateTimeFormatter.ofPattern("hh:mm a");

                LocalTime endTime = LocalTime.of(hour, minutes);
//                endTimeComboBox.getItems().add(startTime.format(myFormat));
                endTimeComboBox.getItems().add(endTime);
            }

        }

        //load ContactComboBox with Contacts
        for (int i = 0; i < AppointmentManager.getAllContacts().size(); i++) {
            contactComboBox.getItems().add(AppointmentManager.getContact(i).getContactName());
        }

        //Lambda used to disable past dates and weekends from being selected
        startDatePicker.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                setDisable(empty || date.compareTo(today) < 0);
                if(date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY)
                    setDisable(true);
            }
        });

        //Lambda used to disable past dates and weekends from being selected
        endDatePicker.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                setDisable(empty || date.compareTo(today) < 0);
                if(date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY)
                    setDisable(true);
            }
        });

    }
}
