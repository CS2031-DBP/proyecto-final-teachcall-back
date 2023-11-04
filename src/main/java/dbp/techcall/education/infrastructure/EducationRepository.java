package dbp.techcall.education.infrastructure;

import dbp.techcall.education.domain.Education;
import dbp.techcall.education.dto.BasicEducationResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EducationRepository extends JpaRepository<Education, Long> {
    List<BasicEducationResponse> findAllByProfessorId(Long id);
}
