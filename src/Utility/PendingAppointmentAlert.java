package Utility;

import Model.Appointment;
import Model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PendingAppointmentAlert {

    public static void pendingAppointmentAlert(int userId, ObservableList<Appointment> appointments) {
        //need to cycle through all appointments where user is = to user
        ObservableList<Appointment> userAppointments = FXCollections.observableArrayList();

        for (Appointment appointment : appointments) {
            if (userId == appointment.getUserId()) {
                System.out.print(appointment.getStartTime());
                userAppointments.add(appointment);
            }
        }

        for (Appointment userAppointment : userAppointments) {
            System.out.println(userAppointment.getStartTime());
        }

    }
}
