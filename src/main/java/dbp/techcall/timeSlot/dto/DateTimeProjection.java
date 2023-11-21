package dbp.techcall.timeSlot.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public interface DateTimeProjection {
    public Long getId();
    public LocalDate getDate();
    public LocalTime getStartTime();
}
