package uz.zafar.onlinecourse.rest.student;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.zafar.onlinecourse.dto.like_dto.req.AddLikeDto;
import uz.zafar.onlinecourse.service.LikeService;

import java.util.UUID;

@RequestMapping("/api/student/like")
@RequiredArgsConstructor
@RestController
@Tag(name = "Student Like Controller", description = "Student paneli uchun layklar bilan ishlash API'lari")
public class StudentLikeRestController {
    private final LikeService likeService;

    @GetMapping("get-like-count-by-lesson-id")
    public ResponseEntity<?> getAllLikeCountByLessonId(@RequestParam("lessonId") UUID lessonId) {
        return ResponseEntity.ok(likeService.getLikeCountByLessonId(lessonId));
    }

    @PostMapping("add-like")
    public ResponseEntity<?> addLike(@RequestBody AddLikeDto like) {
        return ResponseEntity.ok(likeService.addLike(like));
    }

    @DeleteMapping("remove-like")
    public ResponseEntity<?> removeLike(@RequestParam Long studentId, @RequestParam UUID lessonId) {
        return ResponseEntity.ok(likeService.removeLike(
                lessonId, studentId
        ));
    }

    @GetMapping("is-like-by-user")
    public ResponseEntity<?> isLikedByUser(@RequestParam Long studentId, @RequestParam UUID lessonId) {
        return ResponseEntity.ok(likeService.isLikedByUser(
                lessonId, studentId
        ));
    }
}
