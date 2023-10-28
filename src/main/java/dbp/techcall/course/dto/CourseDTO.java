package dbp.techcall.course.dto;
import dbp.techcall.professor.dto.NewProfessorDto ;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class CourseDTO {
        Long id;
        String title;
        String description;
        double pricePerHour;
        NewProfessorDto professorData;
}
