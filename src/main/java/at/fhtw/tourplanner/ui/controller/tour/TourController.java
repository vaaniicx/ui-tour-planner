package at.fhtw.tourplanner.ui.controller.tour;

import at.fhtw.tourplanner.ui.model.ViewMode;
import at.fhtw.tourplanner.ui.viewmodel.TourViewModel;
import javafx.beans.binding.Bindings;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.net.URL;
import java.util.ResourceBundle;

public class TourController implements Initializable {

    private TourViewModel viewModel;

    public MenuItem createTourMenuItem;
    public MenuItem importTourMenuItem;
    public TabPane tabPane;
    public Tab tourLogTab;
    public Tab tourTab;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        viewModel = new TourViewModel();

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
}
