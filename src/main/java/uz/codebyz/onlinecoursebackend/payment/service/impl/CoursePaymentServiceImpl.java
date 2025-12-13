package uz.codebyz.onlinecoursebackend.payment.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.codebyz.onlinecoursebackend.course.entity.Course;
import uz.codebyz.onlinecoursebackend.course.repository.CourseRepository;
import uz.codebyz.onlinecoursebackend.payment.dto.request.CoursePaymentRequest;
import uz.codebyz.onlinecoursebackend.payment.dto.response.CoursePaymentResponse;
import uz.codebyz.onlinecoursebackend.payment.entity.CoursePayment;
import uz.codebyz.onlinecoursebackend.payment.entity.CoursePaymentStatus;
import uz.codebyz.onlinecoursebackend.payment.repository.CoursePaymentRepository;
import uz.codebyz.onlinecoursebackend.payment.service.CoursePaymentService;
import uz.codebyz.onlinecoursebackend.student.entity.Student;
import uz.codebyz.onlinecoursebackend.student.repository.StudentRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CoursePaymentServiceImpl implements CoursePaymentService {

    @Autowired
    private CoursePaymentRepository coursePaymentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Override
    @Transactional
    public CoursePaymentResponse createCoursePayment(CoursePaymentRequest request) {
        Student student = studentRepository.findById(UUID.fromString(request.getStudentId()))
                .orElseThrow(() -> new RuntimeException("O'quvchi topilmadi"));

        Course course = courseRepository.findById(UUID.fromString(request.getCourseId()))
                .orElseThrow(() -> new RuntimeException("Kurs topilmadi"));

        // Check if student already paid for this course
        if (hasPaidForCourse(student.getId(), course.getId())) {
            throw new RuntimeException("O'quvchi ushbu kurs uchun allaqachon to'lov qilgan");
        }

        CoursePayment coursePayment = new CoursePayment();
        coursePayment.setStudent(student);
        coursePayment.setCourse(course);
        coursePayment.setAmount(course.getFinalPrice() != null ? course.getFinalPrice() : course.getPrice());
        coursePayment.setStatus(CoursePaymentStatus.PENDING);

        CoursePayment savedPayment = coursePaymentRepository.save(coursePayment);
        return mapToResponse(savedPayment);
    }

    @Override
    public CoursePaymentResponse getCoursePaymentById(UUID id) {
        CoursePayment coursePayment = coursePaymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kurs to'lovi topilmadi"));
        return mapToResponse(coursePayment);
    }

    @Override
    public List<CoursePaymentResponse> getCoursePaymentsByStudentId(UUID studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("O'quvchi topilmadi"));

        return coursePaymentRepository.findByStudent(student)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CoursePaymentResponse> getCoursePaymentsByCourseId(UUID courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Kurs topilmadi"));

        return coursePaymentRepository.findByCourse(course)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CoursePaymentResponse getCoursePaymentByStudentAndCourse(UUID studentId, UUID courseId) {
        CoursePayment coursePayment = coursePaymentRepository
                .findByStudentIdAndCourseId(studentId, courseId)
                .orElseThrow(() -> new RuntimeException("Kurs to'lovi topilmadi"));
        return mapToResponse(coursePayment);
    }

    @Override
    @Transactional
    public CoursePaymentResponse updateCoursePaymentStatus(UUID id, CoursePaymentStatus status) {
        CoursePayment coursePayment = coursePaymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kurs to'lovi topilmadi"));

        coursePayment.setStatus(status);

        CoursePayment updatedPayment = coursePaymentRepository.save(coursePayment);
        return mapToResponse(updatedPayment);
    }

    @Override
    public boolean hasPaidForCourse(UUID studentId, UUID courseId) {
        return coursePaymentRepository.existsPaidPayment(studentId, courseId);
    }

    @Override
    @Transactional
    public CoursePaymentResponse refundCoursePayment(UUID id) {
        CoursePayment coursePayment = coursePaymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kurs to'lovi topilmadi"));
        coursePayment.setStatus(CoursePaymentStatus.REFUNDED);
        CoursePayment save = coursePaymentRepository.save(coursePayment);
        CoursePaymentResponse r = new CoursePaymentResponse();
        r.setId(save.getId());
        r.setStudentId(save.getStudent().getId());
        r.setCourseId(save.getCourse().getId());
        r.setCourseName(save.getCourse().getName());
        r.setAmount(save.getAmount());
        r.setStatus(save.getStatus());
        r.setPaymentId(save.getPayment().getId().toString());
        r.setCreatedAt(save.getCreatedAt());
        return r;
    }

    @Override
    public List<CoursePaymentResponse> getPaidCoursesByStudent(UUID studentId) {
        return coursePaymentRepository.findPaidCoursesByStudentId(studentId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private CoursePaymentResponse mapToResponse(CoursePayment coursePayment) {
        CoursePaymentResponse response = new CoursePaymentResponse();
        response.setId(coursePayment.getId());
        response.setStudentId(coursePayment.getStudent().getId());
        response.setStudentName(coursePayment.getStudent().getUser().getFirstname() + " " +
                coursePayment.getStudent().getUser().getLastname());
        response.setCourseId(coursePayment.getCourse().getId());
        response.setCourseName(coursePayment.getCourse().getName());
        response.setAmount(coursePayment.getAmount());
        response.setStatus(coursePayment.getStatus());

        if (coursePayment.getPayment() != null) {
            response.setPaymentId(coursePayment.getPayment().getId().toString());
        }

        response.setCreatedAt(coursePayment.getCreatedAt());
        return response;
    }
}