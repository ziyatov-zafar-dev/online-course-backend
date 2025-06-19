package uz.zafar.onlinecourse.rest.teacher;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.zafar.onlinecourse.dto.course_dto.req.AddCourseDto;
import uz.zafar.onlinecourse.dto.course_dto.req.EditCourseDto;
import uz.zafar.onlinecourse.service.CourseService;

import java.util.UUID;

@RequestMapping("/api/teacher/course")
@RequiredArgsConstructor
@RestController
@Tag(name = "Teacher course Controller", description = "Teacher paneli uchun kurslar bilan ishlash API'lari")

public class TeacherCourseRestController {
    private final CourseService courseService;

    @GetMapping("get-course-by-id/{courseId}")
    public ResponseEntity<?> getCourseById(@PathVariable UUID courseId) {
        return ResponseEntity.ok(courseService.getCourseById(courseId));
    }

    @PostMapping("add-course")
    public ResponseEntity<?> addCourse(@RequestBody AddCourseDto addCourseDto) {
        return ResponseEntity.ok(courseService.addCourse(addCourseDto));
    }

    @PutMapping("edit-course/{course-id}")
    public ResponseEntity<?> editCourse(@PathVariable("course-id") UUID courseId, @RequestBody EditCourseDto editCourseDto) {
        return ResponseEntity.ok(courseService.editCourse(courseId, editCourseDto));
    }

    @DeleteMapping("delete-course/{course-id}")
    public ResponseEntity<?> editCourse(@PathVariable("course-id") UUID courseId) {
        return ResponseEntity.ok(courseService.deleteCourse(courseId));
    }

    @GetMapping("get-teacher-courses/{teacherId}")
    public ResponseEntity<?> getTeacherCourses(@PathVariable("teacherId") Long teacherId) {
        return ResponseEntity.ok(courseService.getTeacherCourses(teacherId, false));
    }

    @GetMapping("get-teacher-deleted-courses/{teacherId}")
    public ResponseEntity<?> getTeacherDeletedCourses(@PathVariable("teacherId") Long teacherId) {
        return ResponseEntity.ok(courseService.getTeacherCourses(teacherId, true));
    }
}
