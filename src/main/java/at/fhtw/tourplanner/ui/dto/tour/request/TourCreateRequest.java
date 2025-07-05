package at.fhtw.tourplanner.ui.dto.tour.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TourCreateRequest {
    private String name;
    private String description;
    private String from;
    private String to;
}