package uz.codebyz.onlinecoursebackend.admin.category.dto;

import uz.codebyz.onlinecoursebackend.admin.course.dto.AdminCourseResponseDto;
import uz.codebyz.onlinecoursebackend.category.entity.CategoryStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class AdminCategoryResponseDto {
    private UUID categoryId;
    private String name;
    private String description;
    private String slug;
    private Integer orderNumber;
    private LocalDateTime created;
    private LocalDateTime updated;
    private Object status;
    private List<AdminCourseResponseDto> courses;

    public List<AdminCourseResponseDto> getCourses() {
        return courses;
    }

    public void setCourses(List<AdminCourseResponseDto> courses) {
        this.courses = courses;
    }

    public Object getStatus() {
        return status;
    }

    public AdminCategoryResponseDto() {
    }

    public AdminCategoryResponseDto(UUID categoryId, String name,
                                    String description, String slug, Integer orderNumber, LocalDateTime created, LocalDateTime updated) {
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.slug = slug;
        this.orderNumber = orderNumber;
        this.created = created;
        this.updated = updated;
    }

    public AdminCategoryResponseDto(UUID categoryId, String name, String description, String slug, Integer orderNumber, LocalDateTime created, LocalDateTime updated, Object status, List<AdminCourseResponseDto> courses) {
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.slug = slug;
        this.orderNumber = orderNumber;
        this.created = created;
        this.updated = updated;
        this.status = status;
        this.courses = courses;
    }

    public AdminCategoryResponseDto(UUID categoryId, String name, String description, String slug,
                                    Integer orderNumber, LocalDateTime created, LocalDateTime updated, CategoryStatus status) {
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.slug = slug;
        this.orderNumber = orderNumber;
        this.created = created;
        this.updated = updated;
        this.status = status;
    }



    public void setStatus(Object status) {
        this.status = status;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(UUID categoryId) {
        this.categoryId = categoryId;
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

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }
}
