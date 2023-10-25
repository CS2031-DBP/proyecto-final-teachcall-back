package dbp.techcall.booking.models;

import dbp.techcall.student.student.domain.models.Student;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="booking", schema = "spring_app")
public class Booking {
    @Id
    @Column(name = "id",nullable = false)
    private Integer id;

    @Column(name = "student_id",nullable = false)
    private Integer studentId;

    @Column(name = "course_id",nullable = false)
    private Integer courseId;

    @Column(name="professor_id",nullable = false)
    private Integer professorId;

    @Column(name="date",nullable = false)
    private String date;

    @Column(name="status",nullable = false)
    private String status;

    @Column(name="link",nullable = true)
    private String link;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="student_id", referencedColumnName="id", insertable = false, updatable = false)
    private Student student;
}
