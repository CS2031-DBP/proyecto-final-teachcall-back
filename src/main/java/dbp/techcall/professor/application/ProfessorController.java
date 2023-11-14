package dbp.techcall.professor.application;

import dbp.techcall.education.dto.BasicEducationRequest;
import dbp.techcall.education.dto.BasicEducationResponse;
import dbp.techcall.professor.domain.ProfessorService;
import dbp.techcall.professor.dto.AddCategoriesReq;
import dbp.techcall.professor.dto.BasicEducation;
import dbp.techcall.professor.dto.LastExperienceDto;
import dbp.techcall.professor.infrastructure.BasicProfessorResponse;
import dbp.techcall.workExperience.dto.BasicExperienceRequest;
import dbp.techcall.workExperience.dto.BasicExperienceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/professor")
@CrossOrigin(origins = {"http://localhost:5137", "http://127.0.0.1:5137"})
@PreAuthorize("hasAnyRole('teacher','student')")
public class ProfessorController {

    @Autowired
    private ProfessorService professorService;

    @GetMapping
    public ResponseEntity<Page<BasicProfessorResponse>> getProfessors(@RequestParam(defaultValue = "0") Integer page) {
        return ResponseEntity.ok(professorService.getProfessors(page));
    }

    @GetMapping("/category")
    public ResponseEntity<Page<BasicProfessorResponse>> getProfessorsByCategory(@RequestParam(defaultValue = "0") Integer page, @RequestParam String category) {
        return ResponseEntity.ok(professorService.getProfessorsByCategory(page, category));
    }

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

    @GetMapping("/description/{email}")
    public ResponseEntity<String> getDescription(@PathVariable String email) {
        return ResponseEntity.ok(professorService.getDescription(email));
    }

    @GetMapping("rating/{email}")
    public ResponseEntity<Double> getRating(@PathVariable String email) {
        return ResponseEntity.ok(professorService.getRating(email));
    }

    @GetMapping("experience/last/{email}")
    public ResponseEntity<LastExperienceDto> getExperience(@PathVariable String email) {
        return ResponseEntity.ok(professorService.getExperience(email));
    }

    @GetMapping("education/{email}")
    public ResponseEntity<Page<BasicEducation>> getEducation(@PathVariable String email, @RequestParam int page ){
        return ResponseEntity.ok(professorService.getEducationWithPagination(email, page));
    }

}
