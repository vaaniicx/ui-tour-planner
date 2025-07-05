package at.fhtw.tourplanner.ui.dto.tour.response;

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

    public String from;

    public String to;
}
