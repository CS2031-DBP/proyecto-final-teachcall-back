package dbp.techcall.course.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import dbp.techcall.professor.professor.infrastructure.models.Professor;
import dbp.techcall.student.student.domain.models.Student;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="category")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Category {
    @Id
    @Column(name = "id",nullable = false)
    private Integer id;

    @Column(name = "title",nullable = false)
    private String title;

    @ManyToMany(mappedBy = "interests")
    private Set<Student> students;

    @ManyToMany(mappedBy = "categories")
    private Set<Professor> professors;

    @ManyToMany(mappedBy = "categories")
    private Set<Course> courses;

}


