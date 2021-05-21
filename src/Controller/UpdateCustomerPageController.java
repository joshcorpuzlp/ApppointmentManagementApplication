package Controller;

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

    private boolean confirmChanges = false;
    private boolean isInputInvalid = false;


    //input validation to check if each field is not blank
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

    public void deleteButtonPressed(ActionEvent actionEvent) throws SQLException, IOException {

        confirmChanges = ProgramAlerts.deleteAlert();

        if (confirmChanges) {
            int selectedIndex;
            selectedIndex = customerTableView.getSelectionModel().getFocusedIndex();
            Customer selectedCustomer = AppointmentManager.getAllCustomers().get(selectedIndex);

            AppointmentManager.removeCustomer(selectedCustomer);
            customerDao.removeObject(selectedCustomer);

            //Return to the MainPage.fxml
            MainMenuWindow.returnToMainMenu(actionEvent);
        }


    }

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


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //loads the ObservableList of Customer objects within AppointmentManager with contents of the DB.
        divisionDao.loadDbObjects();
        customerDao.loadDbObjects();



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

