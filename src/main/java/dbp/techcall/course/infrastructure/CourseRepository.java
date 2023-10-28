package dbp.techcall.course.infrastructure;

import dbp.techcall.course.domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CourseRepository extends JpaRepository <Course,Long>{
}
