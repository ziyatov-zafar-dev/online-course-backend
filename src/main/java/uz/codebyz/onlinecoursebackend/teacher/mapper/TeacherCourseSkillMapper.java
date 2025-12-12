package uz.codebyz.onlinecoursebackend.teacher.mapper;

import org.springframework.stereotype.Component;
import uz.codebyz.onlinecoursebackend.admin.course.dto.AdminCourseSkillResponseDto;
import uz.codebyz.onlinecoursebackend.admin.course.mapper.AdminCourseSkillMapper;
import uz.codebyz.onlinecoursebackend.skill.entity.Skill;
import uz.codebyz.onlinecoursebackend.teacher.dto.course.TeacherCourseSkillResponseDto;

import java.util.List;
import java.util.stream.Collectors;
@Component
public class TeacherCourseSkillMapper {
    public TeacherCourseSkillResponseDto toDto(Skill skill) {
        TeacherCourseSkillResponseDto adminCourseSkillResponseDto = new TeacherCourseSkillResponseDto();
        adminCourseSkillResponseDto.setSkillName(skill.getName());
        adminCourseSkillResponseDto.setSkillId(skill.getId());
        adminCourseSkillResponseDto.setSkillImgUrl(skill.getIconUrl());
        adminCourseSkillResponseDto.setCourseId(skill.getCourse().getId());
        adminCourseSkillResponseDto.setCreated(skill.getCreated());
        adminCourseSkillResponseDto.setUpdated(skill.getUpdated());
        adminCourseSkillResponseDto.setOrderNumber(skill.getOrderNumber());
        return adminCourseSkillResponseDto;
    }

    public List<TeacherCourseSkillResponseDto> toDto(List<Skill> skills) {
        return skills.stream().map(this::toDto).collect(Collectors.toList());
    }
}
