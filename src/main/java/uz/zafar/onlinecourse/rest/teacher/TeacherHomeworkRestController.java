package uz.zafar.onlinecourse.rest.teacher;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.zafar.onlinecourse.dto.homework_dto.req.AddHomeworkDto;
import uz.zafar.onlinecourse.dto.homework_dto.req.AddHomeworkFileDto;
import uz.zafar.onlinecourse.dto.homework_dto.req.EditHomeworkDto;
import uz.zafar.onlinecourse.dto.homework_dto.req.RemoveHomeworkFile;
import uz.zafar.onlinecourse.service.HomeworkService;

import java.util.Date;
import java.util.UUID;


@RequestMapping("/api/teacher/homework")
@RequiredArgsConstructor
@RestController
@Tag(name = "Teacher Homework Controller", description = "Teacher paneli uchun homeworklar bilan ishlash API'lari")
public class TeacherHomeworkRestController {
    private final HomeworkService homeworkService;

    @GetMapping("homework-by-id/{homeworkId}")
    public ResponseEntity<?> getHomeworkById(@PathVariable UUID homeworkId) {
        return ResponseEntity.ok(homeworkService.findById(homeworkId));
    }

    @GetMapping("get-homeworks-by-lesson-id/{lessonId}")
    public ResponseEntity<?> findAllByLessonId(@PathVariable UUID lessonId, @RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(homeworkService.findAllByLessonId(lessonId, page, size));
    }

    @PostMapping(value = "add-homework",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(description = """
            Deadline ni yyyy-MM-dd'T'HH:mm formatda kiriting misol uchun deadline=2025-09-01T23:59""")
    public ResponseEntity<?> addHomework(
            @RequestParam MultipartFile file,
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") Date deadline,
            @RequestParam Short typeId,
            @RequestParam UUID lessonId,
            HttpServletRequest request
    ) {
        return ResponseEntity.ok(homeworkService.addHomework(
                new AddHomeworkDto(
                        new EditHomeworkDto(title, description, deadline), file, typeId, lessonId
                ), request
        ));
    }

    @PutMapping("edit-homework/{homeworkId}")
    public ResponseEntity<?> findAllByLessonId(@PathVariable UUID homeworkId, @RequestBody EditHomeworkDto h) {
        return ResponseEntity.ok(homeworkService.edit(homeworkId, h));
    }

    @DeleteMapping("remove-file-from-homework")
    public ResponseEntity<?> removeFile(@RequestBody RemoveHomeworkFile h) {
        return ResponseEntity.ok(homeworkService.removeFile(h));
    }

    @PostMapping("add-file-to-homework")
    public ResponseEntity<?> addFileToHomework(
            @RequestParam MultipartFile file, @RequestParam UUID homeworkId,
            @RequestParam Short typeId, HttpServletRequest request) {
        return ResponseEntity.ok(homeworkService.addFileToHomework(new AddHomeworkFileDto(
                homeworkId, file, typeId
        ), request));
    }
}
