package dbp.techcall.professorAvailability.dto;

import java.time.LocalTime;

public interface BasicDayAvailability {
    Integer getDay();
    LocalTime getStartTime();
    LocalTime getEndTime();
    Boolean getIsAvailable();
}
