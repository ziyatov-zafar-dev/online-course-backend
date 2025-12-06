package uz.codebyz.onlinecoursebackend.admin.category.dto;

public class AdminCategoryCreateRequest {
    private String name;
    private String description;
    private String slug;
    private int orderNumber;

    public AdminCategoryCreateRequest() {
    }

    public AdminCategoryCreateRequest(String name, String description, String slug, int orderNumber) {
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

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }
}
