package dbp.techcall.course.domain;

import dbp.techcall.course.dto.CourseDTO;
import dbp.techcall.course.infrastructure.CourseRepository;
import dbp.techcall.professor.dto.NewProfessorDto;
import dbp.techcall.professor.domain.Professor;
import dbp.techcall.professor.infrastructure.ProfessorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class CourseService {
//
    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CourseRepository courseRepository;

    public List<CourseDTO> getAll() {
        List<Course> courses = courseRepository.findAll();
        return courses.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    public List<CourseDTO> getLimitSixCourses() {
        Pageable limitedPageable = PageRequest.of(0, 6);
        Page<Course> coursePage = courseRepository.findAll(limitedPageable);

        return coursePage.getContent()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public CourseDTO convertToDTO(Course course) {
        CourseDTO dto = new CourseDTO();
        dto.setId(Long.valueOf(course.getId()));
        dto.setTitle(course.getTitle());
        dto.setDescription(course.getDescription());
        dto.setPricePerHour(course.getPricePerHour());

        // Assuming you have a ProfessorRepository and ModelMapper available
        Professor professor = professorRepository.findById(course.getProfessorId());
        NewProfessorDto professorDTO = modelMapper.map(professor, NewProfessorDto.class);
        dto.setProfessorData(professorDTO);

        return dto;
    }
}
