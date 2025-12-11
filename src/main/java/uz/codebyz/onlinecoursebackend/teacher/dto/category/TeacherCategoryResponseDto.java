package uz.codebyz.onlinecoursebackend.teacher.dto.category;

import io.swagger.v3.oas.annotations.media.Schema;
import uz.codebyz.onlinecoursebackend.admin.course.dto.AdminCourseResponseDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TeacherCategoryResponseDto {
    private UUID categoryId;
    private String name;
    private String description;
    private String slug;
    private Integer orderNumber;
    private String created;
    private String updated;
    @Schema(description = "statusi")
    private Map<String, String> status;
//    private List<AdminCourseResponseDto> courses;


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

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public Map<String, String> getStatus() {
        return status;
    }

    public void setStatus(Map<String, String> status) {
        this.status = status;
    }

}
