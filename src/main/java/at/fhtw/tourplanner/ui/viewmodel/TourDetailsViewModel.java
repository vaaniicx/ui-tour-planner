package at.fhtw.tourplanner.ui.viewmodel;

import at.fhtw.tourplanner.ui.model.Tour;
import at.fhtw.tourplanner.ui.model.ViewMode;
import at.fhtw.tourplanner.ui.service.TourService;
import at.fhtw.tourplanner.ui.service.TourSelectionService;
import at.fhtw.tourplanner.ui.service.ViewModeService;
import at.fhtw.tourplanner.ui.service.api.TourApiService;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;

public class TourDetailsViewModel {

    private final TourApiService tourApiService = TourApiService.getInstance();
    private final ViewModeService viewModeService = ViewModeService.getInstance();
    private final TourService tourService = TourService.getInstance();

    @Getter
    private final StringProperty name = new SimpleStringProperty();

    @Getter
    private final StringProperty description = new SimpleStringProperty();

    @Getter
    private final StringProperty from = new SimpleStringProperty();

    @Getter
    private final StringProperty to = new SimpleStringProperty();

    @Getter
    private final ObjectProperty<ViewMode> viewMode = new SimpleObjectProperty<>();

    @Getter
    private final ObjectProperty<Tour> selectedTour = new SimpleObjectProperty<>();

    public TourDetailsViewModel() {
        selectedTour.bindBidirectional(TourSelectionService.getInstance().getSelectedTour());
        viewMode.bindBidirectional(viewModeService.getViewMode());

        selectedTour.addListener((_, _, _) -> {
            if (selectedTour.get() != null) {
                showTour();
            } else {
                showEmptyTour();
            }
        });
        viewMode.addListener((_, _, viewMode) -> onViewModeChange(viewMode));
    }

    private void onViewModeChange(ViewMode viewMode) {
        if (viewMode == ViewMode.CREATE) {
            selectedTour.set(null);
            showEmptyTour();
        }
    }

    public void showTour() {
        Tour tour = selectedTour.get();
        name.setValue(tour.name());
        description.setValue(tour.description());
        from.setValue(tour.from());
        to.setValue(tour.to());
    }

    private void showEmptyTour() {
        name.setValue("");
        description.setValue("");
        from.setValue("");
        to.setValue("");
    }

    public void switchToCreateMode() {
        viewModeService.getViewMode().set(ViewMode.CREATE);
    }

    public void switchToEditMode() {
        viewModeService.getViewMode().set(ViewMode.EDIT);
    }

    public void saveTour() {
        if (selectedTour.get() == null) {
            Tour toBeSaved = new Tour(null, name.get(), description.get(), from.get(), to.get());
            tourApiService.createTour(toBeSaved);
        } else {
            Tour toBeUpdated = new Tour(selectedTour.get().id(), name.get(), description.get(), from.get(), to.get());
            tourApiService.updateTour(toBeUpdated);
        }
        tourService.loadTours();
    }

    public void deleteTour() {
        tourApiService.deleteTour(selectedTour.get().id());
        tourService.loadTours();
    }
}
