package Controller;

import Dao.AppointmentDao;
import Dao.CustomerDao;
import Dao.DivisionDao;
import Model.AppointmentManager;
import Model.Customer;
import Utility.MainMenuWindow;
import Utility.ProgramAlerts;
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
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UpdateCustomerPageController implements Initializable {

    //configures the buttons inside the AddCustomerPage
    @FXML private Button saveNewCustomer;
    @FXML private Button cancelButton;
    @FXML private Button deleteButton;


    //configures the TextFields for user input
    @FXML private TextField customerNameField;
    @FXML private TextField addressField;
    @FXML private TextField postalField;
    @FXML private TextField phoneField;
    //configures the ComboBox selection for user input
    @FXML private ComboBox<String> divisionComboBox;

    //configures the TableView to contain the ObservableList of CustomerObjects
    @FXML private TableView customerTableView;
    @FXML private TableColumn<Customer, String> customerNameColumn;
    @FXML private TableColumn<Customer, String> customerAddressColumn;
    @FXML private TableColumn<Customer, String> customerDivisionColumn;
    @FXML private TableColumn<Customer, String> customerPostalColumn;
    @FXML private TableColumn<Customer, String> customerPhoneColumn;

    private CustomerDao customerDao = new CustomerDao();
    private DivisionDao divisionDao = new DivisionDao();
    private AppointmentDao appointmentDao = new AppointmentDao();

    private boolean confirmChanges = false;
    private boolean isInputInvalid = false;
    private boolean customerAppointmentExists = false;


    /**
     * called when the user wants to save the changes made.
     * @param actionEvent triggered on button press.
     * @throws SQLException
     * @throws IOException
     */
    public void saveButtonPressed(ActionEvent actionEvent) throws SQLException, IOException {
        int selectedIndex;
        selectedIndex = customerTableView.getSelectionModel().getFocusedIndex();

        //stores the errorMessage in a variable
        String errorMessage = inputValidation(actionEvent);

        if (isInputInvalid) {
            //the inputValidation method will make isInputValid = true and return the error message
            ProgramAlerts.inputValidationAlert(errorMessage);
            //exits the method to prevent it from saving.
            return;
        }

        //Calls the ProgramAlerts.saveChangesAlert and changes the flag boolean variable depending on the user response.
        confirmChanges = ProgramAlerts.saveChangesAlert();

        if (confirmChanges) {
            Customer selectedCustomer = AppointmentManager.getAllCustomers().get(selectedIndex);
            selectedCustomer.setCustomerName(customerNameField.getText());
            selectedCustomer.setCustomerAddress(addressField.getText());
            selectedCustomer.setCustomerPostalCode(postalField.getText());
            selectedCustomer.setPhoneNumber(phoneField.getText());
            selectedCustomer.setCustomerDivision(divisionComboBox.getSelectionModel().getSelectedItem());

            AppointmentManager.updateCustomer(selectedIndex, selectedCustomer);
            customerDao.modifyObject(selectedCustomer);

            //Return to the MainPage.fxml
            Parent root = FXMLLoader.load(getClass().getResource("../View/MainPage.fxml"));
            Scene MainPageScene = new Scene(root);

            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.setScene(MainPageScene);
            stage.show();
        }



    }

    /**
     * Called when the user wants to the delete the selected customer from the tableview
     * @param actionEvent - triggered on button press.
     * @throws SQLException
     * @throws IOException
     */
    public void deleteButtonPressed(ActionEvent actionEvent) throws SQLException, IOException {
        int selectedIndex;
        selectedIndex = customerTableView.getSelectionModel().getFocusedIndex();
        Customer selectedCustomer = AppointmentManager.getAllCustomers().get(selectedIndex);

        //First calls the method below to verify if the customer has any exisiting appointments before deleting.
        customerHasAppointment(actionEvent);

        //If method changes the customerAppointmentExists to true, it triggers the conditional statement below.
        if (customerAppointmentExists) {
            //calls an alert to tell the user that they can not delete the user
            ProgramAlerts.customerAppointmentError(selectedCustomer.getCustomerName());
            return;
        }

        confirmChanges = ProgramAlerts.deleteAlert();

        if (confirmChanges) {


            AppointmentManager.removeCustomer(selectedCustomer);
            customerDao.removeObject(selectedCustomer);

            //Return to the MainPage.fxml
            MainMenuWindow.returnToMainMenu(actionEvent);
        }


    }

    /**
     * Method acts like a listener that actively prefills the fields with the selected customer's information.
     */
    public void selectCustomerToChange() {
        int selectedIndex;
        selectedIndex = customerTableView.getSelectionModel().getFocusedIndex();
        Customer selectedCustomer = AppointmentManager.getAllCustomers().get(selectedIndex);

        customerNameField.setText(selectedCustomer.getCustomerName());
        addressField.setText(selectedCustomer.getCustomerAddress());
        postalField.setText(selectedCustomer.getCustomerPostalCode());
        phoneField.setText(selectedCustomer.getPhoneNumber());
        divisionComboBox.getSelectionModel().select(selectedCustomer.getCustomerDivision());
    }

    /**
     * Method called when the user want to return to the MainPage.fxml
     * @param actionEvent - triggered on button press
     * @throws IOException
     */
    public void cancelButtonPressed(ActionEvent actionEvent) throws IOException {

        //calls the ProgramAlerts.cancelAlert() and saves the response as a boolean value.
        confirmChanges = ProgramAlerts.cancelAlert();

        if (confirmChanges) {
            Parent root = FXMLLoader.load(getClass().getResource("../View/MainPage.fxml"));
            Scene MainPageScene = new Scene(root);

            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.setScene(MainPageScene);
            stage.show();
        }

    }

    /**
     * Customer appointment validation method. It determines whether or not a customer selected has existing appointments.
     * @param actionEvent - triggered on button press.
     */
    public void customerHasAppointment(ActionEvent actionEvent) {
        int selectedIndex;
        selectedIndex = customerTableView.getSelectionModel().getFocusedIndex();
        Customer selectedCustomer = AppointmentManager.getAllCustomers().get(selectedIndex);


        for (int i = 0; i < AppointmentManager.getAllAppointments().size(); ++i) {
            if (selectedCustomer.getCustomerName().matches(AppointmentManager.getAllAppointments().get(i).getCustomerName())) {
                customerAppointmentExists = true;
                return;

            }
            else {
                customerAppointmentExists = false;
            }
        }
    }

    /**
     * Input validation to check if each field is not blank
     * @param actionEvent - set on button press.
     * @return - boolean value used in flag variables.
     */
    public String inputValidation(ActionEvent actionEvent) {
        StringBuilder errorMessage = new StringBuilder();

        isInputInvalid = false;
        //validate each input
        try {

            if (customerNameField.getText().isBlank()) {
                throw new myExceptions("Customer name field is empty.\n");
            }
        }
        catch (myExceptions ex) {
            errorMessage.append(ex.getMessage());
            isInputInvalid = true;
        }

        try {
            if (addressField.getText().isBlank()) {
                throw new myExceptions("Address field is empty.\n");
            }
        }
        catch (myExceptions ex) {
            errorMessage.append(ex.getMessage());
            isInputInvalid = true;
        }

        try {
            if (postalField.getText().isBlank()) {
                throw new myExceptions("Postal code field is empty.\n");
            }

        }
        catch (myExceptions ex) {
            errorMessage.append(ex.getMessage());
            isInputInvalid = true;
        }

        try {
            if (phoneField.getText().isBlank()) {
                throw new myExceptions("Phone field is empty.\n");
            }
        }
        catch (myExceptions ex) {
            errorMessage.append((ex.getMessage()));
            isInputInvalid = true;
        }

        try {
            if (divisionComboBox.getSelectionModel().isEmpty()) {
                System.out.println("1");
                throw new myExceptions("No division selected.\n");
            }
        }
        catch (myExceptions ex) {
            errorMessage.append(ex.getMessage());
            isInputInvalid = true;
        }

        return errorMessage.toString();
    }

    // TODO scheduling an appointment outside of business hours defined as 8:00 a.m. to 10:00 p.m. EST, including weekends


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //loads the ObservableList of Customer, Division and Appointment objects within AppointmentManager with contents of the DB.
        divisionDao.loadDbObjects();
        customerDao.loadDbObjects();
        appointmentDao.loadDbObjects();




        //loads the ObservableList of Division objects within the AppointmentManager to the ComboxOptions
        for (int i = 0; i < AppointmentManager.getAllDivisions().size(); ++i) {
            divisionComboBox.getItems().add(AppointmentManager.getDivision(i));
        }

        //initialize each column of the TableView
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerName"));
        customerAddressColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerAddress"));
        customerDivisionColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerDivision"));
        customerPostalColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerPostalCode"));
        customerPhoneColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("phoneNumber"));

        customerTableView.setItems(AppointmentManager.getAllCustomers());
        customerTableView.setEditable(true);
        customerTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);


        //initialize the customerTableView to select the first index by default.
        customerTableView.getSelectionModel().focus(0);
        customerTableView.getSelectionModel().select(0);

        int selectedIndex;
        selectedIndex = customerTableView.getSelectionModel().getFocusedIndex();
        Customer selectedCustomer = AppointmentManager.getAllCustomers().get(selectedIndex);

        //initialize the textFields and the ComboBoxes to default the values of the selectedCustomer.
        customerNameField.setText(selectedCustomer.getCustomerName());
        addressField.setText(selectedCustomer.getCustomerAddress());
        postalField.setText(selectedCustomer.getCustomerPostalCode());
        phoneField.setText(selectedCustomer.getPhoneNumber());
        divisionComboBox.getSelectionModel().select(selectedCustomer.getCustomerDivision());
    }
}

