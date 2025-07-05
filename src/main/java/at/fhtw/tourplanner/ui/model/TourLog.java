package at.fhtw.tourplanner.ui.model;

import java.time.LocalDate;

public record TourLog(Long id, String comment, LocalDate startedAt) {
}
