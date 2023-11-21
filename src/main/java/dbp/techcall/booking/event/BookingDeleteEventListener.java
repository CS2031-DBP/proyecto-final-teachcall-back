package dbp.techcall.booking.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import dbp.techcall.booking.domain.Booking;
import dbp.techcall.booking.domain.BookingService;
import dbp.techcall.booking.email.EmailService;
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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class BookingDeleteEventListener implements ApplicationListener<BookingDeleteEvent> {

    @Autowired
    private MeetingDetailsService meetingDetailsService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private ObjectMapper objectMapper;
    @Value("${whereby}")
    private String apiKey;


    @Autowired
    public BookingDeleteEventListener(MeetingDetailsService meetingDetailsService, @Value("${whereby}") String apiKey) {
        this.meetingDetailsService = meetingDetailsService;
        this.apiKey = apiKey;
    }

    @Override
    public void onApplicationEvent(BookingDeleteEvent event) {
        Long bookingId = event.getBookingId();
        MeetingDetails meetingDetails = meetingDetailsService.getMeetingDetailsById(bookingId);
        String apiUrl = "https://api.whereby.dev/v1/meetings/" + meetingDetails.getMeetingId();
        HttpRequest request = HttpRequest.newBuilder(URI.create(apiUrl))
                .header("Authorization", "Bearer " + apiKey)
                .DELETE()
                .build();

        try {
            HttpResponse<String> response = sendRequestWithRetries(request);
            if (response.statusCode() == 204) {
                meetingDetailsService.deleteMeetingDetailsAsync(bookingId);
                Booking booking = bookingService.getBookingById(bookingId);

                String messageToStudent = String.format(
                        "Hola %s, tu reserva con el profesor %s para el curso %s a las %s horas ha sido cancelada.",
                        booking.getStudent().getFirstName(),
                        booking.getProfessor().getFirstName(),
                        booking.getCourse().getTitle(),
                        booking.getTimeSlot().iterator().next().getStartTime()
                );
                String messageToTeacher = String.format(
                        "Hola %s, la reserva con el estudiante %s para el curso %s a las %s horas ha sido cancelada.",
                        booking.getProfessor().getFirstName(),
                        booking.getStudent().getFirstName(),
                        booking.getCourse().getTitle(),
                        booking.getTimeSlot().iterator().next().getStartTime()
                );

                emailService.sendEmailAsync(booking.getStudent().getEmail(), "Reserva Cancelada", messageToStudent);
                emailService.sendEmailAsync(booking.getProfessor().getEmail(), "Reserva Cancelada", messageToTeacher);
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
