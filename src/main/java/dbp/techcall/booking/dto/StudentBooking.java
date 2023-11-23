package dbp.techcall.booking.dto;

import dbp.techcall.course.dto.TitleDescriptionProjection;
import dbp.techcall.professor.dto.ProfessorNames;
import dbp.techcall.timeSlot.dto.DateTimeProjection;

public interface StudentBooking {
    public Long getId();
    public String getLink();
    public DateTimeProjection getTimeSlot();
    public TitleDescriptionProjection getCourse();
    public ProfessorNames getProfessor();
}
