package Controller;

import Dao.ReportsDao;
import Model.AppointmentManager;
import Model.User;
import Utility.MainMenuWindow;
import Utility.ProgramAlerts;
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

    //configures the cancel button
    @FXML private Button cancelButton;

    //configures the TextArea used to display the results.
    @FXML private TextArea reportTextArea;

    //The GUI element used to select the Users for reports 2 and 3
    @FXML private ComboBox<String> userNamesComboBox;

    //Needed to call the reportsDao methods.
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

            /*
            * surround program under try/catch clause to check if there is a selected User to base report on,
            * if exception is thrown, catch with an alert and a return statement.
            *
             */

            try {
                if (userNamesComboBox.getSelectionModel().getSelectedItem()
                        .matches(AppointmentManager.getAllUsers().get(i).getUserName())) {
                    selectedUser = AppointmentManager.getAllUsers().get(i);
                    reportTextArea.setText(reportsDao.report2(selectedUser));
                }
            }
            catch (Exception ex) {

                ProgramAlerts.selectUserAlert();
                return;
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
    public void report3Pressed(ActionEvent actionEvent)  {
        User selectedUser;
        for (int i =0; i < AppointmentManager.getAllUsers().size(); ++i) {

            /*
             * surround program under try/catch clause to check if there is a selected User to base report on,
             * if exception is thrown, catch with an alert and a return statement.
             *
             */
                try {

                    if (userNamesComboBox.getSelectionModel().getSelectedItem()
                            .matches(AppointmentManager.getAllUsers().get(i).getUserName())) {
                        selectedUser = AppointmentManager.getAllUsers().get(i);

                        reportTextArea.setText(reportsDao.report3(selectedUser));
                    }
                }
                catch (Exception exception) {
                    ProgramAlerts.selectUserAlert();
                    return;
                }

        }

    }

    //method that runs when the cancelButton is pressed. It returns the program to the MainPage.
    public void cancelButtonPressed(ActionEvent actionEvent) throws IOException {
        //Return to the MainPage.fxml
        MainMenuWindow.returnToMainMenu(actionEvent);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //load comboxBox userNames with user objects
        for (int i = 0; i < AppointmentManager.getAllUsers().size(); ++i) {
            userNamesComboBox.getItems().add(AppointmentManager.getAllUsers().get(i).getUserName());
        }


    }
}
