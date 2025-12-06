package uz.codebyz.onlinecoursebackend.admin.course.mapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import uz.codebyz.onlinecoursebackend.admin.course.dto.AdminCourseCreateRequestDto;
import uz.codebyz.onlinecoursebackend.admin.course.dto.AdminCourseResponseDto;
import uz.codebyz.onlinecoursebackend.admin.course.dto.AdminCourseSkillResponseDto;
import uz.codebyz.onlinecoursebackend.admin.module.mapper.AdminModuleMapper;
import uz.codebyz.onlinecoursebackend.course.entity.Course;
import uz.codebyz.onlinecoursebackend.course.entity.CourseStatus;
import uz.codebyz.onlinecoursebackend.helper.FileHelper;
import uz.codebyz.onlinecoursebackend.module.entity.Module;
import uz.codebyz.onlinecoursebackend.skill.entity.Skill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AdminCourseMapper {

    public static AdminCourseResponseDto toDto(Course course) {
        if (course == null) return null;

        AdminCourseResponseDto dto = new AdminCourseResponseDto();

        dto.setCourseId(course.getId());
        dto.setName(course.getName());
        dto.setDescription(course.getDescription());
        dto.setSlug(course.getSlug());
        dto.setOrderNumber(course.getOrderNumber());
        dto.setTelegramGroupLink(course.getTelegramGroupLink());
        dto.setTelegramChannelLink(course.getTelegramChannelLink());
        dto.setHasTelegramGroup(course.getHasTelegramGroup());
        dto.setHasTelegramChannel(course.getHasTelegramChannel());
        List<AdminCourseSkillResponseDto> skills = new ArrayList<>();
        if (course.getSkills() != null) for (Skill skill : course.getSkills()) {
            if (skill.getActive()) skills.add(AdminCourseSkillMapper.toDto(skill));
        }
        dto.setSkills(skills);
        // ⭐ STATUS MAP
        Map<String, String> statusMap = new HashMap<>();
        if (course.getStatus() != null) {
            statusMap.put("name", course.getStatus().name());
            statusMap.put("description", course.getStatus().getDescription());
        }
        dto.setStatus(statusMap);
        dto.setImgSizeMB(FileHelper.getFileSize(course.getImgSize()));
//        try {
//        } catch (Exception e) {
//            dto.setImgSizeMB("0");
//        }
        dto.setPromoCourseVideoFileSizeMB(FileHelper.getFileSize(course.getPromoCourseVideoFileSize()));
//        try {
//        } catch (Exception e) {
//            dto.setPromoCourseVideoFileSizeMB("0");
//        }
        // ⭐ DISCOUNT FIELDS
        dto.setDiscountPrice(course.getDiscountPrice());
        dto.setDiscountPercent(course.getDiscountPercent());
        dto.setDiscountStartAt(course.getDiscountStartAt());
        dto.setDiscountEndAt(course.getDiscountEndAt());
        dto.setPrice(course.getPrice());
        dto.setFinalPrice(course.getFinalPrice());
        // ⭐ IMAGE
        dto.setImgName(course.getImgName());
        dto.setImgUrl(course.getImgUrl());
        dto.setImgSize(course.getImgSize());

        // ⭐ PROMO VIDEO
        dto.setPromoCourseVideoUrl(course.getPromoCourseVideoUrl());
        dto.setPromoCourseVideoFileName(course.getPromoCourseVideoFileName());
        dto.setPromoCourseVideoFileSize(course.getPromoCourseVideoFileSize());
        if (course.getModules() != null && !course.getModules().isEmpty())
            dto.setModules(AdminModuleMapper.toDto(course.getModules().stream().filter(module -> (module.getActive() && !module.getDeleted())).toList()));
        else dto.setModules(new ArrayList<>());
        return dto;
    }

    // --- 2) List<Course> -> List<DTO> ---
    public static List<AdminCourseResponseDto> toDtoList(List<Course> courses) {
        return courses.stream()
                .map(AdminCourseMapper::toDto)
                .collect(Collectors.toList());
    }

    public static Page<AdminCourseResponseDto> toDtoPage(Page<Course> coursePage) {
        List<AdminCourseResponseDto> dtoList = coursePage.getContent()
                .stream()
                .map(AdminCourseMapper::toDto)
                .collect(Collectors.toList());
        return new PageImpl<>(
                dtoList,
                coursePage.getPageable(),
                coursePage.getTotalElements()
        );
    }

    public static Course toEntityFromCreateRequest(AdminCourseCreateRequestDto dto) {
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
