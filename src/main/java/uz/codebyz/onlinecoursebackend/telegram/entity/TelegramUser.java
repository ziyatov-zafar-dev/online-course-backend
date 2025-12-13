package uz.codebyz.onlinecoursebackend.telegram.entity;

import jakarta.persistence.*;
import uz.codebyz.onlinecoursebackend.user.User;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "telegram_users"
)
public class TelegramUser {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid")
    private UUID id;

    /* ===== TELEGRAM DATA ===== */

    @Column(name = "chat_id", nullable = false, unique = true)
    private Long chatId;
    private EventCode eventCode;
    @Column(name = "username")
    private String username;

    @Enumerated(EnumType.STRING)
    private BotUserStatus status;
    @Column(name = "nickname")
    private String nickname;

    /* ===== PLATFORM USER ===== */

    /* ===== STATUS ===== */


    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    /* ===== GETTERS / SETTERS ===== */


    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public EventCode getEventCode() {
        return eventCode;
    }

    public void setEventCode(EventCode eventCode) {
        this.eventCode = eventCode;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public BotUserStatus getStatus() {
        return status;
    }

    public void setStatus(BotUserStatus status) {
        this.status = status;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
