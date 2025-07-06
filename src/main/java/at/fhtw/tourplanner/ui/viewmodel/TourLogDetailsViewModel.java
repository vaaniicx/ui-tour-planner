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

@Getter
public class TourLogDetailsViewModel {

    private final ObjectProperty<LocalDate> date = new SimpleObjectProperty<>();
    private final StringProperty comment = new SimpleStringProperty();
    private final ObjectProperty<ViewMode> viewMode = new SimpleObjectProperty<>();
    private final ObjectProperty<TourLog> selectedTourLog = new SimpleObjectProperty<>();
    private final ObjectProperty<Tour> selectedTour = new SimpleObjectProperty<>();

    public TourLogDetailsViewModel() {
        selectedTourLog.bindBidirectional(TourLogSelectionService.getInstance().getSelectedTourLog());
        selectedTour.bind(TourSelectionService.getInstance().getSelectedTour());
        viewMode.bindBidirectional(ViewModeService.getInstance().getViewMode());

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
            TourLogApiService.getInstance().createTourLog(toBeSaved);
        } else {
            TourLog selectedTourLog = this.selectedTourLog.get();
            TourLog toBeUpdated = new TourLog(selectedTourLog.id(), selectedTourLog.tourId(), comment.get(), date.get());
            TourLogApiService.getInstance().updateTourLog(toBeUpdated);
        }
        System.out.println("todo: refetch tour logs");
    }

    public void deleteTourLog() {
        TourLog toBeDeleted = this.selectedTourLog.get();
        TourLogApiService.getInstance().deleteTourLog(toBeDeleted.tourId(), toBeDeleted.id());
    }

    public void switchToEditMode() {
        ViewModeService.getInstance().getViewMode().set(ViewMode.EDIT);
    }
}
