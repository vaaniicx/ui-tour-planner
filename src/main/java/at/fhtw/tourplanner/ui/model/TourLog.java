package at.fhtw.tourplanner.ui.model;

import java.time.LocalDate;

public record TourLog(Long id, Long tourId, String comment, LocalDate date, Difficulty difficulty, Rating rating,
                      Double distance, Double duration) {

    public TourLog withTourId(Long newTourId) {
        return new TourLog(
                this.id,
                newTourId,
                this.comment,
                this.date,
                this.difficulty,
                this.rating,
                this.distance,
                this.duration
        );
    }
}
