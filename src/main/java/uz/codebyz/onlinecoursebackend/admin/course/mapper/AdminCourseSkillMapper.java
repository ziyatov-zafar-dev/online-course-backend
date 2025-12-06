package uz.codebyz.onlinecoursebackend.admin.course.mapper;

import uz.codebyz.onlinecoursebackend.admin.course.dto.AdminCourseSkillResponseDto;
import uz.codebyz.onlinecoursebackend.skill.entity.Skill;

import java.util.List;
import java.util.stream.Collectors;

public class AdminCourseSkillMapper {
    public static AdminCourseSkillResponseDto toDto(Skill skill) {
        AdminCourseSkillResponseDto adminCourseSkillResponseDto = new AdminCourseSkillResponseDto();
        adminCourseSkillResponseDto.setSkillName(skill.getName());
        adminCourseSkillResponseDto.setSkillId(skill.getId());
        adminCourseSkillResponseDto.setSkillImgUrl(skill.getIconUrl());
        adminCourseSkillResponseDto.setCourseId(skill.getCourse().getId());
        adminCourseSkillResponseDto.setCreated(skill.getCreated());
        adminCourseSkillResponseDto.setUpdated(skill.getUpdated());
        adminCourseSkillResponseDto.setOrderNumber(skill.getOrderNumber());
        return adminCourseSkillResponseDto;
    }

    public static List<AdminCourseSkillResponseDto> toDto(List<Skill> skills) {
        return skills.stream().map(AdminCourseSkillMapper::toDto).collect(Collectors.toList());
    }
}
