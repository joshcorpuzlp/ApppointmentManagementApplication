package Utility;

import Model.Appointment;
import Model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.stage.Modality;

public class ProgramAlerts {

    public static void pendingAppointmentAlert() {

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initModality(Modality.NONE);
        alert.setTitle("Pending Appointment");
        alert.setHeaderText("15 minute warning!");
        alert.setContentText("You have an appointment within the next 15 minutes!");
        alert.showAndWait();

    }

    public static void selectUserAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initModality(Modality.NONE);
        alert.setTitle("No user selected");
        alert.setHeaderText("Select a user.");
        alert.setContentText("Make sure to choose a user to run your program on!");
        alert.showAndWait();
    }

}
