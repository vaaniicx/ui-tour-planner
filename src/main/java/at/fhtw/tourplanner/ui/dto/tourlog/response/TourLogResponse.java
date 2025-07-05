package at.fhtw.tourplanner.ui.dto.tourlog.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TourLogResponse {

    private Long id;

    private Long tourId;

    private LocalDate date;

    private String comment;
}
