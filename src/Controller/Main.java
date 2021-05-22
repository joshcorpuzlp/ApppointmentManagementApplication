package Controller;

import Utility.DbConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;

public class Main extends Application {

    /**
     * Start method for the JAVAFX application
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../View/LogInPage.fxml"));
        primaryStage.setTitle("Appointment Management V1.0");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    /**
     * Main method of the programm
     * @param args
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        //establish the connection with the database.
        DbConnection.makeConnection();

        //sets the statement as the connection.
        DbConnection.setStatement(DbConnection.getConnection());

        launch(args);

        DbConnection.closeConnection();
    }
}
//Created myExceptions to throw my own exceptions
class myExceptions extends Exception {
    public myExceptions() {
        super();
    }

    public myExceptions(String s) {
        super(s);
    }
}