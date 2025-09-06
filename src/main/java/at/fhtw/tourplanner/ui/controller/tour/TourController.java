package at.fhtw.tourplanner.ui.controller.tour;

import at.fhtw.tourplanner.ui.controller.Controller;
import at.fhtw.tourplanner.ui.view.ViewHandler;
import at.fhtw.tourplanner.ui.viewmodel.TourViewModel;
import javafx.application.HostServices;
import javafx.beans.binding.Bindings;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class TourController extends Controller implements Initializable {

    private final TourViewModel viewModel = new TourViewModel();

    public MenuItem createTourMenuItem;
    public MenuItem importTourMenuItem;
    public MenuItem exportTourMenuItem;
    public MenuItem generateSingleReport;
    public MenuItem generateSummaryReport;

    public TabPane tabPane;
    public Tab tourLogTab;
    public Tab tourTab;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupBindings();
        setupListeners();
        setupMenuActions();
    }

    private void setupBindings() {
        tourLogTab.disableProperty().bind(Bindings.createBooleanBinding(() -> {
            boolean isTourLogSelected = viewModel.getSelectedTourLog().get() != null;
            return !(isTourLogSelected || isEditMode());
        }, viewModel.getViewMode(), viewModel.getSelectedTourLog()));

        tourTab.disableProperty().bind(Bindings.createBooleanBinding(() ->
                viewModel.getSelectedTourLog().get() != null, viewModel.getSelectedTourLog()));
    }

    private void setupListeners() {
        viewModel.getSelectedTourLog().addListener((_, _, newTourLog) -> {
            boolean isTourLogSelected = newTourLog != null;
            if (isTourLogSelected) {
                switchTab(tourLogTab);
            } else {
                switchTab(tourTab);
            }
        });
    }

    private void switchTab(Tab tab) {
        tabPane.getSelectionModel().select(tab);
    }

    private void setupMenuActions() {
        createTourMenuItem.setOnAction(_ -> handleCreateTour());
        importTourMenuItem.setOnAction(_ -> handleImportTour());
        exportTourMenuItem.setOnAction(_ -> handleExportTour());
        generateSingleReport.setOnAction(_ -> handleGenerateSingleReport());
        generateSummaryReport.setOnAction(_ -> handleGenerateSummaryReport());
    }

    private void handleCreateTour() {
        viewModel.getSelectedTour().set(null);
        viewModel.getSelectedTourLog().set(null);
        switchToCreateMode();
        switchTab(tourTab);
    }

    private void handleImportTour() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Touren importieren");

        FileChooser.ExtensionFilter jsonFilter = new FileChooser.ExtensionFilter("JSON Dateien (*.json)", "*.json");
        fileChooser.getExtensionFilters().add(jsonFilter);

        Stage stage = (Stage) tabPane.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile == null) {
            System.out.println("Error");
        } else {
            System.out.println("Success");
        }
    }

    private void handleExportTour() {
        HostServices hostServices = ViewHandler.getInstance().getHostServices();
        hostServices.showDocument("http://localhost:8080/api/tours/export");
    }

    private void handleGenerateSingleReport() {
        if (viewModel.getSelectedTour().get() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Bericht erstellen");
            alert.setHeaderText("Keine Tour ausgewählt");
            alert.setContentText("Bitte wählen Sie eine Tour aus, um einen Bericht zu erstellen.");
            alert.showAndWait();
            return;
        }

        HostServices hostServices = ViewHandler.getInstance().getHostServices();
        hostServices.showDocument("http://localhost:8080/api/tours/report/" + viewModel.getSelectedTour().get().getId());
    }

    private void handleGenerateSummaryReport() {
        HostServices hostServices = ViewHandler.getInstance().getHostServices();
        hostServices.showDocument("http://localhost:8080/api/tours/report");
    }
}
