package uz.codebyz.onlinecoursebackend.admin.rest;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.codebyz.onlinecoursebackend.admin.homework.dto.AdminAddHomeworkRequestDto;
import uz.codebyz.onlinecoursebackend.admin.homework.dto.AdminHomeworkResponseDto;
import uz.codebyz.onlinecoursebackend.admin.homework.dto.AdminUpdateHomeworkRequestDto;
import uz.codebyz.onlinecoursebackend.admin.homework.service.AdminHomeworkService;
import uz.codebyz.onlinecoursebackend.admin.lesson.dto.*;
import uz.codebyz.onlinecoursebackend.admin.lesson.service.AdminLessonService;
import uz.codebyz.onlinecoursebackend.common.ResponseDto;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/lessons")
public class AdminLessonRestController {

    private final AdminLessonService lessonService;
    private final AdminHomeworkService homeworkService;

    public AdminLessonRestController(AdminLessonService lessonService, AdminHomeworkService homeworkService) {
        this.lessonService = lessonService;
        this.homeworkService = homeworkService;
    }

    // ------------------- CREATE LESSON -------------------
    @PostMapping("add-lesson")
    public ResponseEntity<ResponseDto<AdminLessonResponseDto>> addLesson(@RequestBody AdminCreateLessonRequestDto req) {
        return ResponseEntity.ok(lessonService.addLesson(req));
    }

    // ------------------- UPDATE LESSON -------------------
    @PutMapping("/edit/{lessonId}")
    public ResponseEntity<ResponseDto<AdminLessonResponseDto>> updateLesson(@PathVariable(value = "lessonId") UUID lessonId, @RequestBody AdminUpdateLessonRequestDto req) {
        return ResponseEntity.ok(lessonService.updateLesson(lessonId, req));
    }

    // ------------------- SOFT DELETE -------------------
    @DeleteMapping("delete/soft/{lessonId}")
    public ResponseEntity<ResponseDto<Void>> softDelete(@PathVariable(value = "lessonId") UUID lessonId) {
        return ResponseEntity.ok(lessonService.softDelete(lessonId));
    }

    // ------------------- HARD DELETE -------------------
    @DeleteMapping("delete/hard/{lessonId}")
    public ResponseEntity<ResponseDto<Void>> hardDelete(@PathVariable(value = "lessonId") UUID lessonId) {
        return ResponseEntity.ok(lessonService.hardDelete(lessonId));
    }

    // ------------------- GET ONE LESSON -------------------
    @GetMapping("lesson-by-id/{lessonId}")
    public ResponseEntity<ResponseDto<AdminLessonResponseDto>> getLesson(@PathVariable(value = "lessonId") UUID lessonId) {
        return ResponseEntity.ok(lessonService.getLesson(lessonId));
    }

    // ------------------- GET ALL LESSONS BY MODULE -------------------
    @GetMapping("/lessons-by-module-id/{moduleId}")
    public ResponseEntity<ResponseDto<List<AdminLessonResponseDto>>> getLessonsByModule(@PathVariable(value = "moduleId") UUID moduleId) {
        return ResponseEntity.ok(lessonService.getLessonsByModule(moduleId));
    }

    // ------------------- PAGINATION: GET LESSONS BY MODULE -------------------
    @GetMapping("/lessons-by-module-id/{moduleId}/pagination")
    public ResponseEntity<ResponseDto<Page<AdminLessonResponseDto>>> getLessonsByModulePaged(@PathVariable(value = "moduleId") UUID moduleId, @RequestParam(defaultValue = "0", value = "page") int page, @RequestParam(defaultValue = "10", value = "size") int size) {
        return ResponseEntity.ok(lessonService.getLessonsByModule(moduleId, page, size));
    }

