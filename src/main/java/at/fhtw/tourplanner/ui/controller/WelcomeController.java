package at.fhtw.tourplanner.ui.controller;

import at.fhtw.tourplanner.ui.view.ViewHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class WelcomeController implements Initializable {

    public Button entryButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        entryButton.setOnAction(_ -> onEntryButtonClick());
    }

    private void onEntryButtonClick() {
        ViewHandler.getInstance().closeStage(getCurrentStage());
        ViewHandler.getInstance().showTourWindow();
    }

    private Stage getCurrentStage() {
        return (Stage) entryButton.getScene().getWindow();
    }
}
