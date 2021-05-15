package Controller;

import Dao.CustomerDao;
import Dao.DivisionDao;
import Model.AppointmentManager;
import Model.Customer;
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

    //configures the TableView to contain the ObservableList of CustomerObjects
    @FXML private TableView customerTableView;
    @FXML private TableColumn<Customer, String> customerNameColumn;
    @FXML private TableColumn<Customer, String> customerAddressColumn;
    @FXML private TableColumn<Customer, String> customerDivisionColumn;
    @FXML private TableColumn<Customer, String> customerPostalColumn;
    @FXML private TableColumn<Customer, String> customerPhoneColumn;

    private CustomerDao customerDao = new CustomerDao();
    private DivisionDao divisionDao = new DivisionDao();

    //method that runs when the cancelButton is pressed. It returns the program to the MainPage.
    public void cancelButtonPressed(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../View/MainPage.fxml"));
        Scene MainPageScene = new Scene(root);

        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.setScene(MainPageScene);
        stage.show();
    }

    public void saveNewCustomerButtonPressed(ActionEvent actionEvent) throws SQLException, IOException {
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
        Parent root = FXMLLoader.load(getClass().getResource("../View/MainPage.fxml"));
        Scene MainPageScene = new Scene(root);

        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.setScene(MainPageScene);
        stage.show();

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        divisionDao.loadDbObjects();

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
    }
}
