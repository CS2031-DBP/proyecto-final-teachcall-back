package dbp.techcall.student.student.domain.models;


import dbp.techcall.professor.post.infrastructure.Conversation;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;


@Table(name="student_reply", schema = "spring_app")
@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class StudentReply {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Timestamp time;
    private String body;
    private int status;

    @ManyToOne
    private Student student_id;

    @ManyToOne
    private Conversation conversation_id;
}
