package dbp.techcall.booking.event;

import org.springframework.context.ApplicationEvent;

public class BookingDeleteEvent extends ApplicationEvent{

        private Long bookingId;

        public BookingDeleteEvent(Object source, Long bookingId) {
            super(source);
            this.bookingId = bookingId;
        }

        public Long getBookingId() {
            return bookingId;
        }
}
