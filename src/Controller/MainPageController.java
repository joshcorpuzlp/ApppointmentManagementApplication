package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
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
    @FXML private TableColumn appointmentColumn;
    @FXML private TableColumn consultantColumn;

    public void addCustomerButtonPressed(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../View/AddCustomerPage.fxml"));
        Scene MainPageScene = new Scene(root);

        Stage stage = (Stage) addCustomerButton.getScene().getWindow();
        stage.setScene(MainPageScene);
        stage.show();
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

}
