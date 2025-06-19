package uz.zafar.onlinecourse.rest.student;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.zafar.onlinecourse.dto.comment_dto.req.AddCommentDto;
import uz.zafar.onlinecourse.service.CommentService;

import java.util.UUID;

@RequestMapping("/api/student/comment")
@RequiredArgsConstructor
@RestController
@Tag(name = "Student Comment Controller", description = "Student paneli uchun kamentariyalar bilan ishlash API'lari")
public class StudentCommentRestController {
    private final CommentService commentService;

    @PostMapping("add-comment")
    public ResponseEntity<?> addComment(@RequestBody AddCommentDto comment) {
        return ResponseEntity.ok(commentService.addComment(comment));
    }

    @DeleteMapping("delete-comment/{commentId}")
    public ResponseEntity<?> addComment(@PathVariable UUID commentId) {
        return ResponseEntity.ok(commentService.deleteComment(commentId));
    }

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
