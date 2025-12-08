package uz.codebyz.onlinecoursebackend.admin.rest;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.codebyz.onlinecoursebackend.admin.users.service.AdminTeacherService;
import uz.codebyz.onlinecoursebackend.admin.users.service.AdminUserService;
import uz.codebyz.onlinecoursebackend.admin.users.teacherDto.AdminCreateTeacherRequestDto;
import uz.codebyz.onlinecoursebackend.admin.users.teacherDto.AdminTeacherResponseDto;
import uz.codebyz.onlinecoursebackend.auth.dto.UserResponse;
import uz.codebyz.onlinecoursebackend.common.ResponseDto;
import uz.codebyz.onlinecoursebackend.teacher.entity.TeacherStatus;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUsersRestController {
    private final AdminTeacherService adminTeacherService;
    private final AdminUserService adminUserService;

    public AdminUsersRestController(AdminTeacherService adminTeacherService, AdminUserService adminUserService) {
        this.adminTeacherService = adminTeacherService;
        this.adminUserService = adminUserService;
    }

    @PostMapping("teachers/add-teacher")
    public ResponseEntity<ResponseDto<UserResponse>> addTeacher(@RequestBody AdminCreateTeacherRequestDto teacher) {
        return ResponseEntity.ok(adminTeacherService.addTeacher(teacher));
    }

    @GetMapping("teachers/teacher/{teacherId}")
    public ResponseEntity<ResponseDto<AdminTeacherResponseDto>> getTeacherById(@PathVariable(name = "teacherId") Long teacherId) {
        return ResponseEntity.ok(adminTeacherService.findById(teacherId));
    }

    @GetMapping("teachers/get-all-teachers")
    public ResponseEntity<ResponseDto<List<AdminTeacherResponseDto>>> getAllTeachers() {
        return ResponseEntity.ok(adminTeacherService.getAllTeachers());
    }

    @GetMapping("teachers/get-all-teachers-pagination")
    public ResponseEntity<ResponseDto<Page<AdminTeacherResponseDto>>> getAllTeachers(
            @RequestParam(name = "page") int page, @RequestParam(name = "size") int size
    ) {
        return ResponseEntity.ok(adminTeacherService.getAllTeachers(page, size));
    }


    @GetMapping("teachers/get-all-teachers-by-status")
    public ResponseEntity<ResponseDto<List<AdminTeacherResponseDto>>> getAllTeachersByStatus(
            @RequestParam("status") TeacherStatus status
    ) {
        return ResponseEntity.ok(adminTeacherService.getAllTeachersByStatus(status));
    }

    @GetMapping("teachers/get-all-teachers-by-status-pagination")
    public ResponseEntity<ResponseDto<Page<AdminTeacherResponseDto>>> getAllTeachersByStatus(
            @RequestParam(name = "status") TeacherStatus status,
            @RequestParam(name = "page") int page,
            @RequestParam(name = "size") int size
    ) {
        return ResponseEntity.ok(adminTeacherService.getAllTeachersByStatus(status, page, size));
    }

    @GetMapping("list")
    public ResponseEntity<?> findAllUsers(
            @RequestParam("page") int page,
            @RequestParam("size") int size
    ) {
        return ResponseEntity.ok(adminUserService.getAllUsers(page, size));
    }
}
