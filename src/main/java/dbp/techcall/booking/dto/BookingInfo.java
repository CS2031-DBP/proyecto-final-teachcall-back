package dbp.techcall.booking.dto;

import dbp.techcall.booking.domain.Booking;
import dbp.techcall.meetingDetails.dto.ViewerRoomDto;
import dbp.techcall.timeSlot.dto.StartEndDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
@NoArgsConstructor
public class BookingInfo {
    private StartEndDto timeSlot;
    private String professorName;
    private String professorLastname;
    private String title;
    private ViewerRoomDto meetingDetailsLink;


    public BookingInfo(Booking booking){

        ModelMapper modelMapper = new ModelMapper();

        this.timeSlot = modelMapper.map(booking.getTimeSlot(), StartEndDto.class);
        this.professorName = booking.getProfessor().getFirstName();
        this.professorLastname = booking.getProfessor().getLastName();
        this.title = booking.getCourse().getTitle();
        this.meetingDetailsLink = modelMapper.map(booking.getMeetingDetails(), ViewerRoomDto.class);
    }
}
