package dbp.techcall.course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import dbp.techcall.course.CourseService;
import dbp.techcall.course.CourseRepository;

import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseController {
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseService courseService;

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

}