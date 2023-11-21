package dbp.techcall.course.domain;

import dbp.techcall.course.dto.BasicCourseResponse;
import dbp.techcall.course.dto.CourseDTO;
import dbp.techcall.course.dto.CourseFullInfo;
import dbp.techcall.course.dto.TopFiveCourses;
import dbp.techcall.course.infrastructure.CourseRepository;
import dbp.techcall.education.infrastructure.EducationRepository;
import dbp.techcall.professor.domain.Professor;
import dbp.techcall.professor.dto.BasicEducation;
import dbp.techcall.professor.dto.NewProfessorDto;
import dbp.techcall.professor.dto.ProfessorNames;
import dbp.techcall.professor.infrastructure.ProfessorRepository;
import dbp.techcall.review.exceptions.ResourceNotFoundException;
import dbp.techcall.workExperience.dto.BasicExperienceResponse;
import dbp.techcall.workExperience.infrastructure.WorkExperienceRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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

    @Autowired
    private EducationRepository educationRepository;

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
        dto.setPricePerHour(course.getPrice());

        // Assuming you have a ProfessorRepository and ModelMapper available
        Optional<Professor> professor = professorRepository.findById(course.getProfessor().getId());
        if(professor.isEmpty()) {
            throw new RuntimeException("Professor not found");
        }

        NewProfessorDto professorDTO = modelMapper.map(professor, NewProfessorDto.class);
        dto.setProfessorData(professorDTO);

        return dto;
    }

    public List<TopFiveCourses> getTopCourses() {
        return courseRepository.findTopCourses();
    }

    public Page<BasicCourseResponse> getCoursesByProfessor(Long id, Integer page) {
        Pageable pageable = PageRequest.of(page, 15);
        return courseRepository.findByProfessorId(id, pageable);
    }

    public CourseFullInfo getCourseFullInfo(Integer id) {
        BasicCourseResponse course = courseRepository.findBasicResponseById(id);
        if(course == null) {
            throw new ResourceNotFoundException("Course not found");
        }

        ProfessorNames professor = professorRepository.findProfessorNamesById(course.getProfessor().getId());

        Pageable pageable = PageRequest.of(0, 5);

        Page<BasicEducation> education = educationRepository.getEducationWithPagination(course.getProfessor().getId(), pageable);
        Double rating = professorRepository.getRating(course.getProfessor().getEmail());

        CourseFullInfo response = new CourseFullInfo();

        response.setRating(rating);
        response.setId(course.getId());
        response.setTitle(course.getTitle());
        response.setDescription(course.getDescription());
        response.setPrice(course.getPrice());
        response.setProfessor(professor);
        response.setEducations(education.getContent());

        return response;
    }

//    getCoursesByProfessorEmail
    public List<CourseDTO> getCoursesByProfessorEmail(String email) {
        List<Course> courses = courseRepository.findByProfessorEmail(email);
        return courses.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}


