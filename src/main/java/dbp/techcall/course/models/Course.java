package dbp.techcall.course.models;

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
@Table(name="course", schema = "spring_app")
public class Course {
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "price_per_hour", nullable = false)
    private Integer price;

    //Foreign key professor
    @Column(name = "professor_id", nullable = false)
    private Integer professorId;

}
