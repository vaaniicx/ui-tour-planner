package at.fhtw.tourplanner.ui.controller.tour;

import at.fhtw.tourplanner.ui.controller.BaseController;
import at.fhtw.tourplanner.ui.model.Tour;
import at.fhtw.tourplanner.ui.model.TourLog;
import at.fhtw.tourplanner.ui.viewmodel.TourListViewModel;
import javafx.collections.ListChangeListener;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class TourListController extends BaseController implements Initializable {

    public static final int SINGLE_CLICK = 1;

    private TourListViewModel viewModel;

    public TreeView<Object> tourTree;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        viewModel = new TourListViewModel();

        viewModel.getTours().addListener((ListChangeListener<Tour>) _ -> buildTreeView());
        // viewModel.getTourLogs().addListener((ListChangeListener<TourLog>) _ -> buildTreeView());

        tourTree.setOnMouseClicked(event -> {
            TreeItem<Object> selectedItem = tourTree.getSelectionModel().getSelectedItem();
            if (selectedItem != null && event.getClickCount() == SINGLE_CLICK) {
                onSelectionChange(selectedItem);
            }
        });

        viewModel.loadTours();
    }

    private void buildTreeView() {
        TreeItem<Object> root = new TreeItem<>();

        for (Tour tour : viewModel.getTours()) {
            TreeItem<Object> tourItem = new TreeItem<>(tour);
            tourItem.setExpanded(true);

            viewModel.loadTourLogs(tour.id());

            List<TourLog> logs = viewModel.getTourLogs();
            for (TourLog log : logs) {
                tourItem.getChildren().add(new TreeItem<>(log));
            }

            root.getChildren().add(tourItem);
        }

        tourTree.setRoot(root);
        tourTree.setShowRoot(false);

        tourTree.setCellFactory(_ -> new TreeCell<>() {
            @Override
            protected void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else if (item instanceof Tour tour) {
                    setText(tour.name());
                } else if (item instanceof TourLog log) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d. MMMM yyyy", Locale.GERMAN);
                    setText("Eintrag vom " + log.date().format(formatter));
                } else {
                    setText(item.toString());
                }
            }
        });
    }

    private void onSelectionChange(TreeItem<Object> selectedItem) {
        if (selectedItem != null) {
            if (selectedItem.getValue() instanceof Tour tour) {
                viewModel.deselectTourLog();
                viewModel.selectTour(tour);
            } else if (selectedItem.getValue() instanceof TourLog tourLog) {
                viewModel.deselectTour();
                viewModel.selectTourLog(tourLog);
            }
            switchToReadOnlyMode();
        }
    }
}
