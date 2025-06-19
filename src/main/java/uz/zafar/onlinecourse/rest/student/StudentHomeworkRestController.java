package uz.zafar.onlinecourse.rest.student;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
