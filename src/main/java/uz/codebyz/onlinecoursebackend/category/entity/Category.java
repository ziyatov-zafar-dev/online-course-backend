package uz.codebyz.onlinecoursebackend.category.entity;

import jakarta.persistence.*;
import uz.codebyz.onlinecoursebackend.course.entity.Course;
import uz.codebyz.onlinecoursebackend.helper.CurrentTime;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid")
    private UUID id;
    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;
    private Boolean active;
    private Boolean deleted;
    private CategoryStatus status;
    @Column(nullable = false, unique = true, length = 100)
    private String slug;
    private Integer orderNumber;
    private LocalDateTime created;
    private LocalDateTime updated;
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Course> courses;

    public CategoryStatus getStatus() {
        return status;
    }

    public void setStatus(CategoryStatus status) {
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setUpdated() {
        this.updated = CurrentTime.currentTime();
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public Category() {
    }

    public Category(UUID id, String name, String description, Boolean active, Boolean deleted, String slug, Integer orderNumber, LocalDateTime created, LocalDateTime updated) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.active = active;
        this.deleted = deleted;
        this.slug = slug;
        this.orderNumber = orderNumber;
        this.created = created;
        this.updated = updated;
    }

    public Category(UUID id, String name, String description, Boolean active, Boolean deleted, String slug, Integer orderNumber, LocalDateTime created, LocalDateTime updated, List<Course> courses) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.active = active;
        this.deleted = deleted;
        this.slug = slug;
        this.orderNumber = orderNumber;
        this.created = created;
        this.updated = updated;
        this.courses = courses;
    }

    public Category(CategoryStatus status,String name, String description, Boolean active, Boolean deleted, String slug, Integer orderNumber, LocalDateTime created, LocalDateTime updated, List<Course> courses) {
        this.status = status;
        this.name = name;
        this.description = description;
        this.active = active;
        this.deleted = deleted;
        this.slug = slug;
        this.orderNumber = orderNumber;
        this.created = created;
        this.updated = updated;
        this.courses = courses;
    }
}
