package dbp.techcall.booking.infrastructure;

import dbp.techcall.booking.domain.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking,Long> {
}
