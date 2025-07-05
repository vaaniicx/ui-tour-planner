package at.fhtw.tourplanner.ui.model;

import java.time.LocalDate;

public record TourLog(Long id, Long tourId, String comment, LocalDate startedAt) {
}
