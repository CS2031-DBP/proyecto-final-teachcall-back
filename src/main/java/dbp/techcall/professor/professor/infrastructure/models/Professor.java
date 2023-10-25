package dbp.techcall.professor.professor.infrastructure.models;


import dbp.techcall.course.models.Category;
import dbp.techcall.course.models.Course;
import dbp.techcall.professor.post.infrastructure.models.Post;
import dbp.techcall.professor.post.infrastructure.Conversation;
import dbp.techcall.professor.professor.infrastructure.Education;
import dbp.techcall.professor.post.infrastructure.ProfessorReply;
import dbp.techcall.professor.professor.infrastructure.Review;
import dbp.techcall.professor.professor.infrastructure.WorkExperience;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "professor", schema = "spring_app")
public class Professor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "professor", cascade = CascadeType.ALL)
    private List<Post> posts;

    @OneToMany(mappedBy = "professor", cascade = CascadeType.ALL)
    private List<Education> educations;

    @ManyToMany
    @JoinTable(
            name = "professor_category",
            joinColumns = @JoinColumn(name = "professor_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories;

    @OneToMany(mappedBy = "professor", cascade = CascadeType.ALL)
    private List<ProfessorShift> shifts;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Professor professor = (Professor) o;
        return Objects.equals(id, professor.id) && Objects.equals(firstName, professor.firstName) && Objects.equals(lastName, professor.lastName) && Objects.equals(email, professor.email) && Objects.equals(password, professor.password) && Objects.equals(createdAt, professor.createdAt) && Objects.equals(updatedAt, professor.updatedAt) && Objects.equals(description, professor.description) && Objects.equals(posts, professor.posts) && Objects.equals(educations, professor.educations) && Objects.equals(categories, professor.categories) && Objects.equals(shifts, professor.shifts) && Objects.equals(courses, professor.courses) && Objects.equals(reviews, professor.reviews) && Objects.equals(workExperiences, professor.workExperiences) && Objects.equals(conversations, professor.conversations) && Objects.equals(professorReplies, professor.professorReplies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email, password, createdAt, updatedAt, description, posts, educations, categories, shifts, courses, reviews, workExperiences, conversations, professorReplies);
    }
}
