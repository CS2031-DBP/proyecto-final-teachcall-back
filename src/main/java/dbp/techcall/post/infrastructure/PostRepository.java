package dbp.techcall.post.infrastructure;

import dbp.techcall.post.domain.Post;
import dbp.techcall.post.dto.PostInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByProfessorId(Long professor_id);

    Page<Long> findLikedPostByProfessorId(Long id, Pageable page);

    @Query(value = """
            select p.id as id, p.title as title, p.body as body, p.media_key as mediaUrl, count(l.student_id) as likes, p.created_at as createdAt
            from post p
            left join student_likes l on l.post_id = p.id
            where p.professor_id = :id
            group by p.id, p.title, p.body
            order by p.created_at;""", nativeQuery = true)
    Page<PostInfo> getCurrentUserPostWithPagination(@Param("id") Long id, Pageable page);
}

