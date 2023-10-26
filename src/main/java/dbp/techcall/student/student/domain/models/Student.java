package dbp.techcall.student.student.domain.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import dbp.techcall.professor.professor.infrastructure.Review;
import dbp.techcall.professor.post.infrastructure.Conversation;
import jakarta.persistence.*;

import java.time.ZonedDateTime;
import java.util.*;

import dbp.techcall.category.Category;
import dbp.techcall.booking.models.Booking;
import dbp.techcall.professor.post.infrastructure.models.Post;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "student", schema = "spring_app")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Student implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @Column(name = "updated_at", nullable = true)
    private ZonedDateTime updatedAt;

    @ManyToMany
    @JoinTable(
            name = "student_interests",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> interests = new HashSet<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<Booking> bookings;

    @OneToMany(mappedBy = "student")
    private Set<Review> reviews = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "student_likes",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id")
    )
    private Set<Post> likedPosts = new HashSet<>();

    @OneToMany(mappedBy = "student")
    private Set<Conversation> conversations = new HashSet<>();

    @OneToMany(mappedBy = "student")
    private Set<StudentReply> replies = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(id, student.id) && Objects.equals(firstName, student.firstName) && Objects.equals(lastName, student.lastName) && Objects.equals(email, student.email) && Objects.equals(password, student.password) && Objects.equals(createdAt, student.createdAt) && Objects.equals(updatedAt, student.updatedAt) && Objects.equals(interests, student.interests) && Objects.equals(bookings, student.bookings) && Objects.equals(reviews, student.reviews) && Objects.equals(likedPosts, student.likedPosts) && Objects.equals(conversations, student.conversations) && Objects.equals(replies, student.replies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email, password, createdAt, updatedAt, interests, bookings, reviews, likedPosts, conversations, replies);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_STUDENT"));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
