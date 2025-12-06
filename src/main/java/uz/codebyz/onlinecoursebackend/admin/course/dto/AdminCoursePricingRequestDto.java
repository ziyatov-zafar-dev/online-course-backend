package uz.codebyz.onlinecoursebackend.admin.course.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AdminCoursePricingRequestDto {

    // Boshlang‘ich narx (original)
    private BigDecimal price;

    // Chegirma summasi (masalan 100$ off)
    private BigDecimal discountPrice;

    // Chegirma foizi (masalan 20%)
    private Integer discountPercent;

    // Chegirma boshlanish va tugash vaqti
    private LocalDateTime discountStartAt;
    private LocalDateTime discountEndAt;

    // Yakuniy narx (price – discount), admin istasa to‘g‘ridan to‘g‘ri kiritadi
    private BigDecimal finalPrice;

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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

    public BigDecimal getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(BigDecimal finalPrice) {
        this.finalPrice = finalPrice;
    }
}
