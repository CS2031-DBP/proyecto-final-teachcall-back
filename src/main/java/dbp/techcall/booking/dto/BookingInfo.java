package dbp.techcall.booking.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class BookingInfo {
    private String courseSubject;
    private String professorName;
    private String professorLastname;
    private String meetingDetailsLink;
}
