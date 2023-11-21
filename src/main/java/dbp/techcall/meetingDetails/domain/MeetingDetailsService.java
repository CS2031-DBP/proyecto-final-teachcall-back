package dbp.techcall.meetingDetails.domain;

import dbp.techcall.booking.domain.Booking;
import dbp.techcall.meetingDetails.infrastructure.MeetingDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MeetingDetailsService {

    @Autowired
    private MeetingDetailsRepository meetingDetailsRepository;

    @Async
    public void createMeetingDetailsAync(Booking booking, String hostRoomUrl, String viewerRoomUrl, String endDate, String meetingId) {
        MeetingDetails meetingDetails = new MeetingDetails();
        meetingDetails.setBooking(booking);
        meetingDetails.setHostRoomUrl(hostRoomUrl);
        meetingDetails.setViewerRoomUrl(viewerRoomUrl);
        meetingDetails.setEndDate(endDate);
        meetingDetails.setMeetingId(meetingId);
        meetingDetailsRepository.save(meetingDetails);
    }

    @Async
    public void deleteMeetingDetailsAsync(Long id) {
        MeetingDetails meetingDetails = meetingDetailsRepository.findById(id).orElseThrow();
        meetingDetailsRepository.delete(meetingDetails);
    }


    public MeetingDetails getMeetingDetailsById(Long id) {
        Optional<MeetingDetails> meetingDetailsOptional = meetingDetailsRepository.findById(id);
        if (!meetingDetailsOptional.isPresent()) {
            throw new RuntimeException("MeetingDetails no encontrado");
        }
        return meetingDetailsOptional.get();
    }
    public String getMeetingDetailsHostRoomUrl(Integer id) {
        return meetingDetailsRepository.findByBookingId(id).getHostRoomUrl();
    }

    public String getMeetingDetailsViewerRoomUrl(Integer id) {
        return meetingDetailsRepository.findByBookingId(id).getViewerRoomUrl();
    }

}