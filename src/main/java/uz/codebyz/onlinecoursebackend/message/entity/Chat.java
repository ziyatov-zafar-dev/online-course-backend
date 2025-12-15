package uz.codebyz.onlinecoursebackend.message.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "chats",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username")
        }
)
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid")
    private UUID id;

    /**
     * Chat yoki Group username
     * Masalan:
     * - private chat: user_123
     * - group chat: java_developers
     */
    @Column(nullable = false, length = 64, unique = true)
    private String username;

    /**
     * PRIVATE | GROUP
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ChatType type;

    /**
     * GROUP uchun majburiy
     * PRIVATE uchun optional
     */
    @Column(length = 255)
    private String title;

    /**
     * Faqat GROUP uchun
     */
    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * ACTIVE | BLOCKED | DELETED
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ChatStatus status = ChatStatus.ACTIVE;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /* ==========================
       JPA CALLBACKS
       ========================== */

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    /* ==========================
       GETTERS & SETTERS
       ========================== */

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ChatType getType() {
        return type;
    }

    public void setType(ChatType type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    /**
     * GROUP bo‘lsa title majburiy bo‘lishi kerak
     */
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Faqat GROUP chatlar uchun ishlatiladi
     */
    public void setDescription(String description) {
        this.description = description;
    }

    public ChatStatus getStatus() {
        return status;
    }

    public void setStatus(ChatStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
