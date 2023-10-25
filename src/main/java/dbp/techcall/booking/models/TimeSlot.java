package dbp.techcall.booking.models;

import dbp.techcall.professor.professor.infrastructure.models.ProfessorShift;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="time_slot", schema = "spring_app")
public class TimeSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "time_start", nullable = false)
    private Integer timeStart;

    @Column(name = "time_end", nullable = false)
    private Integer timeEnd;

    @OneToMany(mappedBy = "timeSlot", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProfessorShift> professorShifts;

}
