package at.fhtw.tourplanner.ui.viewmodel;

import at.fhtw.tourplanner.ui.model.Tour;
import at.fhtw.tourplanner.ui.model.TourLog;
import at.fhtw.tourplanner.ui.model.ViewMode;
import at.fhtw.tourplanner.ui.service.TourLogSelectionService;
import at.fhtw.tourplanner.ui.service.TourLogService;
import at.fhtw.tourplanner.ui.service.TourSelectionService;
import at.fhtw.tourplanner.ui.service.ViewModeService;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import lombok.Getter;

@Getter
public class TourViewModel {

    private final ObjectProperty<ViewMode> viewMode = new SimpleObjectProperty<>();
    private final ListProperty<TourLog> tourLogs = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final ObjectProperty<Tour> selectedTour = new SimpleObjectProperty<>();
    private final ObjectProperty<TourLog> selectedTourLog = new SimpleObjectProperty<>();

    public TourViewModel() {
        viewMode.bindBidirectional(ViewModeService.getInstance().getViewMode());
        Bindings.bindContentBidirectional(tourLogs, TourLogService.getInstance().getTourLogs());
        selectedTour.bindBidirectional(TourSelectionService.getInstance().getSelectedTour());
        selectedTourLog.bindBidirectional(TourLogSelectionService.getInstance().getSelectedTourLog());
    }
}
