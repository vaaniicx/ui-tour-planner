package at.fhtw.tourplanner.ui.mapper;

import at.fhtw.tourplanner.ui.dto.tour.LocationDto;
import at.fhtw.tourplanner.ui.model.Location;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface LocationDtoMapper {

    @Mapping(target = "id", ignore = true)
    LocationDto toDto(Location location);

    Location fromDto(LocationDto locationDto);
}
