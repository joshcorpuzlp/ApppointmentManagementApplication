package Controller;

import Dao.AppointmentDao;
import Dao.ContactDao;
import Dao.CustomerDao;
import Dao.DivisionDao;
import Model.Appointment;
import Model.AppointmentManager;
import Model.Contact;
import Utility.MainMenuWindow;
import Utility.ProgramAlerts;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
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

    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    //configure boolean flag variables
    private boolean confirmChanges = false;
    private boolean isTimeInvalid = false;
    private boolean isInputInvalid = false;

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

        LocalTime startTimeInput = startTimeComboBox.getSelectionModel().getSelectedItem();
        LocalTime endTimeInput = endTimeComboBox.getSelectionModel().getSelectedItem();

        for (int i = 0; i < contactAppointments.size(); ++i) {
            if(
                    (startTimeInput.isAfter(contactAppointments.get(i).getStartTime().toLocalTime()) && startTimeInput.isBefore(contactAppointments.get(i).getEndTime().toLocalTime()) ) ||
                            (endTimeInput.isAfter(contactAppointments.get(i).getStartTime().toLocalTime()) && endTimeInput.isBefore(contactAppointments.get(i).getEndTime().toLocalTime()) )
            )
            {

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
        for (int hour = 8; hour <= 17; ++hour ) {
            for (int minutes = 00; minutes <= 30; minutes += 30) {
//                DateTimeFormatter myFormat = DateTimeFormatter.ofPattern("hh:mm a");

                LocalTime startTime = LocalTime.of(hour, minutes);
//                startTimeComboBox.getItems().add(startTime.format(myFormat));
                startTimeComboBox.getItems().add(startTime);
            }

        }

        //loads the ComboBox with the LocalTimes
        for (int hour = 8; hour <= 17; ++hour ) {
            for (int minutes = 00; minutes <= 30; minutes += 30) {
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

    }
}
