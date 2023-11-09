package dbp.techcall.timeSlot.dto;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@RequiredArgsConstructor
public class WeekAvailabilityRequest {

    private String professorEmail;
    private Integer weekNumber;
    private Map<Integer, TimeRange> timeRanges;
}
