package Controller;

import Utility.dbConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../View/LogInPage.fxml"));
        primaryStage.setTitle("Appointment Management V1.0");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) throws SQLException, ClassNotFoundException {
//        tester code that forces the default locale to be of French language.
//        Locale forcedLocale = new Locale("fr");
//        Locale.setDefault(forcedLocale);

        //establish the connection with the database.
        dbConnection.makeConnection();

        //sets the statement as the connection.
        dbConnection.setStatement(dbConnection.getConnection());

        launch(args);

        dbConnection.closeConnection();
    }
}

class myExceptions extends Exception {
    public myExceptions() {
        super();
    }

    public myExceptions(String s) {
        super(s);
    }
}