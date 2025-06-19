package uz.zafar.onlinecourse.rest.student;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.zafar.onlinecourse.service.CourseService;

import java.util.UUID;

@RequestMapping("/api/student/course")
@RequiredArgsConstructor
@RestController
@Tag(name = "Student course Controller", description = "Student paneli uchun kurslar bilan ishlash API'lari")

public class StudentCourseRestController {
    private final CourseService courseService;

    @GetMapping("list")
    public ResponseEntity<?> getAllStudents(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(courseService.getAllCourses(page, size));
    }
    @GetMapping("course/{courseId}")
    public ResponseEntity<?> courseById(@PathVariable UUID courseId) {
        return ResponseEntity.ok(courseService.getCourseById(courseId));
    }

}
