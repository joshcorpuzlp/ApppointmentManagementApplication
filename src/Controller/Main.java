package Controller;

import Utility.dbConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.lang.module.Configuration;
import java.sql.SQLException;
import java.util.Locale;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../View/LogInPage.fxml"));
        primaryStage.setTitle("Appointment Management V1.0");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        //tester code that forces the default locale to be of French language.
//        Locale forcedLocale = new Locale("fr");
//        Locale.setDefault(forcedLocale);

        dbConnection.makeConnection();
        dbConnection.setStatement(dbConnection.getConnection());



        launch(args);

        dbConnection.closeConnection();
    }
}
