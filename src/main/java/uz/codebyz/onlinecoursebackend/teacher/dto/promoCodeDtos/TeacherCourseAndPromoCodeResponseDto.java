package uz.codebyz.onlinecoursebackend.teacher.dto.promoCodeDtos;

import uz.codebyz.onlinecoursebackend.admin.course.dto.AdminCourseResponseDto;
import uz.codebyz.onlinecoursebackend.auth.dto.UserResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class TeacherCourseAndPromoCodeResponseDto {
    private UUID promoCodeId;
    private AdminCourseResponseDto course;
    private UserResponse user;
    private String code;
    private Integer discountPercent;
    private BigDecimal fixedAmount;
    private Integer maxUsage;
    private Integer userCount;
    private LocalDateTime validFrom;
    private LocalDateTime validUntil;
    private Boolean active;
    private LocalDateTime created;
    private LocalDateTime updated;

    public UUID getPromoCodeId() {
        return promoCodeId;
    }

    public void setPromoCodeId(UUID promoCodeId) {
        this.promoCodeId = promoCodeId;
    }

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(Integer discountPercent) {
        this.discountPercent = discountPercent;
    }

    public BigDecimal getFixedAmount() {
        return fixedAmount;
    }

    public void setFixedAmount(BigDecimal fixedAmount) {
        this.fixedAmount = fixedAmount;
    }

    public Integer getMaxUsage() {
        return maxUsage;
    }

    public void setMaxUsage(Integer maxUsage) {
        this.maxUsage = maxUsage;
    }

    public Integer getUserCount() {
        return userCount;
    }

    public void setUserCount(Integer userCount) {
        this.userCount = userCount;
    }

    public LocalDateTime getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDateTime validFrom) {
        this.validFrom = validFrom;
    }

    public LocalDateTime getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(LocalDateTime validUntil) {
        this.validUntil = validUntil;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
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

    public AdminCourseResponseDto getCourse() {
        return course;
    }

    public void setCourse(AdminCourseResponseDto course) {
        this.course = course;
    }
}
