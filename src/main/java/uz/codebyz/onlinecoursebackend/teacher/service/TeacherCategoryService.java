package uz.codebyz.onlinecoursebackend.teacher.service;

import uz.codebyz.onlinecoursebackend.common.ResponseDto;
import uz.codebyz.onlinecoursebackend.teacher.dto.category.TeacherCategoryResponseDto;

import java.util.List;
import java.util.UUID;

public interface TeacherCategoryService {
    ResponseDto<List<TeacherCategoryResponseDto>> getAllCourses();
    ResponseDto<TeacherCategoryResponseDto> findBySlug(String slug);
    ResponseDto<TeacherCategoryResponseDto> findById(UUID id);
}
