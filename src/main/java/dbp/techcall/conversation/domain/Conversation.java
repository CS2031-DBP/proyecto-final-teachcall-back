package dbp.techcall.conversation.domain;

import dbp.techcall.professor.domain.Professor;
import dbp.techcall.professorReply.domain.ProfessorReply;
import dbp.techcall.student.domain.Student;
import dbp.techcall.studentReply.domain.StudentReply;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
