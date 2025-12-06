package uz.codebyz.onlinecoursebackend.admin.lesson.mapper;

import org.springframework.data.domain.Page;
import uz.codebyz.onlinecoursebackend.admin.lesson.dto.AdminCreateLessonRequestDto;
import uz.codebyz.onlinecoursebackend.admin.lesson.dto.AdminLessonResponseDto;
import uz.codebyz.onlinecoursebackend.admin.lesson.dto.AdminUpdateLessonRequestDto;
import uz.codebyz.onlinecoursebackend.helper.CurrentTime;
import uz.codebyz.onlinecoursebackend.helper.FileHelper;
import uz.codebyz.onlinecoursebackend.lesson.entity.Lesson;
import uz.codebyz.onlinecoursebackend.module.entity.Module;

import java.util.List;
import java.util.stream.Collectors;

public class AdminLessonMapper {
    public static AdminLessonResponseDto toDto(Lesson lesson) {
        AdminLessonResponseDto dto = new AdminLessonResponseDto();
        dto.setLessonId(lesson.getId());
        dto.setName(lesson.getName());
        dto.setDescription(lesson.getDescription());
        dto.setSlug(lesson.getSlug());
        dto.setModuleId(lesson.getModule().getId());
        dto.setOrderNumber(lesson.getOrderNumber());
        dto.setVideoUrl(lesson.getVideoUrl());
        dto.setVideName(lesson.getVideName());
        dto.setVideoDurationSize(lesson.getVideoDurationSize());
        dto.setVideoDurationMB(FileHelper.getFileSize(lesson.getVideoDurationSize()));
        dto.setCreated(lesson.getCreated());
        dto.setUpdated(lesson.getUpdated());
        dto.setHasHomework(lesson.getHasHomework());
        return dto;
    }

    public static List<AdminLessonResponseDto> toDto(List<Lesson> lessons) {
        return lessons.stream().map(AdminLessonMapper::toDto).collect(Collectors.toList());
    }

    public static Page<AdminLessonResponseDto> toDto(Page<Lesson> lessons) {
        return lessons.map(AdminLessonMapper::toDto);
    }

    public static Lesson addLessonMapper(AdminCreateLessonRequestDto dto, Module module) {
        Lesson lesson = new Lesson();
        lesson.setName(dto.getName());
        lesson.setDescription(dto.getDescription());
        lesson.setActive(true);
        lesson.setDeleted(false);
        lesson.setSlug(dto.getSlug());
        lesson.setModule(module);
        lesson.setOrderNumber(dto.getOrderNumber());
        lesson.setVideName(null);
        lesson.setVideoUrl(null);
        lesson.setVideoDurationSize(null);
        lesson.setHasHomework(false);
        lesson.setHomework(null);
        lesson.setCreated(CurrentTime.currentTime());
        lesson.setUpdated(CurrentTime.currentTime());
        return lesson;
    }
    public static Lesson editLessonMapper(AdminUpdateLessonRequestDto dto, Lesson lesson) {
        lesson.setName(dto.getName());
        lesson.setDescription(dto.getDescription());
        lesson.setSlug(dto.getSlug());
        lesson.setOrderNumber(dto.getOrderNumber());
        lesson.setUpdated(CurrentTime.currentTime());
        return lesson;
    }
}
