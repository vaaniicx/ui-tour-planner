package at.fhtw.tourplanner.ui.service;

import at.fhtw.tourplanner.ui.model.Tour;
import at.fhtw.tourplanner.ui.service.api.TourApiService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;

import java.util.Comparator;

public class TourService {

    private static TourService instance;

    private final TourApiService tourApiService = TourApiService.getInstance();

    @Getter
    private final ObservableList<Tour> tours = FXCollections.observableArrayList();

    public void loadTours() {
        tours.setAll(tourApiService.getAllTours());
        tours.sort(Comparator.comparing(Tour::name));
    }

    public static synchronized TourService getInstance() {
        if (instance == null) {
            instance = new TourService();
        }
        return instance;
    }
}
