package uz.codebyz.onlinecoursebackend.lesson.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.codebyz.onlinecoursebackend.lesson.entity.Lesson;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LessonRepository extends JpaRepository<Lesson, UUID> {
    @Query("select l from Lesson l where (l.id=:lessonId and l.active=true and l.deleted=false)")
    Optional<Lesson> findById(@Param("lessonId") UUID lessonId);

    @Query("select l from Lesson l where (l.module.id=:moduleId and l.active=true and l.deleted=false) order by l.orderNumber")
    List<Lesson> findAllByModuleId(@Param("moduleId") UUID moduleId);

    @Query("select l from Lesson l where (l.module.id=:moduleId and l.active=true and l.deleted=false) order by l.orderNumber")
    Page<Lesson> findAllByModuleId(@Param("moduleId") UUID moduleId, Pageable pageable);

    @Query("""
            SELECT CASE WHEN COUNT(l) > 0 THEN TRUE ELSE FALSE END
              FROM Lesson l
              WHERE l.module.id = :moduleId
                AND l.id = :lessonId
                AND l.active = TRUE
                AND l.deleted = FALSE
            """)
    boolean existsByModuleIdAndLessonIdAndActiveAndDeleted(UUID moduleId, UUID lessonId);


    @Query("""
                SELECT l
                FROM Lesson l
                WHERE l.module.id = :moduleId
                  AND (
                        CASE 
                            WHEN :status = 0 THEN (l.active = TRUE AND l.deleted = FALSE)
                            WHEN :status = 1 THEN (l.active = FALSE AND l.deleted = FALSE)
                            WHEN :status = 2 THEN (l.active = FALSE AND l.deleted = TRUE)
                        END
                      ) = TRUE
            """)
    List<Lesson> getLessonsByStatusAndModuleId(@Param("status") Integer status,
                                               @Param("moduleId") UUID moduleId);

    @Query("""
                SELECT l
                FROM Lesson l
                WHERE l.module.id = :moduleId
                  AND (
                        CASE 
                            WHEN :status = 0 THEN (l.active = TRUE AND l.deleted = FALSE)
                            WHEN :status = 1 THEN (l.active = FALSE AND l.deleted = FALSE)
                            WHEN :status = 2 THEN (l.active = FALSE AND l.deleted = TRUE)
                        END
                      ) = TRUE
            """)
    Page<Lesson> getLessonsByStatusAndModuleId(@Param("status") Integer status,
                                               @Param("moduleId") UUID moduleId,
                                               Pageable pageable
    );
}
