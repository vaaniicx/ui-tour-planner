package at.fhtw.tourplanner.ui.service.api;

import at.fhtw.tourplanner.ui.model.TourLog;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;

public class TourLogApiService {

    private final String BASE_URL = "http://localhost:8080/api/tours";

    private final OkHttpClient client = new OkHttpClient();

    private final ObjectMapper mapper = new ObjectMapper();

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

            return List.of(mapper.readValue(response.body().string(), TourLog[].class));
        } catch (Exception e) {
            return List.of();
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
