package at.fhtw.tourplanner.ui.controller.tour;

import at.fhtw.tourplanner.ui.controller.BaseController;
import at.fhtw.tourplanner.ui.view.ViewHandler;
import at.fhtw.tourplanner.ui.viewmodel.TourViewModel;
import javafx.application.HostServices;
import javafx.beans.binding.Bindings;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.net.URL;
import java.util.ResourceBundle;

public class TourController extends BaseController implements Initializable {

    private final TourViewModel viewModel = new TourViewModel();

    public MenuItem createTourMenuItem;
    public MenuItem importTourMenuItem;
    public MenuItem exportTourMenuItem;

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
    }

    private void handleCreateTour() {
        viewModel.getSelectedTour().set(null);
        switchToCreateMode();
        switchTab(tourTab);
    }

    private void handleImportTour() {
        System.out.println("onImportTourClick");
    }

    private void handleExportTour() {
        HostServices hostServices = ViewHandler.getInstance().getHostServices();
        hostServices.showDocument("http://localhost:8080/api/tours/export");
    }
}
