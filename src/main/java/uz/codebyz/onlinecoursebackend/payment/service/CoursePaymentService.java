package uz.codebyz.onlinecoursebackend.payment.service;

import uz.codebyz.onlinecoursebackend.payment.dto.request.CoursePaymentRequest;
import uz.codebyz.onlinecoursebackend.payment.dto.response.CoursePaymentResponse;
import uz.codebyz.onlinecoursebackend.payment.entity.CoursePayment;
import uz.codebyz.onlinecoursebackend.payment.entity.CoursePaymentStatus;
import java.util.List;
import java.util.UUID;

public interface CoursePaymentService {

    CoursePaymentResponse createCoursePayment(CoursePaymentRequest request);

    CoursePaymentResponse getCoursePaymentById(UUID id);

    List<CoursePaymentResponse> getCoursePaymentsByStudentId(UUID studentId);

    List<CoursePaymentResponse> getCoursePaymentsByCourseId(UUID courseId);

    CoursePaymentResponse getCoursePaymentByStudentAndCourse(UUID studentId, UUID courseId);

    CoursePaymentResponse updateCoursePaymentStatus(UUID id, CoursePaymentStatus status);

    boolean hasPaidForCourse(UUID studentId, UUID courseId);

    CoursePaymentResponse refundCoursePayment(UUID id);

    List<CoursePaymentResponse> getPaidCoursesByStudent(UUID studentId);
}