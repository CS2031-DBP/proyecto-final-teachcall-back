package dbp.techcall.studentReply.domain;


import dbp.techcall.conversation.domain.Conversation;
import dbp.techcall.student.domain.Student;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;


@Table(name="student_reply")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class StudentReply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
