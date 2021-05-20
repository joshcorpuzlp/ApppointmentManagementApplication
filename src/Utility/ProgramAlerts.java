package Utility;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;

import java.util.Optional;

public class ProgramAlerts {

    /**
     * Method that is used in conjunction to the event that the logged in user has a pending appointment within 15 minutes
     */
    public static void pendingAppointmentAlert() {

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initModality(Modality.NONE);
        alert.setTitle("Pending Appointment");
        alert.setHeaderText("15 minute warning!");
        alert.setContentText("You have an appointment within the next 15 minutes!");
        alert.showAndWait();

    }

    /**
     * Method that shows an alert used in conjunction with the event that the a user was not selected when needed in the ReportsPageController
     */
    //TODO change this to contact, as well as the reports page that this is used with.
    public static void selectUserAlert(String userOrContact) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initModality(Modality.NONE);
        alert.setTitle("No " + userOrContact + " selected");
        alert.setHeaderText("Select a " + userOrContact + ".");
        alert.setContentText("Make sure to choose a " + userOrContact + " to run your program on!");
        alert.showAndWait();
    }

    /**
     * Alert that is to confirm the changes a user is about to make by showing an alert and returning a boolean value.
     * @return
     */
    public static boolean saveChangesAlert() {
        boolean isOk = false;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirm");
        alert.setHeaderText("Saving changes");
        alert.setContentText("Would you like to save changes?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            isOk = true;
        }

        return isOk;
    }

    /**
     * Alert that is to confirm the deletion a user is about to make by showing an alert and returning a boolean value.
     * @return
     */
    public static boolean deleteAlert() {
        boolean isOk = false;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirm action");
        alert.setHeaderText("Deleting selected item");
        alert.setContentText("Would you like to delete the selected item?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            isOk = true;
        }

        return isOk;
    }

    /**
     * Alert that is to confirm that the user is about to exit a program showing an alert and returning a boolean value.
     * @return
     */
    public static boolean cancelAlert() {
        boolean isOk = false;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Cancel");
        alert.setHeaderText("Cancelling any changes made");
        alert.setContentText("Are you sure? All your changes and progress will be lost.");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            isOk = true;
        }

        return isOk;
    }

    public static boolean overlappingTimes() {
        boolean isTimeInvalid = false;
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initModality(Modality.NONE);
        alert.setTitle("ERROR!");
        alert.setHeaderText("The selected times have already been scheduled for the selected contact.");
        alert.setContentText("Please select different start and end times.");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            isTimeInvalid = true;
        }

        return isTimeInvalid;
    }


}
