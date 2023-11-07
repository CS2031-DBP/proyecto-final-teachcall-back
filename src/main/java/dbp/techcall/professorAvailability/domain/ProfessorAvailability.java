package dbp.techcall.professorAvailability.domain;

import dbp.techcall.booking.domain.Booking;
import dbp.techcall.professor.domain.Professor;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "professor_availability")
public class ProfessorAvailability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Integer professorAvalabilityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professor_id", nullable = false, referencedColumnName = "id")
    private Professor professor;

    @Min(1)
    @Max(6)
    @Column(name = "day" ,
            nullable = false,
            columnDefinition = "INTEGER CHECK (day > 0 AND day < 7)")
    private int day;

    @Column(name = "start_time",
            nullable = false,
            columnDefinition = "TIME WITHOUT TIME ZONE CHECK (date_part('hour', start_time) > 8 AND date_part('hour', start_time) < 21 AND date_part('hour', start_time) < date_part('hour', end_time))")
    private LocalTime startTime;

    @Column(name = "end_time",
            nullable = false,
            columnDefinition = "TIME WITHOUT TIME ZONE CHECK (date_part('hour', end_time) > 8 AND date_part('hour', end_time) < 21 AND date_part('hour', end_time) > date_part('hour', start_time))"
    )
    private LocalTime endTime;

    @Column(name = "week_number" , nullable = false, columnDefinition = "INTEGER CHECK (week_number > 0 AND week_number < 53)")
    private int weekNumber;

    @Column(name="is_available", nullable = false, columnDefinition = "boolean default true")
    private Boolean isAvailable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking", referencedColumnName = "id")
    private Booking booking;

}
