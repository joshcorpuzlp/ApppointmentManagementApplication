package Controller;

import Dao.AppointmentDao;
import Dao.CustomerDao;
import Model.Appointment;
import Model.AppointmentManager;
import Model.User;
import Utility.ProgramAlerts;
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
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MainController implements Initializable {

    @FXML private Button addCustomerButton;
    @FXML private Button updateCustomerButton;
    @FXML private Button addAppointmentButton;
    @FXML private Button modifyAppointmentButton;
    @FXML private Button reportsButton;

    @FXML private ToggleGroup dateFilterGroup;
    @FXML private RadioButton monthlyButton;
    @FXML private RadioButton weeklyButton;
    @FXML private RadioButton customButton;
    @FXML private DatePicker fromDatePicker;
    @FXML private DatePicker toDatePicker;

    @FXML private TableView<Appointment> appointmentCalendar;
    @FXML private TableColumn<Appointment, Date> startDateColumn;
    @FXML private TableColumn<Appointment, Date> endDateColumn;
    @FXML private TableColumn<Appointment, String> startTimeColumn;
    @FXML private TableColumn<Appointment, String> endTimeColumn;
    @FXML private TableColumn<Appointment, String> locationColumn;
    @FXML private TableColumn<Appointment, Integer> customerIdColumn;
    @FXML private TableColumn<Appointment, String> customerNameColumn;
    @FXML private TableColumn<Appointment, Integer> appointmentIdColumn;
    @FXML private TableColumn<Appointment, String> contactNameColumn;
    @FXML private TableColumn<Appointment, String> appointmentTypeColumn;
    @FXML private TableColumn<Appointment, String> titleColumn;
    @FXML private TableColumn<Appointment,String> descriptionColumn;


    //Reference to the selected appointment from the appointmentCalendar. Used in ModifyAppointmentPage
    public static Appointment selectedAppointment;
    public static int selectedAppointmentIndex;

    private ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    private ObservableList<Appointment> filteredAppointments = FXCollections.observableArrayList();

    private AppointmentDao appointmentDao = new AppointmentDao();
    private CustomerDao customerDao = new CustomerDao();


    //create a variable reference to the current user
    private User currentUser;
    //create an ObservableList of Appointment objects of the currentUser
    private ObservableList<Appointment> userAppointments = FXCollections.observableArrayList();



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

    /**
     * Method for filtering the tableView based on which radio button is toggled.
     * used streams instead of a for loop. Utilized a lambda to convert appointment to appointment.getDate()
     */
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
                    .filter(appointment -> appointment.getStartDate().equals(fromDate) || appointment.getStartDate().isAfter(fromDate))
                    .filter(appointment -> appointment.getStartDate().equals(toDate) || appointment.getStartDate().isBefore(toDate))
                    .collect(Collectors.toCollection(FXCollections::observableArrayList)));

        }

        //triggered when monthlyButton is selected
        if (this.dateFilterGroup.getSelectedToggle().equals(this.monthlyButton)) {
            fromDatePicker.setDisable(true);
            toDatePicker.setDisable(true);


            //used streams instead of a for loop. Utilized a lambda to convert appointment to appointment.getDate()
            appointmentCalendar.setItems(appointments.stream()
                    .filter(appointment -> appointment.getStartDate().getMonth() == LocalDate.now().getMonth())
                    .filter(appointment -> appointment.getStartDate().getYear() == LocalDate.now().getYear())
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
                            .filter(appointment -> appointment.getStartDate().equals(firstDay) || appointment.getStartDate().isAfter(firstDay))
                            .filter(appointment -> appointment.getStartDate().equals(lastDay) || appointment.getStartDate().isBefore(lastDay))
                            .collect(Collectors.toCollection(FXCollections::observableArrayList)));


        }

    }


    /**
     * Method that handles changes to the DatePickers
     * @param actionEvent
     */
    public void handle(ActionEvent actionEvent) {
        filteredAppointments.clear();
        LocalDate fromDate = fromDatePicker.getValue();
        LocalDate toDate = toDatePicker.getValue();

        //Utilized stream instead of for loop to create filters by date.
        appointmentCalendar.setItems(appointments.stream()
                .filter(appointment -> appointment.getStartDate().equals(fromDate) || appointment.getStartDate().isAfter(fromDate))
                .filter(appointment -> appointment.getStartDate().equals(toDate) || appointment.getStartDate().isBefore(toDate))
                .collect(Collectors.toCollection(FXCollections::observableArrayList)));
    }

    /**
     * Method that handles changes in the calendar
     * @param actionEvent triggered by button press
     */
    public void handleCalendarChange(ActionEvent actionEvent) {
        modifyAppointmentButton.setDisable(false);
    }

    /**
     * Method that opens the ReportsPage
     * @param actionEvent triggered by button pressed
     * @throws IOException
     */
    public void reportsButtonPressed(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../View/ReportsPage.fxml"));
        Scene MainPageScene = new Scene(root);

        Stage stage = (Stage) reportsButton.getScene().getWindow();
        stage.setScene(MainPageScene);
        stage.show();
    }


    /**
     * Method that initializes the MainController page
     * created a listener that checks for a selection within the appointmentCalendar.
     * If newSelection is not null, then we can enable the modifyAppointmentButton using a Lambda
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        customerDao.loadDbObjects();
        appointmentDao.loadDbObjects();
        appointments = AppointmentManager.getAllAppointments();

        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        appointmentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        contactNameColumn.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));

        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("formattedStartTime"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("formattedEndTime"));

        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

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


        //created a listener that checks for a selection within the appointmentCalendar. If newSelection is not null, then we can enable the modifyAppointmentButton using a lambda
        //else we set it to disable it again.
        appointmentCalendar.getSelectionModel().selectedItemProperty().addListener((abs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                modifyAppointmentButton.setDisable(false);
            }
            else {
                modifyAppointmentButton.setDisable(true);
            }
        });

        //Created a for loop that will load the selected User based on AppointmentManager's loggedInUserId
        for (int i = 0; i < AppointmentManager.getAllUsers().size(); ++i) {
            if (AppointmentManager.getLoggedInUserId() == AppointmentManager.getAllUsers().get(i).getUserId()) {
                currentUser = AppointmentManager.getAllUsers().get(i);
            }
        }
        //Created a for loop that will load the ObservableList userAppointments with Appointments that have the same userId as the currentUser
        for (int i = 0; i < AppointmentManager.getAllAppointments().size(); ++i) {
            if (currentUser.getUserId() == AppointmentManager.getAllAppointments().get(i).getUserId()) {
                userAppointments.add(AppointmentManager.getAllAppointments().get(i));
            }
        }

        //flag variable used to determine whether the program will be calling the ProgramAlerts.noPendingAppointmentAlert()
        boolean noUpcomingAppointments = false;

        //created a for loop that checks userAppointments startTimes to check if it is within 15 minutes of the currentTime, if there is, it calls ProgramAlerts.pendingAppointmentAlert()
        for (Appointment userAppointment : userAppointments) {

            if ((LocalDateTime.now().isBefore(userAppointment.getStartTime()) &&
                    LocalDateTime.now().isAfter(userAppointment.getStartTime().minus(15, ChronoUnit.MINUTES)))) {
                ProgramAlerts.pendingAppointmentAlert();
                noUpcomingAppointments = false;
                break;
            }
            else {
                noUpcomingAppointments = true;
            }

        }

        if (noUpcomingAppointments) {
            ProgramAlerts.noPendingAppointmentAlert();
        }

    }


}
