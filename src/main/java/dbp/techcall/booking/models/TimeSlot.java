package dbp.techcall.booking.models;

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
@Table(name="time_slot", schema = "spring_app")
public class TimeSlot {

    @Id
    @Column(name = "id",nullable = false)
    private Integer id;

    @Column(name = "time_start",nullable = false)
    private String startTime;

    @Column(name = "time_end",nullable = false)
    private String endTime;

}
