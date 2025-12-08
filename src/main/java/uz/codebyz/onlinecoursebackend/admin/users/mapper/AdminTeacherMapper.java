package uz.codebyz.onlinecoursebackend.admin.users.mapper;

import uz.codebyz.onlinecoursebackend.admin.category.mapper.AdminPromoCodeMapper;
import uz.codebyz.onlinecoursebackend.admin.users.teacherDto.AdminTeacherResponseDto;
import uz.codebyz.onlinecoursebackend.teacher.entity.Teacher;
import uz.codebyz.onlinecoursebackend.userDevice.service.UserDeviceService;

public class AdminTeacherMapper {
    public static AdminTeacherResponseDto toDto(Teacher teacher, UserDeviceService deviceService) {
        AdminTeacherResponseDto dto = new AdminTeacherResponseDto();
        dto.setTeacherId(teacher.getId());
        dto.setUserInformation(AdminPromoCodeMapper.mapToUser(teacher.getUser(),deviceService));
        return dto;
    }
}
