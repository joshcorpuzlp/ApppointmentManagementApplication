package Utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DbConnection {

    //configures the parameters needed for the getConnection manager
    private static final String DB_NAME = "WJ07TwY";
    private static final String DB_URL = "jdbc:mysql://wgudb.ucertify.com/" + DB_NAME;
    private static final String DB_USER = "U07TwY";
    private static final String DB_PASSWORD = "53689126394";

    //declares a Connection instance with the name conn, this is where we will store the Connection object.
    private static Connection conn = null;

    //declares a Statement instance with the name statement. This will be used when executing statements.
    private static Statement statement;

    /**
     * Method that creates the connection between the application and the SQL Database.
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static void makeConnection() throws ClassNotFoundException, SQLException {
        conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        System.out.println("Connection Successful!");
    }

    /**
     * Method closes the connection between application and SQL Database.
     * @throws SQLException
     */
    public static void closeConnection() throws SQLException {
        System.out.println("Connection closed!");
        conn.close();
    }

    //returns the Connection object.

    /**
     * Returns the Connection object within the DbConnection Class
     * @return
     */
    public static Connection getConnection() {
        return conn;
    }

    /**
     * Method sets the Statement for the DbConnection class
     * @param conn - A connection object is passed.
     * @throws SQLException
     */
    public static void setStatement(Connection conn) throws SQLException {
        statement = conn.createStatement();
    }

    /**
     * Method returns the Statement object within the DbConnection class
     * @return
     */
    public static Statement getStatement() {
        return statement;
    }



}
