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
import java.util.stream.Collectors;

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

            //Utilized stream instead of for loop to create filters by date.
            appointmentCalendar.setItems(appointments.stream()
                    .filter(appointment -> appointment.getDate().equals(fromDate) || appointment.getDate().isAfter(fromDate))
                    .filter(appointment -> appointment.getDate().equals(toDate) || appointment.getDate().isBefore(toDate))
                    .collect(Collectors.toCollection(FXCollections::observableArrayList)));

        }

        //triggered when monthlyButton is selected
        if (this.dateFilterGroup.getSelectedToggle().equals(this.monthlyButton)) {
            fromDatePicker.setDisable(true);
            toDatePicker.setDisable(true);

            //used streams instead of a for loop. Utilized a lambda to convert appointment to appointment.getDate()
            appointmentCalendar.setItems(appointments.stream()
                    .filter(appointment -> appointment.getDate().getMonth() == LocalDate.now().getMonth())
                    .filter(appointment -> appointment.getDate().getYear() == LocalDate.now().getYear())
                    .collect(Collectors.toCollection(FXCollections::observableArrayList)));



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


            appointmentCalendar.setItems(
                    appointments.stream()
                            .filter(appointment -> appointment.getDate().equals(firstDay) || appointment.getDate().isAfter(firstDay))
                            .filter(appointment -> appointment.getDate().equals(lastDay) || appointment.getDate().isBefore(lastDay))
                            .collect(Collectors.toCollection(FXCollections::observableArrayList)));


        }

    }

    //method that handles changes to the DatePickers
    public void handle(ActionEvent actionEvent) {
        filteredAppointments.clear();
        LocalDate fromDate = fromDatePicker.getValue();
        LocalDate toDate = toDatePicker.getValue();

        //Utilized stream instead of for loop to create filters by date.
        appointmentCalendar.setItems(appointments.stream()
                .filter(appointment -> appointment.getDate().equals(fromDate) || appointment.getDate().isAfter(fromDate))
                .filter(appointment -> appointment.getDate().equals(toDate) || appointment.getDate().isBefore(toDate))
                .collect(Collectors.toCollection(FXCollections::observableArrayList)));
    }

    public void handleCalendarChange(ActionEvent actionEvent) {
        modifyAppointmentButton.setDisable(false);
    }




    @Override
    public void initialize(URL url, ResourceBundle rb) {
        appointmentDao.loadDbObjects();
        appointments = AppointmentManager.getAllAppointments();

        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        appointmentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        contactIdColumn.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("formattedStartTime"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("formattedEndTime"));

        //initialize the appointmentCalendar to only allow one selection at a time.
        appointmentCalendar.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        //initialize the contents of the appointmentCalendar
        appointmentCalendar.setItems(appointments);

        //configure the RadioButtons to be part of a toggleGroup
        dateFilterGroup = new ToggleGroup();
        monthlyButton.setToggleGroup(dateFilterGroup);
        weeklyButton.setToggleGroup(dateFilterGroup);
        customButton.setToggleGroup(dateFilterGroup);

        //Initialize the dateFilterGroup to have no selection
        dateFilterGroup.selectToggle(null);

        //Initializes the fromDatePicker and toDatePicker to show the current date.
        fromDatePicker.setValue(LocalDate.now());
        toDatePicker.setValue(LocalDate.now());

        //Disables the fromDatePicker and toDatePicker. Enabled by actionEvent.
        fromDatePicker.setDisable(true);
        toDatePicker.setDisable(true);

        //ModifyAppointmentButton should be disabled until an appointment is selected
        modifyAppointmentButton.setDisable(true);


        /*create a listener that checks for a selection within the appointmentCalendar. If newSelection is not null, then we can enable the modifyAppointmentButton using a lambda
            else we set it to disable it again.
         */
        appointmentCalendar.getSelectionModel().selectedItemProperty().addListener((abs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                modifyAppointmentButton.setDisable(false);
            }
            else {
                modifyAppointmentButton.setDisable(true);
            }
        });


    }


}
