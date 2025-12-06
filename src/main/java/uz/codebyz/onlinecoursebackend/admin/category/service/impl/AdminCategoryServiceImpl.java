package uz.codebyz.onlinecoursebackend.admin.category.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.codebyz.onlinecoursebackend.admin.category.dto.AdminCategoryCreateRequest;
import uz.codebyz.onlinecoursebackend.admin.category.dto.AdminCategoryResponseDto;
import uz.codebyz.onlinecoursebackend.admin.category.dto.AdminCategoryUpdateRequest;
import uz.codebyz.onlinecoursebackend.admin.category.mapper.AdminCategoryMapper;
import uz.codebyz.onlinecoursebackend.admin.category.service.AdminCategoryService;
import uz.codebyz.onlinecoursebackend.category.entity.Category;
import uz.codebyz.onlinecoursebackend.category.entity.CategoryStatus;
import uz.codebyz.onlinecoursebackend.category.repository.CategoryRepository;
import uz.codebyz.onlinecoursebackend.common.ResponseDto;
import uz.codebyz.onlinecoursebackend.course.repository.CourseRepository;
import uz.codebyz.onlinecoursebackend.helper.CurrentTime;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AdminCategoryServiceImpl implements AdminCategoryService {
    private final CategoryRepository categoryRepository;
    private final CourseRepository courseRepository;

    public AdminCategoryServiceImpl(CategoryRepository categoryRepository, CourseRepository courseRepository) {
        this.categoryRepository = categoryRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public ResponseDto<AdminCategoryResponseDto> createCategory(AdminCategoryCreateRequest request) {
        Category category = AdminCategoryMapper.toEntity(request);
        category.setCreated(CurrentTime.currentTime());
        category.setUpdated();
        category.setActive(true);
        category.setDeleted(false);
        category.setStatus(CategoryStatus.CLOSE);
        category = categoryRepository.save(category);
        AdminCategoryResponseDto responseDto = AdminCategoryMapper.toDto(category);
        return new ResponseDto<>(true, "Ok", responseDto);
    }

    @Override
    public ResponseDto<List<AdminCategoryResponseDto>> getAllAdminCategories() {
        List<AdminCategoryResponseDto> categories = AdminCategoryMapper.toDto(categoryRepository.getAdminAllCategories());
        return new ResponseDto<>(true, "Ok", categories);
    }

    @Override
    public ResponseDto<AdminCategoryResponseDto> getCategoryById(UUID id) throws Exception {
        Optional<Category> cOp = categoryRepository.findAdminByCategoryId(id);
        return cOp.map(category -> new ResponseDto<>(true, "Ok", AdminCategoryMapper.toDto(category))).orElseGet(() -> ResponseDto.error("Not Found Category"));
    }

    @Override
    public ResponseDto<AdminCategoryResponseDto> updateCategory(UUID id, AdminCategoryUpdateRequest request) {
        Optional<Category> cOp = categoryRepository.findAdminByCategoryId(id);
        if (cOp.isEmpty()) return ResponseDto.error("Not Found Category");
        Category category = cOp.get();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setSlug(request.getSlug());
        category.setOrderNumber(request.getOrderNumber());
        category.setUpdated();
        category = categoryRepository.save(category);
        return new ResponseDto<>(true, "Ok", AdminCategoryMapper.toDto(category));
    }

    @Override
    public ResponseDto<Void> deletePermanently(UUID id) {
        Optional<Category> cOp = categoryRepository.getAdminSoftDeleteCategoryById(id);
        if (cOp.isEmpty()) return ResponseDto.error("Not Found Category");
        Category category = cOp.get();
        category.setActive(false);
        category = categoryRepository.save(category);
        return new ResponseDto<>(true, "Muvaffaqiyatli o'chirildi");
    }

    @Override
    public ResponseDto<Void> softDelete(UUID id) {
        Optional<Category> cOp = categoryRepository.findAdminByCategoryId(id);
        if (cOp.isEmpty()) return ResponseDto.error("Not Found Category");
        Category category = cOp.get();
        category.setActive(false);
        category = categoryRepository.save(category);
        return new ResponseDto<>(true, "Muvaffaqiyatli o'chirildi");
    }

    @Override
    public ResponseDto<AdminCategoryResponseDto> changeCategoryStatus(UUID id, CategoryStatus status) {
        Category category = categoryRepository.findAdminByCategoryId(id).orElseThrow(() -> new RuntimeException("Category not found after update"));
        category.setStatus(status);
        category.setUpdated();
        categoryRepository.save(category);
        return ResponseDto.ok("Status updated", AdminCategoryMapper.toDto(category));
    }

    @Override
    public ResponseDto<Boolean> checkSlug(String slug) {
        Optional<Category> checkSlug = categoryRepository.checkAdminSlug(slug);
        boolean check = checkSlug.isPresent();
        return new ResponseDto<>(true, "Ok", check);
    }

    @Override
    public ResponseDto<AdminCategoryResponseDto> getBySlug(String slug) {
        Optional<Category> checkSlug = categoryRepository.getAdminBySlug(slug);
        return checkSlug.map(category -> ResponseDto.ok("Ok", AdminCategoryMapper.toDto(category))).orElseGet(() -> ResponseDto.error("Not Found Category"));
    }

    @Override
    public ResponseDto<List<AdminCategoryResponseDto>> searchCategories(String keyword) {
        List<Category> categories = categoryRepository.searchAdminCategories(keyword);
        return new ResponseDto<>(true, "Ok", AdminCategoryMapper.toDto(categories));
    }

    @Override
    public ResponseDto<List<AdminCategoryResponseDto>> getAllSoftDeleteCategories() {
        List<Category> categories = categoryRepository.findAdminAllByActive(false, false);
        return new ResponseDto<>(true, "Ok", AdminCategoryMapper.toDto(categories));
    }

    @Override
    public ResponseDto<List<AdminCategoryResponseDto>> getAllDeletedCategories() {
        List<Category> categories = categoryRepository.findAdminAllByActive(false, true);
        return new ResponseDto<>(true, "Ok", AdminCategoryMapper.toDto(categories));
    }

    @Override
    public ResponseDto<Page<AdminCategoryResponseDto>> getAllAdminCategories(Pageable pageable) {
        Page<Category> allCategories = categoryRepository.getAdminAllCategories(pageable);
        Page<AdminCategoryResponseDto> dtoPage = allCategories.map(AdminCategoryMapper::toDto);
        return new ResponseDto<>(true, "Ok", dtoPage);
    }

    @Override
    public ResponseDto<Page<AdminCategoryResponseDto>> searchCategories(String keyword, Pageable pageable) {
        Page<Category> allCategories = categoryRepository.searchAdminCategories(keyword, pageable);
        Page<AdminCategoryResponseDto> dtoPage = allCategories.map(AdminCategoryMapper::toDto);
        return new ResponseDto<>(true, "Ok", dtoPage);
    }

    @Override
    public ResponseDto<Page<AdminCategoryResponseDto>> getAllDeletedCategories(Pageable pageable) {
        Page<Category> allCategories = categoryRepository.findAdminAllByActive(false, true, pageable);
        Page<AdminCategoryResponseDto> dtoPage = allCategories.map(AdminCategoryMapper::toDto);
        return new ResponseDto<>(true, "Ok", dtoPage);
    }

    @Override
    public ResponseDto<Page<AdminCategoryResponseDto>> getAllSoftDeleteCategories(Pageable pageable) {
        Page<Category> allCategories = categoryRepository.findAdminAllByActive(false, false, pageable);
        Page<AdminCategoryResponseDto> dtoPage = allCategories.map(AdminCategoryMapper::toDto);
        return new ResponseDto<>(true, "Ok", dtoPage);
    }

    @Override
    public ResponseDto<Page<AdminCategoryResponseDto>> getAllByStatus(CategoryStatus status, int page, int size) {
        Page<Category> allCategories = categoryRepository.findAdminAllActiveByStatus(status, PageRequest.of(page, size));
        Page<AdminCategoryResponseDto> dtoPage = allCategories.map(AdminCategoryMapper::toDto);
        return new ResponseDto<>(true, "Ok", dtoPage);
    }

    @Override
    public ResponseDto<List<AdminCategoryResponseDto>> getAllByStatus(CategoryStatus status) {
        List<Category> categories = categoryRepository.findAdminAllActiveByStatus(status);
        return new ResponseDto<>(true, "Ok", AdminCategoryMapper.toDto(categories));
    }

}
