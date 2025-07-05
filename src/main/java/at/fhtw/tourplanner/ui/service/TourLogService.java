package at.fhtw.tourplanner.ui.service;

import at.fhtw.tourplanner.ui.model.TourLog;
import at.fhtw.tourplanner.ui.service.api.TourLogApiService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TourLogService {

    private static TourLogService instance;

    private final TourLogApiService tourLogApiService = TourLogApiService.getInstance();

    @Getter
    private final ObservableList<TourLog> tourLogs = FXCollections.observableArrayList();

    public void loadTourLogs(long tourId) {
        List<TourLog> logs = tourLogApiService.getLogsForTour(tourId);
        if (logs.isEmpty()) {
            tourLogs.setAll(new ArrayList<>());
        } else {
            tourLogs.setAll(logs);
            tourLogs.sort(Comparator.comparing(TourLog::startedAt).reversed());
        }
    }

    public static synchronized TourLogService getInstance() {
        if (instance == null) {
            instance = new TourLogService();
        }
        return instance;
    }
}
