package uz.zafar.onlinecourse.rest.teacher;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.zafar.onlinecourse.dto.ResponseDto;
import uz.zafar.onlinecourse.dto.grade_dto.req.AddGradeDto;
import uz.zafar.onlinecourse.dto.grade_dto.req.EditGradeDto;
import uz.zafar.onlinecourse.dto.grade_dto.res.GradeDto;
import uz.zafar.onlinecourse.service.GradeService;

import java.util.UUID;

@RequestMapping("/api/teacher/grade")
@RequiredArgsConstructor
@RestController
@Tag(name = "Teacher Grade Controller", description = "Teacher paneli uchun baholar bilan ishlash API'lari")
public class TeacherGradeRestController {
    private final GradeService gradeService;

    @PostMapping("evaluate")
    public ResponseEntity<ResponseDto<GradeDto>> evaluate(@RequestBody AddGradeDto grade) {
        return ResponseEntity.ok(gradeService.evaluate(grade));
    }

    @PutMapping("update-grade/{gradeId}")
    public ResponseEntity<ResponseDto<GradeDto>> updateGrade(
            @RequestBody EditGradeDto grade,
            @PathVariable UUID gradeId
    ) {
        return ResponseEntity.ok(gradeService.editGrade(grade, gradeId));
    }

    @GetMapping("get-grade-by-lesson-id-and-student-id")
    public ResponseEntity<ResponseDto<?>> getGradeByLessonIdAndStudentId(
            @RequestParam UUID lessonId,
            @RequestParam Long studentId
    ) {
        return ResponseEntity.ok(gradeService.getLessonIdAndStudentIdGrade(lessonId, studentId));
    }

}
