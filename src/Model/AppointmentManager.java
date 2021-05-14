package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;

public class AppointmentManager {
    //configures an ObservableList of User objects.
    private static ObservableList<User> users = FXCollections.observableArrayList();
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


    //method that sets the passed ObservableList of Contact objects to be the private ObservableList of the AppointmentManager
    public static void setContacts(ObservableList<Contact> contactListInput) {
        contacts = contactListInput;
    }
    //method that returns the ObservableList of Contact objects
    public static ObservableList<Contact> getAllContacts() {
        return contacts;
    }
    //method returns a Contact object from the ObservableList customers at a specific index.
    public static Contact getContact(int index) {
        return contacts.get(index);
    }
    //method that returns a Contact object that matches the passed contactName
    public static Contact getContactFromName(String contactName) {
        for (int i = 0; i < contacts.size(); i++) {
            if (contacts.get(i).getContactName() == contactName) {
                foundContact = contacts.get(i);
            }

        }
        return foundContact;

    }


    //method that sets the passed ObservableList of Appointment objects to be the private ObservableList of the AppointmentManager
    public static void setAppointments(ObservableList<Appointment> appointmentListInput) {
        appointments = appointmentListInput;
    }
    //method that returns the ObservableList of Appointment objects
    public static ObservableList<Appointment> getAllAppointments() {
        return appointments;
    }
    //method that adds a passed Appointment object to the private ObservableList of the AppointmentManager appointments
    public static void addAppointment(Appointment appointmentToAdd) {
        appointments.add(appointmentToAdd);
    }
    //method that removes a passed Appointment object from the private ObservableList of AppointmentManager's appointments
    public static void removeAppointment(Appointment appointmentToRemove) {
        appointments.remove(appointmentToRemove);
    }

    //method that passes the index and Appointment object to specify where in the ObservableList the Update should occur.
    public static void updateAppointment(int index, Appointment appointmentToUpdate) {
        appointments.set(index, appointmentToUpdate);
    }





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

    public static void updateCustomer(int index, Customer customerToUpdate) {
        customers.set(index, customerToUpdate);
    }

    //method removes the passed customer argument to the ObservableList of Customer objects.
    public static void removeCustomer(Customer customerToRemove) {
        customers.remove(customerToRemove);
    }

    //method that returns AppointmentManager's static attribute of ObservableList of Customer objects.
    public static ObservableList<Customer> getAllCustomers() {
        return customers;
    }

    //method that returns a specific Customer using an index number as the parameter.
    public static Customer getCustomer(int index) {
        return customers.get(index);
    }

    //method that returns a customer based on the passed string for a name
    public static Customer getCustomerFromName(String customerName) {
        for (int i = 0; i < customers.size(); i++) {
            if (customers.get(i).getCustomerName() == customerName) {
                foundCustomer = customers.get(i);
            }

        }
        return foundCustomer;
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
