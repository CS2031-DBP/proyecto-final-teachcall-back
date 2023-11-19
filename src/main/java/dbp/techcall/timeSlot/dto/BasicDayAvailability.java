package dbp.techcall.timeSlot.dto;

import java.time.LocalTime;

public interface BasicDayAvailability {
    Long getId();
    Integer getDay();
    LocalTime getStartTime();
    LocalTime getEndTime();
    Boolean getIsAvailable();
}
