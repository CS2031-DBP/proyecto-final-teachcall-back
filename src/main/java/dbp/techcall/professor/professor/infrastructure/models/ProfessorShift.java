package dbp.techcall.professor.professor.infrastructure.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


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


    //getters
    public Integer getTime_slot_id() {
        return time_slot_id;
    }

    public Integer getProfessorId() {
        return professorId;
    }

    public String getAvailability() {
        return availability;
    }

    //setters
    public void setTime_slot_id(Integer time_slot_id) {
        this.time_slot_id = time_slot_id;
    }

    public void setProfessorId(Integer professorId) {
        this.professorId = professorId;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    //constructor
    public ProfessorShift(Integer time_slot_id, Integer professorId, String availability) {
        this.time_slot_id = time_slot_id;
        this.professorId = professorId;
        this.availability = availability;
    }

    public ProfessorShift() {
    }

    @Override
    public String toString() {
        return "ProfessorShift{" +
                "time_slot_id=" + time_slot_id +
                ", professorId=" + professorId +
                ", availability='" + availability + '\'' +
                '}';
    }

}
