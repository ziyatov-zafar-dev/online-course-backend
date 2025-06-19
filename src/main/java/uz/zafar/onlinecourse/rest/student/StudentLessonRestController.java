package uz.zafar.onlinecourse.rest.student;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.zafar.onlinecourse.service.LessonService;

import java.util.UUID;

@RequestMapping("/api/student/lesson")
@RequiredArgsConstructor
@RestController
@Tag(name = "Student Lesson Controller", description = "Student paneli uchun lessonlar bilan ishlash API'lari")

public class StudentLessonRestController {
    private final LessonService lessonService;

    @Operation(summary = "Lesson ID bo'yicha", description = "Lesson larni id bo'yicha topish")
    @GetMapping("get-lesson-by-id/{lessonId}")
    public ResponseEntity<?> getLessonById(@PathVariable UUID lessonId) {
        return ResponseEntity.ok(lessonService.findById(lessonId));
    }

    @GetMapping("/likes-and-comments-by-lesson/{lessonId}")
    public ResponseEntity<UUID> getLikesAndComments(@PathVariable String lessonId) {
        return ResponseEntity.ok(UUID.fromString(lessonId));

    }

    @Operation(summary = "Lesson group bo'yicha", description = "Guruhning barcha lesson larini olish")
    @GetMapping("get-lessons-by-group-id/{groupID}")
    public ResponseEntity<?> findAllByGroupId(@PathVariable("groupID") UUID groupId, @RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(lessonService.findAllByGroupId(groupId, page, size));
    }

    @Operation(summary = "Yuklab olish", description = "Lesson faylini yuklab olish")
    @GetMapping("download-lesson-file/{lessonFileId}")
    public ResponseEntity<?> downloadLessonFile(@PathVariable UUID lessonFileId) {
        return lessonService.downloadLessonFile(lessonFileId);
    }

    @Operation(summary = "Yuklab olish", description = "Lessonning barcha fayllarini zip ko'rinishida yuklash")
    @GetMapping("download-all-lesson-files/{lessonId}")
    public ResponseEntity<?> downloadAllLessonFiles(@PathVariable UUID lessonId) {
        return lessonService.downloadAllLessonFiles(lessonId);
    }
}
