package dbp.techcall.meetingDetails.application;

import dbp.techcall.meetingDetails.domain.MeetingDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/meetingDetails")
public class MeetingDetailsController {

    @Autowired
    private MeetingDetailsService meetingDetailsService;

}
