package dbp.techcall.meetingDetails.application;

import dbp.techcall.meetingDetails.domain.MeetingDetails;
import dbp.techcall.meetingDetails.domain.MeetingDetailsService;
import dbp.techcall.meetingDetails.dto.HostRoomDto;
import dbp.techcall.meetingDetails.dto.ViewerRoomDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/meetingdetails")
public class MeetingDetailsController {

    @Autowired
    public MeetingDetailsService meetingDetailsService;

    @GetMapping("/viewer/{id}")
    public ResponseEntity<ViewerRoomDto> getViewerLink(@PathVariable Integer id){
        ViewerRoomDto viewerRoomDto = meetingDetailsService.getViewerRoomLinkById(id);
        return ResponseEntity.ok(viewerRoomDto);
    }

    @GetMapping("/host/{id}")
    public ResponseEntity<HostRoomDto> getHostLink(@PathVariable Integer id){
        HostRoomDto hostRoomDto = meetingDetailsService.getHostRoomLinkById(id);
        return ResponseEntity.ok(hostRoomDto);
    }
}
