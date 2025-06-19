package uz.zafar.onlinecourse.rest.admin;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.zafar.onlinecourse.service.ReviewService;

import java.util.UUID;

@RequestMapping("/api/admin/review")
@RequiredArgsConstructor
@RestController
@Tag(name = "Admin Review Controller", description = "Admin paneli uchun reviewlar bilan ishlash API'lari")
public class AdminReviewRestController {
    private final ReviewService reviewService;

    @GetMapping("get-reviews-by-lesson/{lessonId}")
    public ResponseEntity<?> getLessonReviews(@PathVariable UUID lessonId) {
        return ResponseEntity.ok(reviewService.getLessonReviews(lessonId));
    }
}
