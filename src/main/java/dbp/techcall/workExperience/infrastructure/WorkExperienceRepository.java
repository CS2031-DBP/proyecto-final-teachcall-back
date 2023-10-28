package dbp.techcall.workExperience.infrastructure;

import dbp.techcall.workExperience.domain.WorkExperience;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkExperienceRepository extends JpaRepository<WorkExperience, Integer> {
}
