package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;

public class AppointmentManager {


    //configures an ObservableList of User objects.
    private static ObservableList<User> users = FXCollections.observableArrayList();
    //variable that holds the UserId of the login credentials used.
    private static int loggedInUserId;


    //create a hashmap to hold the User userNames and UserIds
    private static HashMap<String, Integer> userHashMap = new HashMap<String, Integer>();



    //configure an Observable list of CustomerObjects
    private static  ObservableList<Customer> customers = FXCollections.observableArrayList();
    private static Customer foundCustomer;
    //configure an ObservableList of Division objects
    private static ObservableList<Division> divisions = FXCollections.observableArrayList();
    private static HashMap<String, Integer> divisionHashMap = new HashMap<String, Integer>();

    //configure an ObservableList of AppointmentObjects
    private static ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    //configure an ObservableList of Appointments of a specific timeframe ie. month, week, or dates
    private static ObservableList<Appointment> filteredAppointments = FXCollections.observableArrayList();

    //configure an ObservableList of Contact objects
    private static ObservableList<Contact> contacts = FXCollections.observableArrayList();
    private static Contact foundContact;

    /**
     * A static method that sets the passed ObservableList of Contact objects to be the private ObservableList of the AppointmentManager
     * @param contactListInput passes am Observable list of Contact objects.
     */
    public static void setContacts(ObservableList<Contact> contactListInput) {
        contacts = contactListInput;
    }

    /**
     * A static method that returns the ObservableList of Contact objects
     * @return
     */
    public static ObservableList<Contact> getAllContacts() {
        return contacts;
    }

    /**
     * A static method returns a Contact object from the ObservableList customers at a specific index.
     * @param index an Integer value
     * @return Returns a Contact object
     */
    public static Contact getContact(int index) {
        return contacts.get(index);
    }


    /**
     * A static method that returns a Contact object that matches the passed contactName
     * @param contactName passes a string object
     * @return Returns a Contact object.
     */
    public static Contact getContactFromName(String contactName) {
        for (int i = 0; i < contacts.size(); i++) {
            if (contacts.get(i).getContactName() == contactName) {
                foundContact = contacts.get(i);
            }

        }
        return foundContact;

    }

    /**
     * A static method that sets the passed ObservableList of Appointment objects to be the private ObservableList of the AppointmentManager
     * @param appointmentListInput
     */
    public static void setAppointments(ObservableList<Appointment> appointmentListInput) {
        appointments = appointmentListInput;
    }

    /**
     * A static method that returns the ObservableList of Appointment objects
     * @return an ObservableList of Appointment objects
     */
    public static ObservableList<Appointment> getAllAppointments() {
        return appointments;
    }

    /**
     * A static method that adds a passed Appointment object to the private ObservableList of the AppointmentManager appointments
     * @param appointmentToAdd
     */
    public static void addAppointment(Appointment appointmentToAdd) {
        appointments.add(appointmentToAdd);
    }

    /**
     * A static method that removes a passed Appointment object from the private ObservableList of AppointmentManager's appointments
     * @param appointmentToRemove
     */
    public static void removeAppointment(Appointment appointmentToRemove) {
        appointments.remove(appointmentToRemove);
    }

    /**
     * A static method that passes the index and Appointment object to specify where in the ObservableList the Update should occur.
     * @param index Passes an integer
     * @param appointmentToUpdate Passes an Appointment object
     */
    public static void updateAppointment(int index, Appointment appointmentToUpdate) {
        appointments.set(index, appointmentToUpdate);
    }

    /**
     * A static method that sets the ObservableList of Users to what was passed as an argument
     * @param userListInput Passes an Observable list of Users
     */
    public static void setUsers(ObservableList<User> userListInput) {
        users = userListInput;
    }

    /**
     * A static method adds the passed user argument to the ObservableList of User objects.
     * @param userToAdd
     */
    public static void addUsers(User userToAdd) {
        users.add(userToAdd);
    }

    /**
     * A static method that removes the passed user argument from the ObservableList of User objects.
     * @param userToRemove passes a User object
     */
    public static void removeUser(User userToRemove) {
        users.remove(userToRemove);
    }


