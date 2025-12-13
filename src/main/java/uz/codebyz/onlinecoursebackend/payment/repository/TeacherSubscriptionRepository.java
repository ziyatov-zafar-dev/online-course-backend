package uz.codebyz.onlinecoursebackend.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.codebyz.onlinecoursebackend.payment.entity.SubscriptionStatus;
import uz.codebyz.onlinecoursebackend.payment.entity.TeacherSubscription;
import uz.codebyz.onlinecoursebackend.teacher.entity.Teacher;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TeacherSubscriptionRepository extends JpaRepository<TeacherSubscription, UUID> {
    List<TeacherSubscription> findByTeacher(Teacher teacher);

    Optional<TeacherSubscription> findByTeacherAndStatus(Teacher teacher, SubscriptionStatus status);

    List<TeacherSubscription> findByStatus(SubscriptionStatus status);

    @Query("SELECT ts FROM TeacherSubscription ts WHERE ts.status = 'ACTIVE' AND ts.endDate < :currentDate")
    List<TeacherSubscription> findExpiredSubscriptions(@Param("currentDate") LocalDateTime currentDate);

    @Query("SELECT ts FROM TeacherSubscription ts WHERE ts.teacher.id = :teacherId AND ts.status = 'ACTIVE'")
    Optional<TeacherSubscription> findActiveSubscriptionByTeacherId(@Param("teacherId") Long teacherId);

    @Query("SELECT ts FROM TeacherSubscription ts WHERE ts.teacher.id = :teacherId ORDER BY ts.createdAt DESC")
    List<TeacherSubscription> findByTeacherIdOrderByCreatedAtDesc(@Param("teacherId") Long teacherId);

    boolean existsByTeacherAndStatus(Teacher teacher, SubscriptionStatus status);
}