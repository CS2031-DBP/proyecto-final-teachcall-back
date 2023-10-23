package dbp.techcall.course.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name="category", schema = "spring_app")
public class Category {

    @Id
    @Column(name = "id",nullable = false)
    private Integer id;

    @Column(name = "title",nullable = false)
    private String title;

    //Constructores:
    //Constructor
    public Category(Integer id, String title) {
        this.id = id;
        this.title = title;
    }

    //Default constructor
    public Category() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}


