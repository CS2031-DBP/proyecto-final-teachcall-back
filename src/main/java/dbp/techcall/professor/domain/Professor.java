package dbp.techcall.professor.domain;

import dbp.techcall.booking.domain.Booking;
import dbp.techcall.category.domain.Category;
import dbp.techcall.conversation.domain.Conversation;
import dbp.techcall.course.domain.Course;
import dbp.techcall.education.domain.Education;
import dbp.techcall.post.domain.Post;
import dbp.techcall.professorReply.domain.ProfessorReply;
import dbp.techcall.review.domain.Review;
import dbp.techcall.timeSlot.domain.TimeSlot;
import dbp.techcall.user.domain.Users;
import dbp.techcall.workExperience.domain.WorkExperience;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "professor")
public class Professor extends Users {

    @Column(name="price_per_hour")
    private Double pricePerHour;

    @Column(name = "description")
    private String description;

    @Column(name = "completed_tour", columnDefinition = "boolean default false")
    private Boolean tourCompleted;

    @OneToMany(mappedBy = "professor", cascade = CascadeType.ALL)
    private List<Post> posts;

    @OneToMany(mappedBy = "professor", cascade = CascadeType.ALL)
    private List<Education> educations;

    @OneToMany(mappedBy = "professor", cascade = CascadeType.ALL)
    private List<Booking> bookings;


    @ManyToMany
    @JoinTable(
            name = "professor_category",
            joinColumns = @JoinColumn(name = "professor_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories;

    @OneToMany(mappedBy = "professor", cascade = CascadeType.ALL)
    private List<Course> courses;

    @OneToMany(mappedBy = "professor", cascade = CascadeType.ALL)
    private List<Review> reviews;

    @OneToMany(mappedBy = "professor", cascade = CascadeType.ALL)
    private List<WorkExperience> workExperiences;

    @OneToMany(mappedBy = "professor", cascade = CascadeType.ALL)
    private List<Conversation> conversations;

    @OneToMany(mappedBy = "professor", cascade = CascadeType.ALL)
    private List<ProfessorReply> professorReplies;

    @OneToMany(mappedBy = "professor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TimeSlot> timeSlots;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Professor professor = (Professor) o;
        return Objects.equals(getId(), professor.getId()) && Objects.equals(getFirstName(), professor.getFirstName()) && Objects.equals(getLastName(), professor.getLastName()) && Objects.equals(getEmail(), professor.getEmail()) && Objects.equals(getPassword(), professor.getPassword()) && Objects.equals(getCreatedAt(), professor.getCreatedAt()) && Objects.equals(getUpdatedAt(), professor.getUpdatedAt()) && Objects.equals(description, professor.description) && Objects.equals(posts, professor.posts) && Objects.equals(educations, professor.educations) && Objects.equals(categories, professor.categories) && Objects.equals(courses, professor.courses) && Objects.equals(reviews, professor.reviews) && Objects.equals(workExperiences, professor.workExperiences) && Objects.equals(conversations, professor.conversations) && Objects.equals(professorReplies, professor.professorReplies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getFirstName(), getLastName(), getEmail(), getPassword(), getCreatedAt(), getUpdatedAt(), description, posts, educations, categories,courses, reviews, workExperiences, conversations, professorReplies);
    }


    public Professor orElse(Object o) { return null; }
}
