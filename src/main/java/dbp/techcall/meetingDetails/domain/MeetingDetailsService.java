package dbp.techcall.meetingDetails.domain;

import dbp.techcall.booking.domain.Booking;
import dbp.techcall.meetingDetails.infrastructure.MeetingDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MeetingDetailsService {

    @Autowired
    private MeetingDetailsRepository meetingDetailsRepository;

    public MeetingDetails createMeetingDetails(Booking booking, String hostRoomUrl, String viewerRoomUrl, String endDate, String meetingId) {
        MeetingDetails meetingDetails = new MeetingDetails();
        meetingDetails.setBooking(booking);
        meetingDetails.setHostRoomUrl(hostRoomUrl);
        meetingDetails.setViewerRoomUrl(viewerRoomUrl);
        meetingDetails.setEndDate(endDate);
        meetingDetails.setMeetingId(meetingId);
        return meetingDetailsRepository.save(meetingDetails);
    }

    public MeetingDetails deleteMeetingDetails(Long id) {
        MeetingDetails meetingDetails = meetingDetailsRepository.findById(id).orElseThrow();
        meetingDetailsRepository.delete(meetingDetails);
        return meetingDetails;
    }

    public MeetingDetails getMeetingDetailsById(Long id) {
        return meetingDetailsRepository.findById(id).orElseThrow();
    }
}