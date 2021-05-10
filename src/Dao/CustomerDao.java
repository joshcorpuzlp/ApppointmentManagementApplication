package Dao;

import Model.AppointmentManager;
import Model.Customer;
import Utility.dbConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDao implements Dao<Customer> {

    @Override
    public void loadDbObjects() {
        String query = "SELECT * FROM customers;";

        try {
            ResultSet rs = dbConnection.getStatement().executeQuery(query);
            while (rs.next()) {
                int customerId = rs.getInt("Customer_ID");
                String customerName = rs.getString("Customer_Name");
                String customerAddress = rs.getString("Address");
                String customerPostalCode = rs.getString("Postal_Code");
                String customerPhone = rs.getString("Phone");
                int customerState = rs.getInt("Division_ID");

                AppointmentManager.addCustomer(new Customer(customerId, customerName, customerAddress, customerPostalCode, customerState,customerPhone));
            }
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }


    }

    @Override
    public void addObject(Customer customer) throws SQLException {
        String query = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Division_ID) VALUES (?, ?, ?, ?, ?);";
        PreparedStatement insertQuery = dbConnection.getConnection().prepareStatement(query);
        insertQuery.setString(1, customer.getCustomerName());
        insertQuery.setString(2, customer.getCustomerAddress());
        insertQuery.setString(3, customer.getCustomerPostalCode());
        insertQuery.setString(4, customer.getPhoneNumber());
        insertQuery.setInt(5, customer.getCustomerState());

        insertQuery.execute();

    }

    @Override
    public void modifyObject(Customer customer) {

    }

    @Override
    public void removeObject(Customer customer) {

    }
}
