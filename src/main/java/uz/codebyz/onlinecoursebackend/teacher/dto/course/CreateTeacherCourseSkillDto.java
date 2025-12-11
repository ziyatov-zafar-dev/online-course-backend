package uz.codebyz.onlinecoursebackend.teacher.dto.course;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public class CreateTeacherCourseSkillDto {
    private UUID courseId;
    private String name;
    private MultipartFile skillIcon;
    private Integer orderNumber;

    public CreateTeacherCourseSkillDto() {
    }

    public CreateTeacherCourseSkillDto(UUID courseId, String name, MultipartFile skillIcon, Integer orderNumber) {
        this.courseId = courseId;
        this.name = name;
        this.skillIcon = skillIcon;
        this.orderNumber = orderNumber;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public MultipartFile getSkillIcon() {
        return skillIcon;
    }

    public void setSkillIcon(MultipartFile skillIcon) {
        this.skillIcon = skillIcon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getCourseId() {
        return courseId;
    }

    public void setCourseId(UUID courseId) {
        this.courseId = courseId;
    }
}
