package uz.zafar.onlinecourse.rest.admin;

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

@RestController
@RequestMapping("/api/admin/lesson/")
@RequiredArgsConstructor
@Tag(name = "Admin Lesson Controller", description = "Admin paneli uchun darslar bilan ishlash API'lari")

public class AdminLessonRestController {
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
        AddLessonFileDto addLessonFileDto = new AddLessonFileDto();
        addLessonFileDto.setLessonId(lessonId);
        addLessonFileDto.setFile(file);
        addLessonFileDto.setTypeId(typeId);
        return ResponseEntity.ok(lessonService.addFileToLesson(addLessonFileDto, request));
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
