package uz.codebyz.onlinecoursebackend.admin.course.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class AdminCourseSkillResponseDto {
    private String skillName;
    private String skillImgUrl;
    private UUID skillId;
    private UUID courseId;
    private LocalDateTime created;
    private LocalDateTime updated;
    private Integer orderNumber;

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
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

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public String getSkillImgUrl() {
        return skillImgUrl;
    }

    public void setSkillImgUrl(String skillImgUrl) {
        this.skillImgUrl = skillImgUrl;
    }

    public UUID getSkillId() {
        return skillId;
    }

    public void setSkillId(UUID skillId) {
        this.skillId = skillId;
    }

    public UUID getCourseId() {
        return courseId;
    }

    public void setCourseId(UUID courseId) {
        this.courseId = courseId;
    }
}
