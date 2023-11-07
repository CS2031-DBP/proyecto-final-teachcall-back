package dbp.techcall.professorAvailability.infrastructure;


import dbp.techcall.professorAvailability.domain.ProfessorAvailability;
import dbp.techcall.professorAvailability.dto.BasicDayAvailability;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfessorAvailabilityRepository extends JpaRepository<ProfessorAvailability,Integer> {

    List<ProfessorAvailability> findByProfessorId(Long professor);

    List<ProfessorAvailability> findByProfessorIdAndWeekNumberAndDay(Long professor, Integer weekNumber, Integer day, Sort sort);

    List<ProfessorAvailability> findByProfessorIdAndWeekNumber(Long professorId, int week);

    List<BasicDayAvailability> findByProfessorIdAndWeekNumber(Long professorId, Integer week);
}
