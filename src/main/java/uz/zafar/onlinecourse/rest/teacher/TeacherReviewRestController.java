package uz.zafar.onlinecourse.rest.teacher;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.zafar.onlinecourse.service.ReviewService;

import java.util.UUID;

@RequestMapping("/api/teacher/review")
@RequiredArgsConstructor
@RestController
@Tag(name = "Teacher Review Controller", description = "Teacher paneli uchun reviewlar bilan ishlash API'lari")
public class TeacherReviewRestController {
    private final ReviewService reviewService;
    @GetMapping("get-reviews-by-lesson/{lessonId}")
    public ResponseEntity<?> getLessonReviews(@PathVariable UUID lessonId) {
        return ResponseEntity.ok(reviewService.getLessonReviews(lessonId));
    }
}
