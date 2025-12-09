package uz.codebyz.onlinecoursebackend.revokedToken.entity;

import jakarta.persistence.*;
import uz.codebyz.onlinecoursebackend.helper.CurrentTime;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class RevokedToken {

    @Id
    private String token;

    private LocalDateTime revokedAt = CurrentTime.currentTime();

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getRevokedAt() {
        return revokedAt;
    }

    public void setRevokedAt(LocalDateTime revokedAt) {
        this.revokedAt = revokedAt;
    }

    public RevokedToken() {
    }

    public RevokedToken(LocalDateTime revokedAt) {
        this.revokedAt = revokedAt;
    }

    public RevokedToken(String token) {
        this.token = token;
    }

    public RevokedToken(String token, LocalDateTime revokedAt) {
        this.token = token;
        this.revokedAt = revokedAt;
    }
}
