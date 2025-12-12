package uz.codebyz.onlinecoursebackend.telegram.entity;

import jakarta.persistence.*;
import uz.codebyz.onlinecoursebackend.user.User;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "telegram_bot_users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "chat_id"),
                @UniqueConstraint(columnNames = "telegram_id")
        })
public class TelegramBotUser {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Telegram internal user id

    @Column(name = "chat_id", nullable = false)
    private Long chatId;

    @Column(length = 100)
    private String username;   // @username

    @Column(length = 100)
    private String nickname;   // first_name + last_name

    // 🔗 Platforma user bilan bog‘lash (ixtiyoriy)
    @Column(nullable = false)
    private boolean linked = false;

    private LocalDateTime firstSeen = LocalDateTime.now();
    private LocalDateTime lastSeen = LocalDateTime.now();

    @Column(nullable = false)
    private boolean blocked = false;
    // ===== getters/setters =====


}
