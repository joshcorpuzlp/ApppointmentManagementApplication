package Controller;

import Dao.UserDao;
import Model.AppointmentManager;
import Model.User;
import Utility.dbConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

public class LogInController implements Initializable {

    @FXML private Label welcomeMessage;
    @FXML private Label logInMessage;
    @FXML private Label userNameLabel;
    @FXML private Label passwordLabel;

    @FXML private TextField userNameField;
    @FXML private PasswordField passwordField;

    @FXML private Button signInButton;
    @FXML private Button cancelButton;

    private Locale locale = Locale.getDefault();
    String lang = locale.getDisplayLanguage();
    String country = locale.getDisplayCountry();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        logInMessage.setText("");
        UserDao userDao = new UserDao();
        userDao.loadDbObjects();

        //initializes between English and French loginPages based on the Locale.language
        if (lang.matches("English")) {
            //System.out.println(lang + country + locale);
            welcomeMessage.setText("Welcome! Please sign in.");
            signInButton.setText("Sign in");
            cancelButton.setText("Cancel");
            userNameLabel.setText("Username");
            passwordLabel.setText("Password");
        }

        if (lang.matches("français")) {
            welcomeMessage.setText("Bienvenue! Veuillez vous connecter.");
            signInButton.setText("S'identifier");
            cancelButton.setText("Annuler");
            userNameLabel.setText("Connexion");
            passwordLabel.setText("Mot de passe");
        }

        //tester code, can be removed later.
//        System.out.println(locale);
//        System.out.println(lang);

    }

    public void cancelButtonPressed(ActionEvent actionEvent) throws SQLException {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        dbConnection.closeConnection();
        stage.close();
    }

    public void signInButtonPressed(ActionEvent actionEvent) throws IOException {
        String userNameInput = userNameField.getText();
        String passwordInput = passwordField.getText();

        for (User user : AppointmentManager.getAllUsers()) {
            if (user.getUserName().matches(userNameInput) && user.getPassword().matches(passwordInput)) {
                logInMessage.setText("Login Successful!");
                logInMessage.setTextFill(Color.web("#008000"));


                //if login is successful, open the MainPage.fxml
                Parent root = FXMLLoader.load(getClass().getResource("../View/MainPage.fxml"));
                Scene MainPageScene = new Scene(root);

                Stage stage = (Stage) signInButton.getScene().getWindow();
                stage.setScene(MainPageScene);
                stage.show();

                //return keyword exits the for loop
                return;
            }
            else {
                logInMessage.setText("Login Failed!");
                logInMessage.setTextFill(Color.web("#ff0000"));
            }
        }

    }




}
