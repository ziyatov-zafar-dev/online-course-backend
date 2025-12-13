package uz.codebyz.onlinecoursebackend.payment.dto.request;

import jakarta.validation.constraints.*;

public class TeacherSubscriptionRequest {

    @NotNull(message = "O'qituvchi ID majburiy")
    private Long teacherId;

    @NotNull(message = "Obuna rejasi ID majburiy")
    private String subscriptionPlanId;

    @Size(max = 500, message = "Tavsif 500 belgidan oshmasligi kerak")
    private String description;

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public String getSubscriptionPlanId() {
        return subscriptionPlanId;
    }

    public void setSubscriptionPlanId(String subscriptionPlanId) {
        this.subscriptionPlanId = subscriptionPlanId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}