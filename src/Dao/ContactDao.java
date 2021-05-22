package Dao;

import Model.AppointmentManager;
import Model.Contact;
import Utility.DbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ContactDao implements Dao<Contact> {
    private ObservableList<Contact> tempContactsHolder = FXCollections.observableArrayList();

    /**
     * Loads the ObservableList of Contact objects with new objects using data from the database
     */
    @Override
    public void loadDbObjects() {
        String query = "SELECT * FROM contacts;";
        try
        {
            ResultSet rs = DbConnection.getStatement().executeQuery(query);
            while (rs.next()) {
                int contactId = rs.getInt("Contact_ID");
                String contactName = rs.getString("Contact_Name");

                tempContactsHolder.add(new Contact(contactId, contactName));
            }
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
        AppointmentManager.setContacts(tempContactsHolder);
    }

    /**
     * Adds A Contact object to the ObservableList of Contacts
     * @param contact passes an Contact object
     * @throws SQLException
     */
    @Override
    public void addObject(Contact contact) throws SQLException {

    }

    /**
     * A method that modifies the Contact object with the Contact object passed
     * @param contact Passes an Appointment object
     * @throws SQLException
     */
    @Override
    public void modifyObject(Contact contact) throws SQLException {

    }

    /**
     * A method that removes the passed Contact object from the Observablelist of Contact objects
     * @param contact Passes a Contact object
     * @throws SQLException
     */
    @Override
    public void removeObject(Contact contact) throws SQLException {

    }
}
