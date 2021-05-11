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

    public void saveButtonPressed(ActionEvent actionEvent) throws SQLException, IOException {
        int selectedIndex;
        selectedIndex = customerTableView.getSelectionModel().getFocusedIndex();

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

    public void deleteButtonPressed(ActionEvent actionEvent) throws SQLException, IOException {
        int selectedIndex;
        selectedIndex = customerTableView.getSelectionModel().getFocusedIndex();
        Customer selectedCustomer = AppointmentManager.getAllCustomers().get(selectedIndex);

        AppointmentManager.removeCustomer(selectedCustomer);
        customerDao.removeObject(selectedCustomer);

        //Return to the MainPage.fxml
        Parent root = FXMLLoader.load(getClass().getResource("../View/MainPage.fxml"));
        Scene MainPageScene = new Scene(root);

        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.setScene(MainPageScene);
        stage.show();
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
        Parent root = FXMLLoader.load(getClass().getResource("../View/MainPage.fxml"));
        Scene MainPageScene = new Scene(root);

        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.setScene(MainPageScene);
        stage.show();
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
    }
}

