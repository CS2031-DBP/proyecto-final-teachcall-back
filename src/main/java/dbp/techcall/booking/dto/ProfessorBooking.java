package dbp.techcall.booking.dto;

import dbp.techcall.course.domain.Course;
import dbp.techcall.professor.domain.Professor;
import dbp.techcall.student.domain.Student;
import dbp.techcall.timeSlot.domain.TimeSlot;

public interface ProfessorBooking {
    public String getLink();
    public Student getStudent();
    public Course getCourse();
    public Long getId();
    public Professor getProfessor();
    public TimeSlot getTimeSlot();
}
