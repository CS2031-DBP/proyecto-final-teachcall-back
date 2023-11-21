package dbp.techcall.timeSlot.application;


import dbp.techcall.booking.dto.BookingInfo;
import dbp.techcall.timeSlot.dto.*;
import dbp.techcall.timeSlot.domain.TimeSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/availability")
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"})
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
        return ResponseEntity.ok(timeSlotService.   getAvailabilityByWeekNumber(email, week));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WeekAvailabilityResponse> getNextFourWeeksAvailability(@PathVariable Long id, @RequestParam Integer week) {
        return ResponseEntity.ok(timeSlotService.getAvailabilityByProfessorIdAndWeekNumber(id, week));
    }


    @GetMapping("/day/{email}/{week}/{day}")
    public ResponseEntity<DayTimeSlotsResponse> getAvailabilityByDay(@PathVariable String email, @PathVariable Integer week, @PathVariable Integer day) {
        return ResponseEntity.ok(timeSlotService.getAvailabilityByDay(email, week, day));
    }

    @GetMapping("/free/{id}")
    public ResponseEntity<List<BasicDayAvailability>> getFreeTimeSlots(@PathVariable Long id , @RequestParam Integer week, @RequestParam Integer day) {
        return ResponseEntity.ok(timeSlotService.getFreeTimeSlots(id, week,day));
    }

    @GetMapping("/booking/{time_slot_id}")
    public ResponseEntity<BookingInfo> getBookingInfo(@PathVariable Long time_slot_id) {
        return ResponseEntity.ok(timeSlotService.getBookingInfo(time_slot_id));
    }

}
