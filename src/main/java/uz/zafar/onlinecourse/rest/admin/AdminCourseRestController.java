package uz.zafar.onlinecourse.rest.admin;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.zafar.onlinecourse.dto.ResponseDto;
import uz.zafar.onlinecourse.dto.course_dto.req.AddCourseDto;
import uz.zafar.onlinecourse.dto.course_dto.req.EditCourseDto;
import uz.zafar.onlinecourse.dto.course_dto.res.CourseDto;
import uz.zafar.onlinecourse.service.CourseService;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin/course")
@RequiredArgsConstructor
@Tag(name = "Admin Course Controller", description = "Admin paneli uchun kurslar bilan ishlash API'lari1")

public class AdminCourseRestController {
    private final CourseService courseService;

    @GetMapping("list")
    public ResponseEntity<ResponseDto<Page<CourseDto>>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(courseService.getAllCourses(page, size));
    }

    @GetMapping("get-all-delete-courses")
    public ResponseEntity<ResponseDto<Page<CourseDto>>> deleteCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(courseService.getAllDeleteCourses(page, size));
    }

    @GetMapping("course/{courseId}")
    public ResponseEntity<?> courseById(@PathVariable UUID courseId) {
        return ResponseEntity.ok(courseService.getCourseById(courseId));
    }

    @PostMapping("/add-course")
    public ResponseEntity<ResponseDto<?>> addCourse(@RequestBody AddCourseDto course) {
        return ResponseEntity.ok(courseService.addCourse(course));
    }

    @DeleteMapping("delete-course/{courseId}")
    public ResponseEntity<?> addCourse(@PathVariable UUID courseId) {
        return ResponseEntity.ok(courseService.deleteCourse(courseId));
    }


    @GetMapping("get-course-teacher/{courseId}")
    public ResponseEntity<?> getCourseTeacher(@PathVariable UUID courseId) {
        return ResponseEntity.ok(courseService.getCourseTeacher(courseId));
    }

    @PutMapping("edit-course/{courseId}")
    public ResponseEntity<?> editCourse(@PathVariable UUID courseId, @RequestBody EditCourseDto course) {
        return ResponseEntity.ok(courseService.editCourse(courseId, course));
    }
}
