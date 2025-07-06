package at.fhtw.tourplanner.ui.viewmodel;

import at.fhtw.tourplanner.ui.model.Location;
import at.fhtw.tourplanner.ui.model.Tour;
import at.fhtw.tourplanner.ui.model.TourLog;
import at.fhtw.tourplanner.ui.model.ViewMode;
import at.fhtw.tourplanner.ui.service.TourLogSelectionService;
import at.fhtw.tourplanner.ui.service.TourService;
import at.fhtw.tourplanner.ui.service.TourSelectionService;
import at.fhtw.tourplanner.ui.service.ViewModeService;
import at.fhtw.tourplanner.ui.service.api.TourApiService;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import lombok.Getter;

import java.util.Objects;
import java.util.Optional;

public class TourDetailsViewModel {

    private final TourApiService tourApiService = TourApiService.getInstance();
    private final ViewModeService viewModeService = ViewModeService.getInstance();
    private final TourService tourService = TourService.getInstance();

    @Getter
    private final StringProperty name = new SimpleStringProperty();

    @Getter
    private final StringProperty description = new SimpleStringProperty();

    @Getter
    private final ObjectProperty<Location> from = new SimpleObjectProperty<>();

    @Getter
    private final ObjectProperty<Location> to = new SimpleObjectProperty<>();

    @Getter
    private final ObjectProperty<ViewMode> viewMode = new SimpleObjectProperty<>();

    @Getter
    private final ObjectProperty<Tour> selectedTour = new SimpleObjectProperty<>();

    private final ObjectProperty<TourLog> selectedTourLog = new SimpleObjectProperty<>();

    private final ListProperty<Tour> tours = new SimpleListProperty<>(FXCollections.observableArrayList());

    public TourDetailsViewModel() {
        selectedTour.bindBidirectional(TourSelectionService.getInstance().getSelectedTour());
        selectedTourLog.bindBidirectional(TourLogSelectionService.getInstance().getSelectedTourLog());
        Bindings.bindContentBidirectional(tours, TourService.getInstance().getTours());
        viewMode.bindBidirectional(viewModeService.getViewMode());

        selectedTour.addListener((_, _, _) -> {
            if (selectedTour.get() != null) {
                showTour();
            } else {
                showEmptyTour();
            }
        });

        selectedTourLog.addListener((_, _, selectedTourLog) -> {
            if (selectedTourLog != null) {
                Optional<Tour> tourContainingTourLog = tours.stream().filter(tour -> Objects.equals(selectedTourLog.tourId(), tour.id())).findFirst();
                tourContainingTourLog.ifPresent(selectedTour::set);
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

    private void showTour() {
        Tour tour = selectedTour.get();
        name.setValue(tour.name());
        description.setValue(tour.description());
        from.setValue(tour.from());
        to.setValue(tour.to());
    }

    private void showEmptyTour() {
        name.setValue("");
        description.setValue("");
        from.setValue(new Location("", ""));
        to.setValue(new Location("", ""));
    }

    public void switchToCreateMode() {
        viewModeService.getViewMode().set(ViewMode.CREATE);
    }

    public void switchToEditMode() {
        viewModeService.getViewMode().set(ViewMode.EDIT);
    }

    public void saveTour() {
        if (selectedTour.get() == null) {
            Tour toBeSaved = new Tour(null, name.get(), description.get(), from.get(), to.get(), "CAR", null, null);
            tourApiService.createTour(toBeSaved);
        } else {
            Tour toBeUpdated = new Tour(selectedTour.get().id(), name.get(), description.get(), from.get(), to.get(), "", null, null);
            tourApiService.updateTour(toBeUpdated);
        }
        tourService.loadTours();
    }

    public void deleteTour() {
        tourApiService.deleteTour(selectedTour.get().id());
        tourService.loadTours();
    }
}
