package at.fhtw.tourplanner.ui.viewmodel;

import at.fhtw.tourplanner.ui.model.Tour;
import at.fhtw.tourplanner.ui.model.TourLog;
import at.fhtw.tourplanner.ui.model.ViewMode;
import at.fhtw.tourplanner.ui.service.*;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import lombok.Getter;

public class TourListViewModel {

    private final TourService tourService = TourService.getInstance();
    private final TourLogService tourLogService = TourLogService.getInstance();
    private final ViewModeService viewModeService = ViewModeService.getInstance();

    @Getter
    private final ListProperty<Tour> tours = new SimpleListProperty<>(FXCollections.observableArrayList());

    @Getter
    private final ListProperty<TourLog> tourLogs = new SimpleListProperty<>(FXCollections.observableArrayList());

    private final ObjectProperty<Tour> selectedTour = new SimpleObjectProperty<>();
    private final ObjectProperty<TourLog> selectedTourLog = new SimpleObjectProperty<>();

    public TourListViewModel() {
        Bindings.bindContentBidirectional(tours, TourService.getInstance().getTours());
        Bindings.bindContentBidirectional(tourLogs, TourLogService.getInstance().getTourLogs());
        selectedTour.bindBidirectional(TourSelectionService.getInstance().getSelectedTour());
        selectedTourLog.bindBidirectional(TourLogSelectionService.getInstance().getSelectedTourLog());
    }

    public void loadTours() {
        tourService.loadTours();
    }

    public void loadTourLogs(long id) {
        tourLogService.loadTourLogs(id);
    }

    public void selectTour(Tour tour) {
        selectedTour.set(tour);
    }

    public void deselectTour() {
        if (selectedTour.get() != null) {
            selectedTour.set(null);
        }
    }

    public void selectTourLog(TourLog tourLog) {
        selectedTourLog.set(tourLog);
    }

    public void deselectTourLog() {
        if (selectedTourLog.get() != null) {
            selectedTourLog.set(null);
        }
    }

    public void switchToReadOnlyViewMode() {
        viewModeService.getViewMode().set(ViewMode.READ_ONLY);
    }
}
