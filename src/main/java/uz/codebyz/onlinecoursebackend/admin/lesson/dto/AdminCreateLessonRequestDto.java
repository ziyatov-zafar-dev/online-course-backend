package uz.codebyz.onlinecoursebackend.admin.lesson.dto;

import uz.codebyz.onlinecoursebackend.lesson.entity.Lesson;
import uz.codebyz.onlinecoursebackend.module.entity.Module;

import java.util.UUID;

public class AdminCreateLessonRequestDto {
    private String name;
    private String description;
    private String slug;
    private UUID moduleId;
    private Integer orderNumber;

    public AdminCreateLessonRequestDto() {
    }

    public AdminCreateLessonRequestDto(String name, String description, String slug, UUID moduleId, Integer orderNumber) {
        this.name = name;
        this.description = description;
        this.slug = slug;
        this.moduleId = moduleId;
        this.orderNumber = orderNumber;
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

    public UUID getModuleId() {
        return moduleId;
    }

    public void setModuleId(UUID moduleId) {
        this.moduleId = moduleId;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }
}
