package dbp.techcall.professorAvailability.application;

import dbp.techcall.professorAvailability.domain.ProfessorAvailabilityService;
import dbp.techcall.professorAvailability.dto.DayTimeSlotsResponse;
import dbp.techcall.professorAvailability.dto.NextFourWeeksAvailabilityResponse;
import dbp.techcall.professorAvailability.dto.WeekAvailabilityRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/professor-availability")
public class ProfessorAvailabilityController {

    @Autowired
    private ProfessorAvailabilityService professorAvailabilityService;

    @PostMapping("/weekly-availability")
    public ResponseEntity<String> setAvailabilityByWeekNumber(@RequestBody WeekAvailabilityRequest weekAvailabilityRequest) {
        professorAvailabilityService.setAvailabilityByWeekNumber(weekAvailabilityRequest);
        return ResponseEntity.ok("Availability set successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<NextFourWeeksAvailabilityResponse> getNextFourWeeksAvailability(@PathVariable Long id) {
        //return ResponseEntity.ok("Availability set successfully");
        return ResponseEntity.ok(professorAvailabilityService.getNextFourWeeksAvailability(id));
    }

    @GetMapping("/day/{professorId}/{week}/{day}")
    public ResponseEntity<DayTimeSlotsResponse> getAvailabilityByDay(@PathVariable Long professorId, @PathVariable Integer week, @PathVariable Integer day) {
        return ResponseEntity.ok(professorAvailabilityService.getAvailabilityByDay(professorId, week, day));
    }

}

