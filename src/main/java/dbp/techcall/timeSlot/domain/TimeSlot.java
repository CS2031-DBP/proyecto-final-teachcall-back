package dbp.techcall.timeSlot.domain;

import dbp.techcall.booking.domain.Booking;
import dbp.techcall.professor.domain.Professor;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

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

    @Min(1)
    @Max(6)
    @Column(name = "day" ,
            nullable = false,
            columnDefinition = "INTEGER CHECK (day > 0 AND day < 7)")
    private int day;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "week_number" , nullable = false, columnDefinition = "INTEGER CHECK (week_number > 0 AND week_number < 53)")
    private int weekNumber;

    @Column(name="is_available", nullable = false, columnDefinition = "boolean default true")
    private Boolean isAvailable;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", referencedColumnName = "id")
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "professor_id", referencedColumnName = "id")
    private Professor professor;

}

