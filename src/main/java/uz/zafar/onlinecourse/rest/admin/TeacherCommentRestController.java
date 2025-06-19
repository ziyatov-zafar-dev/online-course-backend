package uz.zafar.onlinecourse.rest.admin;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.zafar.onlinecourse.service.CommentService;

import java.util.UUID;

@RequestMapping("/api/teacher/comment")
@RequiredArgsConstructor
@RestController
@Tag(name = "Teacher Comment Controller", description = "Teacher paneli uchun kamentariyalar bilan ishlash API'lari")
public class TeacherCommentRestController {
    private final CommentService commentService;

    @GetMapping("get-all-comments-by-lesson-id/{lessonId}")
    public ResponseEntity<?> getAllCommentsByLessonId(@PathVariable UUID lessonId, @RequestParam Long studentId) {
        return ResponseEntity.ok(commentService.getAllCommentsByLessonId(lessonId, studentId));
    }

    @GetMapping("get-comment-by-id/{commentId}")
    public ResponseEntity<?> getCommentById(@PathVariable UUID commentId) {
        return ResponseEntity.ok(commentService.getCommentById(commentId));
    }

    @GetMapping("get-comment-counts/{lessonId}")
    public ResponseEntity<?> getCommentCountByLessonId(@PathVariable UUID lessonId) {
        return ResponseEntity.ok(commentService.getCommentCountByLessonId(lessonId));
    }
}
