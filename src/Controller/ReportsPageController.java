package Controller;

import Dao.ReportsDao;
import Model.AppointmentManager;
import Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ReportsPageController implements Initializable {

    @FXML private Button cancelButton;
    @FXML private TextArea reportTextArea;

    @FXML private ComboBox<String> userNamesComboBox;

    private ReportsDao reportsDao = new ReportsDao();





    /*
     * method that runs the reportsDao.report1 method and then displays the results on the reportTextArea
     *
     ***************************************************/
    public void report1Pressed(ActionEvent actionEvent) {
        reportTextArea.setText(reportsDao.report1());
    }




    /*
     * method that runs the reportsDao.report2 method passing the selected value within the userNamesComboBox as an argument
     * for the reportsDao.report2 method, then displays the String returned on reportTextArea
     *
     ***************************************************/
    public void report2Pressed(ActionEvent actionEvent) throws SQLException {
        User selectedUser;

        for (int i =0; i < AppointmentManager.getAllUsers().size(); ++i) {
            if (userNamesComboBox.getSelectionModel().getSelectedItem()
                    .matches(AppointmentManager.getAllUsers().get(i).getUserName())) {
                selectedUser = AppointmentManager.getAllUsers().get(i);
                reportTextArea.setText(reportsDao.report2(selectedUser));
            }
        }
    }

    /*
     * method that runs the reportsDao.report3 method, passing the selected value of the userNamesComboBox
     * as its argument
     *
     * reportsDao.report3 returns a String which is the report displayed on the reportTextArea
     *
     ***************************************************/
    public void report3Pressed(ActionEvent actionEvent) throws SQLException {
        User selectedUser;
        for (int i =0; i < AppointmentManager.getAllUsers().size(); ++i) {
            if (userNamesComboBox.getSelectionModel().getSelectedItem()
                    .matches(AppointmentManager.getAllUsers().get(i).getUserName())) {
                selectedUser = AppointmentManager.getAllUsers().get(i);
                reportTextArea.setText(reportsDao.report3(selectedUser));
            }
        }

    }

    //method that runs when the cancelButton is pressed. It returns the program to the MainPage.
    public void cancelButtonPressed(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../View/MainPage.fxml"));
        Scene MainPageScene = new Scene(root);

        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.setScene(MainPageScene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //load comboxBox userNames with user objects
        for (int i = 0; i < AppointmentManager.getAllUsers().size(); ++i) {
            userNamesComboBox.getItems().add(AppointmentManager.getAllUsers().get(i).getUserName());
        }
        //disables comboBox userNames, re-enabled when reports2 button is pressed.

    }
}
