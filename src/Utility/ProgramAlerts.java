package Utility;

import Model.Appointment;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;

import java.util.Optional;

public class ProgramAlerts {

    /**
     * Method that is used in conjunction to the event that the logged in user has a pending appointment within 15 minutes
     */
    public static void pendingAppointmentAlert(Appointment pendingAppointment) {

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initModality(Modality.NONE);
        alert.setTitle("Pending Appointment");
        alert.setHeaderText("15 minute warning!");
        alert.setContentText("You have an appointment within the next 15 minutes with Appointment id #: " + pendingAppointment.getAppointmentId() +
                ", " + "at " + pendingAppointment.getStartDate() + ", " + pendingAppointment.getFormattedStartTime());
        alert.showAndWait();

    }

    /**
     * Method that is used in conjunction to the event that the logged in user has no pending appointments within 15 minutes
     */
    public static void noPendingAppointmentAlert() {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("No Upcoming Appointment");
        alert.setHeaderText("There are no appointments pending");
        alert.setContentText("There aren't any appointments within the next 15 minutes!");
        alert.showAndWait();

    }

    /**
     * Method that shows an alert used in conjunction with the event that the a user was not selected when needed in the ReportsPageController
     */
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

    /**
     * Alert that is used to alert the user that there was an error with overlapping times.
     * @param customerOrContact Utilizes a String object as a parameter to change the message of the alert
     * @return Boolean value can be utilized for flag variable use.
     */
    public static void overlappingTimes(String customerOrContact) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initModality(Modality.NONE);
        alert.setTitle("ERROR!");
        alert.setHeaderText("The selected times have already been scheduled for the selected " +  customerOrContact + ".");
        alert.setContentText("Please select different start and end times.");
        alert.showAndWait();
    }

    /**
     * Alert that is used to alert the user that there was an error with user inputs.
     */
    public static void inputValidationAlert(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initModality(Modality.NONE);
        alert.setTitle("ERROR!");
        alert.setHeaderText("Invalid input(s)");
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }

    /**
     * Alert that is used to alert the user that the selected customer can not be deleted due to related appointment(s).
     */
    public static void customerAppointmentError(String customerName) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initModality(Modality.NONE);
        alert.setTitle("ERROR!");
        alert.setHeaderText("Customer has existing appointments");
        alert.setContentText("The customer: " + customerName + " can not be deleted!!\n" +
                        "They have existing appointment(s)");
        alert.showAndWait();
    }

    /**
     * Alert that is used to alert the user that the selected hours are outside business hours.
     */
    public static void outsideBusinessHoursError() {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initModality(Modality.NONE);
        alert.setTitle("ERROR!");
        alert.setHeaderText("The selected times are outside normal business hours.");
        alert.setContentText("Please select different start and end times.");
        alert.showAndWait();


    }

}
