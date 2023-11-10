package dbp.techcall.like.infrastructure;

import dbp.techcall.like.domain.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository  extends JpaRepository<Likes, Long>{
}