    // ------------------- UPLOAD VIDEO -------------------
    @PostMapping(value = "upload-video-to-lesson/{lessonId}/video", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto<AdminLessonResponseDto>> uploadLessonVideo(@PathVariable("lessonId") UUID lessonId, @RequestParam("video") MultipartFile video) {
        return ResponseEntity.ok(lessonService.uploadLessonVideo(lessonId, video));
    }

    // ------------------- CHANGE ORDER -------------------
    @PutMapping("/{lessonId}/change-order/{newOrderNumber}")
    public ResponseEntity<ResponseDto<AdminLessonResponseDto>> changeOrder(@PathVariable("lessonId") UUID lessonId, @PathVariable("newOrderNumber") Integer newOrderNumber) {
        return ResponseEntity.ok(lessonService.changeOrder(lessonId, newOrderNumber));
    }

    // ------------------- GET LESSONS BY STATUS + MODULE -------------------
    @GetMapping("/module/{moduleId}/status")
    public ResponseEntity<ResponseDto<List<AdminLessonResponseDto>>> getLessonsByStatus(@PathVariable("moduleId") UUID moduleId, @RequestParam("status") LessonStatus status) {
        return ResponseEntity.ok(lessonService.getLessonsByStatusAndModuleId(moduleId, status));
    }

    // ------------------- PAGINATION: GET LESSONS BY STATUS -------------------
    @GetMapping("/module/{moduleId}/status/paging")
    public ResponseEntity<ResponseDto<Page<AdminLessonResponseDto>>> getLessonsByStatusPaged(@PathVariable("moduleId") UUID moduleId, @RequestParam("status") LessonStatus status, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(lessonService.getLessonsByStatusAndModuleId(moduleId, status, page, size));
    }


    /// /////////////////////////////////////////////////
    /// ////////homework///////////////////////////
    /// /////////////////////////////////////////
    @Operation(summary = "Yangi homework yaratish", description = """
            Ushbu endpoint orqali kurs moduliga yangi homework qo‘shiladi.
            
            Shu bilan birga, `files` parametri orqali 
            homeworkga tegishli fayllarni (PDF, DOCX, ZIP va boshqalar) yuklashingiz mumkin.
            
            Fayllar ro‘yxati multipart ko‘rinishida yuboriladi.
            """)
    @PostMapping(value = "homework/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto<AdminHomeworkResponseDto>> setHomework(
            @RequestParam(value = "title", required = true) String title,
            @RequestParam(value = "description", required = true) String description,
            @RequestParam(value = "maxScore", required = true) Integer maxScore,
            @RequestParam(value = "minScore", required = true) Integer minScore,
            @RequestParam(value = "lessonId", required = true) UUID lessonId,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) {
        AdminAddHomeworkRequestDto req = new AdminAddHomeworkRequestDto();
        req.setFiles(files);
        req.setTitle(title);
        req.setDescription(description);
        req.setMaxScore(maxScore);
        req.setMinScore(minScore);
        req.setLessonId(lessonId);
        return ResponseEntity.ok(homeworkService.setHomework(req));
    }

    @Operation(summary = "Homework ma'lumotlarini olish", description = """
            Ushbu endpoint yordamida berilgan homework ID bo‘yicha to‘liq ma'lumot qaytariladi.
            """)
    @GetMapping("get-homework-by-id/{homeworkId}")
    public ResponseEntity<ResponseDto<AdminHomeworkResponseDto>> findById(@PathVariable("homeworkId") UUID homeworkId) {
        return ResponseEntity.ok(homeworkService.findById(homeworkId));
    }

    @Operation(summary = "Homework ma'lumotlarini o‘zgartirish", description = """
            Ushbu endpoint homeworkning barcha asosiy ma'lumotlarini o‘zgartirish uchun ishlatiladi.
            
            """)
    @PutMapping(value = "/update-homework/{homeworkId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto<AdminHomeworkResponseDto>> changeHomework(
            @RequestPart(value = "files", required = false) List<MultipartFile> files,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "desc", required = false) String description,
            @RequestParam(value = "max_score", required = false) Integer maxScore,
            @RequestParam(value = "min_score", required = false) Integer minScore,
            @PathVariable("homeworkId") UUID homeworkId) {
        AdminUpdateHomeworkRequestDto req = new AdminUpdateHomeworkRequestDto();
        req.setTitle(title);
        req.setDescription(description);
        req.setMaxScore(maxScore);
        req.setMinScore(minScore);
        req.setFiles(files);
        return ResponseEntity.ok(homeworkService.changeHomework(homeworkId, req));
    }

    @Operation(summary = "Homeworkning maksimal ballini o‘zgartirish", description = """
            Ushbu endpoint homeworkga berilgan maksimal ball qiymatini yangilash uchun ishlatiladi.
            """)
    @PutMapping("/{homeworkId}/max-ball/{newMaxBall}")
    public ResponseEntity<ResponseDto<AdminHomeworkResponseDto>> changeMaxBall(@PathVariable("homeworkId") UUID homeworkId, @PathVariable("newMaxBall") Integer newMaxBall) {
        return ResponseEntity.ok(homeworkService.changeMaxBall(homeworkId, newMaxBall));
    }

    @Operation(
            summary = "Homeworkning minimal ballini o‘zgartirish",
            description = """
                    Ushbu endpoint homework uchun belgilangan minimal ball qiymatini yangilaydi.
                    """
    )
    @PutMapping("/{homeworkId}/min-ball/{newMinBall}")
    public ResponseEntity<ResponseDto<AdminHomeworkResponseDto>> changeMinBall(
            @PathVariable("homeworkId") UUID homeworkId,
            @PathVariable("newMinBall") Integer newMinBall
    ) {
        return ResponseEntity.ok(homeworkService.changeMinBall(homeworkId, newMinBall));
    }

}
