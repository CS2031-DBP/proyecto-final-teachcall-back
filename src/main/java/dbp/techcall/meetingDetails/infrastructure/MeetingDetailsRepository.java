package dbp.techcall.meetingDetails.infrastructure;

import dbp.techcall.booking.domain.Booking;
import dbp.techcall.meetingDetails.domain.MeetingDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingDetailsRepository extends JpaRepository<MeetingDetails, Long>{
    MeetingDetails findByBookingId(Integer bookingId);
    MeetingDetails findByBooking(Booking booking);
}
