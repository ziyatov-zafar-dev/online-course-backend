package uz.codebyz.onlinecoursebackend.admin.users.teacherDto;

import uz.codebyz.onlinecoursebackend.auth.dto.UserResponse;

public class AdminTeacherResponseDto {
    private Long teacherId;
    private UserResponse userInformation;

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public UserResponse getUserInformation() {
        return userInformation;
    }

    public AdminTeacherResponseDto() {
    }

    public AdminTeacherResponseDto(Long teacherId, UserResponse userInformation) {
        this.teacherId = teacherId;
        this.userInformation = userInformation;
    }

    public void setUserInformation(UserResponse userInformation) {
        this.userInformation = userInformation;
    }
}
