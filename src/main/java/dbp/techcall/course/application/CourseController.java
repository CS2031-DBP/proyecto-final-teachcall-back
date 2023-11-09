package dbp.techcall.course.application;

import dbp.techcall.course.domain.Course;
import dbp.techcall.course.domain.CourseService;
import dbp.techcall.course.dto.CourseDTO;
import dbp.techcall.course.dto.TopFiveCourses;
import dbp.techcall.course.infrastructure.CourseRepository;
import dbp.techcall.professor.domain.Professor;
import dbp.techcall.professor.infrastructure.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/course")
@CrossOrigin(origins ={"http://localhost:5173", "http://127.0.0.1:5173"})
public class CourseController {
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseService courseService;

    @Autowired
    private ProfessorRepository professorRepository;

    @GetMapping
    public ResponseEntity<List<CourseDTO>> courses() {
        List<CourseDTO> courses= courseService.getAll();
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/dashboard/student")
    public ResponseEntity<List<CourseDTO>> coursesStudent(){
        List<CourseDTO> courses = courseService.getLimitSixCourses();
        return ResponseEntity.ok(courses);

    }

    @GetMapping("/professor/{id}")
    public ResponseEntity<List<CourseDTO>> coursesByProfessor(@PathVariable Long id){
        Optional<Professor> professor = professorRepository.findById(id);
        if (professor.isEmpty()) {
            return ResponseEntity.status(404).body(null);
        }

        List<Course> courses = professor.get().getCourses();
        List<CourseDTO> coursesDto = courses.stream()
                .map(courseService::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.status(200).body(coursesDto);

    }

    @GetMapping("/top")
    public ResponseEntity<List<TopFiveCourses>> topCourses(){
        List<TopFiveCourses> courses = courseService.getTopCourses();
        return ResponseEntity.ok(courses);
    }

//    @PostMapping
//    public ResponseEntity<CourseDTO> create(@RequestBody CourseDTO courseDTO) {
//        Course course = courseService.convertToEntity(courseDTO);
//        Course courseCreated = courseRepository.save(course);
//
//        return ResponseEntity.status(201).body(courseService.convertToDTO(courseCreated));
//    }



}