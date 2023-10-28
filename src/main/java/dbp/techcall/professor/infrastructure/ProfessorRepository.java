package dbp.techcall.professor.infrastructure;

import dbp.techcall.professor.domain.Professor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessorRepository extends JpaRepository<Professor, Integer> {
    Professor findByEmail(String email);

    Professor findById(Long professorId);
}
