package dbp.techcall.timeSlot.dto;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@RequiredArgsConstructor
public class DayTimeSlotsResponse {
    private Long professorId;
    private Map<Integer, List<SlotAvailability>> dayTimeSlots;
}
