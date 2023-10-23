package dbp.techcall.student.student.domain.models;

import dbp.techcall.professor.post.infrastructure.Review;
import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Objects;
import dbp.techcall.course.models.Category;
import dbp.techcall.booking.models.Booking;
import dbp.techcall.professor.post.infrastructure.models.Post;

import java.util.Set;

@Entity
@Table(name = "student", schema = "spring_app")
public class Student {

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

    @Column(name = "email", nullable = false)
    private String password;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @ManyToMany
    @JoinTable(
            name = "student_interests",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> interests = new HashSet<>();

    @OneToMany(mappedBy = "student")
    private Set<Booking> bookings = new HashSet<>();

    @OneToMany(mappedBy = "student")
    private Set<Review> reviews = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "likes",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id")
    )
    private Set<Post> likedPosts = new HashSet<>();

    @OneToMany(mappedBy = "student")
    private Set<Conversation> conversations = new HashSet<>();

    @OneToMany(mappedBy = "student")
    private Set<StudentReply> replies = new HashSet<>();

    public Student(Integer id, String firstName, String lastName, String email, String password, OffsetDateTime createdAt, OffsetDateTime updatedAt, Set<Category> interests, Set<Booking> bookings, Set<Review> reviews, Set<Post> likedPosts, Set<Conversation> conversations, Set<StudentReply> replies) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.interests = interests;
        this.bookings = bookings;
        this.reviews = reviews;
        this.likedPosts = likedPosts;
        this.conversations = conversations;
        this.replies = replies;
    }

    public Student() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<Category> getInterests() {
        return interests;
    }

    public void setInterests(Set<Category> interests) {
        this.interests = interests;
    }

    public Set<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(Set<Booking> bookings) {
        this.bookings = bookings;
    }

    public Set<Review> getReviews() {
        return reviews;
    }

    public void setReviews(Set<Review> reviews) {
        this.reviews = reviews;
    }

    public Set<Post> getLikedPosts() {
        return likedPosts;
    }

    public void setLikedPosts(Set<Post> likedPosts) {
        this.likedPosts = likedPosts;
    }

    public Set<Conversation> getConversations() {
        return conversations;
    }

    public void setConversations(Set<Conversation> conversations) {
        this.conversations = conversations;
    }

    public Set<StudentReply> getReplies() {
        return replies;
    }

    public void setReplies(Set<StudentReply> replies) {
        this.replies = replies;
    }

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
}
