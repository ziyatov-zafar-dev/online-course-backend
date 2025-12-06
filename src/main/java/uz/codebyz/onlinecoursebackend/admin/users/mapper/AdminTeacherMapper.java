package uz.codebyz.onlinecoursebackend.admin.users.mapper;

import uz.codebyz.onlinecoursebackend.admin.category.mapper.AdminPromoCodeMapper;
import uz.codebyz.onlinecoursebackend.admin.users.teacherDto.AdminTeacherResponseDto;
import uz.codebyz.onlinecoursebackend.teacher.entity.Teacher;

public class AdminTeacherMapper {
    public static AdminTeacherResponseDto toDto(Teacher teacher) {
        AdminTeacherResponseDto dto = new AdminTeacherResponseDto();
        dto.setTeacherId(teacher.getId());
        dto.setUserInformation(AdminPromoCodeMapper.mapToUser(teacher.getUser()));
        return dto;
    }
}
