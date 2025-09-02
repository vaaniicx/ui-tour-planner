package at.fhtw.tourplanner.ui.controller.tour;

import at.fhtw.tourplanner.ui.model.ViewMode;
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

public class TourController implements Initializable {

    private final TourViewModel viewModel = new TourViewModel();

    public MenuItem createTourMenuItem;
    public MenuItem importTourMenuItem;
    public MenuItem exportTourMenuItem;

    public TabPane tabPane;
    public Tab tourLogTab;
    public Tab tourTab;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tourLogTab.disableProperty().bind(Bindings.createBooleanBinding(() -> {
            boolean isEditMode = viewModel.getViewMode().get().equals(ViewMode.EDIT);
            return viewModel.getSelectedTourLog().get() == null && !isEditMode;
        }, viewModel.getViewMode(), viewModel.getSelectedTourLog()));

        viewModel.getSelectedTourLog().addListener((_, _, selectedTourLog) -> {
            if (selectedTourLog != null) {
                switchTab(tourLogTab);
            } else {
                switchTab(tourTab);
            }
        });

        createTourMenuItem.setOnAction(_ -> onCreateTourClick());
        importTourMenuItem.setOnAction(_ -> onImportTourClick());
        exportTourMenuItem.setOnAction(_ -> onExportTourClick());
    }

    private void switchTab(Tab tab) {
        tabPane.getSelectionModel().select(tab);
    }

    private void onCreateTourClick() {
        viewModel.switchToCreateViewMode();
        switchTab(tourTab);
    }

    private void onImportTourClick() {
        System.out.println("onImportTourClick");
    }

    private void onExportTourClick() {
        HostServices hostServices = ViewHandler.getInstance().getHostServices();
        hostServices.showDocument("http://localhost:8080/api/tours/export");
    }
}
