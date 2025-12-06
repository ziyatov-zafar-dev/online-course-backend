package uz.codebyz.onlinecoursebackend.admin.course.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AdminCourseUpdateRequestDto {
    private String name;
    private String description;
    private String slug;
    private Integer orderNumber;
    private String telegramGroupLink;
    private String telegramChannelLink;
    private Boolean hasTelegramGroup;
    private Boolean hasTelegramChannel;
    @JsonIgnore
    private BigDecimal discountPrice;
    @JsonIgnore
    private Integer discountPercent;
    @JsonIgnore
    private LocalDateTime discountStartAt;
    @JsonIgnore
    private LocalDateTime discountEndAt;
    @JsonIgnore
    private MultipartFile image;
    private MultipartFile video;

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

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public MultipartFile getVideo() {
        return video;
    }

    public void setVideo(MultipartFile video) {
        this.video = video;
    }
}
