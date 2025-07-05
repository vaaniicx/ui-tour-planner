package at.fhtw.tourplanner.ui.service.api;

import at.fhtw.tourplanner.ui.model.Tour;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import okhttp3.*;

import java.io.IOException;
import java.util.List;

public class TourApiService {

    private final OkHttpClient client = new OkHttpClient();

    private final ObjectMapper mapper = new ObjectMapper();

    private static TourApiService instance;

    public TourApiService() {
        mapper.registerModule(new JavaTimeModule());
    }

    private final String BASE_URL = "http://localhost:8080/api/tours";

    public List<Tour> getAllTours() {
        Request request = new Request.Builder()
                .url(BASE_URL)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            return List.of(mapper.readValue(response.body().string(), Tour[].class));
        } catch (Exception e) {
            return List.of();
        }
    }

    public void createTour(Tour tour) {
        try {
            Request request = new Request.Builder()
                    .url(BASE_URL)
                    .post(RequestBody.create(mapper.writeValueAsString(tour), MediaType.parse("application/json")))
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

    public void updateTour(Tour tour) {
        try {
            Request request = new Request.Builder()
                    .url(BASE_URL + "/" + tour.id())
                    .put(RequestBody.create(mapper.writeValueAsString(tour), MediaType.parse("application/json")))
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

    public void deleteTour(long tourId) {
        Request request = new Request.Builder()
                .url(BASE_URL + "/" + tourId)
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

    public static synchronized TourApiService getInstance() {
        if (instance == null) {
            instance = new TourApiService();
        }
        return instance;
    }
}
