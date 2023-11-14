package dbp.techcall.timeSlot.application;


import dbp.techcall.timeSlot.dto.DayTimeSlotsResponse;
import dbp.techcall.timeSlot.dto.NextFourWeeksAvailabilityResponse;
import dbp.techcall.timeSlot.dto.WeekAvailabilityRequest;
import dbp.techcall.timeSlot.dto.WeekAvailabilityResponse;
import dbp.techcall.timeSlot.domain.TimeSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/availability")
@CrossOrigin(origins = {"http://localhost:5137", "http://127.0.0.1:5137"})
@PreAuthorize("hasAnyRole('teacher','student')")
public class TimeSlotController{

    @Autowired
    private TimeSlotService timeSlotService;

    @PostMapping("/weekly")
    public ResponseEntity<String> setAvailabilityByWeekNumber(@RequestBody WeekAvailabilityRequest weekAvailabilityRequest) {
        System.out.println("weekAvailabilityRequest ");
        timeSlotService.setAvailabilityByWeekNumber(weekAvailabilityRequest);
        return ResponseEntity.ok("Availability set successfully");
    }

    @GetMapping("/weekly/{email}")
    public ResponseEntity<WeekAvailabilityResponse> getAvailabilityByWeekNumber(@PathVariable String email, @RequestParam Integer week) {
        System.out.println("email " + email);
        return ResponseEntity.ok(timeSlotService.getAvailabilityByWeekNumber(email, week));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NextFourWeeksAvailabilityResponse> getNextFourWeeksAvailability(@PathVariable Long id) {
        return ResponseEntity.ok(timeSlotService.getNextFourWeeksAvailability(id));
    }


    @GetMapping("/day/{email}/{week}/{day}")
    public ResponseEntity<DayTimeSlotsResponse> getAvailabilityByDay(@PathVariable String email, @PathVariable Integer week, @PathVariable Integer day) {
        return ResponseEntity.ok(timeSlotService.getAvailabilityByDay(email, week, day));
    }

}
