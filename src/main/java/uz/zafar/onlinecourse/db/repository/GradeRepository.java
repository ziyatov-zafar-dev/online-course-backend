package uz.zafar.onlinecourse.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.zafar.onlinecourse.db.domain.Grade;

import java.util.Optional;
import java.util.UUID;

public interface GradeRepository extends JpaRepository<Grade, UUID> {
    @Query("""
            SELECT g
            FROm Grade g
            WHERE g.studentId=:studentId
            AND g.lessonId=:lessonId
""")
    Optional<Grade> findByStudentIdAndLessonId(Long studentId, UUID lessonId);
}
