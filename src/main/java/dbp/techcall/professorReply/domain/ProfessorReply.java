package dbp.techcall.professorReply.domain;

import dbp.techcall.conversation.domain.Conversation;
import dbp.techcall.professor.domain.Professor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "professor_reply")
public class ProfessorReply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "time", nullable = false)
    private ZonedDateTime time;

    @Column(name = "body", columnDefinition = "TEXT", nullable = false)
    private String body;

    @Column(name = "status", nullable = false)
    private int status;

    @ManyToOne
    @JoinColumn(name = "conversation_id", referencedColumnName = "id", nullable = false)
    private Conversation conversation;

    @ManyToOne
    @JoinColumn(name = "professor_id", referencedColumnName = "id", nullable = false)
    private Professor professor;
}
