package dbp.techcall.booking.dto;

import dbp.techcall.course.dto.TitleDescriptionProjection;
import dbp.techcall.professor.dto.ProfessorNames;
import dbp.techcall.timeSlot.dto.DateTimeProjection;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class StudentBookingRes {
    public Long id;
    public String link;
    public DateTimeProjection timeSlot;
    public TitleDescriptionProjection course;
    public ProfessorNames professor;
}
