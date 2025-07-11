package at.fhtw.tourplanner.ui.viewmodel;

import at.fhtw.tourplanner.ui.model.*;
import at.fhtw.tourplanner.ui.service.TourLogSelectionService;
import at.fhtw.tourplanner.ui.service.TourSelectionService;
import at.fhtw.tourplanner.ui.service.ViewModeService;
import at.fhtw.tourplanner.ui.service.api.TourLogApiService;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class TourLogDetailsViewModel {

    private final ObjectProperty<LocalDate> date = new SimpleObjectProperty<>();
    private final StringProperty comment = new SimpleStringProperty();
    private final ObjectProperty<Difficulty> difficulty = new SimpleObjectProperty<>();
    private final ObjectProperty<Rating> rating = new SimpleObjectProperty<>();
    private final DoubleProperty distance = new SimpleDoubleProperty();
    private final DoubleProperty duration = new SimpleDoubleProperty();
    private final ListProperty<Difficulty> difficultyTypes = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final ListProperty<Rating> ratingTypes = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final ObjectProperty<ViewMode> viewMode = new SimpleObjectProperty<>();
    private final ObjectProperty<TourLog> selectedTourLog = new SimpleObjectProperty<>();
    private final ObjectProperty<Tour> selectedTour = new SimpleObjectProperty<>();

    public TourLogDetailsViewModel() {
        difficultyTypes.setAll(Difficulty.values());
        ratingTypes.setAll(Rating.values());

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
        difficulty.setValue(tourLog.difficulty());
        rating.setValue(tourLog.rating());
        distance.setValue(tourLog.distance());
        duration.setValue(tourLog.duration());
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
        distance.setValue(0);
    }

    public void saveTourLog() {
        if (selectedTourLog.get() == null) {
            TourLog toBeSaved = new TourLog(null, selectedTour.get().id(), comment.get(), date.get(), difficulty.get(), rating.get(), distance.get(), duration.get());
            TourLogApiService.getInstance().createTourLog(toBeSaved);
        } else {
            TourLog selectedTourLog = this.selectedTourLog.get();
            TourLog toBeUpdated = new TourLog(selectedTourLog.id(), selectedTourLog.tourId(), comment.get(), date.get(), difficulty.get(), rating.get(), distance.get(), duration.get());
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
