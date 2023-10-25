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
@Table(name="course_category", schema = "spring_app")
public class CourseCategory {

    @Id
    @Column(name="course_id", nullable = false)
    private Integer courseId;

    @Id
    @Column(name="category_id", nullable = false)
    private Integer categoryId;
}
