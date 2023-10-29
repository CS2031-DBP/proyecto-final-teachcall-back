package dbp.techcall.professorShift.domain;

import dbp.techcall.timeSlot.domain.TimeSlot;
import dbp.techcall.professor.domain.Professor;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "professor_shifts")
public class ProfessorShift {

    @EmbeddedId
    private ProfessorShiftId id;

    @Column(name = "availability")
    private String availability;

    @ManyToOne
    @MapsId("professorId")
    @JoinColumn(name = "professor_id", nullable = false)
    private Professor professor;

    @ManyToOne
    @MapsId("timeSlotId")
    @JoinColumn(name = "time_slot_id", nullable = false)
    private TimeSlot timeSlot;
}


@Embeddable
class ProfessorShiftId implements Serializable {

    @Column(name = "professor_id")
    private Long professorId;

    @Column(name = "time_slot_id")
    private Long timeSlotId;

    public Long getProfessorId() {
        return professorId;
    }

    public void setProfessorId(Long professorId) {
        this.professorId = professorId;
    }

    public Long getTimeSlotId() {
        return timeSlotId;
    }

    public void setTimeSlotId(Long timeSlotId) {
        this.timeSlotId = timeSlotId;
    }

    public ProfessorShiftId(Long professorId, Long timeSlotId) {
        this.professorId = professorId;
        this.timeSlotId = timeSlotId;
    }

    public ProfessorShiftId() {
    }
}
