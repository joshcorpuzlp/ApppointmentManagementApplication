package Controller;

import Dao.AppointmentDao;
import Model.Appointment;
import Model.AppointmentManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.ResourceBundle;

public class MainPageController implements Initializable {

    @FXML private Button addCustomerButton;
    @FXML private Button updateCustomerButton;
    @FXML private Button addAppointmentButton;
    @FXML private Button modifyAppointmentButton;

    @FXML private ToggleGroup dateFilterGroup;
    @FXML private RadioButton monthlyButton;
    @FXML private RadioButton weeklyButton;
    @FXML private DatePicker fromDatePicker;
    @FXML private DatePicker toDatePicker;

    @FXML private TableView appointmentCalendar;
    @FXML private TableColumn dateColumn;
    @FXML private TableColumn startTimeColumn;
    @FXML private TableColumn endTimeColumn;
    @FXML private TableColumn locationColumn;
    @FXML private TableColumn customerIdColumn;
    @FXML private TableColumn appointmentIdColumn;
    @FXML private TableColumn consultantIdColumn;
    @FXML private TableColumn appointmentTypeColumn;

    private AppointmentDao appointmentDao = new AppointmentDao();

    public void addCustomerButtonPressed(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../View/AddCustomerPage.fxml"));
        Scene MainPageScene = new Scene(root);

        Stage stage = (Stage) addCustomerButton.getScene().getWindow();
        stage.setScene(MainPageScene);
        stage.show();
    }

    public void updateCustomerButtonPressed(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../View/UpdateCustomerPage.fxml"));
        Scene MainPageScene = new Scene(root);

        Stage stage = (Stage) updateCustomerButton.getScene().getWindow();
        stage.setScene(MainPageScene);
        stage.show();
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        appointmentDao.loadDbObjects();

        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("appointmentId"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("location"));
        appointmentTypeColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("type"));
        consultantIdColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("consultantName"));
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("customerName"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<Appointment, Date>("date"));
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("formattedStartTime"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("formattedEndTime"));

        appointmentCalendar.setItems(AppointmentManager.getAllAppointments());

    }

}