    /**
     * A static method that returns AppointmentManager's static attribute of ObservableList of User objects.
     * @return Returns an ObservableList of user objects
     */
    public static ObservableList<User> getAllUsers() {
        return users;
    }

    /**
     * A static method that sets the userId the user logged in.
     * @param user passes a User object.
     */
    public static void setLoggedInUserId(User user) {
        loggedInUserId = user.getUserId();
    }

    /**
     * A static method that returns the userId
     * @return Returns in integer value
     */
    public static int getLoggedInUserId() {
        return loggedInUserId;
    }

    /**
     * A static method that sets the hashmap based on the inputted User object data fields.
     * @param key Passes a String object as a key
     * @param value Passes an int as the value
     */
    public static void addToUserHashMap(String key, int value) {
        userHashMap.put(key, value);
    }

    /**
     * A static method that returns a hash map
     * @return A hashMap
     */
    public static HashMap<String, Integer> getAllUserHashMaps() {
        return userHashMap;
    }

    /**
     * A static method that sets the ObservableList of customers to what was passed as an argument.
     * @param customerListInput Passes a ObservableList of customer objects
     */
    public static void setCustomers(ObservableList<Customer> customerListInput) {
        customers = customerListInput;
    }


    /**
     * A static method adds the passed customer argument to the ObservableList of Customer objects.
     * @param customerToAdd Passes a customer object
     */
    public static void addCustomer(Customer customerToAdd) {
        customers.add(customerToAdd);
    }

    /**
     * A static method that updates the Customer with the indicated index.
     * @param index Passes an int as the index indicator
     * @param customerToUpdate Passes a Customer object to update
     */
    public static void updateCustomer(int index, Customer customerToUpdate) {
        customers.set(index, customerToUpdate);
    }

    /**
     * A static method removes the passed customer argument to the ObservableList of Customer objects.
     * @param customerToRemove Passes a Customer object to remove
     */
    public static void removeCustomer(Customer customerToRemove) {
        customers.remove(customerToRemove);
    }

    /**
     * A static method that returns AppointmentManager's static attribute of ObservableList of Customer objects.
     * @return ObservableList of Customer objects
     */
    public static ObservableList<Customer> getAllCustomers() {
        return customers;
    }

    /**
     * A static method that returns a specific Customer using an index number as the parameter.
     * @param index
     * @return
     */
    public static Customer getCustomer(int index) {
        return customers.get(index);
    }

    /**
     * A static method that returns a customer based on the passed string for a name
     * @param customerName Passes a String object
     * @return Returns a customern object
     */
    public static Customer getCustomerFromName(String customerName) {
        for (int i = 0; i < customers.size(); i++) {
            if (customers.get(i).getCustomerName() == customerName) {
                foundCustomer = customers.get(i);
            }

        }
        return foundCustomer;
    }

    /**
     * A static method that sets the ObservableList of Divisions to what was passed as an argument
     * @param divisionsInput
     */
    public static void setDivisions(ObservableList<Division> divisionsInput) {
        divisions = divisionsInput;
    }

    /**
     * Static method that returns a specific division using an index number as the parameter.
     * @param index passes an integer
     * @return Returns a Division object
     */
    public static String getDivision(int index) {
        return divisions.get(index).getDivision();
    }

    /**
     * Static method that returns the ObservableList of Division objects
     * @return
     */
    public static ObservableList<Division> getAllDivisions() {
        return divisions;
    }

    /**
     * Static method used to add a Division object to the ObservableList of Divisions
     * @param divisionToAdd passes a Division object
     */
    public static void addDivision(Division divisionToAdd) {
        divisions.add(divisionToAdd);
    }

    /**
     * Method that adds to the divisionHashMap
     * @param key A String value passed as the key
     * @param value An integer passed as the value
     */
    public static void addToHashMap(String key, int value) {
        divisionHashMap.put(key, value);
    }
    public static HashMap<String, Integer> getAllHashMaps() {
        return divisionHashMap;
    }


}
