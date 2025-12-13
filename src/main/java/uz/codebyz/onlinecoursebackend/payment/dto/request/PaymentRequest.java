package uz.codebyz.onlinecoursebackend.payment.dto.request;

import jakarta.validation.constraints.*;
import uz.codebyz.onlinecoursebackend.payment.entity.PaymentProvider;
import java.math.BigDecimal;

public class PaymentRequest {

    @NotNull(message = "Provider majburiy")
    private PaymentProvider provider;

    @NotNull(message = "Miqdor majburiy")
    @DecimalMin(value = "0.01", message = "Miqdor 0.01 dan katta bo'lishi kerak")
    private BigDecimal amount;

    @Size(max = 3, message = "Valyuta kod 3 belgidan oshmasligi kerak")
    private String currency;

    @Size(max = 500, message = "Tavsif 500 belgidan oshmasligi kerak")
    private String description;

    private String teacherSubscriptionId;

    private String coursePaymentId;

    public PaymentProvider getProvider() {
        return provider;
    }

    public void setProvider(PaymentProvider provider) {
        this.provider = provider;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTeacherSubscriptionId() {
        return teacherSubscriptionId;
    }

    public void setTeacherSubscriptionId(String teacherSubscriptionId) {
        this.teacherSubscriptionId = teacherSubscriptionId;
    }

    public String getCoursePaymentId() {
        return coursePaymentId;
    }

    public void setCoursePaymentId(String coursePaymentId) {
        this.coursePaymentId = coursePaymentId;
    }
}