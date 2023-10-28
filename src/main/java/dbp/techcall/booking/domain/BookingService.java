package dbp.techcall.booking.domain;

import dbp.techcall.booking.dto.BookingInfo;
import dbp.techcall.booking.exceptions.BookingNotFoundException;
import dbp.techcall.booking.infrastructure.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    private final List<Booking> bookings;

    public BookingService(List<Booking> bookings) {
        this.bookings = bookings;
    }



    /*public Booking getBookingById(Long id) {
        // Buscar una reserva por su ID
        for (Booking booking : bookings) {
            if (booking.getId().equals(id)) {
                return booking;
            }
        }
        return null;
    }*/

    public Booking createBooking(Booking booking) {
        // Agregar una nueva reserva
        bookings.add(booking);
        return booking;
    }

    public Booking updateBooking(Long id, Booking booking) {
        // Actualizar una reserva existente por su ID
        for (int i = 0; i < bookings.size(); i++) {
            if (bookings.get(i).getId().equals(id)) {
                bookings.set(i, booking);
                return booking;
            }
        }
        return null;
    }

    public void deleteBooking(Long id) {
        // Eliminar una reserva por su ID
        bookings.removeIf(booking -> booking.getId().equals(id));
    }

    public BookingInfo getBookingInfoById(Long id) {
        Optional<Booking> booking = bookingRepository.findById(id);

        if (booking.isEmpty()) {
            throw new BookingNotFoundException("La reserva con el ID " + id + " no se encontró.");
        }

        return new BookingInfo(booking.get());

    }
}
