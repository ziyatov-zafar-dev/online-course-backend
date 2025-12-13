package uz.codebyz.onlinecoursebackend.payment.service;

import uz.codebyz.onlinecoursebackend.payment.dto.request.CreateSubscriptionPlanRequest;
import uz.codebyz.onlinecoursebackend.payment.dto.response.SubscriptionPlanResponse;
import uz.codebyz.onlinecoursebackend.payment.entity.SubscriptionPeriod;
import java.util.List;
import java.util.UUID;

public interface SubscriptionPlanService {

    SubscriptionPlanResponse createSubscriptionPlan(CreateSubscriptionPlanRequest request);

    SubscriptionPlanResponse getSubscriptionPlanById(UUID id);

    List<SubscriptionPlanResponse> getAllSubscriptionPlans();

    List<SubscriptionPlanResponse> getActiveSubscriptionPlans();

    SubscriptionPlanResponse updateSubscriptionPlan(UUID id, CreateSubscriptionPlanRequest request);

    void deleteSubscriptionPlan(UUID id);

    SubscriptionPlanResponse toggleSubscriptionPlanStatus(UUID id, boolean active);

    SubscriptionPlanResponse getSubscriptionPlanByPeriod(SubscriptionPeriod period);

    boolean existsByPeriod(SubscriptionPeriod period);
}