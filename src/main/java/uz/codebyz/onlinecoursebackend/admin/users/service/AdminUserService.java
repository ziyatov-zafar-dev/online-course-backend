package uz.codebyz.onlinecoursebackend.admin.users.service;

import org.springframework.data.domain.Page;
import uz.codebyz.onlinecoursebackend.admin.users.userDto.AdminUserResponseDto;
import uz.codebyz.onlinecoursebackend.common.ResponseDto;
import uz.codebyz.onlinecoursebackend.user.UserRole;
import uz.codebyz.onlinecoursebackend.user.UserStatus;

import java.util.UUID;

public interface AdminUserService {
    ResponseDto<AdminUserResponseDto> findByUserId(UUID userId);

    ResponseDto<Page<AdminUserResponseDto>> getAllUsers(int page, int size);

    ResponseDto<Page<AdminUserResponseDto>> getAllUsersByStatus(UserStatus status, int page, int size);

    ResponseDto<Page<AdminUserResponseDto>> getAllUsersByRole(UserRole role, int page, int size);

    ResponseDto<Page<AdminUserResponseDto>> getAllStudents(int page, int size);

    ResponseDto<Page<AdminUserResponseDto>> getAllTeachers(int page, int size);

    ResponseDto<Page<AdminUserResponseDto>> getAllByTeachersAndStatus(UserRole role, UserStatus status, int page, int size);
}
