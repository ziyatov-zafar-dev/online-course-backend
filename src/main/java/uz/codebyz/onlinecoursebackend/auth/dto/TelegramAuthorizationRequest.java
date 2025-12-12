package uz.codebyz.onlinecoursebackend.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TelegramAuthorizationRequest {

    @NotNull
    private Long chatId;

    @NotBlank
    private String login; // username yoki email

    @NotBlank
    private String password;

    // getters & setters


    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
