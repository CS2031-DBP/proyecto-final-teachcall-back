package dbp.techcall.professor.post.infrastructure;

import dbp.techcall.professor.professor.infrastructure.models.Professor;
import dbp.techcall.student.student.domain.models.Student;
import dbp.techcall.student.student.domain.models.StudentReply;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "conversation")
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "status", nullable = false, length = 45)
    private String status;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "professor_id", referencedColumnName = "id", nullable = false)
    private Professor professor;

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL)
    private List<ProfessorReply> professorReplies;

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL)
    private List<StudentReply> studentReplies;

}