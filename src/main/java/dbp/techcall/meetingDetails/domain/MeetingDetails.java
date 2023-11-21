package dbp.techcall.meetingDetails.domain;


import dbp.techcall.booking.domain.Booking;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="meeting_details")
public class MeetingDetails {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="end_date",nullable = false)
    private String endDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", referencedColumnName = "id")
    private Booking booking;

    @Column(name="host_room_url",nullable = false, length = 500)
    private String hostRoomUrl;

    @Column(name="viewer_room_url",nullable = false, length = 500)
    private String viewerRoomUrl;

    @Column(name="meeting_id",nullable = false)
    private String meetingId;
}
