package dbp.techcall.student.student.domain.models;


import dbp.techcall.professor.post.infrastructure.Conversation;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;


@Table(name="student_reply")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class StudentReply {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Timestamp time;
    private String body;
    private int status;

    @ManyToOne
    @JoinColumn(name ="conversation_id", referencedColumnName = "id", nullable = false)
    private Conversation conversation;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "id", nullable = false)
    private Student student;

}
