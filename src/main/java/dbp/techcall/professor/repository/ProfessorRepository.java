package dbp.techcall.professor.repository;

import dbp.techcall.professor.professor.infrastructure.models.Professor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfessorRepository extends JpaRepository<Professor, Integer> {
    Professor findByEmail(String email);

    Professor findById(Long professorId);
}
