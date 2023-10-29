package dbp.techcall.category.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import dbp.techcall.course.domain.Course;
import dbp.techcall.professor.domain.Professor;
import dbp.techcall.student.domain.Student;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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


