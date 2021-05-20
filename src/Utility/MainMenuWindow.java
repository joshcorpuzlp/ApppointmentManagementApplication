package Utility;

import Controller.MainController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class MainMenuWindow {

    /**
     * A reusable method that is called when the program needs to go back to the MainPage.fxml
     * @param actionEvent - passes an ActionEvent as the trigger.
     * @throws IOException
     */
    public static void returnToMainMenu(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(MainController.class.getResource("../view/MainPage.fxml"));
        Scene mainControllerScene = new Scene(root);

        Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        window.setScene(mainControllerScene);
        window.show();
    }
}
