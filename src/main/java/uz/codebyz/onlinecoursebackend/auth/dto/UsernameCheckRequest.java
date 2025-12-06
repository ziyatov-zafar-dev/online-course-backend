package uz.codebyz.onlinecoursebackend.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UsernameCheckRequest {
    @NotBlank
    @Size(min = 5, max = 32)
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
