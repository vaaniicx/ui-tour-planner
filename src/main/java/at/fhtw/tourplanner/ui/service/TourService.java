package at.fhtw.tourplanner.ui.service;

import at.fhtw.tourplanner.ui.model.Tour;
import at.fhtw.tourplanner.ui.model.TourLog;
import at.fhtw.tourplanner.ui.service.api.TourApiService;
import at.fhtw.tourplanner.ui.service.api.TourLogApiService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;

import java.util.Comparator;
import java.util.List;

public class TourService {

    private static TourService instance;

    private static final TourLogApiService tourLogApiService = TourLogApiService.getInstance();

    @Getter
    private final ObservableList<Tour> tours = FXCollections.observableArrayList();

    public void loadTours() {
        tours.setAll(TourApiService.getInstance().getAllTours());
        tours.sort(Comparator.comparing(Tour::getName));
        tours.forEach(tour -> {
            List<TourLog> logs = tourLogApiService.getLogsForTour(tour.getId());
            tour.calculatePopularity(logs);
            tour.calculateChildFriendliness(logs);
        });
    }

    public static synchronized TourService getInstance() {
        if (instance == null) {
            instance = new TourService();
        }
        return instance;
    }
}
