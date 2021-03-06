package Controller;

import Dao.CustomerDao;
import Dao.DivisionDao;
import Model.AppointmentManager;
import Model.Customer;
import Model.Division;
import Utility.MainMenuWindow;
import Utility.ProgramAlerts;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddCustomerPageController implements Initializable {

    //configures the buttons inside the AddCustomerPage
    @FXML private Button saveNewCustomer;
    @FXML private Button cancelButton;


    //configures the TextFields for user input
    @FXML private TextField customerNameField;
    @FXML private TextField addressField;
    @FXML private TextField postalField;
    @FXML private TextField phoneField;
    //configures the ComboBox selection for user input
    @FXML private ComboBox<String> divisionComboBox;
    @FXML private ComboBox<String> countryComboBox;

    //configures the TableView to contain the ObservableList of CustomerObjects
    @FXML private TableView customerTableView;
    @FXML private TableColumn<Customer, String> customerNameColumn;
    @FXML private TableColumn<Customer, String> customerAddressColumn;
    @FXML private TableColumn<Customer, String> customerDivisionColumn;
    @FXML private TableColumn<Customer, String> customerPostalColumn;
    @FXML private TableColumn<Customer, String> customerPhoneColumn;

    private CustomerDao customerDao = new CustomerDao();
    private DivisionDao divisionDao = new DivisionDao();

    private boolean confirmChanges = false;
    private boolean isInputInvalid = false;

    //

    /**
     * Method that runs when the cancelButton is pressed. It returns the program to the MainPage.
     * @param actionEvent Triggered by button pressed
     * @throws IOException
     */
    public void cancelButtonPressed(ActionEvent actionEvent) throws IOException {

        //calls the ProgramAlerts.cancelAlert() and saves the response as a boolean value.
        confirmChanges = ProgramAlerts.cancelAlert();

        if (confirmChanges) {
            MainMenuWindow.returnToMainMenu(actionEvent);
        }


    }

    //

    /**
     * A method for input validation to check if each field is not blank
     * @param actionEvent Triggered by button pressed
     * @return
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
            if (countryComboBox.getSelectionModel().isEmpty()) {
                throw new myExceptions("No country selected.\n");
            }
        }
        catch (myExceptions ex) {
            errorMessage.append(ex.getMessage());
            isInputInvalid = true;
        }

        try {
            if (divisionComboBox.getSelectionModel().isEmpty()) {
                throw new myExceptions("No division selected.\n");
            }
        }
        catch (myExceptions ex) {
            errorMessage.append(ex.getMessage());
            isInputInvalid = true;
        }

        return errorMessage.toString();
    }

    /**
     * Method called when saved button is pressed. Saves a new Customer
     * @param actionEvent triggered by button press
     * @throws SQLException
     * @throws IOException
     */
    public void saveNewCustomerButtonPressed(ActionEvent actionEvent) throws SQLException, IOException {

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
            //1.a pull data from textFields
            int customerCounter = AppointmentManager.getAllCustomers().size();
            String customerName = customerNameField.getText();
            String customerAddress = addressField.getText();
            String customerPostal = postalField.getText();
            String customerPhone = phoneField.getText();


            //1.b pull data from ComboBoxField, then use the HashMap to use the corresponding value with the selected key.
            String selectedDivision = divisionComboBox.getSelectionModel().getSelectedItem();
            int customerDivision = AppointmentManager.getAllHashMaps().get(selectedDivision);

            //2. create Customer object using the data from textFields
            Customer customerToAdd = new Customer(customerCounter + 1, customerName, customerAddress, customerPostal, customerDivision, selectedDivision, customerPhone);
            //3. add the Customer object to the ObservableList of Customer objects from the AppointmentManager class.
            AppointmentManager.addCustomer(customerToAdd);
            //4. Use the CustomerDao method to pass the same class into the addObject method.
            customerDao.addObject(customerToAdd);


            //Return to the MainPage.fxml
            MainMenuWindow.returnToMainMenu(actionEvent);
        }


    }

    /**
     * Method that initializes the AddCustomerPage.
     * loads the ObservableList of Customer objects within AppointmentManager with contents of the DB.
     * loads the ObservableList of Division objects within the AppointmentManager to the ComboBox options.
     * initialize each column of the TableView.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //loads the ObservableList of Customer objects within AppointmentManager with contents of the DB.
        divisionDao.loadDbObjects();
        customerDao.loadDbObjects();

        //disables division at window initiation
        divisionComboBox.setDisable(true);

        //List of countries, hard-coded for simplicity
        countryComboBox.getItems().add("U.S");
        countryComboBox.getItems().add("UK");
        countryComboBox.getItems().add("Canada");

        //disables the division ComboBox if there isnt a country selected.
        countryComboBox.getSelectionModel().selectedItemProperty().addListener((abs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                divisionComboBox.setDisable(false);
                divisionComboBox.getItems().clear();
                //loads the ObservableList of Division objects within the AppointmentManager to the ComboBox options
                for (Division division : AppointmentManager.getAllDivisions()) {
                    if (division.getCountry().equals(countryComboBox.getSelectionModel().getSelectedItem())) {
                        divisionComboBox.getItems().add(division.getDivision());
                    }
                }
            }
            else {
                divisionComboBox.setDisable(true);
            }
        });



        //initialize each column of the TableView
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerName"));
        customerAddressColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerAddress"));
        customerDivisionColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerDivision"));
        customerPostalColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerPostalCode"));
        customerPhoneColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("phoneNumber"));

        customerTableView.setItems(AppointmentManager.getAllCustomers());
    }
}
