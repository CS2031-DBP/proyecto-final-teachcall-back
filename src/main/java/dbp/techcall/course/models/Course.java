package dbp.techcall.course.models;

import dbp.techcall.professor.professor.infrastructure.models.Professor;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
    @ManyToOne
    @JoinColumn(name = "professor_id", nullable = false)
    private Professor professor;


}
