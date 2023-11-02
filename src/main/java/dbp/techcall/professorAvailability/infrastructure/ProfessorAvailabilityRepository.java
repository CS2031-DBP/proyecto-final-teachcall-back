package dbp.techcall.professorAvailability.infrastructure;

import dbp.techcall.professor.domain.Professor;
import dbp.techcall.professorAvailability.domain.ProfessorAvailability;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfessorAvailabilityRepository extends JpaRepository<ProfessorAvailability,Integer> {

    List<ProfessorAvailability> findByProfessorId(Long professor);

    List<ProfessorAvailability> findByProfessorIdAndWeekNumberAndDay(Long professor, Integer weekNumber, Integer day, Sort sort);

    List<ProfessorAvailability> findByProfessorIdAndWeekNumber(Long professorId, int week);
}
