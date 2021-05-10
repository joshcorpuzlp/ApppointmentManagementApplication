package Controller;

import Dao.CustomerDao;
import Model.AppointmentManager;
import Model.Customer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddCustomerPageController implements Initializable {

    //configures the buttons inside the AddCustomerPage
    @FXML private Button saveNewCustomer;
    @FXML private Button cancelButton;

    @FXML private TextField customerNameField;
    @FXML private TextField addressField;
    @FXML private TextField postalField;
    @FXML private TextField phoneField;
    @FXML private TextField stateField;

    CustomerDao customerDao = new CustomerDao();

    //method that runs when the cancelButton is pressed. It returns the program to the MainPage.
    public void cancelButtonPressed(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../View/MainPage.fxml"));
        Scene MainPageScene = new Scene(root);

        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.setScene(MainPageScene);
        stage.show();
    }

    public void saveNewCustomerButtonPressed(ActionEvent actionEvent) throws SQLException {
        //1. pull data from textFields
        int customerCounter = AppointmentManager.getAllCustomers().size();
        String customerName = customerNameField.getText();
        String customerAddress = addressField.getText();
        String customerPostal = postalField.getText();
        String customerPhone = phoneField.getText();
        int customerState = Integer.parseInt(stateField.getText());

        //2. create Customer object using the data from textFields
        Customer customerToAdd = new Customer(customerCounter + 1, customerName, customerAddress, customerPostal, customerState, customerPhone);
        //3. add the Customer object to the ObservableList of Customer objects from the AppointmentManager class.
        AppointmentManager.addCustomer(customerToAdd);
        //4. Use the CustomerDao method to pass the same class into the addObject method.
        customerDao.addObject(customerToAdd);


    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //loads the ObservableList of Customer objects within AppointmentManager with contents of the DB.
        customerDao.loadDbObjects();
    }
}
