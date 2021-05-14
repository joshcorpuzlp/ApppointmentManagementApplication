package Controller;

import Dao.AppointmentDao;
import Model.Appointment;
import Model.AppointmentManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainPageController implements Initializable {

    @FXML private Button addCustomerButton;
    @FXML private Button updateCustomerButton;
    @FXML private Button addAppointmentButton;
    @FXML private Button modifyAppointmentButton;

    @FXML private ToggleGroup dateFilterGroup;
    @FXML private RadioButton monthlyButton;
    @FXML private RadioButton weeklyButton;
    @FXML private RadioButton customButton;
    @FXML private DatePicker fromDatePicker;
    @FXML private DatePicker toDatePicker;

    @FXML private TableView<Appointment> appointmentCalendar;
    @FXML private TableColumn<Appointment, Date> dateColumn;
    @FXML private TableColumn<Appointment, String> startTimeColumn;
    @FXML private TableColumn<Appointment, String> endTimeColumn;
    @FXML private TableColumn<Appointment, String> locationColumn;
    @FXML private TableColumn<Appointment, String> customerIdColumn;
    @FXML private TableColumn<Appointment, Integer> appointmentIdColumn;
    @FXML private TableColumn<Appointment, String> contactIdColumn;
    @FXML private TableColumn<Appointment, String> appointmentTypeColumn;

    //Reference to the selected appointment from the appointmentCalendar. Used in ModifyAppointmentPage
    public static Appointment selectedAppointment;
    public static int selectedAppointmentIndex;

    private ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    private ObservableList<Appointment> filteredAppointments = FXCollections.observableArrayList();

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

    public void addAppointmentButtonPressed(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../View/AddAppointmentPage.fxml"));
        Scene MainPageScene = new Scene(root);

        Stage stage = (Stage) addAppointmentButton.getScene().getWindow();
        stage.setScene(MainPageScene);
        stage.show();
    }

    public void modifyAppointmentButtonPressed(ActionEvent actionEvent) throws IOException {
        //A reference to the selected appointment.
        selectedAppointment = appointmentCalendar.getSelectionModel().getSelectedItem();

        //saves the selected Appointment's index number for use in the ModifyAppointmentPage
        for (int i = 0; i < appointments.size(); i++) {
            if (appointments.get(i).equals(selectedAppointment)) {
                selectedAppointmentIndex = i;
            }
        }

        Parent root = FXMLLoader.load(getClass().getResource("../View/ModifyAppointmentPage.fxml"));
        Scene MainPageScene = new Scene(root);

        Stage stage = (Stage) modifyAppointmentButton.getScene().getWindow();
        stage.setScene(MainPageScene);
        stage.show();
    }


    //Method for filtering the tableView based on which radio button is toggled.
    public void dateFilterButtonSelected() {
        filteredAppointments.clear();

        //triggered when customButton is selected to enable the date pickers
        if (this.dateFilterGroup.getSelectedToggle().equals(this.customButton)) {
            fromDatePicker.setDisable(false);
            toDatePicker.setDisable(false);

            LocalDate fromDate = fromDatePicker.getValue();
            LocalDate toDate = toDatePicker.getValue();

            for (int i = 0; i < appointments.size(); ++i) {
                if ((appointments.get(i).getDate().equals(fromDate) || appointments.get(i).getDate().isAfter(fromDate)) &&
                        (appointments.get(i).getDate().equals(toDate) || appointments.get(i).getDate().isBefore(toDate)) ) {
                    System.out.println(true);
                    filteredAppointments.add(appointments.get(i));
                }
            }
            appointmentCalendar.setItems(filteredAppointments);

        }

        //triggered when monthlyButton is selected
        if (this.dateFilterGroup.getSelectedToggle().equals(this.monthlyButton)) {
            fromDatePicker.setDisable(true);
            toDatePicker.setDisable(true);

            for (int i = 0; i < appointments.size(); ++i) {
                if (appointments.get(i).getDate().getMonth() == LocalDate.now().getMonth()) {
                        System.out.println(true);
                    filteredAppointments.add(appointments.get(i));
                }
            }
            appointmentCalendar.setItems(filteredAppointments);



        }
        // triggered when weekly button is selected
        if (this.dateFilterGroup.getSelectedToggle().equals(this.weeklyButton)) {
            fromDatePicker.setDisable(true);
            toDatePicker.setDisable(true);

            fromDatePicker.setDisable(true);
            toDatePicker.setDisable(true);

            Locale locale = Locale.getDefault();

            final DayOfWeek firstDayOfWeek = WeekFields.of(locale).getFirstDayOfWeek();
            final DayOfWeek lastDayOfWeek = DayOfWeek.of(((firstDayOfWeek.getValue() + 5) % DayOfWeek.values().length) + 1);

            LocalDate firstDay = LocalDate.now().with(TemporalAdjusters.previousOrSame(firstDayOfWeek)); // first day
            LocalDate lastDay = LocalDate.now().with(TemporalAdjusters.nextOrSame(lastDayOfWeek));

            for (int i = 0; i < appointments.size(); ++i) {
                if ((appointments.get(i).getDate().equals(firstDay) || appointments.get(i).getDate().isAfter(firstDay)) &&
                        (appointments.get(i).getDate().equals(lastDay) || appointments.get(i).getDate().isBefore(lastDay)) ) {
                    System.out.println(true);
                    filteredAppointments.add(appointments.get(i));
                }
            }
            appointmentCalendar.setItems(filteredAppointments);


        }

    }





    @Override
    public void initialize(URL url, ResourceBundle rb) {
        appointmentDao.loadDbObjects();
        appointments = AppointmentManager.getAllAppointments();

        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("appointmentId"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("location"));
        appointmentTypeColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("type"));
        contactIdColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("contactName"));
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("customerName"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<Appointment, Date>("date"));
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("formattedStartTime"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("formattedEndTime"));

        appointmentCalendar.setItems(appointments);

        //configure the RadioButtons to be part of a toggleGroup
        dateFilterGroup = new ToggleGroup();
        monthlyButton.setToggleGroup(dateFilterGroup);
        weeklyButton.setToggleGroup(dateFilterGroup);
        customButton.setToggleGroup(dateFilterGroup);

        dateFilterGroup.selectToggle(null);

        fromDatePicker.setValue(LocalDate.now());
        toDatePicker.setValue(LocalDate.now());

        fromDatePicker.setDisable(true);
        toDatePicker.setDisable(true);


    }

}
