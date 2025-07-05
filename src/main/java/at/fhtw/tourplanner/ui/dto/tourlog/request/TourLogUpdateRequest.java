package at.fhtw.tourplanner.ui.dto.tourlog.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TourLogUpdateRequest {
    private LocalDate startedAt;
    private String comment;
}
