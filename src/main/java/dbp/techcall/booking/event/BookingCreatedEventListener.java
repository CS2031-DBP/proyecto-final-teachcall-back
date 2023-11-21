package dbp.techcall.booking.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import dbp.techcall.booking.domain.Booking;
import dbp.techcall.timeSlot.domain.TimeSlot;
import dbp.techcall.meetingDetails.domain.MeetingDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import java.time.ZoneOffset;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.net.http.HttpClient;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class BookingCreatedEventListener implements ApplicationListener<BookingCreatedEvent> {

    private final MeetingDetailsService meetingDetailsService;
    @Value("${whereby}")
    private final String apiKey;

    @Autowired
    public BookingCreatedEventListener(MeetingDetailsService meetingDetailsService, @Value("${whereby}") String apiKey) {
        this.meetingDetailsService = meetingDetailsService;
        this.apiKey = apiKey;
    }

    private String getLatestEndTime(Booking booking) {
        Set<TimeSlot> timeSlots = booking.getTimeSlot();
        return timeSlots.stream()
                .map(TimeSlot::getEndTime)
                .max(LocalTime::compareTo)
                .map(endTime -> ZonedDateTime.of(findLatestDate(timeSlots), endTime, ZoneId.systemDefault()))
                .map(zdt -> zdt.withZoneSameInstant(ZoneOffset.UTC))
                .map(zdt -> zdt.format(DateTimeFormatter.ISO_INSTANT))
                .orElse(null);
    }

    private LocalDate findLatestDate(Set<TimeSlot> timeSlots) {
        return timeSlots.stream()
                .map(TimeSlot::getDate)
                .max(LocalDate::compareTo)
                .orElse(LocalDate.now());
    }

    @Override
    public void onApplicationEvent(BookingCreatedEvent bookingCreatedEvent) {
        Booking booking = bookingCreatedEvent.getBooking();
        String endDate = getLatestEndTime(booking);

        if (endDate == null) {
            return;
        }
        var data = Map.of("endDate", endDate, "fields", Collections.singletonList("hostRoomUrl"));

        HttpRequest request = null;
        try {
            request = HttpRequest.newBuilder(URI.create("https://api.whereby.dev/v1/meetings"))
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(new ObjectMapper().writeValueAsString(data)))
                    .build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        try {
            HttpResponse<String> response = sendRequestWithRetries(request);
            if (response != null && response.statusCode() == 201) {
                var responseBody = new ObjectMapper().readValue(response.body(), Map.class);
                String hostRoomUrl = (String) responseBody.get("hostRoomUrl");
                String viewerRoomUrl = (String) responseBody.get("roomUrl");
                String meetingId = (String) responseBody.get("meetingId");
                meetingDetailsService.createMeetingDetails(booking, hostRoomUrl, viewerRoomUrl, endDate, meetingId);
            }
        } catch (Exception e) {
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
