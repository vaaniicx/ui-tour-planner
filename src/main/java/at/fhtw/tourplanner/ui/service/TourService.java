package at.fhtw.tourplanner.ui.service;

import at.fhtw.tourplanner.ui.model.Tour;
import at.fhtw.tourplanner.ui.service.api.TourApiService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;

import java.util.Comparator;

public class TourService {

    private static TourService instance;

    @Getter
    private final ObservableList<Tour> tours = FXCollections.observableArrayList();

    public void loadTours() {
        tours.setAll(TourApiService.getInstance().getAllTours());
        tours.sort(Comparator.comparing(Tour::name));
    }

    public static synchronized TourService getInstance() {
        if (instance == null) {
            instance = new TourService();
        }
        return instance;
    }
}
