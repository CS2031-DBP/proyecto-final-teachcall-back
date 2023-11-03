package dbp.techcall.review.infrastructure;

import dbp.techcall.review.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
    Page<Review> findAllByProfessorId(Long professorId, Pageable pageable);

    @Query(value = "SELECT CAST(AVG(rating) AS DOUBLE PRECISION), COUNT(id) FROM review WHERE professor_id = :id", nativeQuery = true)
    List<Object[]> getAverageRating(@Param("id")Long professorId);
}
