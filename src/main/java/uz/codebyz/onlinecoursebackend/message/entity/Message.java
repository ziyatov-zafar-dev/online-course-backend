package uz.codebyz.onlinecoursebackend.message.entity;

import jakarta.persistence.*;
import uz.codebyz.onlinecoursebackend.helper.CurrentTime;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid")
    private UUID id;

    // 🔥 MANY → ONE
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;
    @Column(name = "sender_id", nullable = false)
    private UUID senderId;

    @Column(columnDefinition = "text")
    private String caption;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    private boolean hasFile;

    private String fileUrl;
    private Long fileSize;
    private Long fileSizeMB;
    private String fileName;
    private Boolean active;
    private FileType fileType;
    private UUID replyMessageId;
    private LocalDateTime created = CurrentTime.currentTime();
    private Boolean edited = false;
    private LocalDateTime updated = CurrentTime.currentTime();

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public Boolean getEdited() {
        return edited;
    }

    public void setEdited(Boolean edited) {
        this.edited = edited;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }

    public UUID getReplyMessageId() {
        return replyMessageId;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public Long getFileSizeMB() {
        return fileSizeMB;
    }

    public void setFileSizeMB(Long imgSizeMB) {
        this.fileSizeMB = imgSizeMB;
    }

    public void setReplyMessageId(UUID replyMessageId) {
        this.replyMessageId = replyMessageId;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String imgUrl) {
        this.fileUrl = imgUrl;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long imgSize) {
        this.fileSize = imgSize;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String imgName) {
        this.fileName = imgName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isHasFile() {
        return hasFile;
    }

    public void setHasFile(boolean hasFile) {
        this.hasFile = hasFile;
    }

    public void setId(UUID id) {

        this.id = id;
    }

    // getters / setters
    public UUID getId() {
        return id;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public UUID getSenderId() {
        return senderId;
    }

    public void setSenderId(UUID senderId) {
        this.senderId = senderId;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String text) {
        this.caption = text;
    }
}

