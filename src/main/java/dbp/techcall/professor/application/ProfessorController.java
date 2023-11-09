package dbp.techcall.professor.application;

import dbp.techcall.education.dto.BasicEducationRequest;
import dbp.techcall.education.dto.BasicEducationResponse;
import dbp.techcall.professor.domain.ProfessorService;
import dbp.techcall.professor.dto.AddCategoriesReq;
import dbp.techcall.workExperience.dto.BasicExperienceRequest;
import dbp.techcall.workExperience.dto.BasicExperienceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/professor")
@CrossOrigin(origins ={"http://localhost:5173", "http://127.0.0.1:5173"})
public class ProfessorController {

    @Autowired
    private ProfessorService professorService;

    @PostMapping("/categories")
    public ResponseEntity<String> addCategory(@RequestBody AddCategoriesReq categories) {
        System.out.println(categories.getEmail());
        System.out.println(categories.getCategories().get(0));
        professorService.addCategoriesByEmail(categories );
        return ResponseEntity.ok("categories added successfully");
    }

    @PostMapping("education/{email}")
    public ResponseEntity<String> addEducation(@PathVariable String email , @RequestBody BasicEducationRequest education) {
        professorService.addEducation(email, education);
        return ResponseEntity.ok("education added ");
    }

    @GetMapping("/education/{id}")
    public ResponseEntity<List<BasicEducationResponse>> getEducations(@PathVariable Long id) {
        return ResponseEntity.ok(professorService.getEducationsById(id));
    }

    @PostMapping("/experience/{id}")
    public ResponseEntity<String> addExperience(@PathVariable Long id, @RequestBody BasicExperienceRequest experience) {
        professorService.addExperience(id, experience);
        return ResponseEntity.ok("experience added to professor with id: " + id);
    }

    @GetMapping("/experience/{id}")
    public ResponseEntity<Page<List<BasicExperienceResponse>>> getExperiences(@PathVariable Long id, @RequestParam(defaultValue = "0") Integer page) {
        return ResponseEntity.ok(professorService.getExperiencesById(id, page));
    }

    @PatchMapping("/completed-tour/{email}")
    public ResponseEntity<String> setCompletedTour(@PathVariable String email) {
        professorService.setCompletedTour(email);
        return ResponseEntity.ok("completed tour set to true");
    }

    @PatchMapping("/description/{email}")
    public ResponseEntity<String> setDescription(@PathVariable String email, @RequestBody String description) {
        System.out.println(description);
        professorService.setDescription(email, description);
        return ResponseEntity.ok("description set");
    }

}
