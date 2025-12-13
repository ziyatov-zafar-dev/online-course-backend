package uz.codebyz.onlinecoursebackend.payment.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.codebyz.onlinecoursebackend.payment.dto.request.TeacherSubscriptionRequest;
import uz.codebyz.onlinecoursebackend.payment.dto.response.TeacherSubscriptionResponse;
import uz.codebyz.onlinecoursebackend.payment.entity.*;
import uz.codebyz.onlinecoursebackend.payment.repository.SubscriptionPlanRepository;
import uz.codebyz.onlinecoursebackend.payment.repository.TeacherSubscriptionRepository;
import uz.codebyz.onlinecoursebackend.payment.service.TeacherSubscriptionService;
import uz.codebyz.onlinecoursebackend.teacher.entity.Teacher;
import uz.codebyz.onlinecoursebackend.teacher.repository.TeacherRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TeacherSubscriptionServiceImpl implements TeacherSubscriptionService {

    @Autowired
    private TeacherSubscriptionRepository teacherSubscriptionRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;

    @Override
    @Transactional
    public TeacherSubscriptionResponse createTeacherSubscription(TeacherSubscriptionRequest request) {
        Teacher teacher = teacherRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new RuntimeException("O'qituvchi topilmadi"));

        SubscriptionPlan plan = subscriptionPlanRepository.findById(UUID.fromString(request.getSubscriptionPlanId()))
                .orElseThrow(() -> new RuntimeException("Obuna rejasi topilmadi"));

        // Check if teacher already has active subscription
        if (hasActiveSubscription(teacher.getId())) {
            throw new RuntimeException("O'qituvchida allaqachon faol obuna mavjud");
        }

        TeacherSubscription subscription = new TeacherSubscription();
        subscription.setTeacher(teacher);
        subscription.setSubscriptionPlan(plan);
        subscription.setStartDate(LocalDateTime.now());
        subscription.setEndDate(LocalDateTime.now().plusMonths(plan.getPeriod().getMonths()));
        subscription.setStatus(SubscriptionStatus.PENDING);

        TeacherSubscription savedSubscription = teacherSubscriptionRepository.save(subscription);
        return mapToResponse(savedSubscription);
    }

    @Override
    public TeacherSubscriptionResponse getTeacherSubscriptionById(UUID id) {
        TeacherSubscription subscription = teacherSubscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Obuna topilmadi"));
        return mapToResponse(subscription);
    }

    @Override
    public List<TeacherSubscriptionResponse> getTeacherSubscriptionsByTeacherId(Long teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("O'qituvchi topilmadi"));

        return teacherSubscriptionRepository.findByTeacher(teacher)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TeacherSubscriptionResponse getActiveTeacherSubscription(Long teacherId) {
        TeacherSubscription subscription = teacherSubscriptionRepository
                .findActiveSubscriptionByTeacherId(teacherId)
                .orElseThrow(() -> new RuntimeException("Faol obuna topilmadi"));
        return mapToResponse(subscription);
    }

    @Override
    public List<TeacherSubscriptionResponse> getSubscriptionsByStatus(SubscriptionStatus status) {
        return teacherSubscriptionRepository.findByStatus(status)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TeacherSubscriptionResponse updateSubscriptionStatus(UUID id, SubscriptionStatus status) {
        TeacherSubscription subscription = teacherSubscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Obuna topilmadi"));

        subscription.setStatus(status);

        TeacherSubscription updatedSubscription = teacherSubscriptionRepository.save(subscription);
        return mapToResponse(updatedSubscription);
    }

    @Override
    @Transactional
    public void cancelTeacherSubscription(UUID id) {
        TeacherSubscription subscription = teacherSubscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Obuna topilmadi"));

        subscription.setStatus(SubscriptionStatus.CANCELLED);
        teacherSubscriptionRepository.save(subscription);
    }

    @Override
    public boolean hasActiveSubscription(Long teacherId) {
        return teacherSubscriptionRepository.findActiveSubscriptionByTeacherId(teacherId).isPresent();
    }

    @Override
    @Scheduled(cron = "0 0 0 * * ?") // Run daily at midnight
    @Transactional
    public void checkAndUpdateExpiredSubscriptions() {
        LocalDateTime now = LocalDateTime.now();
        List<TeacherSubscription> expiredSubscriptions = teacherSubscriptionRepository
                .findExpiredSubscriptions(now);
        for (TeacherSubscription subscription : expiredSubscriptions) {
            subscription.setStatus(SubscriptionStatus.EXPIRED);
            teacherSubscriptionRepository.save(subscription);
        }
    }

    private TeacherSubscriptionResponse mapToResponse(TeacherSubscription subscription) {
        TeacherSubscriptionResponse response = new TeacherSubscriptionResponse();
        response.setId(subscription.getId());
        response.setTeacherId(subscription.getTeacher().getId());
        response.setTeacherName(subscription.getTeacher().getUser().getFirstname() + " " +
                subscription.getTeacher().getUser().getLastname());
        response.setSubscriptionPlanId(subscription.getSubscriptionPlan().getId().toString());
        response.setSubscriptionPlanName(subscription.getSubscriptionPlan().getNameUz());
        response.setStartDate(subscription.getStartDate());
        response.setEndDate(subscription.getEndDate());
        response.setStatus(subscription.getStatus());

        if (subscription.getPayment() != null) {
            response.setPaymentId(subscription.getPayment().getId().toString());
        }

        response.setCreatedAt(subscription.getCreatedAt());
        return response;
    }
}