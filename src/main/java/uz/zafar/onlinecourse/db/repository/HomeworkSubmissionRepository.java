package uz.zafar.onlinecourse.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.zafar.onlinecourse.db.domain.HomeworkSubmission;

import java.util.UUID;

public interface HomeworkSubmissionRepository extends JpaRepository<HomeworkSubmission, UUID> {
    @Query("""
            SELECT hs
            FROM HomeworkSubmission hs
            WHERE hs.active=true and
            hs.studentId=:studentId and hs.homeworkId=:homeworkId
            """)
    HomeworkSubmission findByStudentAndHomework(Long studentId, UUID homeworkId);


    @Query("""
            SELECT hs
            from HomeworkSubmission hs
            where hs.homeworkId=:homeworkId
            and hs.studentId=:studentId
            and hs.active=true
""")
    HomeworkSubmission findByHomerworkSubmissionHomeworkIdAndStudentId(
            @Param("homeworkId") UUID homeworkId,
            @Param("studentId") Long studentId
    );
}
