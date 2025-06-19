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
}
