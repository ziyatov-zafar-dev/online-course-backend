package uz.codebyz.onlinecoursebackend.admin.category.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uz.codebyz.onlinecoursebackend.admin.category.dto.AdminCategoryCreateRequest;
import uz.codebyz.onlinecoursebackend.admin.category.dto.AdminCategoryResponseDto;
import uz.codebyz.onlinecoursebackend.admin.category.dto.AdminCategoryUpdateRequest;
import uz.codebyz.onlinecoursebackend.category.entity.CategoryStatus;
import uz.codebyz.onlinecoursebackend.common.ResponseDto;

import java.util.List;
import java.util.UUID;

public interface AdminCategoryService {

    // CREATE
    ResponseDto<AdminCategoryResponseDto> createCategory(AdminCategoryCreateRequest request);

    // GET ALL
    ResponseDto<List<AdminCategoryResponseDto>> getAllAdminCategories();

    // GET ONE
    ResponseDto<AdminCategoryResponseDto> getCategoryById(UUID id) throws Exception;

    // UPDATE
    ResponseDto<AdminCategoryResponseDto> updateCategory(UUID id, AdminCategoryUpdateRequest request);

    // DELETE (soft)
    ResponseDto<Void> softDelete(UUID categoryID);

    ResponseDto<Void> deletePermanently(UUID id);

    // STATUS CHANGE
    ResponseDto<AdminCategoryResponseDto> changeCategoryStatus(UUID id, CategoryStatus status);


    // SLUG CHECK
    ResponseDto<Boolean> checkSlug(String slug);

    ResponseDto<AdminCategoryResponseDto> getBySlug(String slug);

    // SEARCH & FILTER
    ResponseDto<List<AdminCategoryResponseDto>> searchCategories(String keyword);

    ResponseDto<List<AdminCategoryResponseDto>> getAllSoftDeleteCategories();

    ResponseDto<List<AdminCategoryResponseDto>> getAllDeletedCategories();

    ResponseDto<Page<AdminCategoryResponseDto>> getAllAdminCategories(Pageable pageable);

    ResponseDto<Page<AdminCategoryResponseDto>> searchCategories(String keyword, Pageable pageable);

    ResponseDto<Page<AdminCategoryResponseDto>> getAllDeletedCategories(Pageable pageable);

    ResponseDto<Page<AdminCategoryResponseDto>> getAllSoftDeleteCategories(Pageable pageable);

    ResponseDto<Page<AdminCategoryResponseDto>> getAllByStatus(CategoryStatus status, int page, int size);

    ResponseDto<List<AdminCategoryResponseDto>> getAllByStatus(CategoryStatus status);

}

