package uz.zafar.onlinecourse.rest.student;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uz.zafar.onlinecourse.dto.ResponseDto;
import uz.zafar.onlinecourse.service.GradeService;

import java.util.UUID;

@RequestMapping("/api/student/grade")
@RequiredArgsConstructor
@RestController
@Tag(name = "Student Grade Controller", description = "Student paneli uchun baholar bilan ishlash API'lari")
public class StudentGradeRestController {
    private final GradeService gradeService;

    @GetMapping("get-grade-by-lesson-id-and-student-id")
    public ResponseEntity<ResponseDto<?>> getGradeByLessonIdAndStudentId(
            @RequestParam UUID lessonId,
            @RequestParam Long studentId
    ) {
        return ResponseEntity.ok(gradeService.getLessonIdAndStudentIdGrade(lessonId, studentId));
    }
}
