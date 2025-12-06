package uz.codebyz.onlinecoursebackend.admin.lesson.dto;

public class AdminUpdateLessonRequestDto {
    private String name;
    private String description;
    private String slug;
    private Integer orderNumber;

    public AdminUpdateLessonRequestDto() {
    }

    public AdminUpdateLessonRequestDto(String name, String description, String slug, Integer orderNumber) {
        this.name = name;
        this.description = description;
        this.slug = slug;
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

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }
}
