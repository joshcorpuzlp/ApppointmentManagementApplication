package Controller;

import Dao.AppointmentDao;
import Dao.ContactDao;
import Dao.CustomerDao;
import Dao.DivisionDao;
import Model.Appointment;
import Model.AppointmentManager;
import Model.Contact;
import Model.Customer;
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

import javax.swing.text.DateFormatter;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ResourceBundle;

public class ModifyAppointmentPageController implements Initializable {
    //Dao objects used to call the Dao methods
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

    @FXML private Button saveButton;
    @FXML private Button cancelButton;
    @FXML private Button deleteButton;

    private int selectedAppointmentIndex = MainPageController.selectedAppointmentIndex;
    private Appointment selectedAppointment = AppointmentManager.getAllAppointments().get(selectedAppointmentIndex);


    //Method that saves the current values and selections within the TextFields, ComboBoxes and DatePicker
    public void saveButtonPressed(ActionEvent actionEvent) throws SQLException, IOException {
        int appointmentId = selectedAppointment.getAppointmentId();
        String location = locationField.getText();
        String type = typeField.getText();
        String customerName = customerComboBox.getSelectionModel().getSelectedItem();
        String contactName = contactComboBox.getSelectionModel().getSelectedItem();
        LocalTime startTime = startTimeComboBox.getSelectionModel().getSelectedItem();
        LocalTime endTime = endTimeComboBox.getSelectionModel().getSelectedItem();
        LocalDate date = datePicker.getValue();

        Appointment appointmentModifications = new Appointment(appointmentId, location, type, customerName, contactName, startTime, endTime, date);
        AppointmentManager.updateAppointment(selectedAppointmentIndex, appointmentModifications);
        appointmentDao.modifyObject(appointmentModifications);

        //Return to the MainPage.fxml
        Parent root = FXMLLoader.load(getClass().getResource("../View/MainPage.fxml"));
        Scene MainPageScene = new Scene(root);

        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.setScene(MainPageScene);
        stage.show();


    }

    //Method that deletes the currently selected Appointment
    public void deleteButtonPressed(ActionEvent actionEvent) throws  SQLException, IOException {
        AppointmentManager.removeAppointment(selectedAppointment);
        appointmentDao.removeObject(selectedAppointment);

        //Return to the MainPage.fxml
        Parent root = FXMLLoader.load(getClass().getResource("../View/MainPage.fxml"));
        Scene MainPageScene = new Scene(root);

        Stage stage = (Stage) deleteButton.getScene().getWindow();
        stage.setScene(MainPageScene);
        stage.show();


    }

    //Method exits out of the ModifyAppointmentPage and back to the MainMenu
    public void cancelButtonPressed(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../View/MainPage.fxml"));
        Scene MainPageScene = new Scene(root);

        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.setScene(MainPageScene);
        stage.show();
    }



    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //loads the ObservableList of Customer objects within AppointmentManager with contents of the DB.
        divisionDao.loadDbObjects();
        customerDao.loadDbObjects();
        appointmentDao.loadDbObjects();
        contactDao.loadDbObjects();

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


        //Load the TextFields, ComboBoxes and DatePicker with the selectedAppointment's data members.
        locationField.setText(selectedAppointment.getLocation());
        typeField.setText(selectedAppointment.getType());
        customerComboBox.getSelectionModel().select(selectedAppointment.getCustomerName());
        startTimeComboBox.setValue(selectedAppointment.getStartTime().toLocalTime());
        endTimeComboBox.setValue(selectedAppointment.getEndTime().toLocalTime());
        datePicker.setValue(selectedAppointment.getDate());
        contactComboBox.getSelectionModel().select(selectedAppointment.getContactName());



    }

}
