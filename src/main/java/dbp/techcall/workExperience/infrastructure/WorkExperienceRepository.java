package dbp.techcall.workExperience.infrastructure;

import dbp.techcall.workExperience.domain.WorkExperience;
import dbp.techcall.workExperience.dto.BasicExperienceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkExperienceRepository extends JpaRepository<WorkExperience, Integer> {
    Page<List<BasicExperienceResponse>> findWorkExperiencesByProfessorId(Long id, Pageable pageable);
}
