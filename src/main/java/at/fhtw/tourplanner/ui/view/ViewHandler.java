package at.fhtw.tourplanner.ui.view;

import javafx.application.HostServices;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;

@Getter
public class ViewHandler {

    private static ViewHandler instance;

    private HostServices hostServices;

    public void initialize(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    public void showWelcomeView() {
        FXMLLoader loader = new FXMLLoader(ViewHandler.class.getResource("/view/welcome.fxml"));
        createStage(loader);
    }

    public void showTourWindow() {
        FXMLLoader loader = new FXMLLoader(ViewHandler.class.getResource("/view/tour/tour.fxml"));
        createStage(loader);
    }

    private void createStage(FXMLLoader loader) {
        Scene scene = null;

        try {
            scene = new Scene(loader.load(), 1280, 720);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Tour Planner");
        stage.show();
    }

    public void closeStage(Stage stage) {
        stage.close();
    }

    public static synchronized ViewHandler getInstance() {
        if (instance == null) {
            instance = new ViewHandler();
        }
        return instance;
    }

}
