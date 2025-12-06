package uz.codebyz.onlinecoursebackend.admin.homework.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import uz.codebyz.onlinecoursebackend.homework.entity.Homework;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class AdminHomeworkResponseDto {
    private UUID homeworkId;
    private String title;
    private String description;
    @JsonIgnore
    private Boolean hasText;
    private Boolean hasFiles;
    private Integer maxScore;
    private Integer minScore;
    private LocalDateTime created;
    private LocalDateTime updated;
    private List<AdminHomeworkFileResponseDto> homeworkFiles;

    public List<AdminHomeworkFileResponseDto> getHomeworkFiles() {
        return homeworkFiles;
    }

    public void setHomeworkFiles(List<AdminHomeworkFileResponseDto> homeworkFiles) {
        this.homeworkFiles = homeworkFiles;
    }

    public UUID getHomeworkId() {
        return homeworkId;
    }

    public void setHomeworkId(UUID homeworkId) {
        this.homeworkId = homeworkId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getHasText() {
        return hasText;
    }

    public void setHasText(Boolean hasText) {
        this.hasText = hasText;
    }

    public Boolean getHasFiles() {
        return hasFiles;
    }

    public void setHasFiles(Boolean hasFiles) {
        this.hasFiles = hasFiles;
    }

    public Integer getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(Integer maxScore) {
        this.maxScore = maxScore;
    }

    public Integer getMinScore() {
        return minScore;
    }

    public void setMinScore(Integer minScore) {
        this.minScore = minScore;
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
