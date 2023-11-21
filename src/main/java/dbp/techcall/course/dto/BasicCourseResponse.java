package dbp.techcall.course.dto;

import dbp.techcall.professor.domain.Professor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class BasicCourseResponse {
    Long professorId;
    Integer courseId;
    String title;
    String description;
    double price;
}
