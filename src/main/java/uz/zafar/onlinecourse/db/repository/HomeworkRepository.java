package uz.zafar.onlinecourse.db.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.zafar.onlinecourse.db.domain.Homework;
import uz.zafar.onlinecourse.db.domain.HomeworkFile;

import java.util.List;
import java.util.UUID;

public interface HomeworkRepository extends JpaRepository<Homework, UUID> {
    @Query("""
            select hf
            from HomeworkFile hf
            where hf.homework.id=:homeworkId
                        and hf.active=true
            """)
     List<HomeworkFile> getHomeworkFiles(@Param("homeworkId") UUID homeworkId);
    @Query("""
            select h
            from Homework h
            where h.active=true and h.lesson.id=:lessonId
            """)
    Page<Homework> findAllByLessonId(Pageable pageable,@Param("lessonId") UUID lessonId);
    @Query("""
            SELECT h
            FROM Homework h
            WHERE h.lesson.id=:lessonId
                        AND h.deadline>=CURRENT_TIMESTAMP
                        AND h.active=true
            """)
    List<Homework>getAllHomeworksByLessonId(UUID lessonId);
}
