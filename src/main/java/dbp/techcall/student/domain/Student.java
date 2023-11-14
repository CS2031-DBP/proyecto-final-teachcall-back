package dbp.techcall.student.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import dbp.techcall.booking.domain.Booking;
import dbp.techcall.category.domain.Category;
import dbp.techcall.conversation.domain.Conversation;
import dbp.techcall.post.domain.Post;
import dbp.techcall.review.domain.Review;
import dbp.techcall.studentReply.domain.StudentReply;
import dbp.techcall.user.domain.Users;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "student", schema = "spring_app")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Student extends Users {

    @ManyToMany
    @JoinTable(
            name = "student_interests",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> interests = new HashSet<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<Booking> bookings;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private Set<Review> reviews = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "student_likes",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id")
    )
    private Set<Post> likedPosts = new HashSet<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private Set<Conversation> conversations = new HashSet<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private Set<StudentReply> replies = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(getId(), student.getId()) && Objects.equals(this.getFirstName(), student.getFirstName()) && Objects.equals(getLastName(), student.getLastName()) && Objects.equals(getEmail(), student.getEmail()) && Objects.equals(getPassword(), student.getPassword()) && Objects.equals(getCreatedAt(), student.getCreatedAt()) && Objects.equals(getUpdatedAt(), student.getUpdatedAt()) && Objects.equals(interests, student.interests) && Objects.equals(bookings, student.bookings) && Objects.equals(reviews, student.reviews) && Objects.equals(likedPosts, student.likedPosts) && Objects.equals(conversations, student.conversations) && Objects.equals(replies, student.replies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId(), this.getFirstName(), getLastName(), getEmail(), getPassword(), getCreatedAt(), getUpdatedAt(), interests, bookings, reviews, likedPosts, conversations, replies);
    }

}
