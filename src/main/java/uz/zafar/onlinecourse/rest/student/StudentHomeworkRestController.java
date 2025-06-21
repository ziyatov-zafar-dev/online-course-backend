package uz.zafar.onlinecourse.rest.student;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.zafar.onlinecourse.service.HomeworkService;

import java.util.UUID;

@RequestMapping("/api/student/homework")
@RequiredArgsConstructor
@RestController
@Tag(name = "Student Homework Controller", description = "Student paneli uchun uyga vazifalar bilan ishlash API'lari")
public class StudentHomeworkRestController {
    private final HomeworkService homeworkService;

    @GetMapping("download-homeworks/{lessonId}")
    public ResponseEntity<?> downloadHomeworks(@PathVariable UUID lessonId) {
        return homeworkService.downloadLessonHomeworks(lessonId);
    }

    @GetMapping("get-all-homeworks-by-lesson-id/{lessonId}")
    public ResponseEntity<?> getHomeworksByLessonId(@PathVariable UUID lessonId, @RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(homeworkService.findAllByLessonId(lessonId, page, size));
    }
}
