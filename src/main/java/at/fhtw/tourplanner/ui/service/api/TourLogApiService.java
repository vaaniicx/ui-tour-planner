package at.fhtw.tourplanner.ui.service.api;

import at.fhtw.tourplanner.ui.dto.tourlog.response.TourLogResponse;
import at.fhtw.tourplanner.ui.mapper.TourLogDtoMapper;
import at.fhtw.tourplanner.ui.model.TourLog;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import okhttp3.*;
import org.mapstruct.factory.Mappers;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TourLogApiService {

    private final String BASE_URL = "http://localhost:8080/api/tours";

    private final OkHttpClient client = new OkHttpClient();

    private final ObjectMapper mapper = new ObjectMapper();

    private final TourLogDtoMapper dtoMapper = Mappers.getMapper(TourLogDtoMapper.class);

    private static TourLogApiService instance;

    public List<TourLog> getLogsForTour(long tourId) {
        String endpoint = BASE_URL + "/" + tourId + "/logs";
        Request request = new Request.Builder()
                .url(endpoint)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            return Stream.of(mapper.readValue(response.body().string(), TourLogResponse[].class))
                    .map(dtoMapper::fromResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return List.of();
        }
    }

    public void createTourLog(TourLog tourLog) {
        try {
            Request request = new Request.Builder()
                    .url(BASE_URL + "/" + tourLog.tourId() + "/logs")
                    .post(RequestBody.create(mapper.writeValueAsString(dtoMapper.toTourLogCreateRequest(tourLog)), MediaType.parse("application/json")))
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateTourLog(TourLog tourLog) {
        try {
            Request request = new Request.Builder()
                    .url(BASE_URL + "/" + tourLog.tourId() + "/logs/" + tourLog.id())
                    .put(RequestBody.create(mapper.writeValueAsString(dtoMapper.toTourLogUpdateRequest(tourLog)), MediaType.parse("application/json")))
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteTourLog(long tourId, long tourLogId) {
        Request request = new Request.Builder()
                .url(BASE_URL  + "/" + tourId + "/logs/" + tourLogId)
                .delete()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static TourLogApiService getInstance() {
        if (instance == null) {
            instance = new TourLogApiService();
            instance.mapper.registerModule(new JavaTimeModule());
        }
        return instance;
    }
}
