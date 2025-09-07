package at.fhtw.tourplanner.ui.service;

import at.fhtw.tourplanner.ui.dto.tour.response.TourExportItem;
import at.fhtw.tourplanner.ui.mapper.TourDtoMapper;
import at.fhtw.tourplanner.ui.model.Tour;
import at.fhtw.tourplanner.ui.service.api.TourApiService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.mapstruct.factory.Mappers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

public class TourImportService {

    private final ObjectMapper om = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private final TourDtoMapper dtoMapper = Mappers.getMapper(TourDtoMapper.class);
    private final TourApiService api = TourApiService.getInstance();

    /**
     * Erwartet eine Datei im Format:
     * [
     *   { "tour": { ...TourResponse... }, "logs": [ ... ] },
     *   ...
     * ]
     * Importiert jede Tour über das Backend (createTour).
     * @return Anzahl importierter Touren
     */
    public int importTours(File file) throws IOException {
        String json = Files.readString(file.toPath());

        // 1) Datei als Array von Export-Items lesen
        TourExportItem[] items = om.readValue(json, TourExportItem[].class);

        // 2) Nur die Touren extrahieren und ins UI-Model mappen
        List<Tour> tours = Arrays.stream(items)
                .map(TourExportItem::getTour)
                .map(dtoMapper::fromResponse)   // TourResponse (UI) -> Tour (UI-Model)
                .toList();

        // 3) Jede Tour via API erstellen (POST /api/tours)
        for (Tour t : tours) {
            api.createTour(t);  // dein Mapper für den CreateRequest ignoriert die id
        }

        return tours.size();
    }
}
