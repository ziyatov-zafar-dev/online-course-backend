package uz.codebyz.onlinecoursebackend.teacher.dto.course;

import org.springframework.web.multipart.MultipartFile;

public class UpdateTeacherCourseSkillDto {
    private String name;
    private MultipartFile skillIcon;
    private Integer orderNumber;

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MultipartFile getSkillIcon() {
        return skillIcon;
    }

    public void setSkillIcon(MultipartFile skillIcon) {
        this.skillIcon = skillIcon;
    }
}
