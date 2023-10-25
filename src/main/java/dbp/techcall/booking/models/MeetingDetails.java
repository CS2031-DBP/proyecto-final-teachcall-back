package dbp.techcall.booking.models;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name="meeting_details", schema = "spring_app")
public class MeetingDetails {
    @Id
    @Column(name = "id",nullable = false)
    private Integer id;

    @Column(name="enddate",nullable = false)
    private String endDate;

    @Column(name="islocked",nullable = false)
    private Boolean isLocked;

    @Column(name="roommode",nullable = false)
    private String roomMode;

   @Column(name="roomnameprefix",nullable = false)
    private String roomNamePrefix;

    @Column(name="templatetype",nullable = false)
    private String templateType;

//    @Column(name="recording",nullable = false)
//    private  recording; //jsonb
    //type jsonb

    @Column(name="hostroomurl",nullable = false)
    private String hostRoomUrl;

    @Column(name="viewerroomurl",nullable = false)
    private String viewerRoomUrl;

    @Column(name="booking_id",nullable = false)
    private Integer bookingId;

}
