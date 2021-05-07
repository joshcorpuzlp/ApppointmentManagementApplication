package Model;

public class User {
    private String userName;
    private String password;
    private int userId;

    public String getUserName() {
        return userName;
    }

    //wont be used on its own because we do not have a create a user function.
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    //wont be used on its own because we do not have a create a user function.
    public void setPassword(String password) {
        this.password = password;
    }

    public int getUserId() {
        return userId;
    }

    //wont be used on its own because we do not have a create a user function.
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public User(int userId, String userName, String password) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
    }
}
