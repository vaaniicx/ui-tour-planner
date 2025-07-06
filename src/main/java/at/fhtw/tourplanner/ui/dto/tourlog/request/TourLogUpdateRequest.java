package at.fhtw.tourplanner.ui.dto.tourlog.request;

import at.fhtw.tourplanner.ui.model.Difficulty;
import at.fhtw.tourplanner.ui.model.Rating;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TourLogUpdateRequest {

    private LocalDate date;

    private String comment;

    private Difficulty difficulty;

    private Rating rating;

    private Double distance;

    private Double duration;
}
