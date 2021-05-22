package Controller;

import Dao.UserDao;
import Model.AppointmentManager;
import Model.User;
import Utility.FileLogger;
import Utility.DbConnection;
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
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;

public class LogInController implements Initializable {

    @FXML private Label welcomeMessage;
    @FXML private Label logInMessage;
    @FXML private Label userNameLabel;
    @FXML private Label passwordLabel;
    @FXML private Label zoneIdLabel;

    @FXML private TextField userNameField;
    @FXML private PasswordField passwordField;

    @FXML private Button signInButton;
    @FXML private Button cancelButton;



    private Locale locale = Locale.getDefault();
    private String lang = locale.getDisplayLanguage();
    private String country = locale.getDisplayCountry();
    private ZoneId zoneId = ZoneId.systemDefault();

    /**
     * Initializes the Login page
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        zoneIdLabel.setText("Zone: " + zoneId.toString());

        //configures the login message to say nothing.
        logInMessage.setText("");

        //loads the ObservableList of User objects in AppointmentManager with the contents of the Database.
        UserDao userDao = new UserDao();
        userDao.loadDbObjects();

        //initializes between English and French loginPages based on the Locale.language
        if (lang.matches("English")) {
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

    }

    public void cancelButtonPressed(ActionEvent actionEvent) throws SQLException {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        DbConnection.closeConnection();
        stage.close();
    }

    //log in button pressed when
    public void signInButtonPressed(ActionEvent actionEvent) throws IOException {
        String userNameInput = userNameField.getText();
        String passwordInput = passwordField.getText();

        //First loop through all the users and check if the login credentials are matched.
        for (User user : AppointmentManager.getAllUsers()) {
            if (user.getUserName().matches(userNameInput) && user.getPassword().matches(passwordInput)) {

                //holds the userId of the user credentials used at log in.
                AppointmentManager.setLoggedInUserId(user);

                //logInMessage message changes depending the System language.
                if (lang.matches("English")) {
                    logInMessage.setText("Login Successful!");
                } else {
                    logInMessage.setText("Connexion réussie!");
                }

                logInMessage.setTextFill(Color.web("#008000"));

                //call the FileLogger class's method to log the user that logged in and the current time
                FileLogger.fileLog(user);


                //if login is successful, open the MainPage.fxml
                Parent root = FXMLLoader.load(getClass().getResource("../View/MainPage.fxml"));
                Scene MainPageScene = new Scene(root);

                Stage stage = (Stage) signInButton.getScene().getWindow();
                stage.setScene(MainPageScene);
                stage.show();


                //return keyword exits the for loop
                return;
            }

        }

        //Since login credentials can not be found, we run the LoginFailed message as well as the FileLogger.invalidLoginLog() method
        if (lang.matches("English")) {
            logInMessage.setText("Login Failed!");

            //calls the FileLogger.invalidLoginLog that passed the userNameInput as the string argument.
            FileLogger.invalidLoginLog(userNameInput);

        } else {
            logInMessage.setText("Échec de la connexion");

            //calls the FileLogger.invalidLoginLog that passed the userNameInput as the string argument.
            FileLogger.invalidLoginLog(userNameInput);

        }

        logInMessage.setTextFill(Color.web("#ff0000"));
    }



}
