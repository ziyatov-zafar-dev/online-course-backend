package uz.zafar.onlinecourse.dto.student_dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface StudentDto {
    Long getStudentId();

    Long getUserId();

    String getUsername();

    String getFirstname();

    String getLastname();

    String getEmail();
}
