package uz.codebyz.onlinecoursebackend.admin.module.mapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import uz.codebyz.onlinecoursebackend.admin.course.mapper.AdminCourseMapper;
import uz.codebyz.onlinecoursebackend.admin.lesson.mapper.AdminLessonMapper;
import uz.codebyz.onlinecoursebackend.admin.module.dto.AdminModuleCreateRequestDto;
import uz.codebyz.onlinecoursebackend.admin.module.dto.AdminModuleResponseDto;
import uz.codebyz.onlinecoursebackend.admin.module.dto.AdminModuleUpdateRequestDto;
import uz.codebyz.onlinecoursebackend.course.entity.Course;
import uz.codebyz.onlinecoursebackend.helper.CurrentTime;
import uz.codebyz.onlinecoursebackend.module.entity.Module;

import java.util.List;
import java.util.stream.Collectors;

public class AdminModuleMapper {
    public static AdminModuleResponseDto toDto(Module module) {

        return new AdminModuleResponseDto(
                module.getId(), module.getName(),
                module.getDescription(), module.getSlug(),
                module.getCourse().getId(), module.getOrderNumber(),
                module.getCreated(), module.getUpdated(),
                AdminLessonMapper.toDto(module.getLessons().stream().filter(lesson -> (lesson.getActive() && !lesson.getDeleted())).toList())
        );
    }

    public static List<AdminModuleResponseDto> toDto(List<Module> modules) {
        return modules.stream().map(AdminModuleMapper::toDto).collect(Collectors.toList());
    }

    public static Page<AdminModuleResponseDto> toDtoPage(Page<Module> modules) {
        return new PageImpl<>(
                toDto(modules.getContent()),
                modules.getPageable(),
                modules.getTotalElements()
        );
    }


    public static Module addModuleMapper(AdminModuleCreateRequestDto dto, Course course) {
        Module module = new Module();
        module.setName(dto.getName());
        module.setDescription(dto.getDescription());
        module.setSlug(dto.getSlug());
        module.setActive(true);
        module.setDeleted(false);
        module.setCourse(course);
        module.setOrderNumber(dto.getOrderNumber());
        module.setCreated(CurrentTime.currentTime());
        module.setUpdated(CurrentTime.currentTime());
        return module;
    }

    public static Module editModuleMapper(AdminModuleUpdateRequestDto dto, Module module) {
        module.setName(dto.getName());
        module.setDescription(dto.getDescription());
        module.setSlug(dto.getSlug());
        module.setOrderNumber(dto.getOrderNumber());
        module.setUpdated(CurrentTime.currentTime());
        return module;
    }

    public static Module changeModuleUpdateTime(Module module) {
        module.setUpdated(CurrentTime.currentTime());
        return module;
    }
}
