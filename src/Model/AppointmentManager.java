package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AppointmentManager {
    //configures an ObservableList of User objects.
    private static ObservableList<User> users = FXCollections.observableArrayList();
    //configure an Observable list of CustomerObjects
    private static  ObservableList<Customer> customers = FXCollections.observableArrayList();




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



    //method that sets the ObservableList of customers to what was passed as an argument.
    public static void setCustomers(ObservableList<Customer> customerListInput) {
        customers = customerListInput;
    }

    //method adds the passed customer argument to the ObservableList of Customer objects.
    public static void addCustomer(Customer customerToAdd) {
        customers.add(customerToAdd);
    }

    //method removes the passed customer argument to the ObservableList of Customer objects.
    public static void removeCustomer(Customer customerToRemove) {
        customers.remove(customerToRemove);
    }

    //method that returns AppointmentManager's static attribute of ObservableList of Customer objects.
    public static ObservableList<Customer> getAllCustomers() {
        return customers;
    }

}
