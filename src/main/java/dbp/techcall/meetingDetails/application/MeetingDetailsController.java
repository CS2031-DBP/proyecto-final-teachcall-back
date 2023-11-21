package dbp.techcall.meetingDetails.application;

import dbp.techcall.meetingDetails.domain.MeetingDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/meetingDetails")
public class MeetingDetailsController {

    @Autowired
    private MeetingDetailsService meetingDetailsService;


    @PreAuthorize("hasRole('teacher')")
    @GetMapping("/teacher/{bookingId}")
    public ResponseEntity<String> getMeetingDetailsHostRoomUrl(@PathVariable Integer bookingId) {
        return ResponseEntity.ok(meetingDetailsService.getMeetingDetailsHostRoomUrl(bookingId));
    }

    @PreAuthorize("hasRole('student')")
    @GetMapping("/student/{bookingId}")
    public ResponseEntity<String> getMeetingDetailsViewerRoomUrl(@PathVariable Integer bookingId) {
        return ResponseEntity.ok(meetingDetailsService.getMeetingDetailsViewerRoomUrl(bookingId));
    }

}
