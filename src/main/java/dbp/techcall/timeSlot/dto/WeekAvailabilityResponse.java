package dbp.techcall.timeSlot.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
public class WeekAvailabilityResponse {
    Set<Integer> availableDays;
}
