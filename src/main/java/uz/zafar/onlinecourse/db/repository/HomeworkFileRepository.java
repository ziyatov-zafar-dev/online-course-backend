package uz.zafar.onlinecourse.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.zafar.onlinecourse.db.domain.HomeworkFile;

import java.util.List;
import java.util.UUID;

public interface HomeworkFileRepository extends JpaRepository<HomeworkFile, UUID> {
    @Query("""
            select hf
            from HomeworkFile hf
            where hf.active=true
            and hf.homework.id=:homeworkId
            """)
    public List<HomeworkFile> findAllByHomeworkId(UUID homeworkId);
}
