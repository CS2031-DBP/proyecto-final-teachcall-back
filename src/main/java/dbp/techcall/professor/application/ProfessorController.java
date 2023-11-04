package dbp.techcall.professor.application;

import dbp.techcall.education.dto.BasicEducationRequest;
import dbp.techcall.education.dto.BasicEducationResponse;
import dbp.techcall.professor.domain.ProfessorService;
import dbp.techcall.workExperience.dto.BasicExperienceRequest;
import dbp.techcall.workExperience.dto.BasicExperienceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/professor")
public class ProfessorController {

    @Autowired
    private ProfessorService professorService;

    @PostMapping("education/{id}")
    public ResponseEntity<String> addEducation(@PathVariable Long id, @RequestBody BasicEducationRequest education) {
        professorService.addEducation(id, education);
        return ResponseEntity.ok("education added to professor with id: " + id);
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

}
