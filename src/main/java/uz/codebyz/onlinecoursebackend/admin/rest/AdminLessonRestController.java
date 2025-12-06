package uz.codebyz.onlinecoursebackend.admin.rest;

import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.codebyz.onlinecoursebackend.admin.lesson.dto.*;
import uz.codebyz.onlinecoursebackend.admin.lesson.service.AdminLessonService;
import uz.codebyz.onlinecoursebackend.common.ResponseDto;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/lessons")
public class AdminLessonRestController {

    private final AdminLessonService lessonService;

    public AdminLessonRestController(AdminLessonService lessonService) {
        this.lessonService = lessonService;
    }

    // ------------------- CREATE LESSON -------------------
    @PostMapping("/{moduleId}")
    public ResponseEntity<ResponseDto<AdminLessonResponseDto>> addLesson(
            @PathVariable(value = "moduleId") UUID moduleId,
            @RequestBody AdminCreateLessonRequestDto req
    ) {
        return ResponseEntity.ok(lessonService.addLesson(moduleId, req));
    }

    // ------------------- UPDATE LESSON -------------------
    @PutMapping("/edit/{lessonId}")
    public ResponseEntity<ResponseDto<AdminLessonResponseDto>> updateLesson(
            @PathVariable(value = "lessonId") UUID lessonId,
            @RequestBody AdminUpdateLessonRequestDto req
    ) {
        return ResponseEntity.ok(lessonService.updateLesson(lessonId, req));
    }

    // ------------------- SOFT DELETE -------------------
    @DeleteMapping("delete/soft/{lessonId}")
    public ResponseEntity<ResponseDto<Void>> softDelete(
            @PathVariable(value = "lessonId") UUID lessonId
    ) {
        return ResponseEntity.ok(lessonService.softDelete(lessonId));
    }

    // ------------------- HARD DELETE -------------------
    @DeleteMapping("delete/hard/{lessonId}")
    public ResponseEntity<ResponseDto<Void>> hardDelete(
            @PathVariable(value = "lessonId") UUID lessonId
    ) {
        return ResponseEntity.ok(lessonService.hardDelete(lessonId));
    }

    // ------------------- GET ONE LESSON -------------------
    @GetMapping("lesson-by-id/{lessonId}")
    public ResponseEntity<ResponseDto<AdminLessonResponseDto>> getLesson(
            @PathVariable(value = "lessonId") UUID lessonId
    ) {
        return ResponseEntity.ok(lessonService.getLesson(lessonId));
    }

    // ------------------- GET ALL LESSONS BY MODULE -------------------
    @GetMapping("/lessons-by-module-id/{moduleId}")
    public ResponseEntity<ResponseDto<List<AdminLessonResponseDto>>> getLessonsByModule(
            @PathVariable(value = "moduleId") UUID moduleId
    ) {
        return ResponseEntity.ok(lessonService.getLessonsByModule(moduleId));
    }

    // ------------------- PAGINATION: GET LESSONS BY MODULE -------------------
    @GetMapping("/lessons-by-module-id/{moduleId}/pagination")
    public ResponseEntity<ResponseDto<Page<AdminLessonResponseDto>>> getLessonsByModulePaged(
            @PathVariable(value = "moduleId") UUID moduleId,
            @RequestParam(defaultValue = "0", value = "page") int page,
            @RequestParam(defaultValue = "10", value = "size") int size
    ) {
        return ResponseEntity.ok(lessonService.getLessonsByModule(moduleId, page, size));
    }

    // ------------------- UPLOAD VIDEO -------------------
    @PostMapping(value = "upload-video-to-lesson/{lessonId}/video", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto<AdminLessonResponseDto>> uploadLessonVideo(
            @PathVariable("lessonId") UUID lessonId,
            @RequestParam("video") MultipartFile video
    ) {
        return ResponseEntity.ok(lessonService.uploadLessonVideo(lessonId, video));
    }

    // ------------------- CHANGE ORDER -------------------
    @PutMapping("/{lessonId}/change-order/{newOrderNumber}")
    public ResponseEntity<ResponseDto<AdminLessonResponseDto>> changeOrder(
            @PathVariable("lessonId") UUID lessonId,
            @PathVariable("newOrderNumber") Integer newOrderNumber
    ) {
        return ResponseEntity.ok(lessonService.changeOrder(lessonId, newOrderNumber));
    }

    // ------------------- GET LESSONS BY STATUS + MODULE -------------------
    @GetMapping("/module/{moduleId}/status")
    public ResponseEntity<ResponseDto<List<AdminLessonResponseDto>>> getLessonsByStatus(
            @PathVariable("moduleId") UUID moduleId,
            @RequestParam("status") LessonStatus status
    ) {
        return ResponseEntity.ok(lessonService.getLessonsByStatusAndModuleId(moduleId, status));
    }

    // ------------------- PAGINATION: GET LESSONS BY STATUS -------------------
    @GetMapping("/module/{moduleId}/status/paging")
    public ResponseEntity<ResponseDto<Page<AdminLessonResponseDto>>> getLessonsByStatusPaged(
            @PathVariable("moduleId") UUID moduleId,
            @RequestParam("status") LessonStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(lessonService.getLessonsByStatusAndModuleId(moduleId, status, page, size));
    }
}
