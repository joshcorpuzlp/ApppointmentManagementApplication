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
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> contactComboBox;
    @FXML private ComboBox<String> userComboBox;


    //configure boolean flag variables
    private boolean confirmChanges = false;
    private boolean isTimeInvalid = false;
    private boolean isInputInvalid = false;
    private boolean isOutsideBusinessHours = false;

    public void cancelButtonPressed(ActionEvent actionEvent) throws IOException {

        confirmChanges = ProgramAlerts.cancelAlert();

        if (confirmChanges) {
            //Return to the MainPage.fxml
            MainMenuWindow.returnToMainMenu(actionEvent);
        }

    }

    //Method called when saveButton is Pressed. This will take in all the fields and selections, create an appointment, and update the database through AppointmentsDao
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
            startTimeComboBox.getSelectionModel().clearSelection();
            endTimeComboBox.getSelectionModel().clearSelection();
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
            LocalDate date = datePicker.getValue();

            //utilizes AppointmentManager.getAllUserHashMaps to get the userId from a list of userNames that are also keys to the hashMap
            int userId =  AppointmentManager.getAllUserHashMaps().get(userComboBox.getSelectionModel().getSelectedItem()) ;


            Appointment appointmentToAdd = new Appointment(appointmentId, location, type, customerName, contactName, startTime, endTime, date, userId);
            AppointmentManager.addAppointment(appointmentToAdd);
            appointmentDao.addObject(appointmentToAdd);

            //Returns to the MainPage.fxml
            MainMenuWindow.returnToMainMenu(actionEvent);
        }

    }

    //Method validates the start and end time inputs for the selected contact.
    //if time is invalid, it will set the flag variable to true, used in the saveButtonPressed as a validation method.
    public void timeValidation() {
        ObservableList<Appointment> contactAppointments = FXCollections.observableArrayList();

        for (int i = 0; i < AppointmentManager.getAllAppointments().size(); ++i) {
            if (contactComboBox.getSelectionModel().getSelectedItem()
                    .matches(AppointmentManager.getAllAppointments().get(i).getContactName())) {
                contactAppointments.add(AppointmentManager.getAllAppointments().get(i));
            }
        }

        LocalDateTime startTimeInput = LocalDateTime.of(datePicker.getValue(), startTimeComboBox.getSelectionModel().getSelectedItem());
        LocalDateTime endTimeInput = LocalDateTime.of(datePicker.getValue(), endTimeComboBox.getSelectionModel().getSelectedItem());

        for (Appointment contactAppointment : contactAppointments) {
            //check for whether the inputs clash with the selected contact's existing appointments.
            if (
                    (startTimeInput.isAfter(LocalDateTime.of(contactAppointment.getDate(), contactAppointment.getStartTime().toLocalTime())) &&
                            startTimeInput.isBefore(LocalDateTime.of(contactAppointment.getDate(), contactAppointment.getEndTime().toLocalTime()))) ||
                            (endTimeInput.isAfter(LocalDateTime.of(contactAppointment.getDate(), contactAppointment.getStartTime().toLocalTime())) &&
                                    endTimeInput.isBefore(LocalDateTime.of(contactAppointment.getDate(), contactAppointment.getEndTime().toLocalTime()))) ||
                            (startTimeInput.isEqual(LocalDateTime.of(contactAppointment.getDate(), contactAppointment.getStartTime().toLocalTime())) &&
                                    endTimeInput.isEqual(LocalDateTime.of(contactAppointment.getDate(), contactAppointment.getEndTime().toLocalTime())))


            ) {

                isTimeInvalid = ProgramAlerts.overlappingTimes();
                //need to return to exit the for loop when conditional statement goes through
                return;

            }
            //necessary to set the isTimeInvalid to false next time that the saveButtonPressed is called..
            else {
                isTimeInvalid = false;
            }

        }


    }

    //input validation to check if each field is not blank
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
                throw new myExceptions("The start time can not be after the end time");
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
            if (datePicker.getValue() == null) {
                throw new myExceptions("No date selected.\n");
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

    //Checks to make sure the start and end times the user selected are within business hours (9:00-17:00 UTD)
    public void insideBusinessHours() {
        ZonedDateTime startTime = LocalDateTime.of(datePicker.getValue(), startTimeComboBox.getSelectionModel().getSelectedItem()).atZone(ZoneId.systemDefault());
        ZonedDateTime endTime = LocalDateTime.of(datePicker.getValue(), endTimeComboBox.getSelectionModel().getSelectedItem()).atZone(ZoneId.systemDefault());



        ZonedDateTime estStart = startTime.toInstant().atZone(ZoneId.of("EST", ZoneId.SHORT_IDS));
        ZonedDateTime estEnd = endTime.toInstant().atZone(ZoneId.of("EST", ZoneId.SHORT_IDS));


        //Compare by using LocalTime datatypes
        LocalTime openingHour = LocalTime.parse("08:59");
        LocalTime closingHour = LocalTime.parse("17:01");

        Boolean startTimeAllowed = estStart.toLocalTime().isAfter(openingHour);
        Boolean endTimeAllowed = estEnd.toLocalTime().isBefore(closingHour);

        //if else is necessary to allow code to validate both ways.
        if (startTimeAllowed && endTimeAllowed)
            isOutsideBusinessHours = false;
        else
            isOutsideBusinessHours = true;

    }




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
        for (int hour = 5; hour <= 20; ++hour ) {
            for (int minutes = 0; minutes <= 45; minutes += 15) {
//                DateTimeFormatter myFormat = DateTimeFormatter.ofPattern("hh:mm a");

                LocalTime startTime = LocalTime.of(hour, minutes);
//                startTimeComboBox.getItems().add(startTime.format(myFormat));
                startTimeComboBox.getItems().add(startTime);
            }

        }

        //loads the ComboBox with the LocalTimes
        for (int hour = 5; hour <= 20; ++hour ) {
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
        datePicker.setDayCellFactory(picker -> new DateCell() {
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
