package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;

public class AppointmentManager {
    //configures an ObservableList of User objects.
    private static ObservableList<User> users = FXCollections.observableArrayList();
    //configure an Observable list of CustomerObjects
    private static  ObservableList<Customer> customers = FXCollections.observableArrayList();
    //configure an ObservableList of Division objects
    private static ObservableList<Division> divisions = FXCollections.observableArrayList();
    private static HashMap<String, Integer> divisionHashMap = new HashMap<String, Integer>();




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


    //method that sets the ObservableList of Divisions to what was passed as an argument
    public static void setDivisions(ObservableList<Division> divisionsInput) {
        divisions = divisionsInput;
    }
    //method that returns a specific division using an index number as the parameter.
    public static String getDivision(int index) {
        return divisions.get(index).getDivision();
    }
    //returns the ObservableList of Division objects
    public static ObservableList<Division> getAllDivisions() {
        return divisions;
    }

    public static void addDivision(Division divisionToAdd) {
        divisions.add(divisionToAdd);
    }

    public static void addToHashMap(String key, int value) {
        divisionHashMap.put(key, value);
    }
    public static HashMap<String, Integer> getAllHashMaps() {
        return divisionHashMap;
    }


}
