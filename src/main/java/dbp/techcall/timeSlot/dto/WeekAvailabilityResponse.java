package dbp.techcall.timeSlot.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class WeekAvailabilityResponse {
    List<Integer> availableDays;
}
