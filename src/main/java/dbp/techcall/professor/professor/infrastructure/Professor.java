package dbp.techcall.professor.professor.infrastructure;


import dbp.techcall.professor.post.infrastructure.Conversation;
import dbp.techcall.professor.post.infrastructure.Post;
import dbp.techcall.professor.post.infrastructure.ProfessorReply;
import jakarta.persistence.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

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
    private List<ProfessorShifts> shifts;

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

    public Professor(Long id, String firstName, String lastName, String email, String password, ZonedDateTime createdAt, ZonedDateTime updatedAt, String description, List<Post> posts, List<Education> educations, List<Category> categories, List<ProfessorShifts> shifts, List<Course> courses, List<Review> reviews, List<WorkExperience> workExperiences, List<Conversation> conversations, List<ProfessorReply> professorReplies) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.description = description;
        this.posts = posts;
        this.educations = educations;
        this.categories = categories;
        this.shifts = shifts;
        this.courses = courses;
        this.reviews = reviews;
        this.workExperiences = workExperiences;
        this.conversations = conversations;
        this.professorReplies = professorReplies;
    }

    public Professor() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public List<Education> getEducations() {
        return educations;
    }

    public void setEducations(List<Education> educations) {
        this.educations = educations;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<ProfessorShifts> getShifts() {
        return shifts;
    }

    public void setShifts(List<ProfessorShifts> shifts) {
        this.shifts = shifts;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<WorkExperience> getWorkExperiences() {
        return workExperiences;
    }

    public void setWorkExperiences(List<WorkExperience> workExperiences) {
        this.workExperiences = workExperiences;
    }

    public List<Conversation> getConversations() {
        return conversations;
    }

    public void setConversations(List<Conversation> conversations) {
        this.conversations = conversations;
    }

    public List<ProfessorReply> getProfessorReplies() {
        return professorReplies;
    }

    public void setProfessorReplies(List<ProfessorReply> professorReplies) {
        this.professorReplies = professorReplies;
    }

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
