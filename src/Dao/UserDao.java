package Dao;

import Model.AppointmentManager;
import Model.User;
import Utility.DbConnection;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao implements Dao<User>{

    /**
     * Loads the ObservableList of User objects with new objects using data from the database
     */
    @Override
    public void loadDbObjects() {
        String query = "SELECT * FROM users";

        try {
            ResultSet rs = DbConnection.getStatement().executeQuery(query);
            while (rs.next()) {
                int userId = rs.getInt("User_ID");
                String userName = rs.getString("User_Name");
                String password = rs.getString("Password");

                //load AppointmentManager's ObservableList of Users.
                AppointmentManager.addUsers(new User(userId, userName, password));

                //load the userHashMap with the userName and userId using the AppointmentManager.addToUserHashMap
                AppointmentManager.addToUserHashMap(userName, userId);


                //console checker, can be deleted if needed.
                System.out.println(userId + ", " + userName + ", " + password);



            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Adds a User object to the ObservableList of Users
     * @param user
     */
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
