package uz.codebyz.onlinecoursebackend.teacher.service.impl;

import org.springframework.stereotype.Service;
import uz.codebyz.onlinecoursebackend.category.entity.Category;
import uz.codebyz.onlinecoursebackend.category.repository.CategoryRepository;
import uz.codebyz.onlinecoursebackend.common.ResponseDto;
import uz.codebyz.onlinecoursebackend.teacher.dto.category.TeacherCategoryResponseDto;
import uz.codebyz.onlinecoursebackend.teacher.mapper.TeacherCategoryMapper;
import uz.codebyz.onlinecoursebackend.teacher.service.TeacherCategoryService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TeacherCategoryServiceImpl implements TeacherCategoryService {
    private final CategoryRepository categoryRepository;
    private final TeacherCategoryMapper teacherCategoryMapper;

    public TeacherCategoryServiceImpl(CategoryRepository categoryRepository, TeacherCategoryMapper teacherCategoryMapper) {
        this.categoryRepository = categoryRepository;
        this.teacherCategoryMapper = teacherCategoryMapper;
    }

    @Override
    public ResponseDto<List<TeacherCategoryResponseDto>> getAllCourses() {
        return new ResponseDto<>(
                true, "Success",
                teacherCategoryMapper.toDto(categoryRepository.teacherFindAllCategories()));
    }


    @Override
    public ResponseDto<TeacherCategoryResponseDto> findBySlug(String slug) {
        ResponseDto<TeacherCategoryResponseDto> success = new ResponseDto<>();
        success.setSuccess(true);
        success.setMessage("Success");
        Optional<Category> cOp = categoryRepository.teacherFindBySlug(slug);
        if (cOp.isEmpty()) {
            return new ResponseDto<>(false, "Not found category");
        }
        success.setData(teacherCategoryMapper.toDto(cOp.get()));
        return success;
    }

    @Override
    public ResponseDto<TeacherCategoryResponseDto> findById(UUID id) {
        ResponseDto<TeacherCategoryResponseDto> success = new ResponseDto<>();
        success.setSuccess(true);
        success.setMessage("Success");
        Optional<Category> cOp = categoryRepository.teacherFindById(id);
        if (cOp.isEmpty()) {
            return new ResponseDto<>(false, "Not found category");
        }
        success.setData(teacherCategoryMapper.toDto(cOp.get()));
        return success;
    }
}
