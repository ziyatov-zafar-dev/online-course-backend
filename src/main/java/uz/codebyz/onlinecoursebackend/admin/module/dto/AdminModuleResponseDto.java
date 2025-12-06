package uz.codebyz.onlinecoursebackend.admin.module.dto;

import uz.codebyz.onlinecoursebackend.admin.lesson.dto.AdminLessonResponseDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class AdminModuleResponseDto {
    private UUID id;
    private String name;
    private String description;
    private String slug;
    private UUID courseId;
    private Integer orderNumber;
    private LocalDateTime created;
    private LocalDateTime updated;
    private List<AdminLessonResponseDto>  lessons;

    public UUID getCourseId() {
        return courseId;
    }

    public AdminModuleResponseDto(UUID id, String name, String description, String slug, UUID courseId, Integer orderNumber, LocalDateTime created, LocalDateTime updated, List<AdminLessonResponseDto> lessons) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.slug = slug;
        this.courseId = courseId;
        this.orderNumber = orderNumber;
        this.created = created;
        this.updated = updated;
        this.lessons = lessons;
    }

    public void setCourseId(UUID courseId) {
        this.courseId = courseId;
    }

    public List<AdminLessonResponseDto> getLessons() {
        return lessons;
    }

    public void setLessons(List<AdminLessonResponseDto> lessons) {
        this.lessons = lessons;
    }

    public AdminModuleResponseDto() {
    }

    public AdminModuleResponseDto(UUID id, String name, String description, String slug, UUID courseId, Integer orderNumber, LocalDateTime created, LocalDateTime updated) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.slug = slug;
        this.courseId = courseId;
        this.orderNumber = orderNumber;
        this.created = created;
        this.updated = updated;
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

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public UUID getCourse() {
        return courseId;
    }

    public void setCourse(UUID course) {
        this.courseId = course;
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
