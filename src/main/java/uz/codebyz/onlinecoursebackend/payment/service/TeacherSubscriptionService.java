package uz.codebyz.onlinecoursebackend.payment.service;

import uz.codebyz.onlinecoursebackend.payment.dto.request.TeacherSubscriptionRequest;
import uz.codebyz.onlinecoursebackend.payment.dto.response.TeacherSubscriptionResponse;
import uz.codebyz.onlinecoursebackend.payment.entity.SubscriptionStatus;
import java.util.List;
import java.util.UUID;

public interface TeacherSubscriptionService {

    TeacherSubscriptionResponse createTeacherSubscription(TeacherSubscriptionRequest request);

    TeacherSubscriptionResponse getTeacherSubscriptionById(UUID id);

    List<TeacherSubscriptionResponse> getTeacherSubscriptionsByTeacherId(Long teacherId);

    TeacherSubscriptionResponse getActiveTeacherSubscription(Long teacherId);

    List<TeacherSubscriptionResponse> getSubscriptionsByStatus(SubscriptionStatus status);

    TeacherSubscriptionResponse updateSubscriptionStatus(UUID id, SubscriptionStatus status);

    void cancelTeacherSubscription(UUID id);

    boolean hasActiveSubscription(Long teacherId);

    void checkAndUpdateExpiredSubscriptions();
}