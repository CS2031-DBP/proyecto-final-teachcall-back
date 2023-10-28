package dbp.techcall.booking.application;

import dbp.techcall.booking.exceptions.BookingNotFoundException;
import dbp.techcall.booking.domain.BookingService;
import dbp.techcall.booking.domain.Booking;
import dbp.techcall.booking.dto.BookingInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @GetMapping("/short/{id}")
    public BookingInfo getBookingInfoById(@PathVariable Long id) {
        Booking booking = bookingService.getBookingById(id);

        if (booking == null) {
            throw new BookingNotFoundException("La reserva con el ID " + id + " no se encontró.");
        }

        BookingInfo bookingInfo = new BookingInfo();
        bookingInfo.setBooking(booking);

        // Verifica si el profesor asociado a la reserva no es nulo
        if (booking.getProfessor() != null) {
            bookingInfo.setProfessorName(booking.getProfessor().getFirstName());
            bookingInfo.setProfessorLastname(booking.getProfessor().getLastName());
        }

        // Verifica si el curso asociado a la reserva no es nulo
        if (booking.getCourse() != null) {
            bookingInfo.setCourseSubject(booking.getCourse().getSubject());
        }

        // Verifica si los detalles de la reunión asociados a la reserva no son nulos
        if (booking.getMeetingDetails() != null) {
            bookingInfo.setMeetingDetailsLink(booking.getMeetingDetails().getLink());
        }
        return bookingInfo;
    }

    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody Booking booking) {
        Booking createdBooking = bookingService.createBooking(booking);
        return new ResponseEntity<>(createdBooking, HttpStatus.CREATED);
    }

    @PutMapping("/short/{id}")
    public Booking updateBooking(@PathVariable Long id, @RequestBody Booking booking) {
        return bookingService.updateBooking(id, booking);
    }

    @DeleteMapping("/short/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        // Llama al servicio para eliminar la reserva por ID
        bookingService.deleteBooking(id);
        // Retorna un ResponseEntity con el estado HTTP NO_CONTENT (204)
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

