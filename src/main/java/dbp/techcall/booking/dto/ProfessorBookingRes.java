package dbp.techcall.booking.dto;

import dbp.techcall.course.dto.TitleDescriptionProjection;
import dbp.techcall.student.dto.StudentNames;
import dbp.techcall.timeSlot.dto.DateTimeProjection;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ProfessorBookingRes {
    public Long id;
    public String link;
    public StudentNames student;
    public DateTimeProjection timeSlot;
    public TitleDescriptionProjection course;
}
