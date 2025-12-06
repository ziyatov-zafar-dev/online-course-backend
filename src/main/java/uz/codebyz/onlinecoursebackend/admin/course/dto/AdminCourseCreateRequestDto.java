package uz.codebyz.onlinecoursebackend.admin.course.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class AdminCourseCreateRequestDto {
    @Schema(description = "Kurs nomi")
    private String name;
    @Schema(description = "Kurs tavsifi")
    private String description;
    @Schema(description = "Kurs slug")
    private String slug;
    @Schema(description = "tartib raqami")
    private Integer orderNumber;
    @Schema(description = "telegram guruh silkasi")
    private String telegramGroupLink;
    @Schema(description = "telegram kanal silkasi")
    private String telegramChannelLink;
    @Schema(description = "telegram guruhi bo'lsa true aks holda false")
    private Boolean hasTelegramGroup;
    @Schema(description = "telegram kanali bo'lsa true akh holda false")
    private Boolean hasTelegramChannel;
    private BigDecimal discountPrice;
    private Integer discountPercent;

    private Long teacherId;

    public Long getTeacherId() {


        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    private LocalDateTime discountStartAt;
    private LocalDateTime discountEndAt;
    private UUID categoryId;
    private BigDecimal price;
    private BigDecimal finalPrice;

    public UUID getCategoryId() {
        return categoryId;
    }

    public BigDecimal getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(BigDecimal finalPrice) {
        this.finalPrice = finalPrice;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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

    public String getTelegramGroupLink() {
        return telegramGroupLink;
    }

    public void setTelegramGroupLink(String telegramGroupLink) {
        this.telegramGroupLink = telegramGroupLink;
    }

    public String getTelegramChannelLink() {
        return telegramChannelLink;
    }

    public void setTelegramChannelLink(String telegramChannelLink) {
        this.telegramChannelLink = telegramChannelLink;
    }

    public Boolean getHasTelegramGroup() {
        return hasTelegramGroup;
    }

    public void setHasTelegramGroup(Boolean hasTelegramGroup) {
        this.hasTelegramGroup = hasTelegramGroup;
    }

    public Boolean getHasTelegramChannel() {
        return hasTelegramChannel;
    }

    public void setHasTelegramChannel(Boolean hasTelegramChannel) {
        this.hasTelegramChannel = hasTelegramChannel;
    }

    public BigDecimal getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(BigDecimal discountPrice) {
        this.discountPrice = discountPrice;
    }

    public Integer getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(Integer discountPercent) {
        this.discountPercent = discountPercent;
    }

    public LocalDateTime getDiscountStartAt() {
        return discountStartAt;
    }

    public void setDiscountStartAt(LocalDateTime discountStartAt) {
        this.discountStartAt = discountStartAt;
    }

    public LocalDateTime getDiscountEndAt() {
        return discountEndAt;
    }

    public void setDiscountEndAt(LocalDateTime discountEndAt) {
        this.discountEndAt = discountEndAt;
    }
}
