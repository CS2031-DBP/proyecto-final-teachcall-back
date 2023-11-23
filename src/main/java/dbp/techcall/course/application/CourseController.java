package dbp.techcall.course.application;

import dbp.techcall.course.domain.Course;
import dbp.techcall.course.domain.CourseService;
import dbp.techcall.course.dto.*;
import dbp.techcall.course.infrastructure.CourseRepository;
import dbp.techcall.professor.domain.Professor;
import dbp.techcall.professor.infrastructure.ProfessorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseController {
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseService courseService;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<CourseDTO>> courses() {
        List<CourseDTO> courses = courseService.getAll();
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/dashboard/student")
    public ResponseEntity<List<CourseDTO>> coursesStudent() {
        List<CourseDTO> courses = courseService.getLimitSixCourses();
        return ResponseEntity.ok(courses);

    }

    @GetMapping("/professor/{id}")
    public ResponseEntity<Page<BasicCourseResponse>> coursesByProfessor(@PathVariable Long id, @RequestParam(defaultValue = "0") Integer page) {

        Page<BasicCourseResponse> response = courseService.getCoursesByProfessor(id, page);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/top")
    public ResponseEntity<List<TopFiveCourses>> topCourses() {
        List<TopFiveCourses> courses = courseService.getTopCourses();
        return ResponseEntity.ok(courses);
    }

    /*@GetMapping("/{id}")
    public ResponseEntity<BasicCourseResponse> course(@PathVariable Integer id) {
        BasicCourseResponse course = courseRepository.findBasicResponseById(id);
        return ResponseEntity.ok(course);
    }*/

    @GetMapping("/{id}/fullinfo")
    public ResponseEntity<CourseFullInfo> getCourseFullInfo(@PathVariable Long id){
        var response = courseService.getCourseFullInfo(id.intValue());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/professord/{email}")
    public ResponseEntity<List<CourseDTO>> coursesByProfessorEmail(@PathVariable String email) {
        List<CourseDTO> courses = courseService.getCoursesByProfessorEmail(email);
        return ResponseEntity.ok(courses);
    }

//    @PostMapping
//    public ResponseEntity<CourseDTO> create(@RequestBody CourseDTO courseDTO) {
//        Course course = courseService.convertToEntity(courseDTO);
//        Course courseCreated = courseRepository.save(course);
//
//        return ResponseEntity.status(201).body(courseService.convertToDTO(courseCreated));
//    }


    @PostMapping("/new")
    public ResponseEntity<String> newCourse(@RequestBody NewCouseReq newCourse){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        Professor professor = professorRepository.findByEmail(username);

        if (professor == null) {
            return ResponseEntity.status(403).body("User is not a professor");
        }
        Course course = new Course();
        course.setTitle(newCourse.getTitle());
        course.setDescription(newCourse.getDescription());
        course.setPrice(newCourse.getPrice());
        course.setProfessor(professor);

        courseRepository.save(course);

        return ResponseEntity.ok("Course created");
    }
}