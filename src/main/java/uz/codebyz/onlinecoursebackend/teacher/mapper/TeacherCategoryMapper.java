package uz.codebyz.onlinecoursebackend.teacher.mapper;

import org.springframework.stereotype.Component;
import uz.codebyz.onlinecoursebackend.category.entity.Category;
import uz.codebyz.onlinecoursebackend.teacher.dto.category.TeacherCategoryResponseDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TeacherCategoryMapper {
    public TeacherCategoryResponseDto toDto(Category category) {
        TeacherCategoryResponseDto dto = new TeacherCategoryResponseDto();
        dto.setCategoryId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        dto.setSlug(category.getSlug());
        dto.setOrderNumber(category.getOrderNumber());
        dto.setCreated(category.getCreated().toString());
        dto.setUpdated(category.getUpdated().toString());
        if (category.getStatus() != null) {
            Map<String, String> statusMap = new HashMap<>();
            statusMap.put(category.getStatus().name(), category.getStatus().getDescription());
            dto.setStatus(statusMap);
        }
        return dto;
    }

    public List<TeacherCategoryResponseDto> toDto(List<Category> categoryList) {
        return categoryList.stream().map(this::toDto).collect(Collectors.toList());
    }
}
