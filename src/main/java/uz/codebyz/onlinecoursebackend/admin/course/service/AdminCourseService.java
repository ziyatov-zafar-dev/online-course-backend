package uz.codebyz.onlinecoursebackend.admin.course.service;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import uz.codebyz.onlinecoursebackend.admin.category.promoCodeDtos.AdminCourseAndPromoCodeResponseDto;
import uz.codebyz.onlinecoursebackend.admin.category.promoCodeDtos.AdminCreatePromoCodeRequestDto;
import uz.codebyz.onlinecoursebackend.admin.course.dto.AdminCourseCreateRequestDto;
import uz.codebyz.onlinecoursebackend.admin.course.dto.AdminCoursePricingRequestDto;
import uz.codebyz.onlinecoursebackend.admin.course.dto.AdminCourseResponseDto;
import uz.codebyz.onlinecoursebackend.admin.course.dto.AdminCourseUpdateRequestDto;
import uz.codebyz.onlinecoursebackend.common.ResponseDto;
import uz.codebyz.onlinecoursebackend.course.entity.CourseStatus;

import java.util.List;
import java.util.UUID;

public interface AdminCourseService {

    // CREATE
    ResponseDto<AdminCourseResponseDto> createCourse(AdminCourseCreateRequestDto request);
    ResponseDto<AdminCourseResponseDto>addVideo(UUID courseId, MultipartFile video);
    ResponseDto<AdminCourseResponseDto>addImage(UUID courseId, MultipartFile image);

    // UPDATE
    ResponseDto<AdminCourseResponseDto> updateCourse(UUID courseId, AdminCourseUpdateRequestDto request);

    // GET BY ID
    ResponseDto<AdminCourseResponseDto> getCourseById(UUID courseId);


    // GET ALL (pagination)
    ResponseDto<Page<AdminCourseResponseDto>> getAllCourses(int page, int size);

    // GET ALL (no pagination)
    ResponseDto<List<AdminCourseResponseDto>> getAllCourses();


    // GET BY STATUS (pagination)
    ResponseDto<Page<AdminCourseResponseDto>> getByStatus(CourseStatus status, int page, int size);

    // GET BY STATUS (no pagination)
    ResponseDto<List<AdminCourseResponseDto>> getByStatus(CourseStatus status);


    // UPDATE COURSE STATUS
    ResponseDto<AdminCourseResponseDto> changeCourseStatus(UUID courseId, CourseStatus status);


    // ⭐ SOFT DELETE (deleted=true)
    ResponseDto<Void> softDelete(UUID courseId);

    // ⭐ HARD DELETE (DB'dan to'liq o‘chiradi)
    ResponseDto<Void> hardDelete(UUID courseId);


    // UPDATE PRICING
    ResponseDto<AdminCourseResponseDto> updateCoursePricing(UUID courseId, AdminCoursePricingRequestDto request);

    ResponseDto<AdminCourseResponseDto> restoreCourse(UUID courseId);

    /// ////////////////apisi tayyormas
    ResponseDto<AdminCourseAndPromoCodeResponseDto> findByPromoCodeId(UUID promoCodeId);

    ResponseDto<AdminCourseAndPromoCodeResponseDto> findByPromoCode(String code);

    boolean existsByPromoCode(String code);

    ResponseDto<List<AdminCourseAndPromoCodeResponseDto>> getAllPromoCodes();

    ResponseDto<Page<AdminCourseAndPromoCodeResponseDto>> getAllPromoCodes(int page, int size);

    ResponseDto<List<AdminCourseAndPromoCodeResponseDto>> getAllPromoCodesByCourseId(UUID courseId);

    ResponseDto<Page<AdminCourseAndPromoCodeResponseDto>> getAllPromoCodesByCourseId(UUID courseId, int page, int size);

    ResponseDto<List<AdminCourseAndPromoCodeResponseDto>> getAllPromoCodesByTeacherId(Long teacherId);

    ResponseDto<Page<AdminCourseAndPromoCodeResponseDto>> getAllPromoCodesByTeacherId(Long teacherId, int page, int size);

    ResponseDto<AdminCourseAndPromoCodeResponseDto> addPromoCodeToCourse(UUID courseId, AdminCreatePromoCodeRequestDto req);

}
