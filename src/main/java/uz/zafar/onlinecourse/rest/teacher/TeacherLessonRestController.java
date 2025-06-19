package uz.zafar.onlinecourse.rest.teacher;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.zafar.onlinecourse.dto.lesson_dto.req.AddLessonDto;
import uz.zafar.onlinecourse.dto.lesson_dto.req.AddLessonFileDto;
import uz.zafar.onlinecourse.dto.lesson_dto.res.LessonFileDto;
import uz.zafar.onlinecourse.service.LessonService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequestMapping("/api/teacher/lesson")
@RequiredArgsConstructor
@RestController
@Tag(name = "Teacher Lesson Controller", description = "Teacher paneli uchun lessonlar bilan ishlash API'lari")

public class TeacherLessonRestController {
    private final LessonService lessonService;

    @PostMapping("add-lesson")
    public ResponseEntity<?> addLesson(
            @RequestParam("groupId") UUID groupId,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("file") MultipartFile file,
            @RequestParam("typeId") Short typeId,
            HttpServletRequest request
    ) {
        AddLessonDto lesson = new AddLessonDto();
        lesson.setGroupId(groupId);
        List<LessonFileDto> files = new ArrayList<>();
        files.add(new LessonFileDto(file, typeId));
        lesson.setFiles(files);
        lesson.setTitle(title);
        lesson.setDescription(description);
        return ResponseEntity.ok(lessonService.addLesson(lesson, request));
    }

    @PostMapping("add-file-to-lesson/{lessonId}")
    public ResponseEntity<?> addFileToLesson(
            @PathVariable UUID lessonId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("typeId") Short typeId,
            HttpServletRequest request
    ) {
        return ResponseEntity.ok(lessonService.addFileToLesson(
                AddLessonFileDto.builder()
                        .lessonId(lessonId)
                        .file(file)
                        .typeId(typeId)
                        .build(), request));
    }

    @GetMapping("get-lesson-by-id/{lessonId}")
    public ResponseEntity<?> getLessonById(@PathVariable UUID lessonId) {
        return ResponseEntity.ok(lessonService.findById(lessonId));
    }

    @GetMapping("get-lessons-by-group-id/{groupID}")
    public ResponseEntity<?> findAllByGroupId(@PathVariable("groupID") UUID groupId, @RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(lessonService.findAllByGroupId(groupId, page, size));
    }

    @GetMapping("download-lesson-file/{lessonFileId}")
    public ResponseEntity<?> downloadLessonFile(@PathVariable UUID lessonFileId) {
        return lessonService.downloadLessonFile(lessonFileId);
    }

    @GetMapping("download-all-lesson-files/{lessonId}")
    public ResponseEntity<?> downloadAllLessonFiles(@PathVariable UUID lessonId) {
        return lessonService.downloadAllLessonFiles(lessonId);
    }
}
