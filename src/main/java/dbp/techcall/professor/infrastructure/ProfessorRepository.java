package dbp.techcall.professor.infrastructure;

import dbp.techcall.professor.domain.Professor;
import dbp.techcall.user.infrastructure.BaseUserRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfessorRepository extends BaseUserRepository<Professor> {
    Professor findByEmail(String email);
    Optional<Professor> findById(Long professorId);
}
