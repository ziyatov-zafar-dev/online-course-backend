package uz.zafar.onlinecourse.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.zafar.onlinecourse.db.domain.Course;
import uz.zafar.onlinecourse.db.domain.Teacher;
import uz.zafar.onlinecourse.db.domain.TeacherCourse;
import uz.zafar.onlinecourse.dto.course_dto.res.CourseDtoResponse;

import java.util.List;
import java.util.UUID;

public interface TeacherCourseRepository extends JpaRepository<TeacherCourse, UUID> {


    List<TeacherCourse> findAllByCourseId(UUID courseId);

    @Query(
            nativeQuery = true,
            value = """
                    SELECT 
                        c.id AS pkey,
                        c.name,
                        c.description,
                        c.telegram_channel AS telegramChannel,
                        c.has_telegram_channel AS hasTelegramChannel,
                        c.created AS createdAt,
                        c.updated AS updatedAt
                    FROM 
                        teacher_courses tc
                    INNER JOIN 
                        teachers t ON t.id = tc.teacher_id
                    INNER JOIN 
                        courses c ON c.id = tc.course_id
                    WHERE 
                        tc.active = true
                        AND t.id = :teacherId
                        AND c.active = true
                    ORDER BY createdAt"""
    )
    List<CourseDtoResponse> getTeacherCourses(Long teacherId);

    @Query(
            nativeQuery = true,
            value = """
                    SELECT 
                        c.id AS pkey,
                        c.deleted_name as name,
                        c.description,
                        c.telegram_channel AS telegramChannel,
                        c.has_telegram_channel AS hasTelegramChannel,
                        c.created AS createdAt,
                        c.updated AS updatedAt
                    FROM 
                        teacher_courses tc
                    INNER JOIN 
                        teachers t ON t.id = tc.teacher_id
                    INNER JOIN 
                        courses c ON c.id = tc.course_id
                    WHERE 
                        tc.active = true
                        AND t.id = :teacherId
                        AND c.active = false
                    ORDER BY createdAt"""
    )
    List<CourseDtoResponse> getTeacherDeletedCourses(Long teacherId);

    @Query(
            value = """
                    SELECT t.*
                    from teacher_courses  tc 
                    inner join courses c on tc.course_id=c.id
                    inner join teachers t on t.id=tc.teacher_id
                    where c.id=:courseId   and tc.active=true                
                    """, nativeQuery = true
    )
    Teacher getCourseTeacher(@Param("courseId") UUID courseId);


}
