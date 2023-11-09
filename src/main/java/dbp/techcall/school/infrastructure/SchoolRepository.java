package dbp.techcall.school.infrastructure;

import dbp.techcall.school.domain.School;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<School, Long>{
    School findByName(String schoolName);
}
