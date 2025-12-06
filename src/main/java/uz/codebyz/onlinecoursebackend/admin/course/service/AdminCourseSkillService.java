package uz.codebyz.onlinecoursebackend.admin.course.service;

import uz.codebyz.onlinecoursebackend.admin.course.dto.AdminCourseSkillResponseDto;
import uz.codebyz.onlinecoursebackend.admin.course.dto.CreateAdminCourseSkillDto;
import uz.codebyz.onlinecoursebackend.admin.course.dto.UpdateAdminCourseSkillDto;
import uz.codebyz.onlinecoursebackend.common.ResponseDto;

import java.util.List;
import java.util.UUID;

public interface AdminCourseSkillService {
    ResponseDto<AdminCourseSkillResponseDto> findById(UUID skillId);
    ResponseDto<List<AdminCourseSkillResponseDto>> findAllByCourseId(UUID courseId);
    ResponseDto<AdminCourseSkillResponseDto> addSkillToCourse(CreateAdminCourseSkillDto skill);
    ResponseDto<AdminCourseSkillResponseDto> editCourseSkill(UUID skillId,UpdateAdminCourseSkillDto skill);
    ResponseDto<Void> deleteCourseSkill(UUID skillId);
}
