package dbp.techcall.professorAvailability.domain;

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
@Table(name = "professor_availability", uniqueConstraints = {
        @UniqueConstraint(
                columnNames = {"professor_id", "day", "week_number"},
                name = "unique_professor_availability_per_day"
        )
})
public class ProfessorAvailability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
            columnDefinition = "TIME WITHOUT TIME ZONE CHECK (date_part('hour', start_time) > 6 AND date_part('hour', start_time) < 22 AND date_part('hour', start_time) < date_part('hour', end_time))")
    private LocalTime startTime;

    @Column(name = "end_time",
            nullable = false,
            columnDefinition = "TIME WITHOUT TIME ZONE CHECK (date_part('hour', end_time) > 6 AND date_part('hour', end_time) < 22 AND date_part('hour', end_time) > date_part('hour', start_time))"
    )
    private LocalTime endTime;

    @Column(name = "week_number" , nullable = false, columnDefinition = "INTEGER CHECK (week_number > 0 AND week_number < 53)")
    private int weekNumber;


}
