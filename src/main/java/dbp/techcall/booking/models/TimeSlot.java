package dbp.techcall.booking.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="time_slot", schema = "spring_app")
public class TimeSlot {

    @Id
    @Column(name = "id",nullable = false)
    private Integer id;

    @Column(name = "time_start",nullable = false)
    private String startTime;

    @Column(name = "time_end",nullable = false)
    private String endTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
