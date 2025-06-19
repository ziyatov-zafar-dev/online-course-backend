package uz.zafar.onlinecourse.dto.student_dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface StudentDto {
    @JsonProperty("student-id")
    Long getStudentId();
    @JsonProperty("user-id")
    Long getUserId();

    String getUsername();

    String getFirstname();

    String getLastname();

    String getEmail();
}
