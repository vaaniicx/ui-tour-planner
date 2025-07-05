package at.fhtw.tourplanner.ui;

import at.fhtw.tourplanner.ui.view.ViewHandler;
import javafx.application.Application;
import javafx.stage.Stage;

public class TourPlannerUI extends Application {

    @Override
    public void start(Stage stage) {
        ViewHandler.getInstance().initialize(getHostServices());
        ViewHandler.getInstance().showWelcomeView();
    }
}
