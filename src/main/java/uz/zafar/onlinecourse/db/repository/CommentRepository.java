package uz.zafar.onlinecourse.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.zafar.onlinecourse.db.domain.Comment;

import java.util.List;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
    List<Comment> findAllByLessonId(UUID lessonId);

    @Query(value = "select count(c) from Comment c where c.lessonId=:lessonId")
    public Long lessonCommentCount(@Param("lessonId") UUID lessonId);
}
