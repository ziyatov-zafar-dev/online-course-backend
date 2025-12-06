package uz.codebyz.onlinecoursebackend.admin.category.mapper;

import uz.codebyz.onlinecoursebackend.admin.category.dto.AdminCategoryCreateRequest;
import uz.codebyz.onlinecoursebackend.admin.category.dto.AdminCategoryResponseDto;
import uz.codebyz.onlinecoursebackend.category.entity.Category;
import uz.codebyz.onlinecoursebackend.category.entity.CategoryStatus;
import uz.codebyz.onlinecoursebackend.helper.CurrentTime;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AdminCategoryMapper {
    public static Category toEntity(AdminCategoryCreateRequest dto) {
        return new Category(
                CategoryStatus.CLOSE,
                dto.getName().trim(),
                dto.getDescription().trim(),
                true, false, dto.getSlug().trim(),
                dto.getOrderNumber(), CurrentTime.currentTime(),
                CurrentTime.currentTime(), new ArrayList<>()
        );
    }


    public static AdminCategoryResponseDto toDto(Category category) {
        StatusDto statusDto = null;
        if (category.getStatus() != null) {
            statusDto = new StatusDto(
                    category.getStatus().name(),
                    category.getStatus().getDescription()
            );
        }
        AdminCategoryResponseDto response = new AdminCategoryResponseDto(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getSlug(),
                category.getOrderNumber(),
                category.getCreated(),
                category.getUpdated(),
                category.getStatus()
        );
        response.setStatus(statusDto);
        return response;
    }

    public static class StatusDto {
        private String name;
        private String description;

        public StatusDto(String name, String description) {
            this.name = name;
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        // getters & setters
    }

    public static List<AdminCategoryResponseDto> toDto(List<Category> categories) {
        if (categories == null || categories.isEmpty()) {
            return List.of();
        }
        return categories.stream()
                .map(AdminCategoryMapper::toDto)
                .collect(Collectors.toList());
    }

}
