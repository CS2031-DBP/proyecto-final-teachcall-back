package dbp.techcall.booking.models;

import dbp.techcall.booking.domain.Booking;
import dbp.techcall.professorShift.domain.ProfessorShift;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="time_slot")
public class TimeSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "time_start", nullable = false)
    private Date timeStart;

    @Column(name = "time_end", nullable = false)
    private Date timeEnd;

    @OneToMany(mappedBy = "timeSlot", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProfessorShift> professorShifts;

    @OneToMany(mappedBy = "timeSlot", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Booking> bookings;

    public MeetingDetails getMeetingDetails() {
        return null;
    }
}

