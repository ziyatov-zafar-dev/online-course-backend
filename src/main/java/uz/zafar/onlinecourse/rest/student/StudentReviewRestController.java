package uz.zafar.onlinecourse.rest.student;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.zafar.onlinecourse.dto.review_dto.req.AddReviewDto;
import uz.zafar.onlinecourse.service.ReviewService;

import java.util.UUID;

@RequestMapping("/api/student/review")
@RequiredArgsConstructor
@RestController
@Tag(name = "Student Review Controller", description = "Student paneli uchun reviewlar bilan ishlash API'lari")
public class StudentReviewRestController {
    private final ReviewService reviewService;

    @PostMapping("add-review")
    public ResponseEntity<?> addReview(@RequestBody AddReviewDto addReviewDto) {
        return ResponseEntity.ok(reviewService.addReview(addReviewDto));
    }
    @GetMapping("get-reviews-by-lesson/{lessonId}")
    public ResponseEntity<?> getLessonReviews(@PathVariable UUID lessonId) {
        return ResponseEntity.ok(reviewService.getLessonReviews(lessonId));
    }
}
