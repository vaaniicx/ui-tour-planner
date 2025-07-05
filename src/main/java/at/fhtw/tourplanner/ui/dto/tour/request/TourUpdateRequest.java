package at.fhtw.tourplanner.ui.dto.tour.request;

import at.fhtw.tourplanner.ui.dto.tour.LocationDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TourUpdateRequest {

    private String name;

    private String description;

    private LocationDto from;

    private LocationDto to;
}
