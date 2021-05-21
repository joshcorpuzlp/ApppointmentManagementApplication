package Model;

public class User {
    private String userName;
    private String password;
    private int userId;

    /**
     * A method that retrieves a User object's userName. Used when validating log in credentials
     * @return - a String object that is a User's username.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * A method that sets a User object's userName. Unused for now as the program does not need to add users
     * @param userName - passes a String object as the User's userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * A method that retrieves a User object's password. Used when validating log in credentials
     * @return - a String object that is a User's password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * A method that sets a User object's password, Unused for now as the program does not need to add new users.
     * Or change passwords.
      * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * A method that retrieves a User object's userId. Used when validating log in credentials
     * @return - an integer value containing the User id
     */
    public int getUserId() {
        return userId;
    }


    /**
     * A method that sets a User object's id. Unused for now as the program does not need to add users
     * @param userId - passes an integer as the user Id.
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Constructor - called to create a User Object
     * @param userId
     * @param userName
     * @param password
     */
    public User(int userId, String userName, String password) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
    }
}
