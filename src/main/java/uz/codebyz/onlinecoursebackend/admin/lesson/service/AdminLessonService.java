package uz.codebyz.onlinecoursebackend.admin.lesson.service;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import uz.codebyz.onlinecoursebackend.admin.lesson.dto.AdminCreateLessonRequestDto;
import uz.codebyz.onlinecoursebackend.admin.lesson.dto.AdminLessonResponseDto;
import uz.codebyz.onlinecoursebackend.admin.lesson.dto.AdminUpdateLessonRequestDto;
import uz.codebyz.onlinecoursebackend.admin.lesson.dto.LessonStatus;
import uz.codebyz.onlinecoursebackend.common.ResponseDto;

import java.util.List;
import java.util.UUID;

public interface AdminLessonService {
    ResponseDto<AdminLessonResponseDto> addLesson( AdminCreateLessonRequestDto req);

    ResponseDto<AdminLessonResponseDto> updateLesson(UUID lessonId, AdminUpdateLessonRequestDto req);

    ResponseDto<Void> softDelete(UUID lessonId);

    ResponseDto<Void> hardDelete(UUID lessonId);

    ResponseDto<AdminLessonResponseDto> getLesson(UUID lessonId);

    ResponseDto<List<AdminLessonResponseDto>> getLessonsByModule(UUID moduleId);

    ResponseDto<Page<AdminLessonResponseDto>> getLessonsByModule(UUID moduleId, int page, int size);

    ResponseDto<AdminLessonResponseDto> uploadLessonVideo(
            UUID lessonId, MultipartFile video);

    ResponseDto<AdminLessonResponseDto> changeOrder(UUID lessonId, Integer newOrderNumber);


    ResponseDto<List<AdminLessonResponseDto>> getLessonsByStatusAndModuleId(UUID moduleId, LessonStatus status);
    ResponseDto<Page<AdminLessonResponseDto>> getLessonsByStatusAndModuleId(UUID moduleId, LessonStatus status,int page, int size);
}
