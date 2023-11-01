package dbp.techcall.professorAvailability.dto;




import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.util.Pair;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@RequiredArgsConstructor
public class DayTimeSlotsResponse {
    private Long professorId;
    private Map<Integer, List<Pair<LocalTime,LocalTime>>> dayTimeSlots;
}
