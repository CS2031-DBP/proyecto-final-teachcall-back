package dbp.techcall.professorAvailability.application;

import dbp.techcall.professorAvailability.domain.ProfessorAvailabilityService;
import dbp.techcall.professorAvailability.dto.DayTimeSlotsResponse;
import dbp.techcall.professorAvailability.dto.NextFourWeeksAvailabilityResponse;
import dbp.techcall.professorAvailability.dto.WeekAvailabilityRequest;
import dbp.techcall.professorAvailability.dto.WeekAvailabilityResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/availability")
@CrossOrigin(origins ={"http://localhost:5173", "http://127.0.0.1:5173"})
public class ProfessorAvailabilityController {

    @Autowired
    private ProfessorAvailabilityService professorAvailabilityService;

    @PostMapping("/weekly")
    public ResponseEntity<String> setAvailabilityByWeekNumber(@RequestBody WeekAvailabilityRequest weekAvailabilityRequest) {
        System.out.println("weekAvailabilityRequest ");
        professorAvailabilityService.setAvailabilityByWeekNumber(weekAvailabilityRequest);
        return ResponseEntity.ok("Availability set successfully");
    }

    @GetMapping("/weekly/{email}")
    public ResponseEntity<WeekAvailabilityResponse> getAvailabilityByWeekNumber(@PathVariable String email, @RequestParam Integer week) {
        System.out.println("email " + email);
        return ResponseEntity.ok(professorAvailabilityService.getAvailabilityByWeekNumber(email, week));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NextFourWeeksAvailabilityResponse> getNextFourWeeksAvailability(@PathVariable Long id) {
        return ResponseEntity.ok(professorAvailabilityService.getNextFourWeeksAvailability(id));
    }


    @GetMapping("/day/{email}/{week}/{day}")
    public ResponseEntity<DayTimeSlotsResponse> getAvailabilityByDay(@PathVariable String email, @PathVariable Integer week, @PathVariable Integer day) {
        return ResponseEntity.ok(professorAvailabilityService.getAvailabilityByDay(email, week, day));
    }

}

