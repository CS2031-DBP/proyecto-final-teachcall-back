package dbp.techcall.course.dto;

import dbp.techcall.professor.domain.Professor;

public interface BasicCourseResponse {
    Professor getProfessor();
    Long getId();
    String getTitle();
    String getDescription();
    double getPrice();
}
