package dbp.techcall.education.infrastructure;

import dbp.techcall.education.domain.Education;
import dbp.techcall.education.dto.BasicEducationResponse;
import dbp.techcall.professor.dto.BasicEducation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EducationRepository extends JpaRepository<Education, Long> {
    List<BasicEducationResponse> findAllByProfessorId(Long id);

    @Query(value =
            "select degree.degree as degree, sc.name as schoolName, degree.start_date as startDate, degree.end_date as endDate\n" +
                    "from professor as p,\n" +
                    "     (select professor_id, degree, school_id, start_date, end_date\n" +
                    "      from education\n" +
                    "      where professor_id = :id\n" +
                    "      order by start_date desc) as degree,\n" +
                    "     (select id, name from school) as sc\n" +
                    "where p.id = degree.professor_id\n" +
                    "  and degree.school_id = sc.id\n"
            , nativeQuery = true)
    Page<BasicEducation> getEducationWithPagination(@Param("id")Long id, Pageable pageable);
}
