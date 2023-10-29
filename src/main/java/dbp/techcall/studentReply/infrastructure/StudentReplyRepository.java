package dbp.techcall.studentReply.infrastructure;

import dbp.techcall.studentReply.domain.StudentReply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentReplyRepository extends JpaRepository<StudentReply, Integer> {
}
