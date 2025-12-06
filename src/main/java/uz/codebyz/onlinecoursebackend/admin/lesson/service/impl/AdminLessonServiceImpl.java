package uz.codebyz.onlinecoursebackend.admin.lesson.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.codebyz.onlinecoursebackend.admin.course.dto.UploadFileResponseDto;
import uz.codebyz.onlinecoursebackend.admin.lesson.dto.AdminCreateLessonRequestDto;
import uz.codebyz.onlinecoursebackend.admin.lesson.dto.AdminLessonResponseDto;
import uz.codebyz.onlinecoursebackend.admin.lesson.dto.AdminUpdateLessonRequestDto;
import uz.codebyz.onlinecoursebackend.admin.lesson.dto.LessonStatus;
import uz.codebyz.onlinecoursebackend.admin.lesson.mapper.AdminLessonMapper;
import uz.codebyz.onlinecoursebackend.admin.lesson.service.AdminLessonService;
import uz.codebyz.onlinecoursebackend.admin.module.mapper.AdminModuleMapper;
import uz.codebyz.onlinecoursebackend.common.ResponseDto;
import uz.codebyz.onlinecoursebackend.helper.CurrentTime;
import uz.codebyz.onlinecoursebackend.helper.FileHelper;
import uz.codebyz.onlinecoursebackend.lesson.entity.Lesson;
import uz.codebyz.onlinecoursebackend.lesson.repository.LessonRepository;
import uz.codebyz.onlinecoursebackend.module.entity.Module;
import uz.codebyz.onlinecoursebackend.module.repository.ModuleRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class AdminLessonServiceImpl implements AdminLessonService {
    @Value("${course.video.lesson.url}")
    private String baseLessonUrl;
    private final ModuleRepository moduleRepository;
    private final LessonRepository lessonRepository;

    public AdminLessonServiceImpl(ModuleRepository moduleRepository, LessonRepository lessonRepository) {
        this.moduleRepository = moduleRepository;
        this.lessonRepository = lessonRepository;
    }

    @Override
    public ResponseDto<AdminLessonResponseDto> addLesson(UUID moduleId, AdminCreateLessonRequestDto req) {
        Optional<Module> mOp = moduleRepository.findById(moduleId);
        if (mOp.isEmpty()) return new ResponseDto<>(false, "Not found lesson");
        Lesson lesson = AdminLessonMapper.addLessonMapper(req, mOp.get());
        return new ResponseDto<>(true, "success",
                AdminLessonMapper.toDto(lessonRepository.save(lesson)));
    }

    @Override
    public ResponseDto<AdminLessonResponseDto> updateLesson(UUID lessonId, AdminUpdateLessonRequestDto req) {
        Optional<Lesson> lOp = lessonRepository.findById(lessonId);
        if (lOp.isEmpty()) return new ResponseDto<>(false, "Not found lesson");
        Lesson lesson = lOp.get();
        return new ResponseDto<>(true, "Successfully updated lesson",
                AdminLessonMapper.toDto(lessonRepository.save(AdminLessonMapper.editLessonMapper(req, lesson))));
    }

    @Override
    public ResponseDto<Void> softDelete(UUID lessonId) {
        Optional<Lesson> lOp = lessonRepository.findById(lessonId);
        if (lOp.isEmpty()) return new ResponseDto<>(false, "Not found lesson");
        Lesson lesson = lOp.get();
        if (lesson.getDeleted()) {
            return new ResponseDto<>(false, "Ushbu dars hard delete qilingan, buni tiklash imkonsiz");
        }
        if (lesson.getActive()) {
            lesson.setActive(false);
            lesson.setUpdated(CurrentTime.currentTime());
            lessonRepository.save(lesson);
            return new ResponseDto<>(true, "Successfully updated lesson");
        }
        return new ResponseDto<>(false, "Kutilmagan vaziyat");
    }

    @Override
    public ResponseDto<Void> hardDelete(UUID lessonId) {
        Optional<Lesson> lOp = lessonRepository.findById(lessonId);
        if (lOp.isEmpty()) return new ResponseDto<>(false, "Not found lesson");
        Lesson lesson = lOp.get();
        if (lesson.getDeleted()) {
            return new ResponseDto<>(false, "Ushbu dars hard delete qilingan, buni tiklash imkonsiz");
        }
        if (lesson.getActive()) {
            return new ResponseDto<>(false, "Buni hard delete qilish uchun oldin soft delete qilishingiz kerak");
        }
        if (lesson.getDeleted() == false && !lesson.getActive()) {
            lesson.setDeleted(true);
            lesson.setUpdated(CurrentTime.currentTime());
            lesson.setActive(false);
            return new ResponseDto<>(true, "Muvaffaqiyatli hard delete qilindi, endi buni tiklash imkonsiz");
        }
        return new ResponseDto<>(false, "Kutilmagan vaziyat");
    }

    @Override
    public ResponseDto<AdminLessonResponseDto> getLesson(UUID lessonId) {
        Optional<Lesson> lOp = lessonRepository.findById(lessonId);
        return lOp.map(lesson -> new ResponseDto<>(true, "Success", AdminLessonMapper.toDto(lesson))).orElseGet(() -> new ResponseDto<>(false, "Not found lesson"));
    }

    @Override
    public ResponseDto<List<AdminLessonResponseDto>> getLessonsByModule(UUID moduleId) {
        List<Lesson> modules = lessonRepository.findAllByModuleId(moduleId);
        return new ResponseDto<>(true, "Success", AdminLessonMapper.toDto(modules));
    }

    @Override
    public ResponseDto<Page<AdminLessonResponseDto>> getLessonsByModule(UUID moduleId, int page, int size) {
        Page<Lesson> modules = lessonRepository.findAllByModuleId(moduleId, PageRequest.of(page, size));
        return new ResponseDto<>(true, "Success", AdminLessonMapper.toDto(modules));
    }

    @Override
    public ResponseDto<AdminLessonResponseDto> uploadLessonVideo(UUID lessonId, MultipartFile video) {
        Optional<Lesson> lOp = lessonRepository.findById(lessonId);
        if (lOp.isEmpty()) return new ResponseDto<>(false, "Not found lesson");
        ResponseDto<UploadFileResponseDto> savedVideo = FileHelper.uploadFile(video, baseLessonUrl, true);
        if (!savedVideo.isSuccess())
            return new ResponseDto<>(false, "Upload file error");
        UploadFileResponseDto uploadVideo = savedVideo.getData();
        Lesson lesson = lOp.get();
        if (lesson.getVideoUrl() != null) {
            FileHelper.deleteFile(lesson.getVideoUrl());
        }
        lesson.setUpdated(CurrentTime.currentTime());
        lesson.setVideoUrl(uploadVideo.getFileUrl());
        lesson.setVideName(uploadVideo.getFileName());
        lesson.setVideoDurationSize(uploadVideo.getFileSize());
        return new ResponseDto<>(true, "Success",
                AdminLessonMapper.toDto(lessonRepository.save(lesson)));
    }

    @Override
    public ResponseDto<AdminLessonResponseDto> changeOrder(UUID lessonId, Integer newOrderNumber) {
        Optional<Lesson> lOp = lessonRepository.findById(lessonId);
        if (lOp.isEmpty()) return new ResponseDto<>(false, "Not found lesson");
        Lesson lesson = lOp.get();
        if (!newOrderNumber.equals(lesson.getOrderNumber())) {
            if (lessonRepository.existsByModuleIdAndLessonIdAndActiveAndDeleted(lesson.getModule().getId(), lessonId)) {
                return new ResponseDto<>(false, "Kechirasiz, afsuski bu order number band");
            }
        }
        lesson.setOrderNumber(newOrderNumber);
        lesson.setUpdated(CurrentTime.currentTime());
        return new ResponseDto<>(true, "Success", AdminLessonMapper.toDto(lessonRepository.save(lesson)));
    }

    @Override
    public ResponseDto<List<AdminLessonResponseDto>> getLessonsByStatusAndModuleId(UUID moduleId, LessonStatus status) {
        Optional<Module> mOp = moduleRepository.findByModuleId(moduleId);
        if (mOp.isEmpty()) return new ResponseDto<>(false, "Not found module");
        Module module = mOp.get();

        int i;//0 active, 1-soft delete, 2-hard delete
        if (status == LessonStatus.ACTIVE) {
            i = 0;
        } else if (status == LessonStatus.SOFT_DELETE) {
            i = 1;
        } else if (status == LessonStatus.HARD_DELETE) {
            i = 2;
        } else return new ResponseDto<>(false, "Not found status");
        List<Lesson> lessons = lessonRepository.getLessonsByStatusAndModuleId(i, moduleId);
        return new ResponseDto<>(true, "Success", AdminLessonMapper.toDto(lessons));
    }

    @Override
    public ResponseDto<Page<AdminLessonResponseDto>> getLessonsByStatusAndModuleId(UUID moduleId, LessonStatus status, int page, int size) {
        Optional<Module> mOp = moduleRepository.findByModuleId(moduleId);
        if (mOp.isEmpty()) return new ResponseDto<>(false, "Not found module");
        Module module = mOp.get();

        int i;//0 active, 1-soft delete, 2-hard delete
        if (status == LessonStatus.ACTIVE) {
            i = 0;
        } else if (status == LessonStatus.SOFT_DELETE) {
            i = 1;
        } else if (status == LessonStatus.HARD_DELETE) {
            i = 2;
        } else return new ResponseDto<>(false, "Not found status");
        Page<Lesson> lessons = lessonRepository.getLessonsByStatusAndModuleId(i, moduleId, PageRequest.of(page, size));
        return new ResponseDto<>(true, "Success", AdminLessonMapper.toDto(lessons));
    }
}
