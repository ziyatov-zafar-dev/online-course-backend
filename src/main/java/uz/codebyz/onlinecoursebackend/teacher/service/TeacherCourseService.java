package uz.codebyz.onlinecoursebackend.teacher.service;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import uz.codebyz.onlinecoursebackend.admin.course.dto.*;
import uz.codebyz.onlinecoursebackend.common.ResponseDto;
import uz.codebyz.onlinecoursebackend.course.entity.CourseStatus;
import uz.codebyz.onlinecoursebackend.teacher.dto.course.*;
import uz.codebyz.onlinecoursebackend.teacher.dto.promoCodeDtos.TeacherCourseAndPromoCodeResponseDto;
import uz.codebyz.onlinecoursebackend.teacher.dto.promoCodeDtos.TeacherCreatePromoCodeRequestDto;
import uz.codebyz.onlinecoursebackend.teacher.entity.Teacher;
import uz.codebyz.onlinecoursebackend.user.User;

import java.util.List;
import java.util.UUID;

public interface TeacherCourseService {
    ResponseDto<List<TeacherCourseResponseDto>> myCourses(Teacher teacher);

    ResponseDto<Page<TeacherCourseResponseDto>> myCourses(Teacher teacher, int page, int size);

    ResponseDto<TeacherCourseResponseDto> findBySkillId(Teacher teacher , UUID id);

    ResponseDto<TeacherCourseResponseDto> findBySlug(String slug);

    ResponseDto<Page<TeacherCourseResponseDto>> findAllByCategoryId(Teacher teacher, UUID categoryId, int page, int size);

    ResponseDto<List<TeacherCourseResponseDto>> findAllByCategoryId(Teacher teacher ,UUID categoryId);


    ResponseDto<AdminCourseResponseDto> updateCourse(Teacher teacher ,UUID courseId, AdminCourseUpdateRequestDto request);

    ResponseDto<Page<TeacherCourseResponseDto>> getByStatus(Teacher teacher, CourseStatus status, int page, int size);

    ResponseDto<List<TeacherCourseResponseDto>> getByStatus(Teacher teacher, CourseStatus status);

    ResponseDto<TeacherCourseResponseDto> changeCourseStatus(UUID courseId, CourseStatus status);

    ResponseDto<Void> softDelete(UUID courseId);

    ResponseDto<Void> hardDelete(UUID courseId);


    ResponseDto<TeacherCourseResponseDto> createCourse(TeacherCourseCreateRequestDto request);

    ResponseDto<TeacherCourseResponseDto> addVideo(UUID courseId, MultipartFile video);

    ResponseDto<TeacherCourseResponseDto> addImage(UUID courseId, MultipartFile image);


    ResponseDto<TeacherCourseResponseDto> updateCoursePricing(UUID courseId, TeacherCoursePricingRequestDto request);

    ResponseDto<TeacherCourseResponseDto> restoreCourse(UUID courseId);

    /// ////////////////apisi tayyormas

    ResponseDto<TeacherCourseAndPromoCodeResponseDto> findByPromoCode(String code);

    boolean existsByPromoCode(String code);

    ResponseDto<List<TeacherCourseAndPromoCodeResponseDto>> getAllPromoCodes(User user);

    ResponseDto<Page<TeacherCourseAndPromoCodeResponseDto>> getAllPromoCodes(User user,int page, int size);

    ResponseDto<List<TeacherCourseAndPromoCodeResponseDto>> getAllPromoCodesByCourseId(Teacher teacher,
                                                                                               UUID courseId);

    ResponseDto<Page<TeacherCourseAndPromoCodeResponseDto>> getAllPromoCodesByCourseId(Teacher teacher ,UUID courseId, int page, int size);

    ResponseDto<List<TeacherCourseAndPromoCodeResponseDto>> getAllPromoCodesByTeacherId(Teacher teacher);

    ResponseDto<Page<TeacherCourseAndPromoCodeResponseDto>> getAllPromoCodesByTeacherId(Long teacherId, int page, int size);

    ResponseDto<TeacherCourseAndPromoCodeResponseDto> addPromoCodeToCourse(UUID courseId, TeacherCreatePromoCodeRequestDto req);

    ResponseDto<TeacherCourseAndPromoCodeResponseDto> findByPromoCodeId(UUID promoCodeId);




    ResponseDto<TeacherCourseSkillResponseDto> findBySkillId(UUID skillId);
    ResponseDto<List<TeacherCourseSkillResponseDto>> findAllByCourseId(UUID courseId);
    ResponseDto<TeacherCourseSkillResponseDto> addSkillToCourse(CreateTeacherCourseSkillDto skill);
    ResponseDto<TeacherCourseSkillResponseDto> editCourseSkill(UUID skillId,UpdateTeacherCourseSkillDto skill);
    ResponseDto<Void> deleteCourseSkill(UUID skillId);
}
