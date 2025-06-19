package uz.zafar.onlinecourse.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.zafar.onlinecourse.db.domain.Like;

import java.util.Optional;
import java.util.UUID;

public interface LikeRepository extends JpaRepository<Like, UUID> {
    @Query("""
            select count(l)
            from Like l
            where l.lessonId=:lessonId
            """)
    Long getLikeCountByLesson(@Param("lessonId") UUID lessonId);

    Optional<Like> findByStudentIdAndLessonId(Long studentId, UUID lessonId);

    @Query(value = """
            WITH liked_data AS (
                SELECT * 
                FROM likes 
                WHERE lesson_id = :lessonId AND student_id = :studentId
            )
            SELECT CASE
                        WHEN count(*)>0  THEN true
                        ELSE false
            END AS like_status
            FROM liked_data
            """, nativeQuery = true)
    Boolean findByLessonIdAndStudentId(@Param("lessonId") UUID lessonId,
                                       @Param("studentId") Long studentId);
}
