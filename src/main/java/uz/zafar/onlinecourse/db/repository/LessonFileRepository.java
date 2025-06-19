package uz.zafar.onlinecourse.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.zafar.onlinecourse.db.domain.LessonFile;
import uz.zafar.onlinecourse.dto.lesson_dto.res.LessonFileAndTypeDto;

import java.util.List;
import java.util.UUID;

public interface LessonFileRepository extends JpaRepository<LessonFile, UUID> {
    @Query(value = """
            select lf.file_url as fileUrl,
                   lf.file_name as fileName,
                   t.name as typeName,
                   t.id as typeId,
                   extract(year from lf.created) as createdYear,
                   extract(month from lf.created) as createdMonth,
                   extract(day from lf.created) as createdDay,
                   extract(hour from lf.created) as createdHour,
                   extract(minute from lf.created) as createdMinute,
                   round(extract(second from lf.created)) as createdSecond
            from "lesson-files" lf
            inner join lessons l
                on l.id=lf.lesson_id
            inner join types t
                on t.id=lf.type_id
            where l.id=:lesson_id
            """,nativeQuery = true)
    List<LessonFileAndTypeDto> getFilesFromLesson(@Param("lesson_id") UUID lessonId);

    @Query("select lf from LessonFile lf where lf.lesson.id=:lesson_id")
    List<LessonFile> getAllLessonFilesByLessonId(@Param("lesson_id") UUID lessonId);
}
