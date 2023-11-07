package dbp.techcall.professorAvailability.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@RequiredArgsConstructor
public class SlotAvailability {
    private Integer slotId;
    private LocalTime startTime;
    private LocalTime endTime;
    private Boolean isAvailable;
}
