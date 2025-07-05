package at.fhtw.tourplanner.ui.viewmodel;

import at.fhtw.tourplanner.ui.model.TourLog;
import at.fhtw.tourplanner.ui.model.ViewMode;
import at.fhtw.tourplanner.ui.service.TourLogSelectionService;
import at.fhtw.tourplanner.ui.service.TourSelectionService;
import at.fhtw.tourplanner.ui.service.ViewModeService;
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

    private final ViewModeService viewModeService = ViewModeService.getInstance();

    public TourLogDetailsViewModel() {
        selectedTourLog.bindBidirectional(TourLogSelectionService.getInstance().getSelectedTourLog());
        viewMode.bindBidirectional(viewModeService.getViewMode());

        selectedTourLog.addListener((_, _, _) -> {
            if (selectedTourLog.get() != null) {
                showTourLog();
            } else {
                showEmptyTourLog();
            }
        });
        viewMode.addListener((_, _, newValue) -> onViewModeChange(newValue));
    }

    public void showTourLog() {
        TourLog tourLog = selectedTourLog.get();
        date.setValue(tourLog.startedAt());
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
            TourLog toBeSaved = new TourLog(null, comment.get(), date.get());
            System.out.println("create tour log // need to implement api layer");
        } else {
            TourLog toBeUpdated = selectedTourLog.get();
            System.out.println("update tour log // need to implement api layer");
        }
        System.out.println("refetch tour logs");
    }

    public void switchToEditMode() {
        viewModeService.getViewMode().set(ViewMode.EDIT);
    }

    public void deleteTourLog() {
        System.out.println("delete tour log // need to implement api layer");
    }
}
