package uz.codebyz.onlinecoursebackend.auth.dto;

public class TelegramAuthorizationResponse {

    private String role;

    public TelegramAuthorizationResponse(String role) {
        this.role = role;
    }

    public TelegramAuthorizationResponse() {
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
