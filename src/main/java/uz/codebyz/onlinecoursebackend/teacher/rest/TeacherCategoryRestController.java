package uz.codebyz.onlinecoursebackend.teacher.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.codebyz.onlinecoursebackend.common.ResponseDto;
import uz.codebyz.onlinecoursebackend.teacher.dto.category.TeacherCategoryResponseDto;
import uz.codebyz.onlinecoursebackend.teacher.service.TeacherCategoryService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/teacher/categories")
public class TeacherCategoryRestController {

    private final TeacherCategoryService teacherCategoryService;

    public TeacherCategoryRestController(TeacherCategoryService teacherCategoryService) {
        this.teacherCategoryService = teacherCategoryService;
    }

    @Operation(
            summary = "Teacher kategoriyalar ro‘yxati",
            description = "Barcha kategoriyalarning ro'yxati"
    )
    @GetMapping("/list")
    public ResponseEntity<ResponseDto<List<TeacherCategoryResponseDto>>> getAllCategories() {
        return ResponseEntity.ok(
                teacherCategoryService.getAllCourses()
        );
    }

    @GetMapping("/category/{slug}")
    public ResponseEntity<ResponseDto<TeacherCategoryResponseDto>> findByCategory(@PathVariable("slug") String slug) {
        return ResponseEntity.ok(
                teacherCategoryService.findBySlug(slug)
        );
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ResponseDto<TeacherCategoryResponseDto>> findByCategory(@PathVariable("categoryId") UUID categoryId) {
        return ResponseEntity.ok(
                teacherCategoryService.findById(categoryId)
        );
    }
}
