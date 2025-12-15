package uz.codebyz.onlinecoursebackend.message.entity;

import jakarta.persistence.*;
import uz.codebyz.onlinecoursebackend.user.User;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "chat_members",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"chat_id", "user_id"})
        }
)
public class ChatMember {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid")
    private UUID id;

    /**
     * Qaysi chatga tegishli
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;

    /**
     * System user
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * OWNER | ADMIN | MEMBER
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ChatMemberRole role;

    /**
     * Chatga qo‘shilgan vaqt
     */
    @Column(name = "joined_at", nullable = false)
    private LocalDateTime joinedAt;

    /**
     * Chatdan chiqqan vaqt (null bo‘lsa — hali chatda)
     */
    @Column(name = "left_at")
    private LocalDateTime leftAt;

    /* ==========================
       JPA CALLBACKS
       ========================== */

    @PrePersist
    public void prePersist() {
        this.joinedAt = LocalDateTime.now();
    }

    /* ==========================
       BUSINESS HELPERS
       ========================== */

    /**
     * Foydalanuvchi hozir chatda bormi?
     */
    public boolean isActiveMember() {
        return this.leftAt == null;
    }

    /**
     * Chatdan chiqish
     */
    public void leave() {
        this.leftAt = LocalDateTime.now();
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

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ChatMemberRole getRole() {
        return role;
    }

    public void setRole(ChatMemberRole role) {
        this.role = role;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }

    public LocalDateTime getLeftAt() {
        return leftAt;
    }

    public void setLeftAt(LocalDateTime leftAt) {
        this.leftAt = leftAt;
    }
}
