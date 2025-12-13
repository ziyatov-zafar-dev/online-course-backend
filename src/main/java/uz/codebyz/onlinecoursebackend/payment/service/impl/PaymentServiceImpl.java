package uz.codebyz.onlinecoursebackend.payment.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.codebyz.onlinecoursebackend.course.entity.Course;
import uz.codebyz.onlinecoursebackend.course.repository.CourseRepository;
import uz.codebyz.onlinecoursebackend.payment.dto.request.PaymentRequest;
import uz.codebyz.onlinecoursebackend.payment.dto.response.PaymentResponse;
import uz.codebyz.onlinecoursebackend.payment.entity.*;
import uz.codebyz.onlinecoursebackend.payment.repository.CoursePaymentRepository;
import uz.codebyz.onlinecoursebackend.payment.repository.PaymentRepository;
import uz.codebyz.onlinecoursebackend.payment.repository.SubscriptionPlanRepository;
import uz.codebyz.onlinecoursebackend.payment.repository.TeacherSubscriptionRepository;
import uz.codebyz.onlinecoursebackend.payment.service.PaymentService;
import uz.codebyz.onlinecoursebackend.student.entity.Student;
import uz.codebyz.onlinecoursebackend.student.repository.StudentRepository;
import uz.codebyz.onlinecoursebackend.teacher.entity.Teacher;
import uz.codebyz.onlinecoursebackend.teacher.repository.TeacherRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private TeacherSubscriptionRepository teacherSubscriptionRepository;

    @Autowired
    private CoursePaymentRepository coursePaymentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;

    @Override
    @Transactional
    public PaymentResponse createPayment(PaymentRequest request) {
        Payment payment = new Payment();
        payment.setProvider(request.getProvider());
        payment.setAmount(request.getAmount());
        payment.setCurrency(request.getCurrency() != null ? request.getCurrency() : "UZS");
        payment.setDescription(request.getDescription());
        payment.setStatus(PaymentStatus.PENDING);

        // Link teacher subscription if provided
        if (request.getTeacherSubscriptionId() != null) {
            TeacherSubscription subscription = teacherSubscriptionRepository
                    .findById(UUID.fromString(request.getTeacherSubscriptionId()))
                    .orElseThrow(() -> new RuntimeException("Obuna topilmadi"));
            payment.setTeacherSubscription(subscription);
        }

        // Link course payment if provided
        if (request.getCoursePaymentId() != null) {
            CoursePayment coursePayment = coursePaymentRepository
                    .findById(UUID.fromString(request.getCoursePaymentId()))
                    .orElseThrow(() -> new RuntimeException("Kurs to'lovi topilmadi"));
            payment.setCoursePayment(coursePayment);
        }

        Payment savedPayment = paymentRepository.save(payment);
        return mapToResponse(savedPayment);
    }

    @Override
    public PaymentResponse getPaymentById(UUID id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("To'lov topilmadi"));
        return mapToResponse(payment);
    }

    @Override
    public PaymentResponse getPaymentByTransactionId(String transactionId) {
        Payment payment = paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new RuntimeException("To'lov topilmadi"));
        return mapToResponse(payment);
    }

    @Override
    public Page<PaymentResponse> getAllPayments(Pageable pageable) {
        return paymentRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    @Override
    public Page<PaymentResponse> getPaymentsByStatus(PaymentStatus status, Pageable pageable) {
        return paymentRepository.findByStatus(status, pageable)
                .map(this::mapToResponse);
    }

    @Override
    @Transactional
    public PaymentResponse updatePaymentStatus(UUID id, PaymentStatus status, String providerTransactionId) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("To'lov topilmadi"));

        payment.setStatus(status);
        if (providerTransactionId != null) {
            payment.setProviderTransactionId(providerTransactionId);
        }

        // If payment is successful, update related entities
        if (status == PaymentStatus.SUCCESS) {
            updateRelatedEntities(payment);
        }

        Payment updatedPayment = paymentRepository.save(payment);
        return mapToResponse(updatedPayment);
    }

    @Override
    @Transactional
    public PaymentResponse refundPayment(UUID id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("To'lov topilmadi"));

        // Check if payment can be refunded
        if (payment.getStatus() != PaymentStatus.SUCCESS) {
            throw new RuntimeException("Faqat muvaffaqiyatli to'lovlar qaytarilishi mumkin");
        }

        payment.setStatus(PaymentStatus.REFUNDED);

        // Update related entities
        if (payment.getTeacherSubscription() != null) {
            TeacherSubscription subscription = payment.getTeacherSubscription();
            subscription.setStatus(SubscriptionStatus.CANCELLED);
            teacherSubscriptionRepository.save(subscription);
        }

        if (payment.getCoursePayment() != null) {
            CoursePayment coursePayment = payment.getCoursePayment();
            coursePayment.setStatus(CoursePaymentStatus.REFUNDED);
            coursePaymentRepository.save(coursePayment);
        }

        Payment refundedPayment = paymentRepository.save(payment);
        return mapToResponse(refundedPayment);
    }

    @Override
    public boolean verifyPayment(String transactionId) {
        Optional<Payment> paymentOpt = paymentRepository.findByTransactionId(transactionId);
        if (paymentOpt.isEmpty()) {
            return false;
        }

        Payment payment = paymentOpt.get();
        return payment.getStatus() == PaymentStatus.SUCCESS;
    }

    private void updateRelatedEntities(Payment payment) {
        if (payment.getTeacherSubscription() != null) {
            TeacherSubscription subscription = payment.getTeacherSubscription();
            subscription.setStatus(SubscriptionStatus.ACTIVE);
            subscription.setStartDate(LocalDateTime.now());

            // Calculate end date based on subscription plan
            SubscriptionPlan plan = subscription.getSubscriptionPlan();
            subscription.setEndDate(LocalDateTime.now().plusMonths(plan.getPeriod().getMonths()));

            teacherSubscriptionRepository.save(subscription);
        }

        if (payment.getCoursePayment() != null) {
            CoursePayment coursePayment = payment.getCoursePayment();
            coursePayment.setStatus(CoursePaymentStatus.PAID);
            coursePaymentRepository.save(coursePayment);
        }
    }

    private PaymentResponse mapToResponse(Payment payment) {
        PaymentResponse response = new PaymentResponse();
        response.setId(payment.getId());
        response.setTransactionId(payment.getTransactionId());
        response.setProvider(payment.getProvider());
        response.setStatus(payment.getStatus());
        response.setAmount(payment.getAmount());
        response.setCurrency(payment.getCurrency());
        response.setDescription(payment.getDescription());
        response.setProviderTransactionId(payment.getProviderTransactionId());

        if (payment.getTeacherSubscription() != null) {
            response.setTeacherSubscriptionId(payment.getTeacherSubscription().getId().toString());
        }

        if (payment.getCoursePayment() != null) {
            response.setCoursePaymentId(payment.getCoursePayment().getId().toString());
        }

        response.setCreatedAt(payment.getCreatedAt());
        response.setUpdatedAt(payment.getUpdatedAt());

        return response;
    }
}