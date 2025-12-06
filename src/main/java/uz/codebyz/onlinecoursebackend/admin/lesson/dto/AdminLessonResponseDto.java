package uz.codebyz.onlinecoursebackend.admin.lesson.dto;

import uz.codebyz.onlinecoursebackend.admin.homework.dto.AdminHomeworkResponseDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class AdminLessonResponseDto {
    private UUID lessonId;
    private String name;
    private String description;
    private String slug;
    private UUID moduleId;
    private Integer orderNumber;
    private String videoUrl;
    private String videName;
    private Long videoDurationSize;
    private String videoDurationMB;
    private LocalDateTime created;
    private LocalDateTime updated;
    private Boolean hasHomework;
    private AdminHomeworkResponseDto homework;

    public AdminHomeworkResponseDto getHomework() {
        return homework;
    }

    public void setHomework(AdminHomeworkResponseDto homework) {
        this.homework = homework;
    }

    public Boolean getHasHomework() {
        return hasHomework;
    }

    public void setHasHomework(Boolean hasHomework) {
        this.hasHomework = hasHomework;
    }

    public UUID getLessonId() {
        return lessonId;
    }

    public void setLessonId(UUID lessonId) {
        this.lessonId = lessonId;
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

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public UUID getModuleId() {
        return moduleId;
    }

    public void setModuleId(UUID moduleId) {
        this.moduleId = moduleId;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideName() {
        return videName;
    }

    public void setVideName(String videName) {
        this.videName = videName;
    }

    public Long getVideoDurationSize() {
        return videoDurationSize;
    }

    public void setVideoDurationSize(Long videoDurationSize) {
        this.videoDurationSize = videoDurationSize;
    }

    public String getVideoDurationMB() {
        return videoDurationMB;
    }

    public void setVideoDurationMB(String videoDurationMB) {
        this.videoDurationMB = videoDurationMB;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }
}
