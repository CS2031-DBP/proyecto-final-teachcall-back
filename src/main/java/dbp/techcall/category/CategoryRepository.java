package dbp.techcall.category;
import dbp.techcall.course.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CategoryRepository extends JpaRepository <Category,Long>{


}
