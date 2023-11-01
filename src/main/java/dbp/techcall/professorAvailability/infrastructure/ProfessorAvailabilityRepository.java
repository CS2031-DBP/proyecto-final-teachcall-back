package dbp.techcall.professorAvailability.infrastructure;

import dbp.techcall.professor.domain.Professor;
import dbp.techcall.professorAvailability.domain.ProfessorAvailability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfessorAvailabilityRepository extends JpaRepository<ProfessorAvailability,Integer> {

    List<ProfessorAvailability> findByProfessorId(Professor professor);

    List<ProfessorAvailability> findByProfessorIdAndWeekNumberAndDay(Professor professor, Integer weekNumber, Integer day);

    List<ProfessorAvailability> findByProfessorIdAndWeekNumber(Long professorId, int week);
}
