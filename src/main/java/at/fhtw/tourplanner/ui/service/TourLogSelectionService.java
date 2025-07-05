package at.fhtw.tourplanner.ui.service;

import at.fhtw.tourplanner.ui.model.TourLog;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Getter;

public class TourLogSelectionService {

    private static TourLogSelectionService instance;

    @Getter
    private final ObjectProperty<TourLog> selectedTourLog = new SimpleObjectProperty<>();

    public static synchronized TourLogSelectionService getInstance() {
        if (instance == null) {
            instance = new TourLogSelectionService();
        }
        return instance;
    }
}
