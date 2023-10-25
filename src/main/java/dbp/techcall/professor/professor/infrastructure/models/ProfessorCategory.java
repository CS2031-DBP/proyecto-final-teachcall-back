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
@Entity
@Table(name="professor_category", schema = "spring_app")
public class ProfessorCategory {
    //Many-to-many relationship between professor and category
    //Primary key is composed by professorId and categoryId\
    @Id
    @Column(name="professor_id", nullable = false)
    private Integer professorId;

    @Id
    @Column(name="category_id", nullable = false)
    private Integer categoryId;

    @Override
    public String toString() {
        return "ProfessorCategory{" +
                "professorId=" + professorId +
                ", categoryId=" + categoryId +
                '}';
    }

}
