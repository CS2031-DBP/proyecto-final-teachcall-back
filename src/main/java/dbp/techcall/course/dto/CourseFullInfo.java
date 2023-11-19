package dbp.techcall.course.dto;

import dbp.techcall.professor.dto.BasicEducation;
import dbp.techcall.professor.dto.ProfessorNames;
import dbp.techcall.workExperience.dto.BasicExperienceResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class CourseFullInfo {
    Double rating;
    Long id;
    String title;
    String description;
    double price;
    ProfessorNames professor;
    List<BasicEducation> educations;
}
