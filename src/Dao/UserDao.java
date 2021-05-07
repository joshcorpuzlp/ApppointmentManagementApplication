package Dao;

import Model.AppointmentManager;
import Model.User;
import Utility.dbConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UserDao implements Dao<User>{
    @Override
    public void loadDbObjects() {
        String query = "SELECT * FROM users";

        try {
            ResultSet rs = dbConnection.getStatement().executeQuery(query);
            while (rs.next()) {
                int userId = rs.getInt("User_ID");
                String userName = rs.getString("User_Name");
                String password = rs.getString("Password");

                AppointmentManager.addUsers(new User(userId, userName, password));

                //console checker, can be deleted if needed.
                System.out.println(userId + ", " + userName + ", " + password);


            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void addObject(User user) {
        AppointmentManager.addUsers(user);
    }

    @Override
    public void modifyObject(User user) {

    }

    @Override
    public void removeObject(User user) {
        AppointmentManager.removeUser(user);
    }
}
