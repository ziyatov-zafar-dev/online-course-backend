package uz.codebyz.onlinecoursebackend.payment.dto.request;

import jakarta.validation.constraints.*;

public class CoursePaymentRequest {

    @NotNull(message = "O'quvchi ID majburiy")
    private String studentId;

    @NotNull(message = "Kurs ID majburiy")
    private String courseId;

    @Size(max = 500, message = "Tavsif 500 belgidan oshmasligi kerak")
    private String description;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}