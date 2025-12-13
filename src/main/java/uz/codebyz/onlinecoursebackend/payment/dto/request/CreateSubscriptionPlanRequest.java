package uz.codebyz.onlinecoursebackend.payment.dto.request;

import jakarta.validation.constraints.*;
import uz.codebyz.onlinecoursebackend.payment.entity.SubscriptionPeriod;
import java.math.BigDecimal;

public class CreateSubscriptionPlanRequest {

    @NotNull(message = "Davr majburiy")
    private SubscriptionPeriod period;

    @NotNull(message = "Narx majburiy")
    @DecimalMin(value = "0.01", message = "Narx 0.01 dan katta bo'lishi kerak")
    private BigDecimal price;

    @NotBlank(message = "O'zbekcha nom majburiy")
    @Size(min = 2, max = 100, message = "O'zbekcha nom 2-100 belgi oralig'ida bo'lishi kerak")
    private String nameUz;

    @NotBlank(message = "Inglizcha nom majburiy")
    @Size(min = 2, max = 100, message = "Inglizcha nom 2-100 belgi oralig'ida bo'lishi kerak")
    private String nameEn;

    @Size(max = 1000, message = "O'zbekcha tavsif 1000 belgidan oshmasligi kerak")
    private String descriptionUz;

    @Size(max = 1000, message = "Inglizcha tavsif 1000 belgidan oshmasligi kerak")
    private String descriptionEn;

    private boolean active = true;

    public SubscriptionPeriod getPeriod() {
        return period;
    }

    public void setPeriod(SubscriptionPeriod period) {
        this.period = period;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getNameUz() {
        return nameUz;
    }

    public void setNameUz(String nameUz) {
        this.nameUz = nameUz;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getDescriptionUz() {
        return descriptionUz;
    }

    public void setDescriptionUz(String descriptionUz) {
        this.descriptionUz = descriptionUz;
    }

    public String getDescriptionEn() {
        return descriptionEn;
    }

    public void setDescriptionEn(String descriptionEn) {
        this.descriptionEn = descriptionEn;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}