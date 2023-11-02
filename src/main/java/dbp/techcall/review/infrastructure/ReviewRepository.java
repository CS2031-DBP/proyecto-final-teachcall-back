package dbp.techcall.review.infrastructure;

import dbp.techcall.review.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findAllByProfessorId(Long professorId, Pageable pageable);

    @Query(value = "SET SEARCH_PATH TO spring_app; SELECT AVG(rating), COUNT(id) FROM review WHERE professor_id = :id", nativeQuery = true)
    List<Double> getAverageRating(@Param("id")Long professorId);
}
