package uz.codebyz.onlinecoursebackend.admin.users.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import uz.codebyz.onlinecoursebackend.admin.users.mapper.AdminUsersMapper;
import uz.codebyz.onlinecoursebackend.admin.users.service.AdminUserService;
import uz.codebyz.onlinecoursebackend.admin.users.userDto.AdminUserResponseDto;
import uz.codebyz.onlinecoursebackend.common.ResponseDto;
import uz.codebyz.onlinecoursebackend.user.UserRepository;
import uz.codebyz.onlinecoursebackend.user.UserRole;
import uz.codebyz.onlinecoursebackend.user.UserStatus;
import uz.codebyz.onlinecoursebackend.userDevice.service.UserDeviceService;

import java.util.UUID;

@Service
public class AdminUserServiceImpl implements AdminUserService {
    private final UserRepository userRepository;
    private final UserDeviceService userDeviceService;

    public AdminUserServiceImpl(UserRepository userRepository, UserDeviceService userDeviceService) {
        this.userRepository = userRepository;
        this.userDeviceService = userDeviceService;
    }

    @Override
    public ResponseDto<AdminUserResponseDto> findByUserId(UUID userId) {
        return userRepository.findById(userId).map(
                        user -> new ResponseDto<>(true, "Success", AdminUsersMapper.toDto(user,userDeviceService)))
                .orElseGet(() -> new ResponseDto<>(false, "Not found user"));
    }

    @Override
    public ResponseDto<Page<AdminUserResponseDto>> getAllUsers(int page, int size) {
        return new ResponseDto<>(true ,
                "Success" ,
                AdminUsersMapper.toDto(userRepository.findAllUsers(PageRequest.of(page,size)), userDeviceService));
    }

    @Override
    public ResponseDto<Page<AdminUserResponseDto>> getAllUsersByStatus(UserStatus status, int page, int size) {
        return null;
    }

    @Override
    public ResponseDto<Page<AdminUserResponseDto>> getAllUsersByRole(UserRole role, int page, int size) {
        return null;
    }

    @Override
    public ResponseDto<Page<AdminUserResponseDto>> getAllStudents(int page, int size) {
        return null;
    }

    @Override
    public ResponseDto<Page<AdminUserResponseDto>> getAllTeachers(int page, int size) {
        return null;
    }

    @Override
    public ResponseDto<Page<AdminUserResponseDto>> getAllByTeachersAndStatus(UserRole role, UserStatus status, int page, int size) {
        return null;
    }
}
