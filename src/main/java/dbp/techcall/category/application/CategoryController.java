package dbp.techcall.category.application;
import dbp.techcall.category.domain.Category;
import dbp.techcall.course.domain.Course;
import dbp.techcall.course.dto.CourseDTO;
import dbp.techcall.course.domain.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import dbp.techcall.category.infrastructure.CategoryRepository;
import dbp.techcall.category.domain.CategoryService;
import dbp.techcall.category.dto.CategoryDto;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CourseService courseService;

    private CategoryDto categoryDto;


    @GetMapping
    public ResponseEntity<List<CategoryDto>> categories() {
        List<CategoryDto> categories= categoryService.getAll();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/dashboard/student")
    public ResponseEntity<List<CategoryDto>> shortCategories(){
        List<CategoryDto> categories = categoryService.getLimitSixCategories();
        return ResponseEntity.ok(categories);

    }

    @GetMapping("/{id}/courses")
    public ResponseEntity<Set<CourseDTO>> coursesByCategory(@PathVariable Integer id){
        //return all category with that id
        Category category = categoryRepository.findById(Long.valueOf(id)).orElse(null);
        if (category == null) {
            return ResponseEntity.status(404).body(null);
        }
        Set<Course> courses = category.getCourses();
        // for each course, convert to dto
        Set<CourseDTO> coursesDto = courses.stream()
                 .map(courseService::convertToDTO)
                 .collect(Collectors.toSet());
        return ResponseEntity.status(200).body(coursesDto);
    }




}
