package dbp.techcall.professorReply.infrastructure;

import dbp.techcall.professorReply.domain.ProfessorReply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessorReplyRepository extends JpaRepository<ProfessorReply, Integer> {
}
