package at.fhtw.tourplanner.ui.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public final class Tour {
    public static final int DURATION_THRESHOLD = 2 * 60 * 60; // = 2 hours
    public static final int DISTANCE_THRESHOLD = 5000; // = 5 kilometres

    private final Long id;
    private final String name;
    private final String description;
    private final Location from;
    private final Location to;
    private final String transportType;
    private final Double distance;
    private final Double duration;

    private double popularity;
    private double childFriendliness;

    public void calculatePopularity(List<TourLog> tourLogs) {
        if (tourLogs.isEmpty()) {
            popularity = 0;
            return;
        }
        popularity = (double) tourLogs.size() / 10;
    }

    public void calculateChildFriendliness(List<TourLog> tourLogs) {
        if (tourLogs.isEmpty()) {
            childFriendliness = 0;
            return;
        }

        double averageDifficulty = tourLogs.stream()
                .mapToInt(log -> log.difficulty().getScore())
                .average()
                .orElse(0);

        double averageDistance = tourLogs.stream().mapToDouble(TourLog::distance).average().orElse(0);
        double averageDuration = tourLogs.stream().mapToDouble(TourLog::duration).average().orElse(0);

        double distanceFactor = averageDistance > DISTANCE_THRESHOLD ? 0.9 : 1.0; // reduce by 10% if distance > 5km
        double durationFactor = averageDuration > DURATION_THRESHOLD ? 0.9 : 1.0; // reduce by 10% if duration > 2h

        double total = averageDifficulty * distanceFactor * durationFactor;
        childFriendliness = roundTwoDigits(total * (10.0 / Difficulty.maxScore()));
    }

    private double roundTwoDigits(double number) {
        return Math.round(number * 100.0) / 100.0;
    }
}

