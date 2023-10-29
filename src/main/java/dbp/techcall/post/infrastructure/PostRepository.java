package dbp.techcall.post.infrastructure;

import dbp.techcall.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer> {
}
