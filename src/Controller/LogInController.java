package Controller;

import Dao.UserDao;
import Model.AppointmentManager;
import Model.User;
import Utility.dbConnection;
import com.mysql.cj.jdbc.JdbcConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.swing.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

public class LogInController implements Initializable {

    @FXML private Label welcomeMessage;
    @FXML private Label logInMessage;

    @FXML private TextField userNameField;
    @FXML private PasswordField passwordField;

    @FXML private Button signInButton;
    @FXML private Button cancelButton;

    private Locale locale = Locale.getDefault();
    String localeString = locale.toString();
    String lang = locale.getDisplayLanguage();
    String country = locale.getDisplayCountry();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        logInMessage.setText("");
        UserDao userDao = new UserDao();
        userDao.loadDbObjects();

        if (lang.matches("en")) {
            //System.out.println(lang + country + locale);
            welcomeMessage.setText("Welcome! Please sign in.");
            signInButton.setText("Sign in");
            cancelButton.setText("Cancel");
        }

        if (lang.matches("fr")) {
            welcomeMessage.setText("Bienvenue! Veuillez vous connecter.");
            signInButton.setText("S'identifier");
            cancelButton.setText("Annuler");
        }



    }

    public void cancelButtonPressed(ActionEvent actionEvent) throws SQLException {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        dbConnection.closeConnection();
        stage.close();
    }

    public void signInButtonPressed(ActionEvent actionEvent) {
        String userNameInput = userNameField.getText();
        String passwordInput = passwordField.getText();

        for (User user : AppointmentManager.getAllUsers()) {
            if (user.getUserName().matches(userNameInput) && user.getPassword().matches(passwordInput)) {
                logInMessage.setText("Login Successful!");
                return;
            }
            else {
                logInMessage.setText("Login Failed!");
            }
        }

    }




}
