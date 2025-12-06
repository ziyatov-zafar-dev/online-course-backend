package uz.codebyz.onlinecoursebackend.admin.users.service;

import org.springframework.data.domain.Page;
import uz.codebyz.onlinecoursebackend.admin.users.teacherDto.AdminCreateTeacherRequestDto;
import uz.codebyz.onlinecoursebackend.admin.users.teacherDto.AdminTeacherResponseDto;
import uz.codebyz.onlinecoursebackend.auth.dto.UserResponse;
import uz.codebyz.onlinecoursebackend.common.ResponseDto;
import uz.codebyz.onlinecoursebackend.teacher.entity.TeacherStatus;

import java.util.List;

public interface AdminTeacherService {
    ResponseDto<UserResponse> addTeacher(AdminCreateTeacherRequestDto req);

    ResponseDto<AdminTeacherResponseDto> findById(Long teacherId);

    ResponseDto<List<AdminTeacherResponseDto>> getAllTeachers();

    ResponseDto<Page<AdminTeacherResponseDto>> getAllTeachers(int page, int size);

    ResponseDto<List<AdminTeacherResponseDto>> getAllTeachersByStatus(TeacherStatus status);

    ResponseDto<Page<AdminTeacherResponseDto>> getAllTeachersByStatus(TeacherStatus status, int page, int size);
}
