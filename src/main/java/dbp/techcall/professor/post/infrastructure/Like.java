package dbp.techcall.professor.post.infrastructure;

import dbp.techcall.student.student.domain.Student;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "likes", schema = "spring_app")
public class Like {

    @EmbeddedId
    private LikeId id = new LikeId();

    @MapsId("studentId")
    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "id", nullable = false)
    private Student student;

    @MapsId("postId")
    @ManyToOne
    @JoinColumn(name = "post_id", referencedColumnName = "id", nullable = false)
    private Post post;

    @Embeddable
    public static class LikeId implements Serializable {
        private Integer studentId;
        private Integer postId;

        public LikeId() {
        }

        public LikeId(Integer studentId, Integer postId) {
            this.studentId = studentId;
            this.postId = postId;
        }

        public Integer getStudentId() {
            return studentId;
        }

        public void setStudentId(Integer studentId) {
            this.studentId = studentId;
        }

        public Integer getPostId() {
            return postId;
        }

        public void setPostId(Integer postId) {
            this.postId = postId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            LikeId likeId = (LikeId) o;
            return Objects.equals(studentId, likeId.studentId) && Objects.equals(postId, likeId.postId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(studentId, postId);
        }
    }

    public Like(LikeId id, Student student, Post post) {
        this.id = id;
        this.student = student;
        this.post = post;
    }

    public Like() {
    }

    public LikeId getId() {
        return id;
    }

    public void setId(LikeId id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Like like = (Like) o;
        return Objects.equals(id, like.id) && Objects.equals(student, like.student) && Objects.equals(post, like.post);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, student, post);
    }

    @Override
    public String toString() {
        return "Like{" +
                "id=" + id +
                ", student=" + student +
                ", post=" + post +
                '}';
    }
}
