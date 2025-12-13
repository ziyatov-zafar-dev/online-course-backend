package uz.codebyz.onlinecoursebackend.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.codebyz.onlinecoursebackend.course.entity.Course;
import uz.codebyz.onlinecoursebackend.payment.entity.CoursePayment;
import uz.codebyz.onlinecoursebackend.payment.entity.CoursePaymentStatus;
import uz.codebyz.onlinecoursebackend.student.entity.Student;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CoursePaymentRepository extends JpaRepository<CoursePayment, UUID> {
    List<CoursePayment> findByStudent(Student student);

    List<CoursePayment> findByCourse(Course course);

    Optional<CoursePayment> findByStudentAndCourse(Student student, Course course);

    List<CoursePayment> findByStatus(CoursePaymentStatus status);

    @Query("SELECT cp FROM CoursePayment cp WHERE cp.student.id = :studentId AND cp.course.id = :courseId")
    Optional<CoursePayment> findByStudentIdAndCourseId(@Param("studentId") UUID studentId,
                                                       @Param("courseId") UUID courseId);

    @Query("SELECT COUNT(cp) > 0 FROM CoursePayment cp WHERE cp.student.id = :studentId AND cp.course.id = :courseId AND cp.status = 'PAID'")
    boolean existsPaidPayment(@Param("studentId") UUID studentId,
                              @Param("courseId") UUID courseId);

    @Query("SELECT cp FROM CoursePayment cp WHERE cp.student.id = :studentId AND cp.status = 'PAID'")
    List<CoursePayment> findPaidCoursesByStudentId(@Param("studentId") UUID studentId);
}