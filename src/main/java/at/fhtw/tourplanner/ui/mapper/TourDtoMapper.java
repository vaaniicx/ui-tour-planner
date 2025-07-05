package at.fhtw.tourplanner.ui.mapper;

import at.fhtw.tourplanner.ui.dto.tour.request.TourCreateRequest;
import at.fhtw.tourplanner.ui.dto.tour.request.TourUpdateRequest;
import at.fhtw.tourplanner.ui.dto.tour.response.TourResponse;
import at.fhtw.tourplanner.ui.model.Tour;
import org.mapstruct.Mapper;

@Mapper
public interface TourDtoMapper {

    Tour fromResponse(TourResponse response);

    TourCreateRequest toTourCreateRequest(Tour tour);

    TourUpdateRequest toTourUpdateRequest(Tour tour);
}
