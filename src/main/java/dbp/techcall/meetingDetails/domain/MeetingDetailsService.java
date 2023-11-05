package dbp.techcall.meetingDetails.domain;

import dbp.techcall.meetingDetails.dto.HostRoomDto;
import dbp.techcall.meetingDetails.dto.ViewerRoomDto;
import dbp.techcall.meetingDetails.exception.MeetingDetailsNotFoundException;
import dbp.techcall.meetingDetails.infrastructure.MeetingDetailsRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MeetingDetailsService {

    @Autowired
    private MeetingDetailsRepository meetingDetailsRepository;

    @Value("${whereby.api-key}")
    private String apiKey;

    @Autowired
    private ModelMapper modelMapper;

    public HostRoomDto getHostRoomLinkById(Integer id) {
        MeetingDetails host = meetingDetailsRepository.findById(id)
                .orElseThrow(() -> new MeetingDetailsNotFoundException("No hay un meeting con el id: " + id));
        return convertHostDTO(host);
    }

    public ViewerRoomDto getViewerRoomLinkById(Integer id) {
        MeetingDetails meetingDetails = meetingDetailsRepository.findById(id)
                .orElseThrow(() -> new MeetingDetailsNotFoundException("No hay un meeting con el id: " + id));
        return convertViewerDTO(meetingDetails);
    }

    protected HostRoomDto convertHostDTO(MeetingDetails meetingDetails) {
        return modelMapper.map(meetingDetails, HostRoomDto.class);
    }

    protected ViewerRoomDto convertViewerDTO(MeetingDetails meetingDetails) {
        return modelMapper.map(meetingDetails, ViewerRoomDto.class);
    }

}
