package at.fhtw.tourplanner.ui.dto.tour.response;

import at.fhtw.tourplanner.ui.dto.tourlog.response.TourLogResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TourExportItem {
    private TourResponse tour;
    private List<TourLogResponse> logs;
}
