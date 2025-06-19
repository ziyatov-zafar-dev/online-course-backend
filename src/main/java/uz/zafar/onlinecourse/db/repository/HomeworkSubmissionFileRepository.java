package uz.zafar.onlinecourse.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.zafar.onlinecourse.db.domain.HomeworkSubmissionFile;

import java.util.List;
import java.util.UUID;

public interface HomeworkSubmissionFileRepository extends JpaRepository<HomeworkSubmissionFile, UUID> {

    @Query(value = """
            
            select *\s
                                                      from "homework-submission-files"  hsb
                                                      where hsb.homework_submission_id=:homeworkSubmissionId
                                                            and hsb.active=true            """, nativeQuery = true)
    List<HomeworkSubmissionFile> getHomeworkFiles(@Param("homeworkSubmissionId") UUID homeworkSubmissionId);

}
