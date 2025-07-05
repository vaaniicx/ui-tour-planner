package at.fhtw.tourplanner.ui.dto.tour.response;

import at.fhtw.tourplanner.ui.dto.tour.LocationDto;
import at.fhtw.tourplanner.ui.model.Location;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TourResponse {

    public Long id;

    public String name;

    public String description;

    public LocationDto from;

    public LocationDto to;
}
