package dbp.techcall.professorAvailability.dto;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.util.Pair;

import java.time.LocalTime;
import java.util.Map;

@Getter
@Setter
@RequiredArgsConstructor
public class WeekAvailabilityRequest {

    private Long professorId;
    private Integer weekNumber;
    private Map<Integer, Pair<LocalTime, LocalTime>> timeRanges;
}
