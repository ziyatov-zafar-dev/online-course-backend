package uz.zafar.onlinecourse.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {
    private String firstname;
    private String lastname;
    private String email;
    private String username;
    @JsonProperty(value = "authorities", required = true)
    private List<String> roles;
    private Long studentId;
    private Long teacherId  ;

    public UserResponseDto(String firstname, String lastname, String email, String username, List<String> roles) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.username = username;
        this.roles = roles;
    }
}
