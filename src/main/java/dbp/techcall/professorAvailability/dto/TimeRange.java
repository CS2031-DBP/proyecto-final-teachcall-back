package dbp.techcall.professorAvailability.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

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
