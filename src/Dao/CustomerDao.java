package Dao;

import Model.AppointmentManager;
import Model.Customer;
import Utility.dbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDao implements Dao<Customer> {

    private ObservableList<Customer> tempCustomerHolder = FXCollections.observableArrayList();

    @Override
    public void loadDbObjects() {
        String query = "SELECT c.Customer_ID, c.Customer_Name, c.Address, c.Postal_Code, c.Phone, c.Division_ID, d.Division \n" +
                "FROM customers c\n" +
                "LEFT OUTER JOIN first_level_divisions d\n" +
                "on c.Division_ID = d.Division_ID;";

        try {
            ResultSet rs = dbConnection.getStatement().executeQuery(query);
            while (rs.next()) {
                int customerId = rs.getInt("Customer_ID");
                String customerName = rs.getString("Customer_Name");
                String customerAddress = rs.getString("Address");
                String customerPostalCode = rs.getString("Postal_Code");
                String customerPhone = rs.getString("Phone");
                int divisionId = rs.getInt("Division_ID");
                String customerDivision = rs.getString("Division");

                //saved all customers from Database into a temporary ObservableList of Customer objects
                tempCustomerHolder.add(new Customer(customerId, customerName, customerAddress, customerPostalCode, divisionId, customerDivision, customerPhone));
            }

            //used the temporary ObservableList of Customers as the argument for the setCustomers method.
            AppointmentManager.setCustomers(tempCustomerHolder);
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }


        //console viewer of the effects. can be removed later.
        for (Customer c : AppointmentManager.getAllCustomers()) {
            System.out.println(c.getCustomerName());
        }
    }

    @Override
    public void addObject(Customer customer) throws SQLException {
        //configures a String object to store the SQL statement that we would like to execute
        String query = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Division_ID, Phone) VALUES (?, ?, ?, ?, ?);";

        //create a PreparedStatement object called the insertQuery that has the value of the above query.
        PreparedStatement insertQuery = dbConnection.getConnection().prepareStatement(query);

        //configure the each value placeholder.
        insertQuery.setString(1, customer.getCustomerName());
        insertQuery.setString(2, customer.getCustomerAddress());
        insertQuery.setString(3, customer.getCustomerPostalCode());
        insertQuery.setInt(4, customer.getCustomerDivisionId());
        insertQuery.setString(5, customer.getPhoneNumber());


        insertQuery.execute();

    }

    @Override
    public void modifyObject(Customer customer) {

    }

    @Override
    public void removeObject(Customer customer) {

    }
}
