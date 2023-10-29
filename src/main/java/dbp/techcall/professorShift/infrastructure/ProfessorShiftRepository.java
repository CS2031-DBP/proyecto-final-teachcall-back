package dbp.techcall.professorShift.infrastructure;

import dbp.techcall.professorShift.domain.ProfessorShift;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessorShiftRepository extends JpaRepository<ProfessorShift,Long> {
}
