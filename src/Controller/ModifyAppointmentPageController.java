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
import java.util.Map;
import java.util.ResourceBundle;

public class ModifyAppointmentPageController implements Initializable {
    //Dao objects used to call the Dao methods
    DivisionDao divisionDao = new DivisionDao();
    CustomerDao customerDao = new CustomerDao();
    AppointmentDao appointmentDao = new AppointmentDao();
    ContactDao contactDao = new ContactDao();


    @FXML
    private TextField locationField;
    @FXML
    private TextField typeField;
    @FXML
    private ComboBox<String> customerComboBox;
    @FXML
    private ComboBox<LocalTime> startTimeComboBox;
    @FXML
    private ComboBox<LocalTime> endTimeComboBox;
    @FXML
    private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML
    private ComboBox<String> contactComboBox;
    @FXML
    private ComboBox<String> userComboBox;


    private Appointment selectedAppointment = MainController.selectedAppointment;
    int selectedAppointmentIndex = MainController.selectedAppointmentIndex;

    //configure boolean flag variables
    private boolean confirmChanges = false;
    private boolean isTimeInvalid = false;
    private boolean isInputInvalid = false;
    private boolean isOutsideBusinessHours = false;

    /**
     * Method that saves the current values and selections within the TextFields, ComboBoxes and DatePicker
     * @param actionEvent triggered by button pressed
     * @throws SQLException
     * @throws IOException
     */
    public void saveButtonPressed(ActionEvent actionEvent) throws SQLException, IOException {

        //stores the errorMessage in a variable
        String errorMessage = inputValidation(actionEvent);

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
            int appointmentId = selectedAppointment.getAppointmentId();
            String location = locationField.getText();
            String type = typeField.getText();
            String customerName = customerComboBox.getSelectionModel().getSelectedItem();
            String contactName = contactComboBox.getSelectionModel().getSelectedItem();
            LocalTime startTime = startTimeComboBox.getSelectionModel().getSelectedItem();
            LocalTime endTime = endTimeComboBox.getSelectionModel().getSelectedItem();
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();

            //utilizes AppointmentManager.getAllUserHashMaps to get the userId from a list of userNames that are also keys to the hashMap
            int userId = AppointmentManager.getAllUserHashMaps().get(userComboBox.getSelectionModel().getSelectedItem());
            System.out.println(userId);

            Appointment appointmentModifications = new Appointment(appointmentId, location, type, customerName, contactName, startTime, endTime, startDate, endDate, userId);
            AppointmentManager.updateAppointment(selectedAppointmentIndex, appointmentModifications);
            appointmentDao.modifyObject(appointmentModifications);

            //Return to the MainPage.fxml
            MainMenuWindow.returnToMainMenu(actionEvent);
        }

    }


    /**
     * Method that deletes the currently selected Appointment
     * @param actionEvent triggered by button pressed
     * @throws SQLException
     * @throws IOException
     */
    public void deleteButtonPressed(ActionEvent actionEvent) throws SQLException, IOException {

        confirmChanges = ProgramAlerts.deleteAlert();

        if (confirmChanges) {
            AppointmentManager.removeAppointment(selectedAppointment);
            appointmentDao.removeObject(selectedAppointment);

            //Return to the MainPage.fxml
            MainMenuWindow.returnToMainMenu(actionEvent);
        }

    }


    /**
     * Method exits out of the ModifyAppointmentPage and back to the MainMenu
     * @param actionEvent triggered by button pressed
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
     * Method validates the start and end time inputs for the selected contact or customer
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
        LocalDateTime endTimeInput = LocalDateTime.of(startDatePicker.getValue(), endTimeComboBox.getSelectionModel().getSelectedItem());

        for (Appointment contactAppointment : contactAppointments) {
            if (
                    (startTimeInput.isAfter(LocalDateTime.of(contactAppointment.getStartDate(), contactAppointment.getStartTime().toLocalTime())) &&
                            startTimeInput.isBefore(LocalDateTime.of(contactAppointment.getEndDate(), contactAppointment.getEndTime().toLocalTime()))) ||
                            (endTimeInput.isAfter(LocalDateTime.of(contactAppointment.getStartDate(), contactAppointment.getStartTime().toLocalTime())) &&
                                    endTimeInput.isBefore(LocalDateTime.of(contactAppointment.getEndDate(), contactAppointment.getEndTime().toLocalTime())))


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
                                    endTimeInput.isBefore(LocalDateTime.of(customerAppointment.getEndDate(), customerAppointment.getEndTime().toLocalTime())))
            ) {
                isTimeInvalid = true;
                ProgramAlerts.overlappingTimes("customer");
                //need to return to exit the for loop when conditional statement goes through
                return;

            }
            //necessary to set the isTimeInvalid to false next time that the saveButtonPressed is called..
            else {
                isTimeInvalid = false;
            }
        }
    }


    /**
     * input validation to check if each field is not blank
     * @param actionEvent triggered by button pressed
     * @return
     */
    public String inputValidation(ActionEvent actionEvent) {
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
                throw new myExceptions("No date selected.\n");
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


    /**
     * Checks to make sure the start and end times the user selected are within business hours (8:00-22:00 EST)
     */
    public void insideBusinessHours() {
        ZonedDateTime startTime = LocalDateTime.of(startDatePicker.getValue(), startTimeComboBox.getSelectionModel().getSelectedItem()).atZone(ZoneId.systemDefault());
        ZonedDateTime endTime = LocalDateTime.of(startDatePicker.getValue(), endTimeComboBox.getSelectionModel().getSelectedItem()).atZone(ZoneId.systemDefault());



        ZonedDateTime estStart = startTime.toInstant().atZone(ZoneId.of("EST", ZoneId.SHORT_IDS));
        ZonedDateTime estEnd = endTime.toInstant().atZone(ZoneId.of("EST", ZoneId.SHORT_IDS));


        //Compare by using LocalTime objects
        LocalTime openingHour = LocalTime.parse("07:59");
        LocalTime closingHour = LocalTime.parse("22:01");

        Boolean startTimeAllowed = estStart.toLocalTime().isAfter(openingHour);
        Boolean endTimeAllowed = estEnd.toLocalTime().isBefore(closingHour);

        //if else is necessary to allow code to validate both ways.
        if (startTimeAllowed && endTimeAllowed)
            isOutsideBusinessHours = false;
        else
            isOutsideBusinessHours = true;

    }

    /**
     * Initializes the ModifyAppointmentPage.
     * loads the ObservableList of Customer objects within AppointmentManager with contents of the DB.
     * loads the ComboBox with User userNames.
     * loads the ObservableList of Customer objects within the AppointmentManager to the customerComboBox.
     * loads the ComboBox with the LocalTimes.
     * load ContactComboBox with Contacts.
     * Load the TextFields, ComboBoxes and DatePicker with the selectedAppointment's data members.
     * Utilized Map to retrieve the key from a given value.
     * Lambda used to disable past dates and weekends from being selected.
     *
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
        for (int hour = 5; hour <= 20; ++hour) {
            for (int minutes = 0; minutes <= 45; minutes +=15) {


                LocalTime startTime = LocalTime.of(hour, minutes);
                startTimeComboBox.getItems().add(startTime);
            }

        }

        //loads the ComboBox with the LocalTimes
        for (int hour = 5; hour <= 20; ++hour) {
            for (int minutes = 0; minutes <= 45; minutes += 15) {

                LocalTime endTime = LocalTime.of(hour, minutes);
                endTimeComboBox.getItems().add(endTime);
            }

        }

        //load ContactComboBox with Contacts
        for (int i = 0; i < AppointmentManager.getAllContacts().size(); i++) {
            contactComboBox.getItems().add(AppointmentManager.getContact(i).getContactName());
        }


        //Load the TextFields, ComboBoxes and DatePicker with the selectedAppointment's data members.
        locationField.setText(selectedAppointment.getLocation());
        typeField.setText(selectedAppointment.getType());
        customerComboBox.getSelectionModel().select(selectedAppointment.getCustomerName());
        startTimeComboBox.setValue(selectedAppointment.getStartTime().toLocalTime());
        endTimeComboBox.setValue(selectedAppointment.getEndTime().toLocalTime());
        startDatePicker.setValue(selectedAppointment.getStartDate());
        endDatePicker.setValue(selectedAppointment.getEndDate());
        contactComboBox.getSelectionModel().select(selectedAppointment.getContactName());

        //Utilized Map to retrieve the key from a given value
        for (Map.Entry<String, Integer> entry : AppointmentManager.getAllUserHashMaps().entrySet()) {
            if (entry.getValue() == selectedAppointment.getUserId()) {
                userComboBox.getSelectionModel().select(entry.getKey());
            }
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