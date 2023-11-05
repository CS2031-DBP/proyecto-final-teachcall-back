package dbp.techcall.meetingDetails.domain;


import dbp.techcall.booking.domain.Booking;
import jakarta.persistence.*;
import lombok.*;

import java.security.Timestamp;

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
    private Integer id;

    @Column(name="end_date", nullable = false)
    private String endDate;

    @Column(name="room_mode", nullable = false)
    private String roomMode;

    @Column(name="room_name_prefix", nullable = false)
    private String roomNamePrefix;

    @Column(name="meeting_id", nullable = false)
    private String meetingId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", referencedColumnName = "id")
    private Booking booking;

    @Column(name="host_room_url", nullable = false)
    private String hostRoomUrl;

    @Column(name="viewer_room_url", nullable = false)
    private String viewerRoomUrl;
}

