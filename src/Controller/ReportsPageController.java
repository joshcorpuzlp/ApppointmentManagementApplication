package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ReportsPageController implements Initializable {

    @FXML private Button report1;
    @FXML private Button report2;
    @FXML private Button report3;
    @FXML private Button cancelButton;

    //method that runs when the cancelButton is pressed. It returns the program to the MainPage.
    public void cancelButtonPressed(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../View/MainPage.fxml"));
        Scene MainPageScene = new Scene(root);

        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.setScene(MainPageScene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
}
