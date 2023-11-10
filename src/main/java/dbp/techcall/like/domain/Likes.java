package dbp.techcall.like.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "likes")
public class Likes {

    @Id
    private Long student_id;

    @Id
    private Long post_id;

    public Likes() {
    }

    public Likes(Long student_id, Long post_id) {
        this.student_id = student_id;
        this.post_id = post_id;
    }

    public Long getStudent_id() {
        return student_id;
    }

    public void setStudent_id(Long student_id) {
        this.student_id = student_id;
    }

    public Long getPost_id() {
        return post_id;
    }

    public void setPost_id(Long post_id) {
        this.post_id = post_id;
    }







}
