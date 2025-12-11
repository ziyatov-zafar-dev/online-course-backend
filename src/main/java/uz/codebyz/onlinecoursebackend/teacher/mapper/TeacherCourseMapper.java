package uz.codebyz.onlinecoursebackend.teacher.mapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;
import uz.codebyz.onlinecoursebackend.admin.course.dto.AdminCourseCreateRequestDto;
import uz.codebyz.onlinecoursebackend.course.entity.Course;
import uz.codebyz.onlinecoursebackend.course.entity.CourseStatus;
import uz.codebyz.onlinecoursebackend.helper.FileHelper;
import uz.codebyz.onlinecoursebackend.teacher.dto.course.TeacherCourseCreateRequestDto;
import uz.codebyz.onlinecoursebackend.teacher.dto.course.TeacherCourseResponseDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Component
public class TeacherCourseMapper {
    public TeacherCourseResponseDto toDto(Course course) {
        TeacherCourseResponseDto dto = new TeacherCourseResponseDto();
        dto.setCategoryId(course.getCategory().getId());
        dto.setTeacherId(course.getTeacher().getId());
        dto.setName(course.getName());
        dto.setCourseId(course.getId());
        dto.setDescription(course.getDescription());
        dto.setSlug(course.getSlug());
        dto.setOrderNumber(course.getOrderNumber());
        dto.setCreated(course.getCreated().toString());
        dto.setUpdated(course.getUpdated().toString());
        dto.setTelegramChannelLink(course.getTelegramChannelLink());
        dto.setTelegramGroupLink(course.getTelegramGroupLink());
        dto.setHasTelegramChannel(course.getHasTelegramChannel());
        dto.setHasTelegramGroup(course.getHasTelegramGroup());
        if (course.getStatus() != null) {
            Map<String, String> statusMap = new HashMap<>();
            statusMap.put(course.getStatus().name(), course.getStatus().getDescription());
            dto.setStatus(statusMap);
        }
        dto.setDiscountPrice(course.getDiscountPrice());
        dto.setDiscountPercent(course.getDiscountPercent());
        dto.setDiscountStartAt(course.getDiscountStartAt());
        dto.setDiscountEndAt(course.getDiscountEndAt());
        dto.setPrice(course.getPrice());
        dto.setFinalPrice(course.getFinalPrice());
        dto.setImgName(course.getImgName());
        dto.setImgUrl(course.getImgUrl());
        dto.setImgSize(course.getImgSize());
        dto.setImgSizeMB(FileHelper.getFileSize(course.getImgSize()));
        dto.setPromoCourseVideoFileName(course.getPromoCourseVideoFileName());
        dto.setPromoCourseVideoFileSize(course.getPromoCourseVideoFileSize());
        dto.setPromoCourseVideoUrl(course.getPromoCourseVideoUrl());
        dto.setPromoCourseVideoFileSizeMB(FileHelper.getFileSize(course.getPromoCourseVideoFileSize()));
        return dto;
    }
    public List<TeacherCourseResponseDto> toDto(List<Course> categoryList) {
        return categoryList.stream().map(this::toDto).collect(Collectors.toList());
    }
    public Page<TeacherCourseResponseDto> toDto(Page<Course> categoryList) {
        List<TeacherCourseResponseDto> dtoList = categoryList.getContent()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return new PageImpl<>(
                dtoList,
                categoryList.getPageable(),
                categoryList.getTotalElements()
        );
    }

    public Course toEntityFromCreateRequest(TeacherCourseCreateRequestDto dto) {
        Course course = new Course();
        course.setName(dto.getName());
        course.setDescription(dto.getDescription());
        course.setActive(true);
        course.setPrice(dto.getPrice());
        course.setDiscountPrice(dto.getDiscountPrice());
        course.setDiscountPercent(dto.getDiscountPercent());
        course.setDiscountStartAt(dto.getDiscountStartAt());
        course.setDiscountEndAt(dto.getDiscountEndAt());
        dto.setFinalPrice(dto.getFinalPrice());
        course.setDeleted(false);
        course.setSlug(dto.getSlug());
        course.setOrderNumber(dto.getOrderNumber());
        course.setTelegramGroupLink(dto.getTelegramGroupLink());
        course.setTelegramChannelLink(dto.getTelegramChannelLink());
        course.setHasTelegramGroup(dto.getHasTelegramGroup());
        course.setHasTelegramChannel(dto.getHasTelegramChannel());
        course.setStatus(CourseStatus.DRAFT);
        course.setDiscountPrice(dto.getDiscountPrice());
        course.setDiscountPercent(dto.getDiscountPercent());
        course.setDiscountStartAt(dto.getDiscountStartAt());
        course.setDiscountEndAt(dto.getDiscountEndAt());
        course.setUpdatedAt();
        return course;
    }
}
