package uz.codebyz.onlinecoursebackend.payment.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uz.codebyz.onlinecoursebackend.payment.dto.request.PaymentRequest;
import uz.codebyz.onlinecoursebackend.payment.dto.response.PaymentResponse;
import uz.codebyz.onlinecoursebackend.payment.entity.PaymentStatus;
import java.util.UUID;

public interface PaymentService {

    PaymentResponse createPayment(PaymentRequest request);

    PaymentResponse getPaymentById(UUID id);

    PaymentResponse getPaymentByTransactionId(String transactionId);

    Page<PaymentResponse> getAllPayments(Pageable pageable);

    Page<PaymentResponse> getPaymentsByStatus(PaymentStatus status, Pageable pageable);

    PaymentResponse updatePaymentStatus(UUID id, PaymentStatus status, String providerTransactionId);

    PaymentResponse refundPayment(UUID id);

    boolean verifyPayment(String transactionId);
}