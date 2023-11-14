package dbp.techcall.like.domain;

import java.io.Serializable;

// LikesId class representing the composite primary key
public class LikesId implements Serializable {
    private Long studentId;
    private Long postId;

    // constructors, getters, setters, and equals/hashCode methods
    //Constructors
    public LikesId() {
    }

    //constructor
    public LikesId(Long studentId, Long postId) {
        this.studentId = studentId;
        this.postId = postId;
    }

    //Getters and Setters
    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

}