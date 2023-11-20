package dbp.techcall.booking.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dbp.techcall.booking.domain.BookingService;
import dbp.techcall.meetingDetails.domain.MeetingDetails;
import dbp.techcall.meetingDetails.domain.MeetingDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.TimeUnit;

@Component
public class BookingDeleteEventListener implements ApplicationListener<BookingDeleteEvent> {

    private final MeetingDetailsService meetingDetailsService;
    private final String apiKey;

    @Autowired
    public BookingDeleteEventListener(MeetingDetailsService meetingDetailsService, @Value("${whereby}") String apiKey) {
        this.meetingDetailsService = meetingDetailsService;
        this.apiKey = apiKey;
    }

    @Override
    public void onApplicationEvent(BookingDeleteEvent bookingDeleteEvent) {
        var data = meetingDetailsService.getMeetingDetailsById(bookingDeleteEvent.getBookingId()).getMeetingId();

        String apiUrl = "https://api.whereby.dev/v1/meetings/" + data;
        java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder(URI.create(apiUrl))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .DELETE()
                .build();

        try {
            HttpResponse<String> response = sendRequestWithRetries(request);
            if (response != null && response.statusCode() == 204) {
                meetingDetailsService.deleteMeetingDetails(bookingDeleteEvent.getBookingId());
            } else {
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private HttpResponse<String> sendRequestWithRetries(HttpRequest request) throws IOException, InterruptedException {
        final int maxRetries = 3;
        int attempt = 0;
        HttpResponse<String> response;

        while (attempt < maxRetries) {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 429) {
                return response;
            }

            attempt++;
            TimeUnit.MINUTES.sleep(1);
        }
        return null;
    }
}
