package at.fhtw.tourplanner.ui.viewmodel;

import at.fhtw.tourplanner.ui.model.Tour;
import at.fhtw.tourplanner.ui.model.TourLog;
import at.fhtw.tourplanner.ui.model.ViewMode;
import at.fhtw.tourplanner.ui.service.TourLogSelectionService;
import at.fhtw.tourplanner.ui.service.TourSelectionService;
import at.fhtw.tourplanner.ui.service.ViewModeService;
import at.fhtw.tourplanner.ui.service.api.TourLogApiService;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;

import java.time.LocalDate;

public class TourLogDetailsViewModel {

    @Getter
    private final ObjectProperty<LocalDate> date = new SimpleObjectProperty<>();

    @Getter
    private final StringProperty comment = new SimpleStringProperty();

    @Getter
    private final ObjectProperty<ViewMode> viewMode = new SimpleObjectProperty<>();

    @Getter
    private final ObjectProperty<TourLog> selectedTourLog = new SimpleObjectProperty<>();

    private final ObjectProperty<Tour> selectedTour = new SimpleObjectProperty<>();

    private final ViewModeService viewModeService = ViewModeService.getInstance();

    private final TourLogApiService tourLogApiService = TourLogApiService.getInstance();

    public TourLogDetailsViewModel() {
        selectedTourLog.bindBidirectional(TourLogSelectionService.getInstance().getSelectedTourLog());
        selectedTour.bind(TourSelectionService.getInstance().getSelectedTour());
        viewMode.bindBidirectional(viewModeService.getViewMode());

        selectedTourLog.addListener((_, _, _) -> {
            if (selectedTourLog.get() != null) {
                showTourLog();
            } else {
                showEmptyTourLog();
            }
        });
        viewMode.addListener((_, _, viewMode) -> onViewModeChange(viewMode));
    }

    public void showTourLog() {
        TourLog tourLog = selectedTourLog.get();
        date.setValue(tourLog.date());
        comment.setValue(tourLog.comment());
    }

    private void onViewModeChange(ViewMode viewMode) {
        if (viewMode == ViewMode.CREATE) {
            selectedTourLog.set(null);
            showEmptyTourLog();
        } else if (selectedTourLog.get() != null) {
            showTourLog();
        }
    }

    private void showEmptyTourLog() {
        date.setValue(LocalDate.now());
        comment.setValue(null);
    }

    public void saveTourLog() {
        if (selectedTourLog.get() == null) {
            TourLog toBeSaved = new TourLog(null, selectedTour.get().id(), comment.get(), date.get());
            tourLogApiService.createTourLog(toBeSaved);
        } else {
            TourLog selectedTourLog = this.selectedTourLog.get();
            TourLog toBeUpdated = new TourLog(selectedTourLog.id(), selectedTourLog.tourId(), comment.get(), date.get());
            tourLogApiService.updateTourLog(toBeUpdated);
        }
        System.out.println("todo: refetch tour logs");
    }

    public void deleteTourLog() {
        TourLog toBeDeleted = this.selectedTourLog.get();
        tourLogApiService.deleteTourLog(toBeDeleted.tourId(), toBeDeleted.id());
    }

    public void switchToEditMode() {
        viewModeService.getViewMode().set(ViewMode.EDIT);
    }
}
