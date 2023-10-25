package dbp.techcall.professor.professor.infrastructure.models;

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
@Table(name="professor_shifts", schema = "spring_app")
public class ProfessorShift {
    //Primary key is composed by professorId and shiftId
    @Id
    @Column(name="time_slot_id", nullable = false)
    private Integer time_slot_id;

    @Id
    @Column(name="professor_id", nullable = false)
    private Integer professorId;

    @Column(name = "availability", nullable = false)
    private String availability;

    @Override
    public String toString() {
        return "ProfessorShift{" +
                "time_slot_id=" + time_slot_id +
                ", professorId=" + professorId +
                ", availability='" + availability + '\'' +
                '}';
    }

}
