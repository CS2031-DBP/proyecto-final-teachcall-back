package dbp.techcall.booking.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class BasicBookingReq {
    Long courseId;
    Long professorId;
    Long timeSlotId;
}
