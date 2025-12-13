package uz.codebyz.onlinecoursebackend.payment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.codebyz.onlinecoursebackend.payment.entity.Payment;
import uz.codebyz.onlinecoursebackend.payment.entity.PaymentProvider;
import uz.codebyz.onlinecoursebackend.payment.entity.PaymentStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    Optional<Payment> findByTransactionId(String transactionId);

    Optional<Payment> findByProviderTransactionId(String providerTransactionId);

    Page<Payment> findByStatus(PaymentStatus status, Pageable pageable);


    //shuyerda kamchilik bolishi mumkin

//    List<Payment> findByProviderAndStatus(PaymentProvider provider, PaymentStatus status);
    List<Payment> findByProviderAndStatus(String provider, PaymentStatus status);

    @Query("SELECT p FROM Payment p WHERE p.createdAt BETWEEN :startDate AND :endDate")
    List<Payment> findPaymentsBetweenDates(@Param("startDate") LocalDateTime startDate,
                                           @Param("endDate") LocalDateTime endDate);

    @Query("SELECT p FROM Payment p WHERE p.teacherSubscription.id = :subscriptionId")
    Optional<Payment> findByTeacherSubscriptionId(@Param("subscriptionId") UUID subscriptionId);

    @Query("SELECT p FROM Payment p WHERE p.coursePayment.id = :coursePaymentId")
    Optional<Payment> findByCoursePaymentId(@Param("coursePaymentId") UUID coursePaymentId);

    boolean existsByTransactionId(String transactionId);
}