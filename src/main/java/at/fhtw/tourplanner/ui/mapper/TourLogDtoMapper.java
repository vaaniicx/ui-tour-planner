package at.fhtw.tourplanner.ui.mapper;

import at.fhtw.tourplanner.ui.dto.tourlog.request.TourLogCreateRequest;
import at.fhtw.tourplanner.ui.dto.tourlog.request.TourLogUpdateRequest;
import at.fhtw.tourplanner.ui.dto.tourlog.response.TourLogResponse;
import at.fhtw.tourplanner.ui.model.TourLog;
import org.mapstruct.Mapper;

@Mapper
public interface TourLogDtoMapper {

    TourLog fromResponse(TourLogResponse response);

    TourLogCreateRequest toTourLogCreateRequest(TourLog tourLog);

    TourLogUpdateRequest toTourLogUpdateRequest(TourLog tourLog);
}
