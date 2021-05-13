package Dao;

import Model.Appointment;
import Model.AppointmentManager;
import Model.Contact;
import Utility.dbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ContactDao implements Dao<Contact> {
    private ObservableList<Contact> tempContactsHolder = FXCollections.observableArrayList();


    @Override
    public void loadDbObjects() {
        String query = "SELECT * FROM contacts;";
        try
        {
            ResultSet rs = dbConnection.getStatement().executeQuery(query);
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

    @Override
    public void addObject(Contact contact) throws SQLException {

    }

    @Override
    public void modifyObject(Contact contact) throws SQLException {

    }

    @Override
    public void removeObject(Contact contact) throws SQLException {

    }
}
