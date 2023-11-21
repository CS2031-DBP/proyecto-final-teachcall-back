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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        Professor professor = professorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Professor not found"));
        
        Page<Course> course = courseRepository.findByProfessor(professor, pageable);
        
       List<BasicCourseResponse> response = new ArrayList<>();
       
         for(Course c : course.getContent()) {
              BasicCourseResponse basicCourseResponse = new BasicCourseResponse();
              basicCourseResponse.setCourseId(c.getId());
              basicCourseResponse.setDescription(c.getDescription());
              basicCourseResponse.setPrice(c.getPrice());
              basicCourseResponse.setTitle(c.getTitle());
              basicCourseResponse.setProfessorId(c.getProfessor().getId());
              response.add(basicCourseResponse);
            }
         
         return new PageImpl<>(response, pageable, course.getTotalElements());
        
    }

    public CourseFullInfo getCourseFullInfo(Integer id) {
        Optional<Course> course = courseRepository.findById(id.longValue());
        if(course == null) {
            throw new ResourceNotFoundException("Course not found");
        }

        ProfessorNames professor = professorRepository.findProfessorNamesById(course.get().getProfessor().getId());

        Pageable pageable = PageRequest.of(0, 5);

        Page<BasicEducation> education = educationRepository.getEducationWithPagination(course.get().getProfessor().getId(), pageable);
        Double rating = professorRepository.getRating(course.get().getProfessor().getEmail());

        CourseFullInfo response =new CourseFullInfo();


            response.setRating(rating);
            response.setId(course.get().getId());
            response.setTitle(course.get().getTitle());
            response.setDescription(course.get().getDescription());
            response.setPrice(course.get().getPrice());
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


