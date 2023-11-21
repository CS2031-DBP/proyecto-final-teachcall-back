package dbp.techcall.course.infrastructure;

import dbp.techcall.course.domain.Course;
import dbp.techcall.course.dto.BasicCourseResponse;
import dbp.techcall.course.dto.TitleDescriptionProjection;
import dbp.techcall.course.dto.TopFiveCourses;
import dbp.techcall.professor.domain.Professor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CourseRepository extends JpaRepository <Course,Long    >{

//    findByProfessorEmail
    List<Course> findByProfessorEmail(String email);

    @Query(value =
            """
                    select c.id, c.title, tp.firstName, tp.s as rating
                    from course as c
                    join (select avg(rating) as s, p.id, p.first_name as firstName
                    from review r
                    join professor p
                        on r.professor_id = p.id
                    group by p.id, p.first_name) as tp
                    on tp.id = c.professor_id
                    ORDER BY  rating DESC
                    LIMIT 6;"""
            , nativeQuery = true)
    List<TopFiveCourses> findTopCourses();

    Page<Course> findByProfessor(Professor professor, Pageable pageable);

    BasicCourseResponse findBasicResponseById(Integer id);

    TitleDescriptionProjection findTitleDescriptionProjectionById(Integer id);


}
