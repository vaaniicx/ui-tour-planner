package at.fhtw.tourplanner.ui.model;

import java.time.LocalDate;

public record TourLog(Long id, Long tourId, String comment, LocalDate date, Difficulty difficulty, Rating rating,
                      Double distance, Double duration) {
}
