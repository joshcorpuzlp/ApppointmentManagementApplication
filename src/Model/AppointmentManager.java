package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AppointmentManager {
    //configures an ObservableList of User objects.
    private static ObservableList<User> users = FXCollections.observableArrayList();

    //method that sets the ObservableList of Users to what was passed as an argument.
    public static void setUsers(ObservableList<User> userListInput) {
        users = userListInput;
    }

    //method adds the passed user argument to the ObservableList of User objects.
    public static void addUsers(User userToAdd) {
        users.add(userToAdd);
    }

    //method removes the passed user argument to the ObservableList of User objects.
    public static void removeUser(User userToRemove) {
        users.remove(userToRemove);
    }

    //method that returns AppointmentManager's static attribute of ObservableList of User objects.
    public static ObservableList<User> getAllUsers() {
        return users;
    }

}
