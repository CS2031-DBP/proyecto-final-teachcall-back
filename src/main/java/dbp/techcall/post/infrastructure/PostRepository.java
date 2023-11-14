package dbp.techcall.post.infrastructure;

import dbp.techcall.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByProfessorId(Long professor_id);


}

