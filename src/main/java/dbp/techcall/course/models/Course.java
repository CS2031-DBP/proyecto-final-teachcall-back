package dbp.techcall.course.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name="course", schema = "spring_app")
public class Course {
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "price_per_hour", nullable = false)
    private Integer price;

    //Foreign key professor
    @Column(name = "professor_id", nullable = false)
    private Integer professorId;

    //Constructor
    public Course(Integer id, String title, String description, Integer price, Integer professorId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.professorId = professorId;
    }

    //Default constructor
    public Course() {
    }


    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getProfessorId() {
        return professorId;
    }



    public void setId(Integer id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setProfessorId(Integer professorId) {
        this.professorId = professorId;
    }




}
