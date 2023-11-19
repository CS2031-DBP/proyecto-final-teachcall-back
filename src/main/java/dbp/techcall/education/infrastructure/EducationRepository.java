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
            """
                    select degree.id as id, degree.degree as degree, sc.name as schoolName, degree.start_date as startDate, degree.end_date as endDate
                    from professor as p,
                         (select id, professor_id, degree, school_id, start_date, end_date
                          from education
                          where professor_id = :id
                          order by start_date desc) as degree,
                         (select id, name from school) as sc
                    where p.id = degree.professor_id
                      and degree.school_id = sc.id
                    """
            , nativeQuery = true)
    Page<BasicEducation> getEducationWithPagination(@Param("id")Long id, Pageable pageable);
}
