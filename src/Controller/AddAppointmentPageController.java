package Controller;

import Dao.AppointmentDao;
import Dao.ContactDao;
import Dao.CustomerDao;
import Dao.DivisionDao;
import Model.Appointment;
import Model.AppointmentManager;
import Utility.MainMenuWindow;
import Utility.ProgramAlerts;
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

    private boolean confirmChanges = false;

    public void cancelButtonPressed(ActionEvent actionEvent) throws IOException {

        confirmChanges = ProgramAlerts.cancelAlert();

        if (confirmChanges) {
            //Return to the MainPage.fxml
            MainMenuWindow.returnToMainMenu(actionEvent);
        }

    }

    //need to fix to actually work with Appointment Constructor method.
    public void saveButtonPressed(ActionEvent actionEvent) throws IOException, SQLException {

        //Calls the ProgramAlerts.saveChangesAlert and changes the flag boolean variable depending on the user response.

        confirmChanges = ProgramAlerts.saveChangesAlert();

        if (confirmChanges) {
            System.out.println(locationField.getText());
            System.out.println(typeField.getText());
            System.out.println(customerComboBox.getSelectionModel().getSelectedItem());
            System.out.println(startTimeComboBox.getSelectionModel().getSelectedItem());
            System.out.println(endTimeComboBox.getSelectionModel().getSelectedItem());
            System.out.println(datePicker.getEditor().getText());

            int appointmentId = AppointmentManager.getAllAppointments().size() + 1;
            String location = locationField.getText();
            String type = typeField.getText();
            String customerName = (String) customerComboBox.getSelectionModel().getSelectedItem();
            String contactName = (String) contactComboBox.getSelectionModel().getSelectedItem();
            LocalTime startTime = (LocalTime) startTimeComboBox.getSelectionModel().getSelectedItem();
            LocalTime endTime = (LocalTime) endTimeComboBox.getSelectionModel().getSelectedItem();
            LocalDate date = datePicker.getValue();

            //utilizes AppointmentManager.getAllUserHashMaps to get the userId from a list of userNames that are also keys to the hashMap
            int userId =  AppointmentManager.getAllUserHashMaps().get(userComboBox.getSelectionModel().getSelectedItem()) ;


            Appointment appointmentToAdd = new Appointment(appointmentId, location, type, customerName, contactName, startTime, endTime, date, userId);
            AppointmentManager.addAppointment(appointmentToAdd);
            appointmentDao.addObject(appointmentToAdd);

            //Return to the MainPage.fxml
            MainMenuWindow.returnToMainMenu(actionEvent);
        }

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
