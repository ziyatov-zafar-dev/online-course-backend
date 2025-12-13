package uz.codebyz.onlinecoursebackend.payment.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.codebyz.onlinecoursebackend.payment.dto.request.CreateSubscriptionPlanRequest;
import uz.codebyz.onlinecoursebackend.payment.dto.response.SubscriptionPlanResponse;
import uz.codebyz.onlinecoursebackend.payment.entity.SubscriptionPeriod;
import uz.codebyz.onlinecoursebackend.payment.entity.SubscriptionPlan;
import uz.codebyz.onlinecoursebackend.payment.repository.SubscriptionPlanRepository;
import uz.codebyz.onlinecoursebackend.payment.service.SubscriptionPlanService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SubscriptionPlanServiceImpl implements SubscriptionPlanService {

    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;

    @Override
    @Transactional
    public SubscriptionPlanResponse createSubscriptionPlan(CreateSubscriptionPlanRequest request) {
        // Check if plan with this period already exists
        if (subscriptionPlanRepository.existsByPeriodAndActiveTrue(request.getPeriod())) {
            throw new RuntimeException("Bu davr uchun obuna rejasi allaqachon mavjud");
        }

        SubscriptionPlan plan = new SubscriptionPlan();
        plan.setPeriod(request.getPeriod());
        plan.setPrice(request.getPrice());
        plan.setNameUz(request.getNameUz());
        plan.setNameEn(request.getNameEn());
        plan.setDescriptionUz(request.getDescriptionUz());
        plan.setDescriptionEn(request.getDescriptionEn());
        plan.setActive(request.isActive());

        SubscriptionPlan savedPlan = subscriptionPlanRepository.save(plan);
        return mapToResponse(savedPlan);
    }

    @Override
    public SubscriptionPlanResponse getSubscriptionPlanById(UUID id) {
        SubscriptionPlan plan = subscriptionPlanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Obuna rejasi topilmadi"));
        return mapToResponse(plan);
    }

    @Override
    public List<SubscriptionPlanResponse> getAllSubscriptionPlans() {
        return subscriptionPlanRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<SubscriptionPlanResponse> getActiveSubscriptionPlans() {
        return subscriptionPlanRepository.findByActiveTrue()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SubscriptionPlanResponse updateSubscriptionPlan(UUID id, CreateSubscriptionPlanRequest request) {
        SubscriptionPlan plan = subscriptionPlanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Obuna rejasi topilmadi"));

        // Check if another active plan with this period exists
        if (!plan.getPeriod().equals(request.getPeriod())) {
            Optional<SubscriptionPlan> existingPlan = subscriptionPlanRepository
                    .findByPeriodAndActiveTrue(request.getPeriod());
            if (existingPlan.isPresent() && !existingPlan.get().getId().equals(id)) {
                throw new RuntimeException("Bu davr uchun obuna rejasi allaqachon mavjud");
            }
        }

        plan.setPeriod(request.getPeriod());
        plan.setPrice(request.getPrice());
        plan.setNameUz(request.getNameUz());
        plan.setNameEn(request.getNameEn());
        plan.setDescriptionUz(request.getDescriptionUz());
        plan.setDescriptionEn(request.getDescriptionEn());
        plan.setActive(request.isActive());

        SubscriptionPlan updatedPlan = subscriptionPlanRepository.save(plan);
        return mapToResponse(updatedPlan);
    }

    @Override
    @Transactional
    public void deleteSubscriptionPlan(UUID id) {
        SubscriptionPlan plan = subscriptionPlanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Obuna rejasi topilmadi"));

        // Don't delete if there are active subscriptions using this plan
        // You can add this check if needed

        subscriptionPlanRepository.delete(plan);
    }

    @Override
    @Transactional
    public SubscriptionPlanResponse toggleSubscriptionPlanStatus(UUID id, boolean active) {
        SubscriptionPlan plan = subscriptionPlanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Obuna rejasi topilmadi"));

        plan.setActive(active);

        SubscriptionPlan updatedPlan = subscriptionPlanRepository.save(plan);
        return mapToResponse(updatedPlan);
    }

    @Override
    public SubscriptionPlanResponse getSubscriptionPlanByPeriod(SubscriptionPeriod period) {
        SubscriptionPlan plan = subscriptionPlanRepository.findByPeriodAndActiveTrue(period)
                .orElseThrow(() -> new RuntimeException("Obuna rejasi topilmadi"));
        return mapToResponse(plan);
    }

    @Override
    public boolean existsByPeriod(SubscriptionPeriod period) {
        return subscriptionPlanRepository.existsByPeriodAndActiveTrue(period);
    }

    private SubscriptionPlanResponse mapToResponse(SubscriptionPlan plan) {
        SubscriptionPlanResponse response = new SubscriptionPlanResponse();
        response.setId(plan.getId());
        response.setPeriod(plan.getPeriod());
        response.setPrice(plan.getPrice());
        response.setNameUz(plan.getNameUz());
        response.setNameEn(plan.getNameEn());
        response.setDescriptionUz(plan.getDescriptionUz());
        response.setDescriptionEn(plan.getDescriptionEn());
        response.setActive(plan.isActive());
        response.setCreatedAt(plan.getCreatedAt());
        response.setUpdatedAt(plan.getUpdatedAt());
        return response;
    }
}