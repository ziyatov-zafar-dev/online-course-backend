package uz.codebyz.onlinecoursebackend.message.entity;

import jakarta.persistence.*;
import uz.codebyz.onlinecoursebackend.user.User;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "messages")
public class Message {

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

    private Boolean edited;
    private Boolean deleted = false;
    private LocalDateTime deletedAt;
    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    /**
     * Kim yubordi
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    /**
     * TEXT | IMAGE | VIDEO | FILE | SYSTEM
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "message_type", nullable = false, length = 20)
    private MessageType messageType;

    /**
     * Matn yoki caption
     */
    @Column(columnDefinition = "TEXT")
    private String content;

    /**
     * Reply bo‘lsa — qaysi message ga javob
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_to_id")
    private Message replyTo;

    /**
     * PRIVATE chatlar uchun o‘qilganligi
     */
    @Column(name = "is_read", nullable = false)
    private Boolean read = false;

    /**
     * Yaratilgan vaqt
     */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /**
     * Tahrirlangan vaqt
     */
    @Column(name = "edited_at")
    private LocalDateTime editedAt;

    private Boolean active = true;

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    /* ==========================
       JPA CALLBACKS
       ========================== */

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    /* ==========================
       BUSINESS METHODS
       ========================== */

    /**
     * Xabarni o‘qilgan deb belgilash (faqat PRIVATE chatlar)
     */
    public void markAsRead() {
        this.read = true;
    }

    /**
     * Xabarni tahrirlash
     */
    public void edit(String newContent) {
        this.content = newContent;
        this.editedAt = LocalDateTime.now();
    }

    /**
     * Tahrirlanganmi
     */
    public boolean isEdited() {
        return this.editedAt != null;
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

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Message getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(Message replyTo) {
        this.replyTo = replyTo;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getEditedAt() {
        return editedAt;
    }

    public void setEditedAt(LocalDateTime editedAt) {
        this.editedAt = editedAt;
    }


    public Boolean getEdited() {
        return edited;
    }

    public void setEdited(Boolean edited) {
        this.edited = edited;
    }
}
