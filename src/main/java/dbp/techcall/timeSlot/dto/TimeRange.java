package dbp.techcall.timeSlot.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class TimeRange {

    private String startTime;
    private String endTime;

    public String getFirst() {
        return startTime;
    }

    public String getSecond() {
        return endTime;
    }

}
