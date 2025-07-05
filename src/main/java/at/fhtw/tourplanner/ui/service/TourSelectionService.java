package at.fhtw.tourplanner.ui.service;

import at.fhtw.tourplanner.ui.model.Tour;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Getter;

public class TourSelectionService {

    private static TourSelectionService instance;

    @Getter
    private final ObjectProperty<Tour> selectedTour = new SimpleObjectProperty<>();

    public static synchronized TourSelectionService getInstance() {
        if (instance == null) {
            instance = new TourSelectionService();
        }
        return instance;
    }
}
