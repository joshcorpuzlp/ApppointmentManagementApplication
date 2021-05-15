package Controller;

import Dao.AppointmentDao;
import Dao.ContactDao;
import Dao.CustomerDao;
import Dao.DivisionDao;
import Model.Appointment;
import Model.AppointmentManager;
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
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class AddAppointmentPageController implements Initializable {
    DivisionDao divisionDao = new DivisionDao();
    CustomerDao customerDao = new CustomerDao();
    AppointmentDao appointmentDao = new AppointmentDao();
    ContactDao contactDao = new ContactDao();


    @FXML private TextField locationField;
    @FXML private TextField typeField;
    @FXML private ComboBox customerComboBox;
    @FXML private ComboBox startTimeComboBox;
    @FXML private ComboBox endTimeComboBox;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox contactComboBox;

    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    public void cancelButtonPressed(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../View/MainPage.fxml"));
        Scene MainPageScene = new Scene(root);

        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.setScene(MainPageScene);
        stage.show();
    }

    //need to fix to actually work with Appointment Constructor method.
    public void saveButtonPressed(ActionEvent actionEvent) throws IOException, SQLException {
//        System.out.println(locationField.getText().getClass());
//        System.out.println(typeField.getText().getClass());
//        System.out.println(customerComboBox.getSelectionModel().getSelectedItem().getClass());
//        System.out.println(startTimeComboBox.getSelectionModel().getSelectedItem().getClass());
//        System.out.println(endTimeComboBox.getSelectionModel().getSelectedItem().getClass());
//        System.out.println(datePicker.getEditor().getClass());

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
        int userId = AppointmentManager.getLoggedInUserId();


        Appointment appointmentToAdd = new Appointment(appointmentId, location, type, customerName, contactName, startTime, endTime, date, userId);
        AppointmentManager.addAppointment(appointmentToAdd);
        appointmentDao.addObject(appointmentToAdd);

        //Return to the MainPage.fxml
        Parent root = FXMLLoader.load(getClass().getResource("../View/MainPage.fxml"));
        Scene MainPageScene = new Scene(root);

        Stage stage = (Stage) saveButton.getScene().getWindow();
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

    }
}
